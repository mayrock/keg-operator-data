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
			schema = data[dsIndex].schema;
			/**********need to do**********/
			/**********which type should be set to this dataset**********/
			type = "pieChart";
		}).error(function(){
			alert("Oops, we got an error...");
		});
		len = schema.length;
		view_ds = document.createElement("div");
		view_ds.setAttribute("id","view_ds" + tabNum + "_" + dsIndex);
		view_ds.setAttribute("class","view_ds");
		$(view_ds).appendTo("#view" + tabNum);
		$("<p>" + des + "</p>").appendTo(view_ds);
		/**********need to do**********/
		/**********this is case of lv1**********/
		/**********case of lv2 need to do some change**********/
		view_chart = document.createElement("div");
		view_chart.setAttribute("id","view_chart" + tabNum + "_" + dsIndex + "_" + 0);
		view_chart.setAttribute("class","view_chart");
		$(view_chart).appendTo(view_ds);
		$("<div style = 'clear:both;'></div>").appendTo(view_ds);
		if(type == "pieChart"){
			for(var i = 0; i < len - 1; i++){
				pieContainer = document.createElement("div");
				pieContainer.setAttribute("id","pieContainer" + tabNum + "_" + dsIndex + "_" + 0 + "_" + i);
				pieContainer.setAttribute("class","pieContainer");
				$(pieContainer).appendTo(view_chart);
				view_pie = document.createElement("div");
				view_pie.setAttribute("id","view_pie" + tabNum + "_" + dsIndex + "_" + 0 + "_" + i);
				view_pie.setAttribute("class","view_pie");
				$(view_pie).appendTo(pieContainer);
			}
		}
		Common.staKey[tabNum][dsIndex] = new Array();
		Common.staValue[tabNum][dsIndex] = new Array();
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
		}
	}
	
	function lineChart(){}
	
	function columnChart(){}
	
	function barChart(){}
	
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
			$("#account_bg").css("display","block");
			$("#largeChart").css("display","block");
			view = document.getElementById(containerName);
		}else{
			view = document.getElementById(containerName + tabNum + "_" + dsIndex + "_" + chartIndex + "_" + pieIndex);
		}
		chart = new google.visualization.PieChart(view);
		chart.draw(data,options);
	}
};

Chart.closeFrame = function(){
	$("#account_bg").css("display","none");
	$("#largeChart").css("display","none");
};