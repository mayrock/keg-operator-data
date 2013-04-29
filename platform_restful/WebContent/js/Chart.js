getStatisticsUrl = "http://10.1.1.55:8081/mdap/rest/dsg/getstads/";

function staInformation(name,num){
	url = getStatisticsUrl + name;
	staKey = new Array();
	staName = new Array();
	staValue = new Array();
	$.getJSON(url,function(data){
		l = data.length;
		for(i = 0; i < l; i++){
			staKey[i] = data[i].key;
			if (name == "FilteredByCT_Domain")
				staValue[i] = data[i].value[4];
			else
				staValue[i] = data[i].value[0];
		}
		google.load("visualization","1",{packages:["corechart"],"callback":drawPieChart});
		function drawPieChart(){
	arr = "[";
			arr += "[\"" + "key" + "\",\"" + "value" + "\"],";
			for(i = 0; i < l-1; i++){
				arr += "[\"" + staKey[i] + "\"," + staValue[i] + "],";
			}
			arr += "[\"" + staKey[l-1]  + "\"," + staValue[l-1] + "]]";
			var arrr = $.parseJSON(arr);
			data = google.visualization.arrayToDataTable(arrr);
			options = {
				title: name
			};
			var ccccc = document.getElementById("content" + num);
			chart = new google.visualization.PieChart(ccccc);
			chart.draw(data,options);
}
	}).error(function(){
		alert("Oops, we got an error...");
	});
}



function drawLineChart(){
}

function drawColumnChart(){
}

function drawBarChart(){
}