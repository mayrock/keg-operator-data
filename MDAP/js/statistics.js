Sta = {};

Sta.lcHeight = function(){return 480;};
Sta.lcWidth = function(){return 800;};
Sta.lcCntrWidth = function(){return 600;};

/**********initialize # related to sta**********/
Sta.init = function(){
	$("#largeChart").css({
		"position": "absolute",
		"margin-top": (Common.height() - Sta.lcHeight()) / 2,
		"margin-right": (Common.width() - Sta.lcWidth()) / 2,
		"margin-bottom": (Common.height() - Sta.lcHeight()) / 2,
		"margin-left": (Common.width() - Sta.lcWidth()) / 2,
		"border": "1px solid #000000",
		"height": Sta.lcHeight(),
		"width": Sta.lcWidth(),
		"z-index": "1000",
		"background-color": "white",
		"display": "none"
	});
	$("#lcContainer").css({
		"position": "absolute",
		"top": 0,
		"left": 0,
		"height": Sta.lcHeight(),
		"width": Sta.lcCntrWidth()
	});
	$("#lcCheckbox").css({
		"position": "absolute",
		"right": 0,
		"bottom": 0,
		"height": Sta.lcHeight() - 40,
		"width": Sta.lcWidth() - Sta.lcCntrWidth()
	});
};

/**********initialize chartType and yAxis of a chart**********/
Sta.guide = function(tabIndex,dsIndex){
	/**********Cross-domain requests and dataType: "jsonp" requests do not support synchronous operation.**********/
	$.getJSON(Common.datasetUrl().replace("tabType","sta"),function(data){
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

/**********create one chart container of a dataset**********/
Sta.createChart = function(tabIndex,dsIndex,chartIndex){
	view_chart = document.createElement("div");
	view_chart.setAttribute("id","view_chart" + tabIndex + "_" + dsIndex + "_" + chartIndex);
	view_chart.setAttribute("class","view_chart");
	$(view_chart).appendTo("#view_ds" + tabIndex + "_" + dsIndex);
	$("#special" + tabIndex + "_" + dsIndex).remove();
	$("<div id = 'special" + tabIndex + "_" + dsIndex + "' style = 'clear:both;'></div>").appendTo("#view_ds" + tabIndex + "_" + dsIndex);
	if($("#view_ds" + tabIndex + "_" + dsIndex).width() > (Common.width() - 345)){
		$("#view_ds" + tabIndex + "_" + dsIndex).css("width",Common.width() - 345);
		$("#view_ds" + tabIndex + "_" + dsIndex).css("overflow","auto");
	}
	chartContainer = document.createElement("div");
	chartContainer.setAttribute("id","chartContainer" + tabIndex + "_" + dsIndex + "_" + chartIndex);
	chartContainer.setAttribute("class","chartContainer");
	$(chartContainer).appendTo(view_chart);
	$("<img src = 'css/images/close.png' onclick = \"Sta.closeChart(" + tabIndex + "," + dsIndex + "," + chartIndex + ");\"/>")
		.appendTo("#view_chart" + tabIndex + "_" + dsIndex + "_" + chartIndex)
		.css({
			"position": "absolute",
			"top": "5px",
			"right": "5px"
		}).hover(
			function(){$(this).attr("src","css/images/close_hover.png");},
			function(){$(this).attr("src","css/images/close.png");}
		);
	Common.footer();
	Sta.showChart(tabIndex,dsIndex,chartIndex,"chartContainer");
};

/**********close one chart container**********/
Sta.closeChart = function(tabIndex,dsIndex,chartIndex){
	Common.clearFooter();
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
	if($("#view_ds" + tabIndex + "_" + dsIndex).width() <= (Common.width() - 345)){
		$("#view_ds" + tabIndex + "_" + dsIndex).css("width","auto");
		$("#view_ds" + tabIndex + "_" + dsIndex).css("overflow","visible");
	}
	l = Common.chartIndex[tabIndex][dsIndex].length;
	if(l == 1){
		$("#view_ds" + tabIndex + "_" + dsIndex).css("display","none");
	}
	Common.footer();
};

/**********magnify one chart**********/
Sta.magnifier = function(tabIndex,dsIndex,chartIndex,chartType){
	Common.background();
	Sta.init();
	Sta.createFrame();
	Common.chartType[tabIndex][dsIndex][chartIndex] = chartType;
	$.getJSON(Common.datasetUrl().replace("tabType","sta"),function(data){
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
		/**********set the next chart type**********/
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
		$("<img src = 'css/images/switch.png' onclick = \"Sta.magnifier(" + tabIndex + "," + dsIndex + "," + chartIndex + ",'" + nextType + "');\"/>")
			.appendTo("#icon_lc")
			.css({
				"position": "absolute",
				"top": "5px",
				"right": "25px"
			});
		$("<img src = 'css/images/close.png' onclick = \"Sta.closeFrame(" + tabIndex + "," + dsIndex + "," + chartIndex + ");\"/>")
			.appendTo("#icon_lc")
			.css({
				"position": "absolute",
				"top": "5px",
				"right": "5px"
			}).hover(
				function(){$(this).attr("src","css/images/close_hover.png");},
				function(){$(this).attr("src","css/images/close.png");}
			);
		Sta.showChart(tabIndex,dsIndex,chartIndex,"lcContainer");
	}).error(function(){
		alert("Oops, we got an error...");
	});
};

/**********change chart by y axis**********/
Sta.setYAxis = function(tabIndex,dsIndex,chartIndex,index){
	if(Common.yAxis[tabIndex][dsIndex][chartIndex][index] == true){
		Common.yAxis[tabIndex][dsIndex][chartIndex][index] = false;
	}else if(Common.yAxis[tabIndex][dsIndex][chartIndex][index] == false){
		Common.yAxis[tabIndex][dsIndex][chartIndex][index] = true;
	}else{
		/**********need to do**********/
	}
	$("#lcContainer").empty();
	Sta.showChart(tabIndex,dsIndex,chartIndex,"lcContainer");
};

/**********open window for a magnified chart**********/
Sta.createFrame = function(){
	$("#lcContainer").empty();
	$("#lcCheckbox").empty();
	$("#icon_lc").empty();
	if($("#background").css("display") == "none"){
		$("#background").css("display","block");
		$("#largeChart").css("display","block");
	}
};

/**********close window**********/
Sta.closeFrame = function(tabIndex,dsIndex,chartIndex){
	$("#background").css("display","none");
	$("#largeChart").css("display","none");
	$("#chartContainer" + tabIndex + "_" + dsIndex + "_" + chartIndex).empty();
	$("#magnifier" + tabIndex + "_" + dsIndex + "_" + chartIndex).remove();
	Sta.showChart(tabIndex,dsIndex,chartIndex,"chartContainer");
};

/**********show one chart**********/
Sta.showChart = function(tabIndex,dsIndex,chartIndex,ccName){
	$.getJSON(Common.datasetUrl().replace("tabType","sta"),function(data){
		var des = data[dsIndex].description;
		var name = data[dsIndex].datasetName;
		var values = data[dsIndex].values;
		var len = values.length;
		var key = new Array();
		var value = new Array();
		$.getJSON(Common.dsDataUrl().replace("tabType","sta").replace("dsName",name),function(data){
			var l = data.length;
			for(var i = 0; i < l; i++){
				key[i] = data[i].key[0];
				value[i] = new Array();
				for(var j = 0; j < len; j++){
					value[i][j] = data[i].value[j];
				}
			}
			l = key.length;
			/**/
			var arr = "[";
			chartType = Common.chartType[tabIndex][dsIndex][chartIndex];
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
				pieIndex = parseInt(chartType);
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
			/**/
			if(ccName == "chartContainer"){
				$("<img id = 'magnifier" + tabIndex + "_" + dsIndex + "_" + chartIndex + "' src = 'css/images/magnifier.png' " +
				"onclick = \"Sta.magnifier(" + tabIndex + "," + dsIndex + "," + chartIndex + ",'" + chartType + "');\"/>")
				.appendTo("#view_chart" + tabIndex + "_" + dsIndex + "_" + chartIndex)
				.css({
					"position": "absolute",
					"right": "5px",
					"bottom": "5px"
				});
				view = document.getElementById(ccName + tabIndex + "_" + dsIndex + "_" + chartIndex);
			}else if(ccName == "lcContainer"){
				view = document.getElementById(ccName);
			}else{
				/**********need to do**********/
			}
			
			var data = google.visualization.arrayToDataTable(jsonArr);
			var options = {
				title: des
			};
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