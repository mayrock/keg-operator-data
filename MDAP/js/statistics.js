Sta = {};

/*****initialize chartType and yAxis of a chart*****/
Sta.guide = function(tabIndex,dsIndex){
	/*****Cross-domain requests and dataType: "jsonp" requests do not support synchronous operation.*****/
	$.getJSON(Common.dataviewUrl().replace("tabType","sta"),function(data){
		var values = data[dsIndex].values;
		var len = values.length;
		var l = Common.chartIndex[tabIndex][dsIndex].length;
		var chartIndex = Common.chartIndex[tabIndex][dsIndex][l - 1];
		if(l == 1){
			$("#view_ds" + tabIndex + "_" + dsIndex).css("display","block");
		}
		Common.chartIndex[tabIndex][dsIndex][l] = chartIndex + 1;
		Common.chartType[tabIndex][dsIndex][chartIndex] = "lineChart";
		Common.yAxis[tabIndex][dsIndex][chartIndex] = new Array();
		for(var i = 0; i < len; i++){
			Common.yAxis[tabIndex][dsIndex][chartIndex][i] = true;
		}
		google.load("visualization","1",{packages:["corechart"],"callback":drawChart});
		function drawChart(){
			Sta.createChart(tabIndex,dsIndex,chartIndex);
		}
	}).error(function(){
		alert("Oops, we got an error...");
	});
};

/*****create one chart container*****/
Sta.createChart = function(tabIndex,dsIndex,chartIndex){
	var view_chart = document.createElement("div");
	view_chart.setAttribute("id","view_chart" + tabIndex + "_" + dsIndex + "_" + chartIndex);
	view_chart.setAttribute("class","view_chart");
	$(view_chart).appendTo("#view_ds" + tabIndex + "_" + dsIndex);
	$("#special" + tabIndex + "_" + dsIndex).remove();
	$("<div id = 'special" + tabIndex + "_" + dsIndex + "' style = 'clear:both;'></div>").appendTo("#view_ds" + tabIndex + "_" + dsIndex);
	var optionWidth = $("#option" + tabIndex).width() + 80;
	if($("#view_ds" + tabIndex + "_" + dsIndex).width() > (Common.width() - optionWidth)){
		$("#view_ds" + tabIndex + "_" + dsIndex).css("width",Common.width() - optionWidth);
		$("#view_ds" + tabIndex + "_" + dsIndex).css("overflow","auto");
	}
	var chartContainer = document.createElement("div");
	chartContainer.setAttribute("id","chartContainer" + tabIndex + "_" + dsIndex + "_" + chartIndex);
	chartContainer.setAttribute("class","chartContainer");
	$(chartContainer).appendTo(view_chart);
	$("<img src = 'css/images/close_256x256.png' onclick = \"Sta.closeChart(" + tabIndex + "," + dsIndex + "," + chartIndex + ");\"/>")
		.appendTo("#view_chart" + tabIndex + "_" + dsIndex + "_" + chartIndex)
		.css({
			"position": "absolute",
			"top": "5px",
			"right": "5px",
			"width": "16px"
		});
	var viewHeight = $("#view" + tabIndex).height();
	if((viewHeight + 25) > Common.tabHeight){
		$("#tab_bg").css({
			"height": viewHeight + 25
		});
	}
	Sta.showChart(tabIndex,dsIndex,chartIndex,"chartContainer");
};

/*****close one chart container*****/
Sta.closeChart = function(tabIndex,dsIndex,chartIndex){
	var l = Common.chartIndex[tabIndex][dsIndex].length;
	for(var i = 0; i < l - 1; i++){
		temp = Common.chartIndex[tabIndex][dsIndex][i];
		if(temp == chartIndex){
			index = i;
			break;
		}
	}
	Common.chartIndex[tabIndex][dsIndex].splice(index,1);
	$("#view_chart" + tabIndex + "_" + dsIndex + "_" + chartIndex).remove();
	$("#view_ds" + tabIndex + "_" + dsIndex).css("width","auto");
	$("#view_ds" + tabIndex + "_" + dsIndex).css("overflow","visible");
	var optionWidth = $("#option" + tabIndex).width() + 80;
	if($("#view_ds" + tabIndex + "_" + dsIndex).width() > (Common.width() - optionWidth)){
		$("#view_ds" + tabIndex + "_" + dsIndex).css("width",Common.width() - optionWidth);
		$("#view_ds" + tabIndex + "_" + dsIndex).css("overflow","auto");
	}
	l = Common.chartIndex[tabIndex][dsIndex].length;
	if(l == 1){
		$("#view_ds" + tabIndex + "_" + dsIndex).css("display","none");
	}
	var viewHeight = $("#view" + tabIndex).height();
	if((viewHeight + 25) > Common.tabHeight){
		$("#tab_bg").css({
			"height": viewHeight + 25
		});
	}else{
		$("#tab_bg").css({
			"height": Common.tabHeight
		});
	}
};

/*****magnify one chart*****/
Sta.magnifier = function(tabIndex,dsIndex,chartIndex,chartType){
	Common.background();
	Common.largeChart();
	Sta.createFrame();
	Common.chartType[tabIndex][dsIndex][chartIndex] = chartType;
	$.getJSON(Common.dataviewUrl().replace("tabType","sta"),function(data){
		var values = data[dsIndex].values;
		var len = values.length;
		var count = 0;
		var pieIndex;
		if((chartType == "columnChart") || (chartType == "lineChart")){
			for(var i = 0; i < len; i++){
				$("<input type = 'checkbox' id = 'checkbox" + tabIndex + "_" + dsIndex + "_" + chartIndex + "_" + i + "' " +
					"onclick = \"Sta.setYAxis(" + tabIndex + "," + dsIndex + "," + chartIndex + "," + i + ");\"/>" + values[i] +
					"<br/>").appendTo("#lcCheckbox");
				if(Common.yAxis[tabIndex][dsIndex][chartIndex][i] == true){
					$("#checkbox" + tabIndex + "_" + dsIndex + "_" + chartIndex + "_" + i).prop("checked",true);
					pieIndex = i;
					count ++;
				}
			}
		}else{
			$("#lcCheckbox").empty();
		}
		/*****set next chart type*****/
		if(chartType == "columnChart"){
			nextType = "lineChart";
		}else if(chartType == "lineChart"){
			if(count == 1){
				nextType = pieIndex;
			}else{
				nextType = "columnChart";
			}
		}else{
			nextType = "columnChart";
		}
		$("<img src = 'css/images/close_256x256.png' onclick = \"Sta.closeFrame(" + tabIndex + "," + dsIndex + "," + chartIndex + ");\"/>")
			.appendTo("#icon_lc")
			.css({
				"float": "right",
				"width": "16px",
				"margin-top": "5px",
				"margin-right": "5px"
			});
		$("<img src = 'css/images/change_100x100.jpg' onclick = \"Sta.magnifier(" + tabIndex + "," + dsIndex + "," + chartIndex + ",'" + nextType + "');\"/>")
			.appendTo("#icon_lc")
			.css({
				"float": "right",
				"width": "16px",
				"margin-top": "5px",
				"margin-right": "5px"
			});
		Sta.showChart(tabIndex,dsIndex,chartIndex,"lcContainer");
	}).error(function(){
		alert("Oops, we got an error...");
	});
};

/*****change chart by y axis*****/
Sta.setYAxis = function(tabIndex,dsIndex,chartIndex,index){
	if(Common.yAxis[tabIndex][dsIndex][chartIndex][index] == true){
		Common.yAxis[tabIndex][dsIndex][chartIndex][index] = false;
	}else{
		Common.yAxis[tabIndex][dsIndex][chartIndex][index] = true;
	}
	$("#lcContainer").empty();
	Sta.showChart(tabIndex,dsIndex,chartIndex,"lcContainer");
};

/*****open window for one magnified chart*****/
Sta.createFrame = function(){
	$("#lcContainer").empty();
	$("#lcCheckbox").empty();
	$("#icon_lc").empty();
	$("#background").css("display","block");
	$("#largeChart").css("display","block");
};

/*****close window*****/
Sta.closeFrame = function(tabIndex,dsIndex,chartIndex){
	$("#background").css("display","none");
	$("#largeChart").css("display","none");
	$("#chartContainer" + tabIndex + "_" + dsIndex + "_" + chartIndex).empty();
	$("#magnifier" + tabIndex + "_" + dsIndex + "_" + chartIndex).remove();
	Sta.showChart(tabIndex,dsIndex,chartIndex,"chartContainer");
};

/*****show one chart*****/
Sta.showChart = function(tabIndex,dsIndex,chartIndex,ccName){
	$.getJSON(Common.dataviewUrl().replace("tabType","sta"),function(data){
		var des = data[dsIndex].descriptionZh;
		var name = data[dsIndex].datasetName;
		var values = data[dsIndex].values;
		var len = values.length;
		var key = new Array();
		var value = new Array();
		$.getJSON(Common.dvDataUrl().replace("tabType","sta"),{
			dataset: name
		},function(data){
			var l = data.length;
			for(var i = 0; i < l; i++){
				key[i] = data[i].key[0];
				value[i] = new Array();
				for(var j = 0; j < len; j++){
					value[i][j] = data[i].value[j];
				}
			}
			l = key.length;
			
			var arr = "[";
			var chartType = Common.chartType[tabIndex][dsIndex][chartIndex];
			if((chartType == "columnChart") || (chartType == "lineChart")){
				arr += "[\"" + "key" + "\"";
				for(var i = 0; i < len; i++){
					if(Common.yAxis[tabIndex][dsIndex][chartIndex][i] == true){
						arr += ",\"" + values[i] + "\"";
					}
				}
				arr += "],";
				for(var i = 0; i < l; i++){
					arr += "[\"" + key[i] + "\"";
					for(var j = 0; j < len; j++){
						if(Common.yAxis[tabIndex][dsIndex][chartIndex][j] == true){
							arr += "," + value[i][j];
						}
					}
					arr += "]";
					if(i == l - 1){
						arr += "]";
					}else{
						arr += ",";
					}
				}
			}else{
				var pieIndex = parseInt(chartType);
				arr += "[\"" + "key" + "\",\"" + "value" + "\"],";
				for(var i = 0; i < l; i++){
					arr += "[\"" + key[i] + "\"," + value[i][pieIndex] + "]";
					if(i == l - 1){
						arr += "]";
					}else{
						arr += ",";
					}
				}
			}
			var jsonArr = $.parseJSON(arr);
			
			var view = new Object;
			if(ccName == "chartContainer"){
				$("<img id = 'magnifier" + tabIndex + "_" + dsIndex + "_" + chartIndex + "' src = 'css/images/magnifier_256x256.png' " +
					"onclick = \"Sta.magnifier(" + tabIndex + "," + dsIndex + "," + chartIndex + ",'" + chartType + "');\"/>")
					.appendTo("#view_chart" + tabIndex + "_" + dsIndex + "_" + chartIndex)
					.css({
						"position": "absolute",
						"right": "5px",
						"bottom": "5px",
						"width": "16px"
					});
				view = document.getElementById(ccName + tabIndex + "_" + dsIndex + "_" + chartIndex);
			}else{
				view = document.getElementById(ccName);
			}
			
			var data = google.visualization.arrayToDataTable(jsonArr);
			var options = {
				title: des
			};
			var chart = new Object;
			if(chartType == "columnChart"){
				chart = new google.visualization.ColumnChart(view);
			}else if(chartType == "lineChart"){
				chart = new google.visualization.LineChart(view);
			}else{
				chart = new google.visualization.PieChart(view);
			}
			chart.draw(data,options);
		}).error(function(){
			alert("Oops, we got an error...");
			return;
		});
	}).error(function(){
		alert("Oops, we got an error...");
	});
};