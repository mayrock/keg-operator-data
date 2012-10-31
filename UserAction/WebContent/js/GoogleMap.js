var track_loc_data = new Object;
track_loc_data.locinfo = new Array();

function getLocData(imsi,begin,end) {
	$.ajax({
		type : "get",
		url : "GetLoc.action",
		data : "imsi = " + imsi + "&begin = " + begin + "&end = " + end,
		success : function(msg) {
		var result = eval("(" + msg + ")");
		var loc_list = result.locinfo;
		if(loc_list.length == 0) {
			alert("No trace or your input is wrong");
			cleanInfo();
		}
		for(var i = 0;i < loc_list.length;i++) {
			track_loc_data.locinfo[i] = loc_list[i];
		}
		showMarks();
	}
	});
}

function showMarks() {
	var mapOptions = {
			center: new google.maps.LatLng(
					track_loc_data.locinfo[0].lat,track_loc_data.locinfo[0].lng),
			zoom: 12,
			mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	var map = new google.maps.Map(document.getElementById("map"),mapOptions);
	for (var i = 0; i < track_loc_data.locinfo.length; i++) {
		var mark = new google.maps.Marker({
			position : new google.maps.LatLng(
					track_loc_data.locinfo[i].lat,track_loc_data.locinfo[i].lng),
			map : map,
			title : track_loc_data.locinfo[i].msg
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

function cleanInfo() {
	var mapOptions = {
			center: new google.maps.LatLng(40.003834809598516,116.3263213634491),
			zoom: 16,
			mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	var map = new google.maps.Map(document.getElementById("map"),mapOptions);
	track_loc_data = new Object;
	track_loc_data.locinfo = new Array();
}