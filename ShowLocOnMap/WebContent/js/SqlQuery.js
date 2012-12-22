var locData;
var map;
var query;
var mrkrArr;
var now;
var check;
var trace;
var flag;
var mrkrNum;

function cleanInfo_Sql() {
	locData = new Object;
	locData.locinfo = new Array();
	map = new Object;
	query = new Object;
	mrkrArr = new Array();
	now = 1;
	check = new Array();
	trace = new Object;
	flag = 0;
	mrkrNum = 0;
	document.lat_lng_10_sqlQuery.innerHTML = "";
	document.trace_sqlQuery.innerHTML = "";
}

/**
 * main
 */
function SqlQuery() {
	cleanInfo_Sql();
	query = document.getElementById("sqlQuery").value;
	var reg = new RegExp("\r\n","g");
	query = query.replace(reg," ");
	getLocInfo_Sql();
	var mapOptions = {
			center: new google.maps.LatLng(locData.locinfo[0].lat,locData.locinfo[0].lng),
			zoom: 12,
			scaleControl: true,
			scaleControlOptions: {
				position: google.maps.ControlPosition.BOTTOM_LEFT
			},
			mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	map = new google.maps.Map(document.getElementById("map"),mapOptions);
	if(choose_sqlQuery.showAllLoc_sqlQuery[0].checked == true) {
		showAllMrkr_Sql(i);
		document.trace_sqlQuery.innerHTML = "<br/>&nbsp;&nbsp;" +
				"<input type='button' value='showTrace' onClick='showTrace_sql(0)'/>";
	}
	else {
		addHtmlTitle();
		for(var i = 0;(i < 10) && (i < mrkrArr.length); i++) {
			document.lat_lng_10_sqlQuery.innerHTML += "<input type='checkbox' " +
					"onClick='markInfo_Sql(" + i + ")'/>" + locData.locinfo[i].id + " " +
					locData.locinfo[i].lat + " " + locData.locinfo[i].lng + "<br/>";
		}
		if(now * 10 < mrkrArr.length)
			document.lat_lng_10_sqlQuery.innerHTML += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
					"<input type='button' value='next' onClick='next_10()'/><br/>";
		document.trace_sqlQuery.innerHTML = "<br/>&nbsp;&nbsp;" +
				"<input type='button' value='showTrace' onClick='showTrace_sql(1)'/>";
	}
	
			
}

function showAllMrkr_Sql() {
	for(var i = 0; i < mrkrArr.length; i++)
		mrkrArr[i].setMap(map);
}

function showTrace_sql(i) {
	if(i == 0) {
		var path = [new google.maps.LatLng(locData.locinfo[0].lat,locData.locinfo[0].lng)];
		trace = new google.maps.Polyline({
			path: path,
			strokeColor: "#FF0000",
			strokeOpacity: 1.0,
			strokeWeight: 2
		});
		for(var j = 1; j < locData.locinfo.length; j++) {
			path = trace.getPath();
			path.push(new google.maps.LatLng(locData.locinfo[j].lat,locData.locinfo[j].lng));
		}
		document.trace_sqlQuery.innerHTML = "<br/>&nbsp;&nbsp;" +
				"<input type='button' value='hideTrace' onClick='hideTrace_sql(0)'/><br/>";
	}
	else {
		if((flag != 0) && (mrkrNum != 0)) {
			trace.setMap(null);
		}
		flag = 1;
		mrkrNum = 0;
		for(var j = 0; j < locData.locinfo.length; j++) {
			if((mrkrNum == 0) && (check[j] == 1)) {
				var path = [new google.maps.LatLng(locData.locinfo[j].lat,locData.locinfo[j].lng)];
				trace = new google.maps.Polyline({
					path: path,
					strokeColor: "#FF0000",
					strokeOpacity: 1.0,
					strokeWeight: 2
				});
				mrkrNum++;
			}
			else if(check[j] == 1) {
				path = trace.getPath();
				path.push(new google.maps.LatLng(locData.locinfo[j].lat,locData.locinfo[j].lng));
				mrkrNum++;
			}
		}
		if(mrkrNum == 0) {
			alert("no trace!");
		}
		else {
			document.trace_sqlQuery.innerHTML = "<br/>&nbsp;&nbsp;" +
					"<input type='button' value='showTrace' onClick='showTrace_sql(1)'/>" +
					"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
					"<input type='button' value='hideTrace' onClick='hideTrace_sql(1)'/><br/>";
		}
	}
	trace.setMap(map);
}

function hideTrace_sql(i) {
	if(i == 0) {
		document.trace_sqlQuery.innerHTML = "<br/>&nbsp;&nbsp;" +
				"<input type='button' value='showTrace' onClick='showTrace_sql(0)'/><br/>";
	}
	trace.setMap(null);
}

/**
 * show single marker
 */
function markInfo_Sql(i) {
	var k = (now - 1) * 10;
	if (document.lat_lng_10_sqlQuery.elements[i - k].checked == true) {
		mrkrArr[i].setMap(map);
		check[i] = 1;
	}
	else {
		mrkrArr[i].setMap(null);
		check[i] = 0;
	}
}

function next_10() {
	now++;
	addHtmlTitle();
	var k = (now - 1) * 10;
	if(now * 10 < mrkrArr.length) {
		for(var i = 0; i < 10; i++) {
			var j = i + k;
			addHtmlBody(j);
		}
		addHtmlTail();
	}
	else {
		for(var i = 0; i < (mrkrArr.length - k); i++) {
			var j = i + k;
			addHtmlBody(j);
		}
		document.lat_lng_10_sqlQuery.innerHTML += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
				"<input type='button' value='front' onClick='front_10()'/><br/>";
	}
}

function addHtmlBody(j) {
	if(check[j] == 1)
		document.lat_lng_10_sqlQuery.innerHTML += "<input type='checkbox' " +
				"onClick='markInfo_Sql(" + j + ")' checked/>";
	else
		document.lat_lng_10_sqlQuery.innerHTML += "<input type='checkbox' " +
				"onClick='markInfo_Sql(" + j + ")'/>";
	document.lat_lng_10_sqlQuery.innerHTML += locData.locinfo[j].id + " " +
			locData.locinfo[j].lat + " " + locData.locinfo[j].lng + "<br/>";
}

function addHtmlTitle() {
	document.lat_lng_10_sqlQuery.innerHTML = "&nbsp;&nbsp;&nbsp;&nbsp;siteId&nbsp;&nbsp;&nbsp;" +
			"&nbsp;&nbsp;latitude&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;longitude<br/>";
}

function addHtmlTail() {
	document.lat_lng_10_sqlQuery.innerHTML += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
			"<input type='button' value='front' onClick='front_10()'/>" +
			"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
			"<input type='button' value='next' onClick='next_10()'/><br/>";
}

function front_10() {
	now--;
	addHtmlTitle();
	var k = (now - 1) * 10;
	for(var i = 0; i < 10; i++) {
		var j = i + k;
		addHtmlBody(j);
	}
	if(now != 1)
		addHtmlTail();
	else
		document.lat_lng_10_sqlQuery.innerHTML += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
				"<input type='button' value='next' onClick='next_10()'/><br/>";
}

/**
 * communication
 */
function getLocInfo_Sql() {
	$.ajax({
		type : "get",
		async: false,
		//request and response synchronization
		url : "GetSqlQuery.action",
		data : "query = " + query,
		success : function(msg) {
			var result = eval("(" + msg + ")");
			var loc_list = result.locinfo;
			if(loc_list.length == 0) {
				alert("Your sql query isn't right!");
				cleanInfo_Sql();
			}
			for(var i = 0; i < loc_list.length; i++) {
				locData.locinfo[i] = loc_list[i];
			}
			saveMarker_Sql();
		}
	});
}

/**
 * save marker and trace
 */
function saveMarker_Sql() {
	var pinIcon = new google.maps.MarkerImage(
			"http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|FF0000",
			null, /* size is determined at runtime */
			null, /* origin is 0,0 */
			null, /* anchor is bottom center of the scaled image */
			new google.maps.Size(10.5, 17)
			);
	for(var i = 0; i < locData.locinfo.length; i++) {
		var marker = new google.maps.Marker({
			position : new google.maps.LatLng(locData.locinfo[i].lat,locData.locinfo[i].lng),
			icon : pinIcon
		});
		mrkrArr.push(marker);
	}
}