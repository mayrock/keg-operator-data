Sta = {};

Sta.accordion = function(tabIndex,type){
	var head = document.createElement("h3");
	head.setAttribute("id","sta-" + type + "-head-" + tabIndex);
	head.setAttribute("class","head");
	$(head).appendTo("#accordion" + tabIndex);
	$(head).css({
		"padding-top": 0,
		"padding-right": "10px",
		"padding-bottom": 0,
	});
	if(type == 0){
		$(head).html("data view");
	}else{
		$(head).html("data set");
	}
	var content = document.createElement("div");
	content.setAttribute("id","sta-" + type + "-content-" + tabIndex);
	content.setAttribute("class","content");
	$(content).appendTo("#accordion" + tabIndex);
	$(content).css({
		"padding-top": 0,
		"padding-right": 0,
		"padding-bottom": 0,
		"padding-left": "20px"
	});
	
	var accordion = document.createElement("div");
	accordion.setAttribute("id","sta-" + type + "-accordion-" + tabIndex);
	accordion.setAttribute("class","accordion");
	$(accordion).appendTo(content);
	Sta.subAcc(tabIndex,type,0);//public
	Sta.subAcc(tabIndex,type,1);//limit
	Sta.subAcc(tabIndex,type,2);//own
	$("#sta-" + type + "-accordion-" + tabIndex).accordion({
		collapsible: true,
		activate: function(event,ui){
			var activeTab = $("#tabs").tabs("option","active");
			var tabIndex = Common.tabIndex[activeTab];
			var type = $("#accordion" + tabIndex).accordion("option","active");
			if(typeof(type) == "boolean"){
				return;
			}
			var permit = $("#sta-" + type + "-accordion-" + tabIndex).accordion("option","active");
			if(typeof(permit) == "boolean"){
				$("#staList" + tabIndex).css({
					"width": "200px"
				});
				var subAccHeight = $("#sta-" + type + "-accordion-" + tabIndex).height();
				$("#sta-" + type + "-content-" + tabIndex).css({
					"height": subAccHeight + 10
				});
				var staListHeight = $("#staList" + tabIndex).height();
				$("#tab" + tabIndex).css({
					"height": staListHeight + 10
				});
				Tab.adjustHeight(tabIndex);
				return;
			}
			Sta.adjustAccWidth(tabIndex,type,permit);
			var listHeight = $("#sta-" + type + "-" + permit + "-list-" + tabIndex).height();
			$("#sta-" + type + "-" + permit + "-content-" + tabIndex).css({
				"height": listHeight + 10
			});
			var subAccHeight = $("#sta-" + type + "-accordion-" + tabIndex).height();
			$("#sta-" + type + "-content-" + tabIndex).css({
				"height": subAccHeight + 10
			});
			var staListHeight = $("#staList" + tabIndex).height();
			$("#tab" + tabIndex).css({
				"height": staListHeight + 10
			});
			Tab.adjustHeight(tabIndex);
		}
	});
	
	Sta.loadList(tabIndex,type,0);
	Sta.loadList(tabIndex,type,1);
	Sta.loadList(tabIndex,type,2);
	$("#sta-" + type + "-accordion-" + tabIndex).accordion("option","active",false);
};

Sta.adjustAccWidth = function(tabIndex,type,permit){
	$("#staList" + tabIndex).css({
		"width": "100%"
	});
	var a = $("#sta-" + type + "-" + permit + "-list-" + tabIndex).find("a");
	var max = 0;
	for(var i = 0; i < a.length; i++){
		if(a.eq(i).width() > max){
			max = a.eq(i).width();
		}
	}
	if(max == 0){
		max = 125;
	}
	$("#sta-" + type + "-" + permit + "-list-" + tabIndex).css({
		"width": max + 10
	});
	$("#staList" + tabIndex).css({
		"width": max + 75
	});
};

Sta.subAcc = function(tabIndex,type,permit){
	var head = document.createElement("h3");
	head.setAttribute("id","sta-" + type + "-" + permit + "-head-" + tabIndex);
	head.setAttribute("class","head");
	$(head).appendTo("#sta-" + type + "-accordion-" + tabIndex);
	$(head).css({
		"padding-top": 0,
		"padding-right": "10px",
		"padding-bottom": 0
	});
	if(permit == 0){
		$(head).html("public data");
	}else if(permit == 1){
		$(head).html("limit data");
	}else{
		$(head).html("own data");
	}
	var content = document.createElement("div");
	content.setAttribute("id","sta-" + type + "-" + permit + "-content-" + tabIndex);
	content.setAttribute("class","content");
	$(content).appendTo("#sta-" + type + "-accordion-" + tabIndex);
	$(content).css({
		"padding-top": "5px",
		"padding-right": 0,
		"padding-bottom": 0,
		"padding-left": "20px"
	});
	
	var list = document.createElement("div");
	list.setAttribute("id","sta-" + type + "-" + permit + "-list-" + tabIndex);
	list.setAttribute("class","list");
	$(list).appendTo(content);
};

Sta.loadList = function(tabIndex,type,permit){
	var url = "";
	var msg = "{}";
	var username = Common.username;
	if(type == 0){
		url = Common.dataviewUrl().replace("tabType","sta");
	}else{
		if(permit == 0){
			url = Common.pubDsUrl();
		}else if(permit == 1){
			url = Common.limDsUrl();
			msg = "{\"userid\":\"" + username + "\"}";
		}else{
			url = Common.limDsUrl();
			msg = "{\"userid\":\"" + username + "\"}";
		}
	}
	$.getJSON(url,$.parseJSON(msg),function(data){
		var len = data.length;
		for(var i = 0; i < len; i++){
			var length = Common.chartIndex[tabIndex].length;
			Common.chartIndex[tabIndex][length] = new Array();
			Common.chartIndex[tabIndex][length][0] = 0;
			Common.chartType[tabIndex][length] = new Array();
			Common.yAxis[tabIndex][length] = new Array();
			
			var des = data[i].descriptionZh;
			var dsName = document.createElement("div");
			dsName.setAttribute("id","sta-" + type + "-" + permit + "-dsName-" + tabIndex + "-" + length);
			dsName.setAttribute("class","dsName");
			$(dsName).appendTo("#sta-" + type + "-" + permit + "-list-" + tabIndex);
			$("<a href = 'javascript:void(0);' onClick = \"Sta.guide(" + tabIndex + "," + i + ");\">" + des + "</a>").appendTo(dsName);
		}
	}).error(function(){
		alert("Oops, we got an error...");
	});
};

/*****initialize chartType and yAxis of a chart*****/
Sta.guide = function(tabIndex,dsIndex){
	/*****Cross-domain requests and dataType: "jsonp" requests do not support synchronous operation.*****/
	$.getJSON(Common.dataviewUrl().replace("tabType","sta"),function(data){
		var values = data[dsIndex].values;
		var len = values.length;
		var l = Common.chartIndex[tabIndex][dsIndex].length;
		var chartIndex = Common.chartIndex[tabIndex][dsIndex][l - 1];
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
	var l = Common.chartIndex[tabIndex][dsIndex].length;
	$("#view_ds" + tabIndex + "_" + dsIndex).css({
		"width": 304 * (l - 1)
	});
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
	var l = Common.chartIndex[tabIndex][dsIndex].length;
	$("#view_ds" + tabIndex + "_" + dsIndex).css({
		"width": 304 * (l - 1)
	});
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
			$("#accordion" + tabIndex).accordion("option","active",dsIndex);
		}).error(function(){
			alert("Oops, we got an error...");
			return;
		});
	}).error(function(){
		alert("Oops, we got an error...");
	});
};