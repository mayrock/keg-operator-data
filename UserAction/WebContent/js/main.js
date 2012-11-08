var map = new Object;
var imsi = new Object;
var date = new Array();
var mrkrArr = new Array();
var locData = new Object;
locData.locinfo = new Array();
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
	//处理输入信息
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
	if(imsi == "") {
		alert("Input begin date!");
		return;
	}
	if(begin == "") {
		alert("Input begin date!");
		return;
	}
	if(end == "") {
		alert("Input end date!");
		return;
	}
	//处理空输入
	getDateMsg(begin,end);
	document.dateForm.innerHTML = "";
	for(var i = 1;i <= date.length;i++) {
		document.dateForm.innerHTML +=
			"<input type='checkbox' name='day" + i + "' onClick='getLocByDate(" + i + ")' checked/>day" + i;
		mrkrArr[i-1] = new Array();
	}
	getInitLoc();
}

function getLocByDate(i) {
	//根据复选框的状态添加或是删除轨迹
	if(document.dateForm.elements[i-1].checked == true)
		showOverlays(i-1);
	else cleanOverlays(i-1);
}

function showOverlays(i) {
	//添加轨迹
	if(mrkrArr[i].length != 0) {
		userPath[i].setMap(map);
		for(j in mrkrArr[i])
			mrkrArr[i][j].setMap(map);
	}
	else alert("No trace in " + date[i]);
}

function cleanOverlays(i) {
	//删除轨迹
	if(mrkrArr[i].length != 0) {
		userPath[i].setMap(null);
		for(j in mrkrArr[i])
			mrkrArr[i][j].setMap(null);
	}
}

function getDateMsg(begin,end) {
	//处理时间区间
	//使用的是预设值
	date[0] = "2012-09-18";
	date[1] = "2012-09-19";
	date[2] = "2012-09-20";
	date[3] = "2012-09-21";
	date[4] = "2012-09-22";
	date[5] = "2012-09-23";
	date[6] = "2012-09-24";
}

function getInitLoc() {
	//在谷歌地图上预定位并保存各天的轨迹
	var count = 0;
	for(var i = 0;i < date.length;i++) {
		getLocData(i);
		count += mrkrArr[i].length;
	}
	if(count == 0) {
		alert("No trace in these days, maybe your input is wrong.");
		cleanInfo();
	}
	//处理错误输入
	else {
		for(var i = 0;i < date.length;i++) {
			if(mrkrArr[i].length != 0){
				var mapOptions = {
						center: mrkrArr[i][0].position,
						zoom: 15,
						mapTypeId: google.maps.MapTypeId.ROADMAP
				};
				//以第一个点为中心初始化地图
				map = new google.maps.Map(document.getElementById("map"),mapOptions);
				break;
			}
		}
		for(var i = 0;i < date.length;i++)
			showOverlays(i);
		//在地图上预置所有天的轨迹
	}
}

function getLocData(n) {
	//生成用户在某一天的轨迹
	$.ajax({
		type : "get",
		async: false,
		//请求与回应同步
		url : "GetLoc.action",
		data : "imsi = " + imsi + "&begin = " + date[n] + "&end = " + date[n],
		success : function(msg) {
			var result = eval("(" + msg + ")");
			var loc_list = result.locinfo;
			locData = new Object;
			locData.locinfo = new Array();
			//初始化记录经纬度信息的数组
			for(var i = 0;i < loc_list.length;i++) {
				locData.locinfo[i] = loc_list[i];
			}
			saveOneDayInfo(n);
		}
	});
}

function saveOneDayInfo(n) {
	//保存轨迹
	for(var i = 0;i < locData.locinfo.length;i++) {
		var marker = new google.maps.Marker({
			position : new google.maps.LatLng(
					locData.locinfo[i].lat,locData.locinfo[i].lng),
			title : locData.locinfo[i].msg,
		});
		mrkrArr[n].push(marker);
	}
	var path = [new google.maps.LatLng(
			locData.locinfo[0].lat,locData.locinfo[0].lng)];
	userPath[n] = new google.maps.Polyline({
		path: path,
		strokeColor: color[n],
		strokeOpacity: 1.0,
		strokeWeight: 2
	});
	for(var i = 1;i < locData.locinfo.length;i++) {
		path = userPath[n].getPath();
		path.push(new google.maps.LatLng(
				locData.locinfo[i].lat,locData.locinfo[i].lng));
	}
}

function cleanInfo() {
	//初始化全局变量
	map = new Object;
	imsi = new Object;
	date = new Array();
	mrkrArr = new Array();
	locData = new Object;
	locData.locinfo = new Array();
	userPath = new Array();
	document.dateForm.innerHTML = "";
}