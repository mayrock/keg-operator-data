getStatisticsUrl = "http://10.1.1.55:8081/mdap/rest/dsg/getstads/";

staKey = new Array();
staValue = new Array();

function staInformation(name,num){
//	url = getStatisticsUrl + name + "?jsoncallback=?";
	url = getStatisticsUrl + name;
	$.getJSON(url,function(data){
		l = data.length;
		len = data[0].value.length;
		for(j = 0; j < len; j++){
			staValue[j] = new Array();
		}
		for(i = 0; i < l; i++){
			staKey[i] = data[i].key;
			for(j = 0; j < len; j++){
				staValue[i][j] = data[i].value[j];
			}
		}
		if(len == 1){
		}else{
		}
		google.load("visualization","1",{packages:["corechart"],"callback":drawPieChart});
		function drawPieChart(){
			arr = "[";
			arr += "[\"" + "key" + "\",\"" + "value" + "\"],";
			for(i = 0; i < l - 1; i++){
				arr += "[\"" + staKey[i] + "\"," + staValue[i] + "],";
			}
			arr += "[\"" + staKey[l - 1]  + "\"," + staValue[l - 1] + "]]";
			jsonArr = $.parseJSON(arr);
			data = google.visualization.arrayToDataTable(jsonArr);
			options = {
				title: name
			};
			view = document.getElementById("view" + num);
			chart = new google.visualization.PieChart(view);
			chart.draw(data,options);
		}
	}).error(function(){
		alert("Oops, we got an error...");
	});
}

function drawPieChart(){
}

function drawLineChart(){
}

function drawColumnChart(){
}

function drawBarChart(){
}