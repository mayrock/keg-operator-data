Sta = {};

/**********initialize when loading page**********/
Sta.init = function(){
	lchart_h = 480;
	lchart_w = 800;
	lcContainer_w = 600;
	$("#largeChart").css({
		"position": "absolute",
		"margin-top": (Common.height() - lchart_h) / 2,
		"margin-right": (Common.width() - lchart_w) / 2,
		"margin-bottom": (Common.height() - lchart_h) / 2,
		"margin-left": (Common.width() - lchart_w) / 2,
		"border": "1px solid #000000",
		"height": lchart_h,
		"width": lchart_w,
		"z-index": "1000",
		"background-color": "white",
		"display": "none"
	});
	$("#lcContainer").css({
		"position": "absolute",
		"top": 0,
		"left": 0,
		"height": lchart_h,
		"width": lcContainer_w
	});
	$("#lcCheckbox").css({
		"position": "absolute",
		"right": 0,
		"bottom": 0,
		"height": lchart_h - 40,
		"width": lchart_w - lcContainer_w
	});
	$("#lcBackground").css({
		"position": "absolute",
		"margin-top": (Common.height() - lchart_h) / 2,
		"margin-right": (Common.width() - lchart_w) / 2,
		"margin-bottom": (Common.height() - lchart_h) / 2,
		"margin-left": (Common.width() - lchart_w) / 2,
		"height": lchart_h,
		"width": lchart_w,
		"filter": "alpha(opacity = 60)",
		"opacity": "0.6",
		"z-index": "1001",
		"background-color": "gray",
		"display": "none"
	});
};

/**********create one chart container of a dataset**********/
Sta.createChart = function(tabIndex,dsIndex,chartType){
	var chartIndex = Common.chartIndex[tabIndex][dsIndex];
	if(Common.chartCount[tabIndex][dsIndex] == 0){
		$("#view_ds" + tabIndex + "_" + dsIndex).css("display","block");
	}
	Common.chartCount[tabIndex][dsIndex] ++;
	Common.chartIndex[tabIndex][dsIndex] ++;
	var des = new Object;
	var schema = new Object;
	$.ajaxSettings.async = false;
	$.getJSON(Common.datasetUrl() + "stadss",function(data){
		des = data[dsIndex].description;
		schema = data[dsIndex].schema;
	}).error(function(){
		alert("Oops, we got an error...");
	});
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
	if(chartIndex != 0){
		var len = schema.length;
		Common.staControl[tabIndex][dsIndex][chartIndex] = new Array();
		for(var i = 0; i < len - 1; i++){
			Common.staControl[tabIndex][dsIndex][chartIndex][i] = true;
		}
	}
	Sta.showChart(tabIndex,dsIndex,chartIndex,chartType,"chartContainer");
};

/**********close one chart container**********/
Sta.closeChart = function(tabIndex,dsIndex,chartIndex){
	$("#view_chart" + tabIndex + "_" + dsIndex + "_" + chartIndex).remove();
	Common.chartCount[tabIndex][dsIndex] --;
	if($("#view_ds" + tabIndex + "_" + dsIndex).width() <= (Common.width() - 320)){
		$("#view_ds" + tabIndex + "_" + dsIndex).css("width","auto");
		$("#view_ds" + tabIndex + "_" + dsIndex).css("overflow","visible");
	}
	if(Common.chartCount[tabIndex][dsIndex] == 0){
		$("#view_ds" + tabIndex + "_" + dsIndex).css("display","none");
	}
};

/**********magnify one chart**********/
Sta.magnifier = function(tabIndex,dsIndex,chartIndex,chartType){
	Sta.createFrame();
	$.ajaxSettings.async = false;
	var schema = new Object;
	$.getJSON(Common.datasetUrl() + "stadss",function(data){
		schema = data[dsIndex].schema;
	}).error(function(){
		alert("Oops, we got an error...");
	});
	var len = schema.length;
	var count = 0;
	var pieIndex;
	if((chartType == "columnChart") || (chartType == "lineChart")){
		for(var i = 0; i < len - 1; i++){
			$("<input type = 'checkbox' id = 'checkbox" + tabIndex + "_" + dsIndex + "_" + chartIndex + "_" + i + "' onclick = \"Sta.yAxis(" + tabIndex + "," + dsIndex + "," + chartIndex + "," + i + ",'" + chartType + "');\"/>" + schema[i + 1] + "<br/>").appendTo("#lcCheckbox");
			if(Common.staControl[tabIndex][dsIndex][chartIndex][i] == true){
				$("#checkbox" + tabIndex + "_" + dsIndex + "_" + chartIndex + "_" + i).prop("checked",true);
				pieIndex = i;
				count ++;
			}
		}
	}else{
		$("#lcCheckbox").empty();
	}
	if(chartType == "columnChart"){
		nextType = "lineChart";
	}else if(chartType == "lineChart"){
		if(count == 1){
			nextType = pieIndex;
		}else{
			nextType = "columnChart";
		}
	}else{
		nextType = "columnChart"
	}
	$("<img src = 'css/images/save.png' onclick = \"Sta.save(" + tabIndex + "," + dsIndex + "," + chartIndex + ",'" + chartType + "');\"/>")
		.appendTo("#icon_lc")
		.css({
			"position": "absolute",
			"top": "5px",
			"right": "45px"
		});
	$("<img src = 'css/images/switch.png' onclick = \"Sta.magnifier(" + tabIndex + "," + dsIndex + "," + chartIndex + ",'" + nextType + "');\"/>")
		.appendTo("#icon_lc")
		.css({
			"position": "absolute",
			"top": "5px",
			"right": "25px"
		});
	$("<img src = 'css/images/close.png' onclick = \"Sta.closeFrame(" + tabIndex + "," + dsIndex + "," + chartIndex + ",'" + chartType + "');\"/>")
		.appendTo("#icon_lc")
		.css({
			"position": "absolute",
			"top": "5px",
			"right": "5px"
		}).hover(
			function(){$(this).attr("src","css/images/close_hover.png");},
			function(){$(this).attr("src","css/images/close.png");}
		);
	Sta.showChart(tabIndex,dsIndex,chartIndex,chartType,"lcContainer");
};

/**********save one chart into my favorite**********/
Sta.save = function(tabIndex,dsIndex,chartIndex,chartType){
	var count;
	if($.cookie("fav_count") == null){
		count = 0;
	}else{
		count = parseInt($.cookie("fav_count"));
	}
	$.cookie("fav_count",count + 1,{expires: 7,path: "/"});
	var data = "{";
	data += "\"dsIndex\":" + dsIndex + ",";
	data += "\"chartType\":\"" + chartType + "\",";
	data += "\"staControl\":[";
	var len = Common.staControl[tabIndex][dsIndex][chartIndex].length;
	for(var i = 0; i < len; i++){
		data += Common.staControl[tabIndex][dsIndex][chartIndex][i];
		if(i == len - 1){
			data += "]";
		}else{
			data += ",";
		}
	}
	data += "}";
	$.cookie("fav_data" + count,JSON.stringify(data),{expires: 7,path: "/"});
	$("#myFavInfo").css("display","block");
	$("#lcBackground").css("display","block");
};

/**********save the name of chart**********/
Sta.confirm = function(){
	name = $("#name_fav").val();
	des = $("#des_fav").val();
	var count;
	count = parseInt($.cookie("fav_count"));
	count --;
	$.cookie("fav_name" + count,name,{expires: 7,path: "/"});
	$.cookie("fav_des" + count,des,{expires: 7,path: "/"});
	$("#name_fav").val("my favorite");
	$("#myFavInfo").css("display","none");
	$("#lcBackground").css("display","none");
};

/**********change chart by y axis**********/
Sta.yAxis = function(tabIndex,dsIndex,chartIndex,index,chartType){
	if(Common.staControl[tabIndex][dsIndex][chartIndex][index] == true){
		Common.staControl[tabIndex][dsIndex][chartIndex][index] = false;
	}else if(Common.staControl[tabIndex][dsIndex][chartIndex][index] == false){
		Common.staControl[tabIndex][dsIndex][chartIndex][index] = true;
	}
	$("#lcContainer").empty();
	Sta.showChart(tabIndex,dsIndex,chartIndex,chartType,"lcContainer");
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
Sta.closeFrame = function(tabIndex,dsIndex,chartIndex,chartType){
	$("#background").css("display","none");
	$("#largeChart").css("display","none");
	$("#chartContainer" + tabIndex + "_" + dsIndex + "_" + chartIndex).empty();
	$("#magnifier" + tabIndex + "_" + dsIndex + "_" + chartIndex).remove();
	Sta.showChart(tabIndex,dsIndex,chartIndex,chartType,"chartContainer");
};

/**********show one chart**********/
Sta.showChart = function(tabIndex,dsIndex,chartIndex,chartType,ccName){
	$.ajaxSettings.async = false;
	var des = new Object;
	var name = new Object;
	var schema = new Object;
	$.getJSON(Common.datasetUrl() + "stadss",function(data){
		des = data[dsIndex].description;
		name = data[dsIndex].datasetName;
		schema = data[dsIndex].schema;
	}).error(function(){
		alert("Oops, we got an error...");
	});
	var len = schema.length;
	var key = new Array();
	var value = new Array();
	$.ajaxSettings.async = false;
	$.getJSON(Common.staDataUrl() + name,function(data){
		var l = data.length;
		for(var i = 0; i < l; i++){
			/**********the key of these data**********/
//			key[i] = data[i].key;
			key[i] = i;
			value[i] = new Array();
			for(var j = 0; j < len - 1; j++){
				value[i][j] = data[i].value[j];
			}
		}
	}).error(function(){
		alert("Oops, we got an error...");
		return;
	});
	var l = key.length;
	var arr = "[";
	if((chartType == "columnChart") || (chartType == "lineChart")){
		arr += "[\"" + "key" + "\"";
		for(var i = 0; i < len; i++){
			if(Common.staControl[tabIndex][dsIndex][chartIndex][i] == true){
				arr += ",\"" + schema[i + 1] + "\"";
			}
		}
		arr += "],";
		for(var i = 0; i < l; i++){
			arr += "[\"" + key[i] + "\"";
			for(var j = 0; j < len; j++){
				if(Common.staControl[tabIndex][dsIndex][chartIndex][j] == true){
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
	if(ccName == "chartContainer"){
		$("<img id = 'magnifier" + tabIndex + "_" + dsIndex + "_" + chartIndex + "' src = 'css/images/magnifier.png' onclick = \"Sta.magnifier(" + tabIndex + "," + dsIndex + "," + chartIndex + ",'" + chartType + "');\"/>")
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
	google.load("visualization","1",{packages:["corechart"],"callback":drawChart});
	
	function drawChart(){
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
	}
};