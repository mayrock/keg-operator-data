var map = new Object;
getLocationUrl = "http://10.1.1.55:8081/mdap/rest/dataset/getdatasetlocation/";

function mapInitialize(){
	mapOptions = {
		center: new google.maps.LatLng(39.8825,116.3873),
		zoom: 10,
		scaleControl: true,
		scaleControlOptions: {
			position: google.maps.ControlPosition.BOTTOM_LEFT
		},
		mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	map = new google.maps.Map(document.getElementById("map"),mapOptions);
/*	width = document.documentElement.clientWidth;alert(width);
//	<style type="text/css"></style>
	doc = document;
	style = doc.createElement("style");
	style.setAttribute("type","text/css");
	if(style.styleSheet){//IE
		style.styleSheet.cssText = cssString;
	}else{//w3c
		var cssText = doc.createTextNode(cssString);
		style.appendChild(cssText);
	}
	var head = doc.getElementsByTagName("head");
	if(head.length)
15
        heads[0].appendChild(style);
16
    else
17
        doc.documentElement.appendChild(style);
18
}
*/
	getDataset("Geo");
}
function getGeoInformation(name){
	url = getLocationUrl + name;
	pinIcon = new google.maps.MarkerImage(
		"http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|FF0000",
		null, /* size is determined at runtime */
		null, /* origin is 0,0 */
		null, /* anchor is bottom center of the scaled image */
		new google.maps.Size(10.5,17)
	);
	mapOptions = {
			center: new google.maps.LatLng(39.8825,116.3873),
			zoom: 10,
			scaleControl: true,
			scaleControlOptions: {
				position: google.maps.ControlPosition.BOTTOM_LEFT
			},
			mapTypeId: google.maps.MapTypeId.ROADMAP
		};
		map = new google.maps.Map(document.getElementById("map"),mapOptions);
	$.getJSON(url,function(data){
		locinfo = data.jLocation;
		for(i = 0; i < locinfo.length; i++){
			var marker = new google.maps.Marker({
				position : new google.maps.LatLng(locinfo[i].latitude,locinfo[i].longitude),
				title : locinfo[i].tag,
				icon : pinIcon
			});
			marker.setMap(map);
		}
	}).error(function(){
		alert("Oops, we got an error...");
	});
}

function changeOption(){
	alert("Sorry, this function hasn't been achieved!");
}