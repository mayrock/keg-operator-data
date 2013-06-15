Fav = {};

/*****open add favorite window*****/
Fav.createFrame = function(tabIndex){
	Common.background();
	Common.favInfo();
	$("<span>Input a name:</span><br/><input type = 'text' value = 'my favorite' id = 'favName' maxlength = '16'/><br/>" +
		"<input type = 'button' value = 'confirm' onclick = \"Fav.saveSta(" + tabIndex + ");\"/>").appendTo("#favInfo");
	$("<img src = 'css/images/close_256x256.png' onclick = \"Fav.closeFrame();\"/>")
		.appendTo("#favInfo")
		.css({
			"position": "absolute",
			"top": "5px",
			"right": "5px",
			"width": "16px"
		});
	$("#background").css("display","block");
	$("#favInfo").css("display","block");
};

/*****close window*****/
Fav.closeFrame = function(){
	$("#favInfo").empty();
	$("#background").css("display","none");
	$("#favInfo").css("display","none");
};

/*****save one sta tab*****/
Fav.saveSta = function(tabIndex){
	$.getJSON(Common.dataviewUrl().replace("tabType","sta"),function(data){
		var len = data.length;
		var datasetData = JSON.parse("[]");
		for(var i = 0; i < len; i++){
			var name = data[i].datasetName;
			var l = Common.chartIndex[tabIndex][i].length;
			if(l == 1){
				continue;
			}
			var chartData = JSON.parse("[]");
			for(var j = 0; j < l - 1; j++){
				var chartIndex = Common.chartIndex[tabIndex][i][j];
				var yAxisData = JSON.parse("[]");
				yAxisLen = Common.yAxis[tabIndex][i][chartIndex].length;
				for(var k = 0; k < yAxisLen; k++){
					yAxisData[k] = Common.yAxis[tabIndex][i][chartIndex][k];
				}
				/*****chart type and yAxis array make up a chart*****/
				var cData = JSON.parse("{}");
				cData.type = Common.chartType[tabIndex][i][chartIndex];
				cData.yAxis = yAxisData;
				/*****chart array*****/
				var chartLen = chartData.length;
				chartData.splice(chartLen,0,cData);
			}
			/*****dataset name and chart array make up a dataset*****/
			var dsData = JSON.parse("{}");
			dsData.name = name;
			dsData.chart = chartData;
			/*****dataset array*****/
			var dsLen = datasetData.length;
			datasetData.splice(dsLen,0,dsData);
		}
		/*****tab name and dataset array make up a tab*****/
		var tabData = JSON.parse("{}");
		var tabName = $("#favName").val();
		tabData.name = tabName;
		tabData.dataset = datasetData;
		
		var username = Common.username;
		$.post(Common.addFavUrl(),{
			userid: username,
			favstring: JSON.stringify(tabData)
		},function(data){
			if(data.status == true){
				Fav.addDownList(data.favid,tabName);
				Fav.closeFrame();
			}else if(data.status == false){
				alert("Oops, we got an error...");
				return;
			}else{
				/*****need to do*****/
			}
		},"json").error(function(){
			alert("Oops, we got an error...");
			return;
		});
	}).error(function(){
		alert("Oops, we got an error...");
	});
};

/*****load drop-down list of favorite*****/
Fav.loadDownList = function(){
	$("#extendedFav").empty();
	var username = Common.username;
	$.getJSON(Common.favListUrl(),{
		userid: username
	},function(data){
		var len = data.length;
		for(var i = 0; i < len; i++){
			var favid = data[i].favid;
			var tabData = JSON.parse(data[i].favstring);
			var favname = tabData.name;
			Fav.addDownList(favid,favname);
		}
	}).error(function(){
		alert("Oops, we got an error...");
		return;
	});
};

/*****add one favorite to drop-down list*****/
Fav.addDownList = function(favid,favname){
	var username = Common.username;
	fav = document.createElement("div");
	fav.setAttribute("id",favid);
	$(fav).appendTo("#extendedFav");
	staFav = document.createElement("a");
	$(staFav).attr("href","javascript:void(0);");
	$(staFav).attr("onClick","Fav.revertSta(" + favid + ");");
	staFav.innerHTML = favname;
	$(staFav).appendTo(fav);
	$("<img src = 'css/images/close_256x256.png' onClick = \"Fav.delSta(" + favid + ");\"/>")
		.appendTo(fav)
		.css({
			"width": "16px"
		});
};

/*****revert one saved favorite*****/
Fav.revertSta = function(favid){
	var username = Common.username;
	Tab.createFrame("sta");
	var length = Common.tabIndex.length;
	var tabIndex = Common.tabIndex[length - 2];
	$.getJSON(Common.favDataUrl(),{
		userid: username,
		favid: favid
	},function(data){
		var tabData = JSON.parse(data.favstring);
		var datasetData = tabData.dataset;
		var dsLen = datasetData.length;
		$.getJSON(Common.dataviewUrl().replace("tabType","sta"),function(data){
			var len = data.length;
			google.load("visualization","1",{packages:["corechart"],"callback":drawChart});
			
			function drawChart(){
				for(var i = 0; i < dsLen; i++){
					var dsData = datasetData[i];
					var dsName = dsData.name;
					var dsIndex = new Object;
					for(var j = 0; j < len; j++){
						var name = data[j].datasetName;
						if(dsName == name){
							dsIndex = j;
							break;
						}
					}
					var chartData = dsData.chart;
					var chartLen = chartData.length;
					for(var j = 0; j < chartLen; j++){
						Common.chartIndex[tabIndex][dsIndex][j + 1] = j + 1;
					}
					$("#view_ds" + tabIndex + "_" + dsIndex).css("display","block");
					for(var j = 0; j < chartLen; j++){
						var cData = chartData[j];
						Common.chartType[tabIndex][dsIndex][j] = cData.type;
						var yAxisData = cData.yAxis;
						yAxisLen = yAxisData.length;
						Common.yAxis[tabIndex][dsIndex][j] = new Array();
						for(var k = 0; k < yAxisLen; k++){
							Common.yAxis[tabIndex][dsIndex][j][k] = yAxisData[k];
						}
						Sta.createChart(tabIndex,dsIndex,j);
					}
				}
			}
		}).error(function(){
			alert("Oops, we got an error...");
		});
	}).error(function(){
		alert("Oops, we got an error...");
		return;
	});
};

/*****delete one favorite from drop-down list*****/
Fav.delSta = function(favid){
	var username = Common.username;
	$.post(Common.delFavUrl(),{
		userid: username,
		favid: favid
	},function(data){
		if(data.status == true){
			$("#" + favid).remove();
		}else{
			alert("Oops, we got an error...");
			return;
		}
	},"json").error(function(){
		alert("Oops, we got an error...");
		return;
	});
};