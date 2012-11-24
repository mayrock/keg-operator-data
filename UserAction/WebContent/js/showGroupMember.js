var mrkrArr = new Array();
var locData = new Object;
locData.locinfo = new Array();
var map = new Object;
var color = new Array(
		"FF0000",
		"FF0000",
		"FF0000",
		"FF0000",
		"FF0000",//��
		"D94600",//��
		"00EC00",//��
		"FFD306"//��
		);//�켣��ѡ�õ���ɫ

function InitGroup() {
	for(var i = 1;i <= 4;i++) {
		mrkrArr[i-1] = new Array();
		getGroupInfo(i);
	}
	for(var i = 1;i <= 4;i++) {
		mrkrArr[i+3] = new Array();
		getGroupInfo_2(i);
	}
	var mapOptions = {
			center: mrkrArr[0][0].position,
			zoom: 12,
			scaleControl: true,
			scaleControlOptions: {
				position: google.maps.ControlPosition.BOTTOM_LEFT
			},
			mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	//�Ե�һ����Ϊ���ĳ�ʼ����ͼ
	map = new google.maps.Map(document.getElementById("map"),mapOptions);
}

function getGroupInfo(i) {
	$.ajax({
		type : "get",
		async: false,
		//�������Ӧͬ��
		url : "GetGroup.action",
		data : "groupNum = " + i,
		success : function(msg) {
			var result = eval("(" + msg + ")");
			var loc_list = result.locinfo;
			locData = new Object;
			locData.locinfo = new Array();
			//��ʼ����¼��γ����Ϣ������
			for(var j = 0;j < loc_list.length;j++) {
				locData.locinfo[j] = loc_list[j];
			}
			saveGroupInfo(i-1);
		}
	});
}

function getGroupInfo_2(i) {
	$.ajax({
		type : "get",
		async: false,
		//�������Ӧͬ��
		url : "GetGroup_2.action",
		data : "groupNum = " + i,
		success : function(msg) {
			var result = eval("(" + msg + ")");
			var loc_list = result.locinfo;
			locData = new Object;
			locData.locinfo = new Array();
			//��ʼ����¼��γ����Ϣ������
			for(var j = 0;j < loc_list.length;j++) {
				locData.locinfo[j] = loc_list[j];
			}
			saveGroupInfo(i+3);
		}
	});
}

function showMemberOnMap(i) {
	//���ݸ�ѡ���״̬��ӻ���ɾ���켣
	if(document.groups.elements[i-1].checked == true)
		showOverlays_2(i-1);
	else cleanOverlays_2(i-1);
}

function showOverlays_2(i) {
	//��ӹ켣
	if(mrkrArr[i].length != 0) {
		for(j in mrkrArr[i])
			mrkrArr[i][j].setMap(map);
	}
}

function cleanOverlays_2(i) {
	//ɾ���켣
	if(mrkrArr[i].length != 0) {
		for(j in mrkrArr[i])
			mrkrArr[i][j].setMap(null);
	}
}

function saveGroupInfo(n) {
	//����켣
	var pinIcon = new google.maps.MarkerImage(
			"http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|" + color[n],
			null, /* size is determined at runtime */
			null, /* origin is 0,0 */
			null, /* anchor is bottom center of the scaled image */
			new google.maps.Size(10.5, 17)
			);
	for(var i = 0;i < locData.locinfo.length;i++) {
		var marker = new google.maps.Marker({
			position : new google.maps.LatLng(locData.locinfo[i].lat,locData.locinfo[i].lng),
			icon : pinIcon
		});
		mrkrArr[n].push(marker);
	}
}