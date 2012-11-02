var track_loc_data = new Object;
track_loc_data.locinfo = new Array();
var imsi = new Object;
var date = new Array();
var map = new Object;
var color = new Array("#000000",//黑色
		"#FF0000",//红色
		"#000079",//蓝色
		"#00EC00",//绿色
		"#FFD306",//黄色
		"#D94600",//棕色
		"#9F4D95");//青色

function getLocFromDate() {
	var mapOptions = {
			center: new google.maps.LatLng(40.841692,111.649827),
			zoom: 16,
			mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	map = new google.maps.Map(document.getElementById("map"),mapOptions);
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
	/*document.orderForm.day1.click();
	document.orderForm.day2.click();
	document.orderForm.day3.click();
	document.orderForm.day4.click();
	document.orderForm.day5.click();
	document.orderForm.day6.click();
	document.orderForm.day7.click();*/
}

function getLocByData1() {
	if(document.orderForm.day1.checked == true)
		getLocData(map,imsi,date[0],0);
}

function getLocByData2() {
	if(document.orderForm.day2.checked == true)
		getLocData(map,imsi,date[1],1);
}

function getLocByData3() {
	if(document.orderForm.day3.checked == true)
		getLocData(map,imsi,date[2],2);
}

function getLocByData4() {
	if(document.orderForm.day4.checked == true)
		getLocData(map,imsi,date[3],3);
}

function getLocByData5() {
	if(document.orderForm.day5.checked == true)
		getLocData(map,imsi,date[4],4);
}

function getLocByData6() {
	if(document.orderForm.day6.checked == true)
		getLocData(map,imsi,date[5],5);
}

function getLocByData7() {
	if(document.orderForm.day7.checked == true)
		getLocData(map,imsi,date[6],6);
}

function getDateMsg(begin,end) {
	date[0] = "2012-09-18";
	date[1] = "2012-09-19";
	date[2] = "2012-09-20";
	date[3] = "2012-09-21";
	date[4] = "2012-09-22";
	date[5] = "2012-09-23";
	date[6] = "2012-09-24";
}

function getLocData(map,imsi,date,n) {
	$.ajax({
		type : "get",
		url : "GetLoc.action",
		data : "imsi = " + imsi + "&date = " + date,
		success : function(msg) {
			var result = eval("(" + msg + ")");
			var loc_list = result.locinfo;
			if(loc_list.length == 0) {
				alert("No trace in " + date);
			}
			for(var i = 0;i < loc_list.length;i++) {
				track_loc_data.locinfo[i] = loc_list[i];
			}
			showMarks(map,n);
		}
	});
}

function showMarks(map,n) {
	for(var i = 0;i < track_loc_data.locinfo.length;i++) {
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
		strokeColor: color[n],
		strokeOpacity: 1.0,
		strokeWeight: 2
	});
	userPath.setMap(map);
	for(var i = 1;i < track_loc_data.locinfo.length;i++) {
		path = userPath.getPath();
		path.push(new google.maps.LatLng(
				track_loc_data.locinfo[i].lat,track_loc_data.locinfo[i].lng));
		userPath.setMap(map);
	}
}

function cleanInfo() {
	track_loc_data = new Object;
	track_loc_data.locinfo = new Array();
	imsi = new Object;
	date = new Array();
}