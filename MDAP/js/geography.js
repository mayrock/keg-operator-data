Geo = {};

/*****initialize map of one tab*****/
Geo.initMap = function(tabIndex){
	var optionWidth = $("#option" + tabIndex).width() + 80;
	$("#view" + tabIndex).css("width",Common.width() - optionWidth);
	$("#view" + tabIndex).css("height","400px");
	$("#view" + tabIndex).css("border","1px solid #000000");
	mapOptions = {
		center: new google.maps.LatLng(39.90960456049752,116.3972282409668),
		zoom: 12,
		scaleControl: true,
		scaleControlOptions: {
			position: google.maps.ControlPosition.BOTTOM_LEFT
		},
		mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	Common.mapArr[tabIndex] = new google.maps.Map(document.getElementById("view" + tabIndex),mapOptions);
	Common.mapInfoArr[tabIndex] = new Array();
};

/*****load data of one dataset*****/
Geo.loadData = function(tabIndex,dsIndex,name,type){
	Common.mapInfoArr[tabIndex][dsIndex] = new Array();
	$.getJSON(Common.dvDataUrl().replace("tabType","geo"),{
		dataset: name
	},function(data){
		for(var i = 0; i < data.length; i++){
			if(type == "points"){
				Common.mapInfoArr[tabIndex][dsIndex][i] = new google.maps.Marker({
					position : new google.maps.LatLng(data[i].latitude,data[i].longitude),
					title : data[i].tag
					/*****get a good icon image*****/
//					icon : "css/images/.png"
				});
			}else{
				Common.mapInfoArr[tabIndex][dsIndex][i] = new google.maps.Circle({
					/*****make region looks better*****/
					strokeColor: "gray",
					strokeOpacity: 1.0,
					strokeWeight: 2,
					fillColor: "gray",
					fillOpacity: 1.0,
					center: new google.maps.LatLng(data[i].latitude,data[i].longitude),
					radius: 250
				});
			}
		}
	}).error(function(){
		alert("Oops, we got an error...");
		return;
	});
};

/*****choose one dataset*****/
Geo.clickEvent = function(tabIndex,dsIndex){
	if($("#checkbox" + tabIndex + "_" + dsIndex).is(":checked") == true){
		for(var i = 0; i < Common.mapInfoArr[tabIndex][dsIndex].length; i++){
			Common.mapInfoArr[tabIndex][dsIndex][i].setMap(Common.mapArr[tabIndex]);
		}
	}else{
		for(var i = 0; i < Common.mapInfoArr[tabIndex][dsIndex].length; i++){
			Common.mapInfoArr[tabIndex][dsIndex][i].setMap(null);
		}
	}
};

/*****select all the datasets*****/
Geo.selectAll = function(tabIndex,len){
	for(var i = 0; i < len; i++){
		if($("#checkbox" + tabIndex + "_" + i).is(":checked") == false){
			$("#checkbox" + tabIndex + "_" + i).prop("checked",true);
			Geo.clickEvent(tabIndex,i);
		}
	}
};

/*****invert all the datasets*****/
Geo.invertAll = function(tabIndex,len){
	for(var i = 0; i < len; i++){
		if($("#checkbox" + tabIndex + "_" + i).is(":checked") == false){
			$("#checkbox" + tabIndex + "_" + i).prop("checked",true);
		}else{
			$("#checkbox" + tabIndex + "_" + i).prop("checked",false);
		}
		Geo.clickEvent(tabIndex,i);
	}
};