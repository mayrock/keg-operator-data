var track_loc_data = new Object;
track_loc_data.locinfo = new Array();
var location = new Object;
var imsi = new Object;
var date = new Array();
var map = new Object;
var days = new Object;
var markersArray = new Array();
var userPath = new Array();

var color = new Array(
		"#000000",//黑
		"#FF0000",//红
		"#000079",//蓝
		"#00EC00",//绿
		"#FFD306",//黄
		"#D94600",//棕
		"#9F4D95"//青
		);//轨迹可选用的颜色

function getLocFromDates() {
	//处理输入信息并作错误处理
	cleanInfo();
	var mapOptions = {
			center: new google.maps.LatLng(40.841692,111.649827),
			zoom: 16,
			mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	map = new google.maps.Map(document.getElementById("map"),mapOptions);
	//初始化地图
	imsi = document.getElementById("datepicker1").value;
	var begin = document.getElementById("datepicker2").value;
	var end = document.getElementById("datepicker3").value;
	if(begin == "") {
		alert("Input begin date!");
		return;
	}
	if(end == "") {
		alert("Input end date!");
		return;
	}
	getDateMsg(begin,end);
	getInitializeLoc(begin,end);
	document.dateForm.innerHTML = "";
	for(var i = 1;i <= days;i++) {
		document.dateForm.innerHTML +=
			"<input type='checkbox' name='day" + i + "' onClick='getLocByDate(" + i + ")'/>day" + i;
		markersArray[i-1] = new Array();
	}
	for(var i = 1;i <= days;i++) {
		getLocData(i-1);
	}
}

function getLocByDate(i) {
	//根据复选框的状态添加或是删除轨迹
	if(document.dateForm.elements[i-1].checked == true)
		showOverlays(i-1);
	else cleanOverlays(i-1);
}

function showOverlays(i) {
	//添加标记和轨迹
	if(markersArray[i].length != 0) {
		userPath[i].setMap(map);
		for(j in markersArray[i])
			markersArray[i][j].setMap(map);
	}
	else alert("No trace in " + date[i]);
}

function cleanOverlays(i) {
	//删除标记和轨迹
	if(markersArray[i]) {
		userPath[i].setMap(null);
		for(j in markersArray[i])
			markersArray[i][j].setMap(null);
	}
}

function getDateMsg(begin,end) {
	/**
	 * 处理指定的时间区间
	 * 使用的是预设值
	 */
	date[0] = "2012-09-18";
	date[1] = "2012-09-19";
	date[2] = "2012-09-20";
	date[3] = "2012-09-21";
	date[4] = "2012-09-22";
	date[5] = "2012-09-23";
	date[6] = "2012-09-24";
	days = 7;
}

function getLocData(n) {
	//在指定地图上显示指定用户在指定天的指定颜色的轨迹
	$.ajax({
		type : "get",
		url : "GetLoc.action",
		data : "imsi = " + imsi + "&begin = " + date[n] + "&end = " + date[n],
		success : function(msg) {
			var result = eval("(" + msg + ")");
			var loc_list = result.locinfo;
			track_loc_data = new Object;
			track_loc_data.locinfo = new Array();
			//初始化记录经纬度信息的数组
			for(var i = 0;i < loc_list.length;i++) {
				track_loc_data.locinfo[i] = loc_list[i];
			}
			setInfo(n);
		}
	});
}

function getInitializeLoc(begin,end) {
	/**
	 * 在谷歌地图上预定位
	 * 保存各天的标记和轨迹
	 */
	$.ajax({
		type : "get",
		url : "GetLoc.action",
		data : "imsi = " + imsi + "&begin = " + begin + "&end = " + end,
		success : function(msg) {
			var result = eval("(" + msg + ")");
			var loc_list = result.locinfo;
			if(loc_list.length == 0)
				alert("No trace in these days, maybe your input is wrong.");
			location = loc_list[0];
			//获得第一个点的信息并保存
			var mapOptions = {
					center: new google.maps.LatLng(location.lat,location.lng),
					zoom: 15,
					mapTypeId: google.maps.MapTypeId.ROADMAP
			};
			//以第一个点为中心初始化地图
			map = new google.maps.Map(document.getElementById("map"),mapOptions);
		}
	});
}

function setInfo(n) {
	//保存一天的标记和轨迹
	for(var i = 0;i < track_loc_data.locinfo.length;i++) {
		var marker = new google.maps.Marker({
			position : new google.maps.LatLng(
					track_loc_data.locinfo[i].lat,track_loc_data.locinfo[i].lng),
			title : track_loc_data.locinfo[i].msg
		});
		markersArray[n].push(marker);
	}
	var path = [new google.maps.LatLng(
			track_loc_data.locinfo[0].lat,track_loc_data.locinfo[0].lng)];
	userPath[n] = new google.maps.Polyline({
		path: path,
		strokeColor: color[n],
		strokeOpacity: 1.0,
		strokeWeight: 2
	});
	for(var i = 1;i < track_loc_data.locinfo.length;i++) {
		path = userPath[n].getPath();
		path.push(new google.maps.LatLng(
				track_loc_data.locinfo[i].lat,track_loc_data.locinfo[i].lng));
	}
}

function cleanInfo() {
	//初始化
	track_loc_data = new Object;
	track_loc_data.locinfo = new Array();
	location = new Object;
	imsi = new Object;
	date = new Array();
	map = new Object;
	noTrace = new Array();
	days = new Object;
	markersArray = new Array();
	userPath = new Array();
}