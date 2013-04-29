getLocationUrl = "http://10.1.1.55:8081/mdap/rest/dsg/getgeods/";

var map;
var locinfo;

function mapInitialize(num){
	mapOptions = {
		center: new google.maps.LatLng(39.90960456049752,116.3972282409668),
		zoom: 15,
		scaleControl: true,
		scaleControlOptions: {
			position: google.maps.ControlPosition.BOTTOM_LEFT
		},
		mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	map = new google.maps.Map(document.getElementById("content" + num),mapOptions);
}

function geoInformation(name,num){
	url = getLocationUrl + name;
	pinIcon = new google.maps.MarkerImage(
		"http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|FF0000",
		null, /* size is determined at runtime */
		null, /* origin is 0,0 */
		null, /* anchor is bottom center of the scaled image */
		new google.maps.Size(10.5,17)
	);
	$.getJSON(url,function(data){
		for(i = 0; i < data.length; i++){
			mrkrArr = new google.maps.Marker({
				position : new google.maps.LatLng(data[i].latitude,data[i].longitude),
				title : data[i].tag,
				icon : pinIcon
			});
			mrkrArr.setMap(map);
		}
	}).error(function(){
		alert("Oops, we got an error...");
	});
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
//瀵逛簬姣忕粍鍖哄煙閲岀殑鐐�鐢诲渾灏監K浜�
function setRegion(){
	
}

function hideRegion(){
}