getDatasetUrl = "http://10.1.1.55:8081/mdap/rest/dataset/getdatasets";
getDescriptionUrl = "http://10.1.1.55:8081/mdap/rest/dataset/getdatasetfields/";

function identifyId(id,tab){
	if(id == "Geo"){
		getDataset();
	}
	else if(id == "Sta") alert("Sorry, this function hasn't been achieved!");
	else alert("Oops, we got an error...");
}

function getDataset(id){
	if(id == "Geo"){
		url = getDatasetUrl;
		$.getJSON(url,function(data){
			dataset = data.jDatasetName;
			ul = document.getElementById('geoDataset');
			for(i = 0; i < dataset.length; i++){
				name = dataset[i].datasetName;
				li = document.createElement("li");
				li.innerHTML = "<li><a id = \"" + name + "\" href = \"javascript:void(0);\" onclick = \"getDescription('" + name + "'," + i + ");\">" + name + "</li>";
				ul.insertBefore(li,ul.childNodes[0]);
			}
		}).error(function(){
			alert("Oops, we got an error...");
		});
	}
}

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
}