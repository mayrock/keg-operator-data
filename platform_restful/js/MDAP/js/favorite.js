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
	$.getJSON(Common.dataviewUrl(),{
		featuretype: "DistributionFeature"
	},function(data){
		var len = data.length;
		var dataviewData = JSON.parse("[]");
		for(var i = 0; i < len; i++){
			var id = data[i].id;
			var l = Common.chartIndex[tabIndex][i].length;
			if(l == 1){
				continue;
			}
			var chartData = JSON.parse("[]");
			for(var j = 0; j < l - 1; j++){
				var chartIndex = Common.chartIndex[tabIndex][i][j];
				var yAxisData = JSON.parse("[]");
				var yAxisLen = Common.yAxis[tabIndex][i][chartIndex].length;
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
			var dvData = JSON.parse("{}");
			dvData.id = id;
			dvData.chart = chartData;
			/*****dataset array*****/
			var dvLen = dataviewData.length;
			dataviewData.splice(dvLen,0,dvData);
		}
		/*****tab name and dataset array make up a tab*****/
		var tabData = JSON.parse("{}");
		var tabName = $("#favName").val();
		tabData.name = tabName;
		tabData.dataset = dataviewData;
		
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
		return;
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
	var fav = document.createElement("div");
	fav.setAttribute("id",favid);
	fav.setAttribute("class","favorite");
	$(fav).appendTo("#extendedFav");
	var staFav = document.createElement("a");
	$(staFav).attr("href","javascript:void(0);");
	$(staFav).attr("onClick","Fav.revertSta(" + favid + ");");
	staFav.innerHTML = favname;
	$(staFav).appendTo(fav);
	$("<img src = 'css/images/close_256x256.png' onClick = \"Fav.delSta(" + favid + ");\"/>")
		.appendTo(fav)
		.css({
			"float": "right",
			"margin-top": "2px",
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
		var dataviewData = tabData.dataset;
		var dvLen = dataviewData.length;
		$.getJSON(Common.dataviewUrl(),{
			featuretype: "DistributionFeature"
		},function(data){
			var len = data.length;
			google.load("visualization","1",{packages:["corechart"],"callback":drawChart});
			
			function drawChart(){
				for(var i = 0; i < dvLen; i++){
					var dvData = dataviewData[i];
					var dvID = dvData.id;
					var dvIndex = -1;
					for(var j = 0; j < len; j++){
						var id = data[j].id;
						if(dvID == id){
							dvIndex = j;
							break;
						}
					}
					if(dvIndex == -1){
						alert("The data view this favorite saved from isn't the current data view!");
						Fav.delSta(favid);
						Tab.close("sta",tabIndex);
						return;
					}
					var chartData = dvData.chart;
					var chartLen = chartData.length;
					for(var j = 0; j < chartLen; j++){
						Common.chartIndex[tabIndex][dvIndex][j + 1] = j + 1;
					}
					$("#sta-view-dv-" + tabIndex + "-" + dvIndex).css("display","block");
					for(var j = 0; j < chartLen; j++){
						var cData = chartData[j];
						Common.chartType[tabIndex][dvIndex][j] = cData.type;
						var yAxisData = cData.yAxis;
						var yAxisLen = yAxisData.length;
						Common.yAxis[tabIndex][dvIndex][j] = new Array();
						for(var k = 0; k < yAxisLen; k++){
							Common.yAxis[tabIndex][dvIndex][j][k] = yAxisData[k];
						}
						Sta.createChart(tabIndex,dvIndex,j);
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