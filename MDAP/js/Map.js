getDatasetUrl = "http://10.1.1.55:8088/platform_restful/rest/dsg/get";
getLocationUrl = "http://10.1.1.55:8088/platform_restful/rest/dsg/getgeods/";

var map = new Object;
var mrkrArr = new Array();

function mapInitialize(tabNum,len){
	$("#view" + tabNum).css("width","800px");
	$("#view" + tabNum).css("height","400px");
	$("#view" + tabNum).css("border-bottom","1px solid #000000");
	mapOptions = {
		center: new google.maps.LatLng(39.90960456049752,116.3972282409668),
		zoom: 15,
		scaleControl: true,
		scaleControlOptions: {
			position: google.maps.ControlPosition.BOTTOM_LEFT
		},
		mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	map = new google.maps.Map(document.getElementById("view" + tabNum),mapOptions);
	for(i = 0; i < len; i++){
		mrkrArr[i] = new Array();
	}
}

function geoInformation(tabName,tabNum,index){
	input = document.getElementById("checkbox" + tabNum).getElementsByTagName("input");
	if(input[index].checked == true){
		setGeoChecked(tabName,index);
	}else{
		setGeoUnchecked(index);
	}
}

function setGeoChecked(tabName,index){
	url = getDatasetUrl + tabName + "dss";
	$.ajaxSettings.async = false;
	$.getJSON(url,function(data){
		name = data[index].datasetName;
	}).error(function(){
		alert("Oops, we got an error...");
	});
	url = getLocationUrl + name;
	image = "css/images/tower.png";
	$.ajaxSettings.async = false;
	$.getJSON(url,function(data){
		for(i = 0; i < data.length; i++){
			mrkrArr[index][i] = new google.maps.Marker({
				position : new google.maps.LatLng(data[i].latitude,data[i].longitude),
				title : data[i].tag,
				icon : image
			});
			mrkrArr[index][i].setMap(map);
		}
	}).error(function(){
		alert("Oops, we got an error...");
	});
}

function setGeoUnchecked(index){
	for(i = 0; i < mrkrArr[index].length; i++){
		mrkrArr[index][i].setMap(null);
	}
}

function setMarker(){

}

function hideMarker(){

}
/*
function setTrace(){
	path = [new google.maps.LatLng(locinfo[0].latitude,locinfo[0].longitude)];
	trace = new google.maps.Polyline({
		path: path,
		strokeColor: "#FF0000",
		strokeOpacity: 1.0,
		strokeWeight: 2
	});
	for(i = 1; i < locinfo.length; i++){
		path = trace.getPath();
		path.push(new google.maps.LatLng(locinfo[i].latitude,locinfo[i].longitude));
	}
	trace.setMap(map);
}

function hideTrace(){
	trace.setMap(null);
}
*/
//对于每组区域里的点,画圆就OK了
function setRegion(){
	
}

function hideRegion(){
}