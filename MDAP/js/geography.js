Geo = {};

/*****initialize map of one tab*****/
Geo.initMap = function(tabIndex){
	var optionWidth = $("#geo-option-" + tabIndex).width() + 80;
	$("#geo-view-" + tabIndex).css("width",Common.width() - optionWidth);
	$("#geo-view-" + tabIndex).css("height","400px");
	$("#geo-view-" + tabIndex).css("border","1px solid #000000");
	var mapOptions = {
		center: new google.maps.LatLng(39.90960456049752,116.3972282409668),
		zoom: 12,
		scaleControl: true,
		scaleControlOptions: {
			position: google.maps.ControlPosition.BOTTOM_LEFT
		},
		mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	Common.mapArr[tabIndex] = new google.maps.Map(document.getElementById("geo-view-" + tabIndex),mapOptions);
};

/*****load data of one dataset*****/
Geo.loadData = function(tabIndex,dsIndex,dvID,type){
	Common.mapInfoArr[tabIndex][dsIndex] = new Array();
	$.getJSON(Common.dvDataUrl(),{
		id: dvID
	},function(data){
		for(var i = 0; i < data.length; i++){
			if(type == "points"){
				var arr = "";
				for(var j = 0; j < data[i].values.length; j++){
					arr += data[i].values[j].value + "\n";
				}
				Common.mapInfoArr[tabIndex][dsIndex][i] = new google.maps.Marker({
					position : new google.maps.LatLng(data[i].indentifiers[0].value,data[i].indentifiers[1].value),
					title : arr,
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
					center: new google.maps.LatLng(data[i].indentifiers[0].value,data[i].indentifiers[1].value),
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
	if($("#geo-checkbox-" + tabIndex + "-" + dsIndex).is(":checked") == true){
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
		if($("#geo-checkbox-" + tabIndex + "-" + i).is(":checked") == false){
			$("#geo-checkbox-" + tabIndex + "-" + i).prop("checked",true);
			Geo.clickEvent(tabIndex,i);
		}
	}
};

/*****invert all the datasets*****/
Geo.invertAll = function(tabIndex,len){
	for(var i = 0; i < len; i++){
		if($("#geo-checkbox-" + tabIndex + "-" + i).is(":checked") == false){
			$("#geo-checkbox-" + tabIndex + "-" + i).prop("checked",true);
		}else{
			$("#geo-checkbox-" + tabIndex + "-" + i).prop("checked",false);
		}
		Geo.clickEvent(tabIndex,i);
	}
};