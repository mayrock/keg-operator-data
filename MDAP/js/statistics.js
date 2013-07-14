Sta = {};

/*****initialize chartType and yAxis of a chart*****/
Sta.guide = function(tabIndex,dvIndex){
	/*****Cross-domain requests and dataType: "jsonp" requests do not support synchronous operation.*****/
	$.getJSON(Common.dataviewUrl(),{
		featuretype: "DistributionFeature"
	},function(data){
		var values = data[dvIndex].values;
		var len = values.length;
		var l = Common.chartIndex[tabIndex][dvIndex].length;
		var chartIndex = Common.chartIndex[tabIndex][dvIndex][l - 1];
		if(l == 1){
			$("#sta-view-dv-" + tabIndex + "-" + dvIndex).css("display","block");
		}
		Common.chartIndex[tabIndex][dvIndex][l] = chartIndex + 1;
		Common.chartType[tabIndex][dvIndex][chartIndex] = "lineChart";
		Common.yAxis[tabIndex][dvIndex][chartIndex] = new Array();
		for(var i = 0; i < len; i++){
			Common.yAxis[tabIndex][dvIndex][chartIndex][i] = true;
		}
		google.load("visualization","1",{packages:["corechart"],"callback":drawChart});
		function drawChart(){
			Sta.createChart(tabIndex,dvIndex,chartIndex);
		}
	}).error(function(){
		alert("Oops, we got an error...");
		return;
	});
};

/*****create one chart container*****/
Sta.createChart = function(tabIndex,dvIndex,chartIndex){
	var view_chart = document.createElement("div");
	view_chart.setAttribute("id","sta-view-chart-" + tabIndex + "-" + dvIndex + "-" + chartIndex);
	view_chart.setAttribute("class","view_chart");
	$(view_chart).appendTo("#dvContainer-" + tabIndex + "-" + dvIndex);
	
	var l = Common.chartIndex[tabIndex][dvIndex].length;
	var ctnrWidth = 304 * (l - 1);
	$("#dvContainer-" + tabIndex + "-" + dvIndex).css({
		"width": ctnrWidth
	});
	if($("#staView" + tabIndex).width() < ctnrWidth + 60){
		$("#sta-view-dv-" + tabIndex + "-" + dvIndex).css({
			"height": "263px"
		});
	}
	
	var listHeight = $("#staList" + tabIndex).height();
	var viewHeight = $("#staView" + tabIndex).height();
	if(listHeight > viewHeight){
		$("#tab" + tabIndex).css({
			"height": listHeight + 10
		});
	}else{
		$("#tab" + tabIndex).css({
			"height": viewHeight + 10
		});
	}
	Tab.adjustHeight(tabIndex);
	
	var chartContainer = document.createElement("div");
	chartContainer.setAttribute("id","sta-chart-container-" + tabIndex + "-" + dvIndex + "-" + chartIndex);
	chartContainer.setAttribute("class","chartContainer");
	$(chartContainer).appendTo(view_chart);
	$("<img src = 'css/images/close_256x256.png' " +
		"onclick = \"Sta.closeChart(" + tabIndex + "," + dvIndex + "," + chartIndex + ");\"/>")
		.appendTo(view_chart)
		.css({
			"position": "absolute",
			"top": "5px",
			"right": "5px",
			"width": "16px"
		});
	
	Sta.showChart(tabIndex,dvIndex,chartIndex,"chartContainer");
};

/*****close one chart container*****/
Sta.closeChart = function(tabIndex,dvIndex,chartIndex){
	var l = Common.chartIndex[tabIndex][dvIndex].length;
	var index = -1;
	var temp = -1;
	for(var i = 0; i < l - 1; i++){
		temp = Common.chartIndex[tabIndex][dvIndex][i];
		if(temp == chartIndex){
			index = i;
			break;
		}
	}
	
	Common.chartIndex[tabIndex][dvIndex].splice(index,1);
	Common.chartType[tabIndex][dvIndex][chartIndex] = new Object;
	Common.yAxis[tabIndex][dvIndex][chartIndex] = new Object;
	$("#sta-view-chart-" + tabIndex + "-" + dvIndex + "-" + chartIndex).remove();
	
	var l = Common.chartIndex[tabIndex][dvIndex].length;
	var ctnrWidth = 304 * (l - 1);
	$("#dvContainer-" + tabIndex + "-" + dvIndex).css({
		"width": ctnrWidth
	});
	if($("#staView" + tabIndex).width() >= ctnrWidth + 60){
		$("#sta-view-dv-" + tabIndex + "-" + dvIndex).css({
			"height": "246px"
		});
	}
	if(l == 1){
		$("#sta-view-dv-" + tabIndex + "-" + dvIndex).css({
			"display": "none"
		});
	}
	
	var listHeight = $("#staList" + tabIndex).height();
	var viewHeight = $("#staView" + tabIndex).height();
	if(listHeight > viewHeight){
		$("#tab" + tabIndex).css({
			"height": listHeight + 10
		});
	}else{
		$("#tab" + tabIndex).css({
			"height": viewHeight + 10
		});
	}
	Tab.adjustHeight(tabIndex);
};

Sta.closeAllChart = function(tabIndex,dvIndex){
	var l = Common.chartIndex[tabIndex][dvIndex].length;
	for(var i = 0; i < l - 1; i++){
		var chartIndex = Common.chartIndex[tabIndex][dvIndex][0];
		Sta.closeChart(tabIndex,dvIndex,chartIndex);
	}
};

/*****magnify one chart*****/
Sta.magnifier = function(tabIndex,dvIndex,chartIndex,chartType){
	Common.background();
	Common.largeChart();
	Sta.createFrame();
	Common.chartType[tabIndex][dvIndex][chartIndex] = chartType;
	$.getJSON(Common.dataviewUrl(),{
		featuretype: "DistributionFeature"
	},function(data){
		var values = data[dvIndex].values;
		var len = values.length;
		var count = 0;
		var pieIndex = -1;
		if((chartType == "columnChart") || (chartType == "lineChart")){
			for(var i = 0; i < len; i++){
				$("<input type = 'checkbox' id = 'checkbox" + i + "' " +
					"onclick = \"Sta.setYAxis(" + tabIndex + "," + dvIndex + "," + chartIndex + "," + i + ");\"/>" +
					values[i] + "<br/>").appendTo("#lcCheckbox");
				if(Common.yAxis[tabIndex][dvIndex][chartIndex][i] == true){
					$("#checkbox" + i).prop("checked",true);
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
		
		$("<img src = 'css/images/close_256x256.png' onclick = \"Sta.closeFrame(" + tabIndex + "," + dvIndex + "," + chartIndex + ");\"/>")
			.appendTo("#icon_lc")
			.css({
				"float": "right",
				"width": "16px",
				"margin-top": "5px",
				"margin-right": "5px"
			});
		$("<img src = 'css/images/change_100x100.jpg' " +
			"onclick = \"Sta.magnifier(" + tabIndex + "," + dvIndex + "," + chartIndex + ",'" + nextType + "');\"/>")
			.appendTo("#icon_lc")
			.css({
				"float": "right",
				"width": "16px",
				"margin-top": "5px",
				"margin-right": "5px"
			});
		
		Sta.showChart(tabIndex,dvIndex,chartIndex,"lcContainer");
	}).error(function(){
		alert("Oops, we got an error...");
		return;
	});
};

/*****change chart by y axis*****/
Sta.setYAxis = function(tabIndex,dvIndex,chartIndex,index){
	if(Common.yAxis[tabIndex][dvIndex][chartIndex][index] == true){
		Common.yAxis[tabIndex][dvIndex][chartIndex][index] = false;
	}else{
		Common.yAxis[tabIndex][dvIndex][chartIndex][index] = true;
	}
	$("#lcContainer").empty();
	Sta.showChart(tabIndex,dvIndex,chartIndex,"lcContainer");
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
Sta.closeFrame = function(tabIndex,dvIndex,chartIndex){
	$("#background").css("display","none");
	$("#largeChart").css("display","none");
	$("#sta-chart-container-" + tabIndex + "-" + dvIndex + "-" + chartIndex).empty();
	$("#magnifier-" + tabIndex + "-" + dvIndex + "-" + chartIndex).remove();
	Sta.showChart(tabIndex,dvIndex,chartIndex,"chartContainer");
};

/*****show one chart*****/
Sta.showChart = function(tabIndex,dvIndex,chartIndex,ccName){
	$.getJSON(Common.dataviewUrl(),{
		featuretype: "DistributionFeature"
	},function(data){
		var id = data[dvIndex].id;
		var dvName = data[dvIndex].dataviewName;
		var keys = data[dvIndex].identifiers;
		var values = data[dvIndex].values;
		var len = values.length;
		var key = new Array();
		var value = new Array();
		
		$.getJSON(Common.dvDataUrl(),{
			id: id
		},function(data){
			var l = data.length;
			for(var i = 0; i < l; i++){
				key[i] = data[i].indentifiers[0].value;
				value[i] = new Array();
				for(var j = 0; j < len; j++){
					value[i][j] = data[i].values[j].value;
				}
			}
			l = key.length;
			
			var arr = "[";
			var chartType = Common.chartType[tabIndex][dvIndex][chartIndex];
			if((chartType == "columnChart") || (chartType == "lineChart")){
				arr += "[\"" + keys[0] + "\"";
				for(var i = 0; i < len; i++){
					if(Common.yAxis[tabIndex][dvIndex][chartIndex][i] == true){
						arr += ",\"" + values[i] + "\"";
					}
				}
				arr += "],";
				for(var i = 0; i < l; i++){
					arr += "[\"" + key[i] + "\"";
					for(var j = 0; j < len; j++){
						if(Common.yAxis[tabIndex][dvIndex][chartIndex][j] == true){
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
				$("<img id = 'magnifier-" + tabIndex + "-" + dvIndex + "-" + chartIndex + "' src = 'css/images/magnifier_256x256.png' " +
					"onclick = \"Sta.magnifier(" + tabIndex + "," + dvIndex + "," + chartIndex + ",'" + chartType + "');\"/>")
					.appendTo("#sta-view-chart-" + tabIndex + "-" + dvIndex + "-" + chartIndex)
					.css({
						"position": "absolute",
						"right": "5px",
						"bottom": "5px",
						"width": "16px"
					});
				view = document.getElementById("sta-chart-container-" + tabIndex + "-" + dvIndex + "-" + chartIndex);
			}else{
				view = document.getElementById(ccName);
			}
			
			var data = google.visualization.arrayToDataTable(jsonArr);
			var options = new Object;
			if(ccName == "chartContainer"){
				options = {};
			}else{
				options = {
					title: dvName
				};
			}
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