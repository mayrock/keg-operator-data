Fav = {};

Fav.height = function(){return 200;};
Fav.weight = function(){return 300;};

/**********open window for confirm**********/
Fav.createFrame = function(tabIndex){
	Common.bgInit();
	Common.favInit();
	$("<span>Input a name:</span><br/><input type = 'text' value = 'my favorite' id = 'favName' maxlength = '36'/><br/>" +
		"<input type = 'button' value = 'confirm' onclick = \"Fav.saveSta(" + tabIndex + ");\"/>").appendTo("#favInfo");
	$("#background").css("display","block");
	$("#favInfo").css("display","block");
};

/**********close window**********/
Fav.closeFrame = function(){
	$("#favInfo").empty();
	$("#background").css("display","none");
	$("#favInfo").css("display","none");
};

/**********save one sta tab**********/
Fav.saveSta = function(tabIndex){
	$.getJSON(Common.datasetUrl().replace("tabType","sta"),function(data){
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
				/**********chart type and yAxis array make up a chart**********/
				var cData = JSON.parse("{}");
				cData.type = Common.chartType[tabIndex][i][chartIndex];
				cData.yAxis = yAxisData;
				/**********chart array**********/
				var chartLen = chartData.length;
				chartData.splice(chartLen,0,cData);
			}
			/**********dataset name and chart array make up a dataset**********/
			var dsData = JSON.parse("{}");
			dsData.name = name;
			dsData.chart = chartData;
			/**********dataset array**********/
			var dsLen = datasetData.length;
			datasetData.splice(dsLen,0,dsData);
		}
		/**********tab name and dataset array make up a tab**********/
		var tabData = JSON.parse("{}");
		var tabName = $("#favName").val();
		tabData.name = tabName;
		tabData.dataset = datasetData;
		/**/
		var favData = JSON.parse($.cookie("favData"));
		var length = favData.length;
		favData.splice(length,0,tabData);
		$.cookie("favData",JSON.stringify(favData),{expires: 7,path: "/"});
		Fav.closeFrame();
	}).error(function(){
		alert("Oops, we got an error...");
	});
};

/**********revert one saved favorite**********/
Fav.revertSta = function(favIndex){
	Tab.createFrame("sta");
	var tabIndex = Common.tabIndex - 1;
	var favData = JSON.parse($.cookie("favData"));
	var tabData = favData[favIndex];
	var datasetData = tabData.dataset;
	var dsLen = datasetData.length;
	$.getJSON(Common.datasetUrl().replace("tabType","sta"),function(data){
		var len = data.length;
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
	}).error(function(){
		alert("Oops, we got an error...");
	});
};

/**********delete**********/
Fav.delSta = function(favIndex){
	var favData = JSON.parse($.cookie("favData"));alert(JSON.stringify(favData));
	favData.splice(favIndex,1);alert(JSON.stringify(favData));
	$.cookie("favData",JSON.stringify(favData),{expires: 7,path: "/"});
};