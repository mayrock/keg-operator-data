getDatasetUrl = "http://10.1.1.55:8088/platform_restful/rest/dsg/get";
getStatisticsUrl = "http://10.1.1.55:8088/platform_restful/rest/dsg/getstads/";

staKey = new Array();
staValue = new Array();
staSchema = new Array();

function staInformation(tabName,tabNum,index){
	input = document.getElementById("checkbox" + tabNum).getElementsByTagName("input");
	if(input[index].checked == true){
		setStaChecked(tabName,tabNum,index);
	}else{
		setStaUnchecked(tabNum,index);
	}
}

function setStaChecked(tabName,tabNum,index){
	url = getDatasetUrl + tabName + "dss";
	$.ajaxSettings.async = false;
	$.getJSON(url,function(data){
		name = data[index].datasetName;
		des = data[index].description;
		
	}).error(function(){
		alert("Oops, we got an error...");
	});
	len = staSchema.length;
	view = document.getElementById("view" + tabNum);
	subView = document.createElement("div");
	subView.setAttribute("id","subView" + tabNum + "_" + index);
	subView.setAttribute("class","subView");
	$("<p>" + des + "</p>").appendTo(subView);
	for(i = 0; i < len - 1; i++){
		chartContainer = document.createElement("div");
		chartContainer.setAttribute("id","chartContainer" + tabNum + "_" + index + "_" + i);
		chartContainer.setAttribute("class","chartContainer");
		$(chartContainer).appendTo(subView);
	}
	$(subView).appendTo(view);
	url = getStatisticsUrl + name;
	$.ajaxSettings.async = false;
	$.getJSON(url,function(data){
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
	for(i = 0; i < len - 1; i++){
		pieChart(tabNum,index,i);
	}
}

function setStaUnchecked(tabNum,index){
	$("#subView" + tabNum + "_" + index).remove();
}

function pieChart(tabNum,index,subIndex){
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
		view = document.getElementById("chartContainer" + tabNum + "_" + index + "_" + subIndex);
		chart = new google.visualization.PieChart(view);
		chart.draw(data,options);
	}
}

function lineChart(){
}

function columnChart(){
}

function barChart(){
}