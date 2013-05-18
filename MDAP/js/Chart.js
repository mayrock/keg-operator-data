staKey = new Array();
staValue = new Array();
staSchema = new Array();
Chart = {};

Chart.clickEvent = function(tabNum,dsIndex){
	if($("#checkbox" + tabNum + "_" + dsIndex).is(":checked") == true){
		checkedEvent();
	}else{
		uncheckedEvent();
	}
	
	staKey = new Array();
	staValue = new Array();
	
	function checkedEvent(){
		$.ajaxSettings.async = false;
		$.getJSON(Common.datasetUrl + "stadss",function(data){
			name = data[dsIndex].datasetName;
			des = data[dsIndex].description;
			type = "";
			schema = data[dsIndex].schema;
			/**********to do**********/
//			type = "pieChart";
		}).error(function(){
			alert("Oops, we got an error...");
		});
		len = schema.length;
		view_ds = document.createElement("div");
		view_ds.setAttribute("id","view_ds" + tabNum + "_" + dsIndex);
		view_ds.setAttribute("class","view_ds");
		$(view_ds).appendTo("#view" + tabNum);
		$("<p>" + des + "</p>").appendTo(view_ds);
		/**********this is case of lv1**********/
		/**********case of lv2 need to do some change**********/
		view_chart = document.createElement("div");
		view_chart.setAttribute("id","view_chart" + tabNum + "_" + dsIndex + "_" + 0);
		view_chart.setAttribute("class","view_chart");
		$(view_chart).appendTo(view_ds);
		if(type == "pieChart"){
			for(i = 0; i < len - 1; i++){
				view_pie = document.createElement("div");
				view_pie.setAttribute("id","view_pie" + tabNum + "_" + dsIndex + "_" + 0 + "_" + i);
				view_pie.setAttribute("class","view_pie");
				$(view_pie).appendTo(view_chart);
			}
		}
		$.ajaxSettings.async = false;
		$.getJSON(Common.staDataUrl + name,function(data){
			l = data.length;
			for(i = 0; i < l; i++){
				staKey[i] = data[i].key;
				staValue[i] = new Array();
				for(j = 0; j < len; j++){
					staValue[i][j] = data[i].value[j];
				}
			}
		}).error(function(){
			alert("Oops, we got an error...");
		});
		if(type == "pieChart"){
			for(i = 0; i < len - 1; i++){
				pieChart(0,i);
			}
		}
	}
	
	function pieChart(chartIndex,pieIndex){
		google.load("visualization","1",{packages:["corechart"],"callback":drawPieChart});
		
		function drawPieChart(){
			arr = "[";
			arr += "[\"" + "key" + "\",\"" + "value" + "\"],";
			l = staKey.length;
			for(i = 0; i < l - 1; i++){
				arr += "[\"" + staKey[i] + "\"," + staValue[i][subIndex] + "],";
			}
			arr += "[\"" + staKey[l - 1]  + "\"," + staValue[l - 1][subIndex] + "]]";
			jsonArr = $.parseJSON(arr);
			data = google.visualization.arrayToDataTable(jsonArr);
			options = {
				title: staSchema[subIndex + 1] + " / " + staSchema[0]
			};
			view = document.getElementById("view_pie" + tabNum + "_" + dsIndex + "_" + chartIndex + "_" + pieIndex);
			chart = new google.visualization.PieChart(view);
			chart.draw(data,options);
		}
	}
	
	function lineChart(){}
	
	function columnChart(){}
	
	function barChart(){}
	
	function uncheckedEvent(){
		$("#view_ds" + tabNum + "_" + dsIndex).remove();
	}
}