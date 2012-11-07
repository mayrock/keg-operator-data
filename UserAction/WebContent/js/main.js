var track_loc_data = new Object;
track_loc_data.locinfo = new Array();
var location = new Object;
var imsi = new Object;
var date = new Array();
var map = new Object;
var noTrace = new Array();
var days = new Object;

var color = new Array(
		"#000000",//��
		"#FF0000",//��
		"#000079",//��
		"#00EC00",//��
		"#FFD306",//��
		"#D94600",//��
		"#9F4D95"//��
		);//�켣��ѡ�õ���ɫ

function getLocFromDates() {
	//����������Ϣ����������
	cleanInfo();
	var mapOptions = {
			center: new google.maps.LatLng(40.841692,111.649827),
			zoom: 16,
			mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	map = new google.maps.Map(document.getElementById("map"),mapOptions);
	//��ʼ����ͼ
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
	getInitializeLoc(begin,end);
	getDateMsg(begin,end);
	document.dateForm.innerHTML = "";
	for(var i = 1;i <= days;i++) {
		noTrace[i-1] = 0;
		//Ĭ�����е��춼���ڹ켣
		document.dateForm.innerHTML +=
			"<input type='checkbox' name='day" + i + "' onClick='getLocByDate(" + i + ")'/>day" + i;
	}
}

function getLocByDate(i) {
	//���ݸ�ѡ���״̬��ӻ���ɾ���켣
	if(document.dateForm.elements[i-1].checked == true)
		getLocData(i-1);
	else refresh();
}

function refresh() {
	/**
	 * �������汾
	 * ��������Ĺ��ܱ�Ӧ��ɾ���켣
	 * ʵ��ʵ�ֵĹ�����ˢ�µ�ͼ
	 * �ƻ���ɾ���켣ʱӦ�е�����
	 */
	var mapOptions = {
			center: new google.maps.LatLng(location.lat,location.lng),
			zoom: 15,
			mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	map = new google.maps.Map(document.getElementById("map"),mapOptions);
	for(var i = 0;i < document.dateForm.length;i++)
		if(document.dateForm.elements[i].checked == true && noTrace[i] != 1)
			getLocData(i);
}

function getDateMsg(begin,end) {
	/**
	 * ����ָ����ʱ������
	 * ʹ�õ���Ԥ��ֵ
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
	//��ָ����ͼ����ʾָ���û���ָ�����ָ����ɫ�Ĺ켣
	$.ajax({
		type : "get",
		url : "GetLoc.action",
		data : "imsi = " + imsi + "&begin = " + date[n] + "&end = " + date[n],
		success : function(msg) {
			var result = eval("(" + msg + ")");
			var loc_list = result.locinfo;
			if(loc_list.length == 0) {
				noTrace[n] = 1;
				//Ϊ�����ڹ켣������ӱ��
				alert("No trace in " + date[n]);
			}
			track_loc_data = new Object;
			track_loc_data.locinfo = new Array();
			//��ʼ����¼��γ����Ϣ������
			for(var i = 0;i < loc_list.length;i++) {
				track_loc_data.locinfo[i] = loc_list[i];
			}
			showMarks(n);
		}
	});
}

function getInitializeLoc(begin,end) {
	//�ڹȸ��ͼ��Ԥ��λ
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
			//��õ�һ�������Ϣ������
			var mapOptions = {
					center: new google.maps.LatLng(location.lat,location.lng),
					zoom: 15,
					mapTypeId: google.maps.MapTypeId.ROADMAP
			};
			//�Ե�һ����Ϊ���ĳ�ʼ����ͼ
			map = new google.maps.Map(document.getElementById("map"),mapOptions);
		}
	});
}

function showMarks(n) {
	//��ָ����ͼ����ʾָ����ɫ�Ĺ켣
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
	//��ʼ��
	track_loc_data = new Object;
	track_loc_data.locinfo = new Array();
	location = new Object;
	imsi = new Object;
	date = new Array();
	map = new Object;
	noTrace = new Array();
	days = new Object;
}