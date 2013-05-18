Map = {};

Map.initialize = function(tabNum){
	$("#view" + tabNum).css("width",Common.width());
	$("#view" + tabNum).css("height",Common.height());
	$("#view" + tabNum).css("border","1px solid #000000");
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
	Common.mrkrArr[tabNum] = new Array();
	return map;
}

Map.loadData = function(tabNum,dsIndex,type){
	Common.mrkrArr[tabNum][dsIndex] = new Array();
	$.ajaxSettings.async = false;
	$.getJSON(Common.latlngUrl + name,function(data){
		if(type == "points"){
			for(i = 0; i < data.length; i++){
				mrkrArr[tabNum][dsIndex][i] = new google.maps.Marker({
					position : new google.maps.LatLng(data[i].latitude,data[i].longitude),
					title : data[i].tag,
					icon : "css/images/tower.png";
				});
			}
		}
	}).error(function(){
		alert("Oops, we got an error...");
	});
}

Map.clickEvent = function(tabNum,dsIndex,map,type){
	if($("#checkbox" + tabNum + "_" + dsIndex).is(":checked") == true){
		if(type == "points"){
			for(i = 0; i < mrkrArr.length; i++){
				mrkrArr[tabNum][dsIndex][i].setMap(map);
			}
		}else{
			for(i = 0; i < mrkrArr.length; i++){
				mrkrArr[tabNum][dsIndex][i].setMap(null);
			}
		}
	}else{
		if(type == "points"){
			hidePoints();
		}else{
			hideRegions();
		}
	}
	
	//对于每组区域里的点,画圆就OK了
	function showRegions(){}
	
	function hideRegions(){}
}