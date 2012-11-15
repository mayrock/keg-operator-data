var loc1 = new Object;
var loc2 = new Object;
var hour = new Object;
var count = new Object;
var loc1Arr = new Array;
var loc2Arr = new Array;
var hourArr = new Array;
var countArr = new Array;
var lat1Arr = new Array;
var lng1Arr = new Array;
var lat2Arr = new Array;
var lng2Arr = new Array;
var mrkr1Arr = new Array;
var mrkr2Arr = new Array;
document.adjLoc.innerHTML = "";
var userPath = new Array;
var map=new Object;

function Init() {
	loc1 = "";
	loc2 = "";
	hour = "";
	count = "";
	 getMsgData2();
	 document.adjLoc.innerHTML= "";
		for(var i = 0;i < loc1Arr.length;i++) {
			document.adjLoc.innerHTML +=
				"<input type='checkbox' " +
				"onClick='getLatlng(" + i + ")'/>" +
				loc1Arr[i] + "\t" + loc2Arr[i] + "\t" + hourArr[i] + "\t" +countArr[i]+ "<br/>";
		}
		getInitLoc2();
		setMarker2();
}
function getMsgFromLoc() {
	cleanInfo2();
	loc1 = document.getElementById("datepicker4").value;
	loc2 = document.getElementById("datepicker5").value;
	hour = document.getElementById("datepicker6").value;
	count = document.getElementById("datepicker7").value;
	getMsgData2();
	document.adjLoc.innerHTML = "";
	for(var i = 0;i < loc1Arr.length;i++) {
		document.adjLoc.innerHTML +=
			"<input type='checkbox' " +
			"onClick='getLatlng(" + i + ")'/>" +
			loc1Arr[i] + "\t" + loc2Arr[i] + "\t" + hourArr[i] + "\t" +countArr[i] + "<br/>";
	}
	getInitLoc2();
	setMarker2();
}

function getLatlng(i) {
	//根据复选框的状态添加或是删除标记
	if(document.adjLoc.elements[i].checked == true)
		{mrkr1Arr[i].setMap(map);mrkr2Arr[i].setMap(map);userPath[i].setMap(map);}
	else {mrkr1Arr[i].setMap(null);mrkr2Arr[i].setMap(null);userPath[i].setMap(null);}
}

function getInitLoc2() {
	//在谷歌地图上预定位
	var mapOptions = {
			center: new google.maps.LatLng(lat1Arr[0],lng1Arr[0]),
			zoom: 12,
			scaleControl: true,
		    scaleControlOptions: {
		        position: google.maps.ControlPosition.BOTTOM_LEFT
		    },
			mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	map = new google.maps.Map(document.getElementById("map"),mapOptions);
}

function getMsgData2() {
	//得到地址和经纬度信息
	$.ajax({
		type : "get",
		async: false,
		//请求与回应同步
		url : "GetMsgByLoc.action",
		data : "loc1 = " + loc1 + "&loc2 = " + loc2 + "&hour = " + hour + "&count = " + count,
		success : function(msg) {
			var result = eval("(" + msg + ")");
			var msg_list = result.msginfo;
			for(var i = 0;i < msg_list.length;i++) {
				loc1Arr[i] = msg_list[i].loc1;
				loc2Arr[i] = msg_list[i].loc2;
				hourArr[i] = msg_list[i].hour;
				countArr[i] = msg_list[i].count;
			}
			var latlng_list = result.latlnginfo;
			for(var i = 0;i < latlng_list.length;i++) {
				lat1Arr[i] = latlng_list[i].lat1;
				lng1Arr[i] = latlng_list[i].lng1;
				lat2Arr[i] = latlng_list[i].lat2;
				lng2Arr[i] = latlng_list[i].lng2;
			}
		}
	});
}

function setMarker2() {
	//保存标记
	for(var i = 0;i < lat1Arr.length;i++) {
		var marker = new google.maps.Marker({
			position : new google.maps.LatLng(lat1Arr[i],lng1Arr[i]),
			visible : false
		});
		mrkr1Arr.push(marker);
	}
	for(var i = 0;i < lat2Arr.length;i++) {
		var marker = new google.maps.Marker({
			position : new google.maps.LatLng(lat2Arr[i],lng2Arr[i]),
			visible : false
		});
		mrkr2Arr.push(marker);
	}
	for(var i = 0;i < lat1Arr.length;i++) {
		var path = [new google.maps.LatLng(lat1Arr[i],lng1Arr[i]),
		            new google.maps.LatLng(lat2Arr[i],lng2Arr[i])];
		userPath[i] = new google.maps.Polyline({
			path: path,
			strokeColor: "#FF0000",
			strokeOpacity: 1.0,
			strokeWeight: 2
		});
	}
}

function cleanInfo2() {
	//初始化全局变量
	loc_a = new Object;
	loc_b = new Object;
	hour = new Object;
	count = new Object;
	loc1Arr = new Array;
	loc2Arr = new Array;
	hourArr = new Array;
	countArr = new Array;
	lat1Arr = new Array;
	lng1Arr = new Array;
	lat2Arr = new Array;
	lng2Arr = new Array;
	mrkr1Arr = new Array;
	mrkr2Arr = new Array;
	document.adjLoc.innerHTML = "";
	userPath = new Array;
	map=new Object;
}