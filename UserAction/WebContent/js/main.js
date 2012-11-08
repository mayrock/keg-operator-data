var map = new Object;
var imsi = new Object;
var date = new Array();
var mrkrArr = new Array();
var locData = new Object;
locData.locinfo = new Array();
var userPath = new Array();

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
	//����������Ϣ
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
	//���������
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
	//���ݸ�ѡ���״̬��ӻ���ɾ���켣
	if(document.dateForm.elements[i-1].checked == true)
		showOverlays(i-1);
	else cleanOverlays(i-1);
}

function showOverlays(i) {
	//��ӹ켣
	if(mrkrArr[i].length != 0) {
		userPath[i].setMap(map);
		for(j in mrkrArr[i])
			mrkrArr[i][j].setMap(map);
	}
	else alert("No trace in " + date[i]);
}

function cleanOverlays(i) {
	//ɾ���켣
	if(mrkrArr[i].length != 0) {
		userPath[i].setMap(null);
		for(j in mrkrArr[i])
			mrkrArr[i][j].setMap(null);
	}
}

function getDateMsg(begin,end) {
	//����ʱ������
	//ʹ�õ���Ԥ��ֵ
	date[0] = "2012-09-18";
	date[1] = "2012-09-19";
	date[2] = "2012-09-20";
	date[3] = "2012-09-21";
	date[4] = "2012-09-22";
	date[5] = "2012-09-23";
	date[6] = "2012-09-24";
}

function getInitLoc() {
	//�ڹȸ��ͼ��Ԥ��λ���������Ĺ켣
	var count = 0;
	for(var i = 0;i < date.length;i++) {
		getLocData(i);
		count += mrkrArr[i].length;
	}
	if(count == 0) {
		alert("No trace in these days, maybe your input is wrong.");
		cleanInfo();
	}
	//�����������
	else {
		for(var i = 0;i < date.length;i++) {
			if(mrkrArr[i].length != 0){
				var mapOptions = {
						center: mrkrArr[i][0].position,
						zoom: 15,
						mapTypeId: google.maps.MapTypeId.ROADMAP
				};
				//�Ե�һ����Ϊ���ĳ�ʼ����ͼ
				map = new google.maps.Map(document.getElementById("map"),mapOptions);
				break;
			}
		}
		for(var i = 0;i < date.length;i++)
			showOverlays(i);
		//�ڵ�ͼ��Ԥ��������Ĺ켣
	}
}

function getLocData(n) {
	//�����û���ĳһ��Ĺ켣
	$.ajax({
		type : "get",
		async: false,
		//�������Ӧͬ��
		url : "GetLoc.action",
		data : "imsi = " + imsi + "&begin = " + date[n] + "&end = " + date[n],
		success : function(msg) {
			var result = eval("(" + msg + ")");
			var loc_list = result.locinfo;
			locData = new Object;
			locData.locinfo = new Array();
			//��ʼ����¼��γ����Ϣ������
			for(var i = 0;i < loc_list.length;i++) {
				locData.locinfo[i] = loc_list[i];
			}
			saveOneDayInfo(n);
		}
	});
}

function saveOneDayInfo(n) {
	//����켣
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
	//��ʼ��ȫ�ֱ���
	map = new Object;
	imsi = new Object;
	date = new Array();
	mrkrArr = new Array();
	locData = new Object;
	locData.locinfo = new Array();
	userPath = new Array();
	document.dateForm.innerHTML = "";
}