getDatasetUrl = "http://10.1.1.55:8081/mdap/rest/ds/get";
getDescriptionUrl = "http://10.1.1.55:8081/mdap/rest/dataset/getdatasetfields/";

function getDataset(id,tab){
//	getDatasetUrl = "http://10.1.1.55:8081/mdap/rest/ds/get";
//	url = getDatasetUrl + id + "datasets";
	if($(tab).hasClass("here")){
		return;
	}
	$(tab).addClass("here");
	$(tab).parent().siblings().children("a").removeClass();
	if(id == "geo"){
		url = "http://10.1.1.55:8081/mdap/rest/ds/getgeodatasets";
		$("#geo").css("display","block");
		$("#map").css("display","block");
		$("#sta").css("display","none");
		$("#chart").css("display","none");
		$.getJSON(url,function(data){
			dataset = data.jDatasetName;
			ul = document.getElementById('geoDataset');
			l = ul.childNodes.length;
			for(i = 0; i < l; i++){
				ul.removeChild(ul.childNodes[0]);
			}
			for(i = 0; i < dataset.length; i++){
				des = dataset[i].description;
				name = dataset[i].datasetName;
				li = document.createElement("li");
				li.innerHTML = "<li><a id = \"" + name + "\" href = \"javascript:void(0);\" onclick = \"getGeoInformation('" + name + "');\">" + des + "</li>";
				ul.insertBefore(li,ul.childNodes[0]);
			}
		}).error(function(){
			alert("Oops, we got an error...");
		});
	}else if(id == "sta"){
		url = "http://10.1.1.55:8081/mdap/rest/ds/getstadatasets";
		$("#sta").css("display","block");
		$("#chart").css("display","block");
		$("#geo").css("display","none");
		$("#map").css("display","none");
		$.getJSON(url,function(data){
			dataset = data.jDatasetName;
			ul = document.getElementById('staDataset');
			l = ul.childNodes.length;
			for(i = 0; i < l; i++){
				ul.removeChild(ul.childNodes[0]);
			}
			for(i = 0; i < dataset.length; i++){
				des = dataset[i].description;
				name = dataset[i].datasetName;
				li = document.createElement("li");
				li.innerHTML = "<li><a id = \"" + name + "\" href = \"javascript:void(0);\" onclick = \"getStatisticsInfo('" + name + "');\">" + des + "</li>";
				ul.insertBefore(li,ul.childNodes[0]);
			}
		}).error(function(){
			alert("Oops, we got an error...");
		});
	}else{
		alert("Oops, we got an error...");
	}
}
/*
function getDescription(name,n){
	url = getDescriptionUrl + name;
	$.getJSON(url,function(data){
		describe = data.jFieldName;
		ul = document.getElementById('geoDataset');
		l = ul.childNodes.length;
		ul.removeChild(ul.childNodes[l - n - 1]);
		li = document.createElement("li");
		li.innerHTML = "<li><a id = \"" + name + "\" href = \"javascript:void(0);\" onclick = \"getGeoInformation('" + name + "');\">" + name + "<br/>";
		for(i = 0; i < describe.length; i++){
			field = describe[i].fieldName;
			if(i != describe.length - 1){
				li.innerHTML += field + " ";
			}else{
				li.innerHTML += field + "</li>";
			}
		}
		ul.insertBefore(li,ul.childNodes[l - n - 1]);
	}).error(function(){
		alert("Oops, we got an error...");
	});
	getGeoInformation(name);
}*/