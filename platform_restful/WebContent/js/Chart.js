getStatisticsUrl = "http://10.1.1.55:8081/mdap/rest/ds/getstatistic/";
staKey = new Array();
staName = new Array();
staValue = new Array();

function getStatisticsInfo(name){
	url = getStatisticsUrl + name;
	staKey = new Array();
	staName = new Array();
	staValue = new Array();
	$.getJSON(url,function(data){
		staData = data.jStatistic;
		l = staData.length;
		for(i = 0; i < l; i++){
			staKey[i] = staData[i].key;
			if (name == "FilteredByCT_Domain")
				staValue[i] = staData[i].value[4];
			else
				staValue[i] = staData[i].value[0];
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
			var ccccc = document.getElementById('chart');
			chart = new google.visualization.PieChart(ccccc);
			chart.draw(data,options);
		}
	}).error(function(){
		alert("Oops, we got an error...");
	});
}