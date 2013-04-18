var map = new Object;
getDatasetUrl = "http://10.1.1.55:8081/mdap/rest/ds/getgeodatasets";
getLocationUrl = "http://10.1.1.55:8081/mdap/rest/ds/getlocation/";

function initialize(){
	mapInitialize();
	width = document.documentElement.clientWidth;
	url = getDatasetUrl;
	$.getJSON(url,function(data){
		dataset = data.jDatasetName;
		ul = document.getElementById('geoDataset');
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
}

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
	mapInitialize();
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