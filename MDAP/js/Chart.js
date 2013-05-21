Chart = {};

Chart.clickEvent = function(tabNum,dsIndex){
	if($("#checkbox" + tabNum + "_" + dsIndex).is(":checked") == true){
		checkedEvent();
	}else{
		uncheckedEvent();
	}
	
	function checkedEvent(){
		$.ajaxSettings.async = false;
		$.getJSON(Common.datasetUrl() + "stadss",function(data){
			name = data[dsIndex].datasetName;
			des = data[dsIndex].description;
			type = "";
			Common.staSchema[tabNum][dsIndex] = data[dsIndex].schema;
			/**********need to do**********/
			/**********which type should be set to this dataset**********/
			type = "columnChart";
		}).error(function(){
			alert("Oops, we got an error...");
		});
		len = Common.staSchema[tabNum][dsIndex].length;
		view_ds = document.createElement("div");
		view_ds.setAttribute("id","view_ds" + tabNum + "_" + dsIndex);
		view_ds.setAttribute("class","view_ds");
		$(view_ds).appendTo("#view" + tabNum);
		$("<p>" + des + "</p>").appendTo(view_ds);
		/**********need to do**********/
		/**********this is case of lv1**********/
		/**********case of lv2 need to do some change**********/
		chartContainer = document.createElement("div");
		chartContainer.setAttribute("id","chartContainer" + tabNum + "_" + dsIndex + "_" + 0);
		chartContainer.setAttribute("class","chartContainer");
		$(chartContainer).appendTo(view_ds);
		$("<div style = 'clear:both;'></div>").appendTo(view_ds);
		if(type == "pieChart"){
			for(var i = 0; i < len - 1; i++){
				pieContainer = document.createElement("div");
				pieContainer.setAttribute("id","pieContainer" + tabNum + "_" + dsIndex + "_" + 0 + "_" + i);
				pieContainer.setAttribute("class","pieContainer");
				$(pieContainer).appendTo(chartContainer);
				view_pie = document.createElement("div");
				view_pie.setAttribute("id","view_pie" + tabNum + "_" + dsIndex + "_" + 0 + "_" + i);
				view_pie.setAttribute("class","view_pie");
				$(view_pie).appendTo(pieContainer);
			}
		}else{
			$(chartContainer).css({
				"position": "relative",
				"width": "240px",
				"height": "120px",
				"border-right": "1px solid #0000FF"
			});
			view_chart = document.createElement("div");
			view_chart.setAttribute("id","view_chart" + tabNum + "_" + dsIndex + "_" + 0);
			view_chart.setAttribute("class","view_chart");
			$(view_chart).appendTo(chartContainer);
		}
		Common.staKey[tabNum][dsIndex] = new Array();
		Common.staValue[tabNum][dsIndex] = new Array();
		Common.staControl[tabNum][dsIndex] = new Array();
		$.ajaxSettings.async = false;
		$.getJSON(Common.staDataUrl() + name,function(data){
			l = data.length;
			for(var i = 0; i < l; i++){
				/**********need to do**********/
				/**********the key of these data**********/
//				Common.staKey[tabNum][dsIndex][i] = data[i].key;
				Common.staKey[tabNum][dsIndex][i] = i;
				Common.staValue[tabNum][dsIndex][i] = new Array();
				for(var j = 0; j < len - 1; j++){
					Common.staValue[tabNum][dsIndex][i][j] = data[i].value[j];
				}
			}
		}).error(function(){
			alert("Oops, we got an error...");
		});
		if(type == "pieChart"){
			for(var i = 0; i < len - 1; i++){
				Chart.pie("view_pie",tabNum,dsIndex,0,i);
				$("<img src = 'css/images/magnifier.png' onclick = \"Chart.pie('lcContainer'," + tabNum + "," + dsIndex + "," + 0 + "," + i + ");\"/>")
					.appendTo("#pieContainer" + tabNum + "_" + dsIndex + "_" + 0 + "_" + i)
					.css({
						"position": "absolute",
						"right": "5px",
						"bottom": "5px"
					});
			}
		}else if(type == "columnChart"){
			for(var i = 0; i < len - 1; i++){
				Common.staControl[tabNum][dsIndex][i] = true;
			}
			Chart.column("view_chart",tabNum,dsIndex,0);
			$("<img src = 'css/images/magnifier.png' onclick = \"Chart.largeColumn('lcContainer'," + tabNum + "," + dsIndex + "," + 0 + ");\"/>")
				.appendTo("#chartContainer" + tabNum + "_" + dsIndex + "_" + 0)
				.css({
					"position": "absolute",
					"right": "5px",
					"bottom": "5px"
				});
		}else if(type == "lineChart"){
			for(var i = 0; i < len - 1; i++){
				Common.staControl[tabNum][dsIndex][i] = true;
			}
			Chart.line("view_chart",tabNum,dsIndex,0);
			$("<img src = 'css/images/magnifier.png' onclick = \"Chart.largeLine('lcContainer'," + tabNum + "," + dsIndex + "," + 0 + ");\"/>")
				.appendTo("#chartContainer" + tabNum + "_" + dsIndex + "_" + 0)
				.css({
					"position": "absolute",
					"right": "5px",
					"bottom": "5px"
				});
		}else{
			alert("Oops, we got an error...");
		}
	}
	
	function uncheckedEvent(){
		$("#view_ds" + tabNum + "_" + dsIndex).remove();
	}
};

Chart.pie = function(containerName,tabNum,dsIndex,chartIndex,pieIndex){
	google.load("visualization","1",{packages:["corechart"],"callback":drawPieChart});
	
	function drawPieChart(){
		var l = Common.staKey[tabNum][dsIndex].length;
		arr = "[";
		arr += "[\"" + "key" + "\",\"" + "value" + "\"],";
		for(var i = 0; i < l - 1; i++){
			arr += "[\"" + Common.staKey[tabNum][dsIndex][i] + "\"," + Common.staValue[tabNum][dsIndex][i][pieIndex] + "],";
		}
		arr += "[\"" + Common.staKey[tabNum][dsIndex][l - 1]  + "\"," + Common.staValue[tabNum][dsIndex][l - 1][pieIndex] + "]]";
		jsonArr = $.parseJSON(arr);
		data = google.visualization.arrayToDataTable(jsonArr);
		options = {
			/**********need to do**********/
			/**********the title of pie chart**********/
//			title: staSchema[pieIndex + 1] + " / " + staSchema[0]
		};
		if(containerName == "lcContainer"){
			Chart.createFrame();
			$("<img src = 'css/images/switch.png' onclick = \"Chart.change('" + containerName + "'," + tabNum + "," + dsIndex + "," + chartIndex + ",'column');\"/>")
		.appendTo("#switchIcon")
		.css({
			"position": "absolute",
			"top": "5px",
			"right": "25px"
		});
			view = document.getElementById(containerName);
		}else{
			view = document.getElementById(containerName + tabNum + "_" + dsIndex + "_" + chartIndex + "_" + pieIndex);
		}
		chart = new google.visualization.PieChart(view);
		chart.draw(data,options);
	}
};

Chart.column = function(containerName,tabNum,dsIndex,chartIndex){
	google.load("visualization","1",{packages:["corechart"],"callback":drawColumnChart});
	
	function drawColumnChart(){
		var l = Common.staKey[tabNum][dsIndex].length;
		var len = Common.staControl[tabNum][dsIndex].length;
		arr = "[";
		arr += "[\"" + "key" + "\"";
		for(var i = 0; i < len; i++){
			if(Common.staControl[tabNum][dsIndex][i] == true){
				arr += ",\"" + Common.staSchema[tabNum][dsIndex][i + 1] + "\"";
			}
		}
		arr += "],";
		for(var i = 0; i < l; i++){
			arr += "[\"" + Common.staKey[tabNum][dsIndex][i] + "\"";
			for(var j = 0; j < len; j++){
				if(Common.staControl[tabNum][dsIndex][j] == true){
					arr += "," + Common.staValue[tabNum][dsIndex][i][j];
				}
			}
			arr += "]";
			if(i == l - 1){
				arr += "]";
			}else{
				arr += ",";
			}
		}
		jsonArr = $.parseJSON(arr);
		data = google.visualization.arrayToDataTable(jsonArr);
		options = {
			/**********need to do**********/
			/**********the title of pie chart**********/
//			title: staSchema[pieIndex + 1] + " / " + staSchema[0]
		};
		if(containerName == "lcContainer"){
			view = document.getElementById(containerName);
		}else{
			view = document.getElementById(containerName + tabNum + "_" + dsIndex + "_" + chartIndex);
		}
		chart = new google.visualization.ColumnChart(view);
		chart.draw(data,options);
	}
};

Chart.line = function(containerName,tabNum,dsIndex,chartIndex){
	google.load("visualization","1",{packages:["corechart"],"callback":drawLineChart});
	
	function drawLineChart(){
		var l = Common.staKey[tabNum][dsIndex].length;
		var len = Common.staControl[tabNum][dsIndex].length;
		arr = "[";
		arr += "[\"" + "key" + "\"";
		for(var i = 0; i < len; i++){
			if(Common.staControl[tabNum][dsIndex][i] == true){
				arr += ",\"" + Common.staSchema[tabNum][dsIndex][i + 1] + "\"";
			}
		}
		arr += "],";
		for(var i = 0; i < l; i++){
			arr += "[\"" + Common.staKey[tabNum][dsIndex][i] + "\"";
			for(var j = 0; j < len; j++){
				if(Common.staControl[tabNum][dsIndex][j] == true){
					arr += "," + Common.staValue[tabNum][dsIndex][i][j];
				}
			}
			arr += "]";
			if(i == l - 1){
				arr += "]";
			}else{
				arr += ",";
			}
		}
		jsonArr = $.parseJSON(arr);
		data = google.visualization.arrayToDataTable(jsonArr);
		options = {
			/**********need to do**********/
			/**********the title of pie chart**********/
//			title: staSchema[pieIndex + 1] + " / " + staSchema[0]
		};
		if(containerName == "lcContainer"){
			view = document.getElementById(containerName);
		}else{
			view = document.getElementById(containerName + tabNum + "_" + dsIndex + "_" + chartIndex);
		}
		chart = new google.visualization.LineChart(view);
		chart.draw(data,options);
	}
};

Chart.largeColumn = function(containerName,tabNum,dsIndex,chartIndex){
	var len = Common.staControl[tabNum][dsIndex].length;
	for(var i = 0; i < len; i++){
		$("<input type = 'checkbox' id = 'checkbox" + tabNum + "_" + dsIndex + "_" + i + "' onclick = \"Chart.controlColumn('lcContainer'," + tabNum + "," + dsIndex + "," + 0 + "," + i + ");\"/>" + Common.staSchema[tabNum][dsIndex][i + 1] + "<br/>").appendTo("#lcCheckbox");
		if(Common.staControl[tabNum][dsIndex][i] == true){
			$("#checkbox" + tabNum + "_" + dsIndex + "_" + i).attr("checked",true);
		}
	}
	$("<img src = 'css/images/switch.png' onclick = \"Chart.change('" + containerName + "'," + tabNum + "," + dsIndex + "," + chartIndex + ",'line');\"/>")
		.appendTo("#switchIcon")
		.css({
			"position": "absolute",
			"top": "5px",
			"right": "25px"
		});
	Chart.column(containerName,tabNum,dsIndex,chartIndex);
	Chart.createFrame();
};

Chart.largeLine = function(containerName,tabNum,dsIndex,chartIndex){
	var len = Common.staControl[tabNum][dsIndex].length;
	var count = 0;
	var pieIndex;
	for(var i = 0; i < len; i++){
		$("<input type = 'checkbox' id = 'checkbox" + tabNum + "_" + dsIndex + "_" + i + "' onclick = \"Chart.controlLine('lcContainer'," + tabNum + "," + dsIndex + "," + 0 + "," + i + ");\"/>" + Common.staSchema[tabNum][dsIndex][i + 1] + "<br/>").appendTo("#lcCheckbox");
		if(Common.staControl[tabNum][dsIndex][i] == true){
			$("#checkbox" + tabNum + "_" + dsIndex + "_" + i).attr("checked",true);
			pieIndex = i;
			count ++;
		}
	}
	if(count == 1){
		changeType = pieIndex;
	}else{
		changeType = "column";
	}
	$("<img src = 'css/images/switch.png' onclick = \"Chart.change('" + containerName + "'," + tabNum + "," + dsIndex + "," + chartIndex + ",'" + changeType + "');\"/>")
		.appendTo("#switchIcon")
		.css({
			"position": "absolute",
			"top": "5px",
			"right": "25px"
		});
	Chart.line(containerName,tabNum,dsIndex,chartIndex);
	Chart.createFrame();
};

Chart.controlColumn = function(containerName,tabNum,dsIndex,chartIndex,checkboxIndex){
	if($("#checkbox" + tabNum + "_" + dsIndex + "_" + checkboxIndex).is(":checked") == true){
		Common.staControl[tabNum][dsIndex][checkboxIndex] = true;
	}else{
		Common.staControl[tabNum][dsIndex][checkboxIndex] = false;
	}
	$("#lcContainer").empty();
	Chart.column(containerName,tabNum,dsIndex,chartIndex);
};

Chart.controlLine = function(containerName,tabNum,dsIndex,chartIndex,checkboxIndex){
	if($("#checkbox" + tabNum + "_" + dsIndex + "_" + checkboxIndex).is(":checked") == true){
		Common.staControl[tabNum][dsIndex][checkboxIndex] = true;
	}else{
		Common.staControl[tabNum][dsIndex][checkboxIndex] = false;
	}
	$("#lcContainer").empty();
	Chart.line(containerName,tabNum,dsIndex,chartIndex);
};

Chart.createFrame = function(){
	$("#account_bg").css("display","block");
	$("#largeChart").css("display","block");
};

Chart.closeFrame = function(){
	$("#lcCheckbox").empty();
	$("#switchIcon").empty();
	$("#lcContainer").empty();
	$("#account_bg").css("display","none");
	$("#largeChart").css("display","none");
};

Chart.change = function(containerName,tabNum,dsIndex,chartIndex,changeType){
	Chart.closeFrame();
	if(changeType == "column"){
		Chart.largeColumn(containerName,tabNum,dsIndex,chartIndex);
	}else if(changeType == "line"){
		Chart.largeLine(containerName,tabNum,dsIndex,chartIndex);
	}else{
		pieIndex = parseInt(changeType);
		Chart.pie(containerName,tabNum,dsIndex,chartIndex,pieIndex);
	}
};