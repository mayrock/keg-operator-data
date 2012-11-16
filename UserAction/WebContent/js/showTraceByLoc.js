var loc1 = new Object;
var loc2 = new Object;
var hour = new Object;
var count = new Object;
var loc1Arr = new Array;
var loc2Arr = new Array;
var countArr = new Array;
var lat1 = new Array;
var lng1 = new Array;
var lat2 = new Array;
var lng2 = new Array;
document.adjLoc.innerHTML = "";
var userPath = new Array;

function Init() {
	loc1 = "";
	loc2 = "";
	count = "";
	getMsgData();
	document.adjLoc.innerHTML = "";
	for ( var i = 0; i < loc1Arr.length; i++) {
		document.adjLoc.innerHTML += "<input type='checkbox' " +
				"onClick='getLatlng(" + i + ")'/>" + loc1Arr[i] +
				"\t" + loc2Arr[i] + "\t" + countArr[i] + "<br/>";
	}
	getInitLoc_2();
	setTrace();
}

function getMsgFromLoc() {
	cleanInfo_2();
	loc1 = document.getElementById("datepicker4").value;
	loc2 = document.getElementById("datepicker5").value;
	count = document.getElementById("datepicker6").value;
	getMsgData();
	document.adjLoc.innerHTML = "";
	for(var i = 0;i < loc1Arr.length;i++) {
		document.adjLoc.innerHTML += "<input type='checkbox' " +
			"onClick='getLatlng(" + i + ")'/>" + loc1Arr[i] +
			"\t" + loc2Arr[i] + "\t" + countArr[i] + "<br/>";
	}
	getInitLoc_2();
	setTrace();
}

function getLatlng(i) {
	// 根据复选框的状态添加或是删除标记
	if (document.adjLoc.elements[i].checked == true)
		userPath[i].setMap(map);
	else
		userPath[i].setMap(null);
}

function getInitLoc_2() {
	//在谷歌地图上预定位
	var mapOptions = {
			center: new google.maps.LatLng(lat1[0],lng1[0]),
			zoom: 12,
			scaleControl: true,
		    scaleControlOptions: {
		        position: google.maps.ControlPosition.BOTTOM_LEFT
		    },
			mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	map = new google.maps.Map(document.getElementById("map"),mapOptions);
}

function getMsgData() {
	//得到地址和经纬度信息
	$.ajax({
		type : "get",
		async : false,
		//请求与回应同步
		url : "GetMsgByLoc.action",
		data : "loc1 = " + loc1 + "&loc2 = " + loc2 + "&count = " + count,
		success : function(msg) {
			var result = eval("(" + msg + ")");
			var msg_list = result.msginfo;
			for (var i = 0; i < msg_list.length; i++) {
				loc1Arr[i] = msg_list[i].loc1;
				loc2Arr[i] = msg_list[i].loc2;
				countArr[i] = msg_list[i].count;
			}
			var latlng_list = result.latlnginfo;
			for (var i = 0; i < latlng_list.length; i++) {
				lat1[i] = latlng_list[i].lat1;
				lng1[i] = latlng_list[i].lng1;
				lat2[i] = latlng_list[i].lat2;
				lng2[i] = latlng_list[i].lng2;
			}
		}
	});
}

function setTrace() {
	//保存轨迹
	for (var i = 0; i < lat1.length; i++) {
		var path = [new google.maps.LatLng(lat1[i], lng1[i]),
				new google.maps.LatLng(lat2[i], lng2[i])];
		userPath[i] = new google.maps.Polyline({
			path : path,
			strokeColor : "#FF0000",
			strokeOpacity : 1.0,
			strokeWeight : 2
		});
	}
}

function cleanInfo_2() {
	//初始化全局变量
	loc1 = new Object;
	loc2 = new Object;
	count = new Object;
	loc1Arr = new Array;
	loc2Arr = new Array;
	countArr = new Array;
	lat1 = new Array;
	lng1 = new Array;
	lat2 = new Array;
	lng2 = new Array;
	document.adjLoc.innerHTML = "";
	userPath = new Array;
}