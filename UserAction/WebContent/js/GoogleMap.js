var track_loc_data = new Object;
track_loc_data.locinfo = new Array();

function getLocData() {
	var lat = new Array();
	var lng = new Array();
	$.ajax({
		type : "get",
		url : "GetLoc.action",
		success : function(msg) {
		var result = eval("(" + msg + ")");
		var loc_list = result.locinfo;
		for(i = 0;i < loc_list.length;i++) {
			track_loc_data.locinfo[i] = loc_list[i];
		}
		showMarks();
	}
	});
}

function showMarks() {
	var mapOptions = {
			center: new google.maps.LatLng(40.003834809598516,116.3263213634491),
			zoom: 16,
			mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	var map = new google.maps.Map(document.getElementById("map_canvas"),mapOptions);
	for (var i = 0; i < track_loc_data.locinfo.length; i++) {
		var mark = new google.maps.Marker({
			position : new google.maps.LatLng(
					track_loc_data.locinfo[i].lat,track_loc_data.locinfo[i].lng),
			map : map,
			title : track_loc_data.locinfo[i].time.toString()
		});
	}
	var path = [new google.maps.LatLng(
			track_loc_data.locinfo[0].lat,track_loc_data.locinfo[0].lng)];
	var userPath = new google.maps.Polyline({
		path: path,
		strokeColor: "#FF0000",
		strokeOpacity: 1.0,
		strokeWeight: 2
	});
	userPath.setMap(map);
	for (var i = 1; i < track_loc_data.locinfo.length; i++) {
		path = userPath.getPath();
		path.push(new google.maps.LatLng(
				track_loc_data.locinfo[i].lat,track_loc_data.locinfo[i].lng));
		userPath.setMap(map);
	}
}