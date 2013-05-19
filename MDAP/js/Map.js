Map = {};

/**********initialize map in a tab**********/
Map.initialize = function(tabNum){
	$("#view" + tabNum).css("width",Common.width() - 320);
	$("#view" + tabNum).css("height","400px");
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
	Common.mapArr[tabNum] = new google.maps.Map(document.getElementById("view" + tabNum),mapOptions);
	Common.mrkrArr[tabNum] = new Array();
}

/**********load data of all datasets in a tab**********/
Map.loadData = function(tabNum,dsIndex,name,type){
	Common.mrkrArr[tabNum][dsIndex] = new Array();
	$.ajaxSettings.async = false;
	$.getJSON(Common.latlngUrl() + name,function(data){
		if(type == "points"){
			for(var i = 0; i < data.length; i++){
				Common.mrkrArr[tabNum][dsIndex][i] = new google.maps.Marker({
					position : new google.maps.LatLng(data[i].latitude,data[i].longitude),
					title : data[i].tag
					/**********need to do**********/
					/**********get a good icon image**********/
//					icon : "css/images/tower.png"
				});
			}
		}
	}).error(function(){
		alert("Oops, we got an error...");
	});
}

/**********what to do when click a dataset**********/
Map.clickEvent = function(tabNum,dsIndex,type){
	if($("#checkbox" + tabNum + "_" + dsIndex).is(":checked") == true){
		if(type == "points"){
			for(var i = 0; i < Common.mrkrArr[tabNum][dsIndex].length; i++){
				Common.mrkrArr[tabNum][dsIndex][i].setMap(Common.mapArr[tabNum]);
			}
		}else{
			showRegions();
		}
	}else{
		if(type == "points"){
			for(var i = 0; i < Common.mrkrArr[tabNum][dsIndex].length; i++){
				Common.mrkrArr[tabNum][dsIndex][i].setMap(null);
			}
		}else{
			hideRegions();
		}
	}
	
	//对于每组区域里的点,画圆就OK了
	function showRegions(){}
	
	function hideRegions(){}
}