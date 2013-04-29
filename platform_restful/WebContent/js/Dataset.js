//getDescriptionUrl = "http://10.1.1.55:8081/mdap/rest/dataset/getdatasetfields/";

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