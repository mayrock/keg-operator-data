var Info = {};

Info.loadImsi = function(pageIndex){
	var minLimit = pageIndex * 10;
	var maxLimit = (pageIndex + 1) * 10;
	var query = "select r,imsi,sumcnt from (" +
		"select rownum r,t.* from (" +
		"select * from t_gb_130610_imsi order by sumcnt desc" +
		") t where rownum <= " + maxLimit +
		") where r > " + minLimit;
	
	$.getJSON(Common.runSqlUrl(),{
		db: "oracle",
		dbname: "orcl",
		user: "bj_gb",
		password: "root",
		sql: query
	}).done(function(data,textStatus,jqXHR){
		var row = new Object;
		var imsi = new Object;
		var html = new Object;
		var count = new Object;
		
		var length = data.length;
		var tabIndex = Tab.getIndex();
		if(length < 10){
			var pageCntr = $("#imsi_pageCntr_" + tabIndex);
			var nextPage = pageCntr.find("input").eq(1);
			nextPage.prop("disabled",true);
		}
		
		var arr = "[";
		for(var i = 0; i < length; i++){
			row = data[i].field[0].value;
			imsi = data[i].field[1].value;
			html = "<a href = 'javascript:void(0);' onClick = 'Info.imsiDetail(" + imsi + ");' style = 'text-decoration: none;'>" + imsi + "</a>";
			count = data[i].field[2].value;
			arr += "[" + row + ",\"" + html + "\"," + count + "]";
			if(i == length - 1){
				arr += "]"
			}else{
				arr += ",";
			}
		}
		
		$("#imsi_tableCntr_" + tabIndex).empty();
		
		google.load("visualization","1",{packages:["table"],"callback":drawTable});
		function drawTable(){
			var view = document.getElementById("imsi_tableCntr_" + tabIndex);
			var data = new google.visualization.DataTable();
			data.addColumn("number","No.");
			data.addColumn("string","Imsi");
			data.addColumn("number","Count");
			data.addRows($.parseJSON(arr));
			var options = {
				allowHtml: true
			};
			var table = new google.visualization.Table(view);
			table.draw(data,options);
			
			Info.adjustHeight();
		}
	}).fail(function(jqXHR,textStatus,errorThrown){
		console.log(jqXHR);
		console.log(textStatus);
		console.log(errorThrown);
		alert("Oops, we got an error...");
		return;
	});
};

Info.imsiDetail = function(imsi){
	var tabIndex = Tab.getIndex();
	if($("#detailImsi_" + tabIndex).css("display") == "none"){
		$("#detailImsi_" + tabIndex).css("display","block");
	}
	
	$("#detail_titleCntr_" + tabIndex).empty();
	$("#detail_titleCntr_" + tabIndex).append("<span>Imsi:</span><span>" + imsi + "</span>");
	
	var pageCntr = $("#detail_pageCntr_" + tabIndex);
	var frontPage = pageCntr.find("input").eq(0);
	var nextPage = pageCntr.find("input").eq(1);
	frontPage.prop("disabled",true);
	nextPage.attr("onClick","Info.turnPage('detail','next',1);");
	
	Info.loadTable(0);
	Info.loadMap();
	Info.loadChart();
};

Info.loadTable = function(pageIndex){
	var tabIndex = Tab.getIndex();
	var span = $("#detail_titleCntr_" + tabIndex).find("span").eq(1);
	var imsi = span.text();
	
	var minLimit = pageIndex * 10;
	var maxLimit = (pageIndex + 1) * 10;
	var query = "select r,areaname_zh,longitude,latitude,imei,requesttime,traffic,useragent,host,contenttype from (" +
		"select rownum r,t.* from t_gb_130610_filter_0702_loc t where imsi = " + imsi + " and rownum <= " + maxLimit +
		") where r > " + minLimit;
	$.getJSON(Common.runSqlUrl(),{
		db: "oracle",
		dbname: "orcl",
		user: "bj_gb",
		password: "root",
		sql: query
	}).done(function(data,textStatus,jqXHR){
		var value = new Object;
		
		var length = data.length;
		if(length < 10){
			var pageCntr = $("#detail_pageCntr_" + tabIndex);
			var nextPage = pageCntr.find("input").eq(1);
			nextPage.prop("disabled",true);
		}
		
		var arr = "[";
		for(var i = 0; i < length; i++){
			arr += "["
			for(var j = 0; j < 10; j++){
				value = data[i].field[j].value;
				arr += "\"" + value + "\"";
				if(j == 9){
					arr += "]";
				}else{
					arr += ",";
				}
			}
			if(i == length - 1){
				arr += "]"
			}else{
				arr += ",";
			}
		}
		
		$("#detail_tableCntr_" + tabIndex).empty();
		
		var document_w = $(document).width();
		var table_w = document_w - 360;
		
		google.load("visualization","1",{packages:["table"],"callback":drawTable});
		function drawTable(){
			var view = document.getElementById("detail_tableCntr_" + tabIndex);
			var data = new google.visualization.DataTable();
			data.addColumn("string","No.");
			data.addColumn("string","Area Name");
			data.addColumn("string","Longitude");
			data.addColumn("string","Latitude");
			data.addColumn("string","Imei");
			data.addColumn("string","Request Time");
			data.addColumn("string","Traffic");
			data.addColumn("string","User Agent");
			data.addColumn("string","Host");
			data.addColumn("string","Content Type");
			data.addRows($.parseJSON(arr));
			var options = {
				width: table_w
			};
			var table = new google.visualization.Table(view);
			table.draw(data,options);
			
			Info.adjustHeight();
		}
	}).fail(function(jqXHR,textStatus,errorThrown){
		console.log(jqXHR);
		console.log(textStatus);
		console.log(errorThrown);
		alert("Oops, we got an error...");
		return;
	});
};

Info.turnPage = function(type,direction,pageIndex){
	var pageCntr = new Object;
	var frontPage = new Object;
	var nextPage = new Object;
	
	var tabIndex = Tab.getIndex();
	if(type == "imsi"){
		Info.loadImsi(pageIndex);
	}else if(type == "detail"){
		Info.loadTable(pageIndex);
	}
	
	pageCntr = $("#" + type + "_pageCntr_" + tabIndex);
	frontPage = pageCntr.find("input").eq(0);
	nextPage = pageCntr.find("input").eq(1);
	frontPage.attr("onClick","Info.turnPage('" + type + "','front'," + (pageIndex - 1) + ");");
	nextPage.attr("onClick","Info.turnPage('" + type + "','next'," + (pageIndex + 1) + ");");
	
	if(direction == "front"){
		if(pageIndex == 0){
			frontPage.prop("disabled",true);
		}
		if(nextPage.prop("disabled") == true){
			nextPage.prop("disabled",false);
		}
	}else if(direction == "next"){
		if(frontPage.prop("disabled") == true){
			frontPage.prop("disabled",false);
		}
	}
};
/*
Info.loadMap = function(){
	var tabIndex = Tab.getIndex();
	
	var canvasCntr = $("#detail_canvasCntr_" + tabIndex);
	canvasCntr.empty();
	var canvas = $("<canvas></canvas>");
	canvas.appendTo(canvasCntr);
	canvas.attr("id","detail_canvas_" + tabIndex);
	canvas.attr("class","info_detail_canvas");
	
	var worldCoor = GMap.getWorldCoor(39.9073,116.3911);
	var pixelCoor = GMap.getPixelCoor(worldCoor,11);
	var center = new Object;
	center.x = Math.floor(pixelCoor.x);
	center.y = Math.floor(pixelCoor.y);
	
	var query = "select latitude,longitude,requesttime,traffic " +
		"from t_gb_130610_specific_loc order by requesttime asc";
	$.getJSON(Common.runSqlUrl(),{
		db: "oracle",
		dbname: "orcl",
		user: "bj_gb",
		password: "root",
		sql: query
	}).done(function(data,textStatus,jqXHR){
		var length = data.length;
		
		function sketchProc(processing){
			var map = new Object;
			var index = new Object;
			var lat = new Object;
			var lng = new Object;
			var point = new Object;
			var diameter = new Object;
			
			processing.setup = function(){
				processing.size(600,600);
				processing.frameRate(10);
				
				map = processing.loadImage("css/images/staticmap_600x600.png");
				index = 0;
			}
			
			processing.draw = function(){
				processing.image(map,0,0);
				
				lat = data[index].field[0].value;
				lng = data[index].field[1].value;
				worldCoor = GMap.getWorldCoor(lat,lng);
				pixelCoor = GMap.getPixelCoor(worldCoor,11);
				point.x = Math.floor(pixelCoor.x) - center.x + 300;
				point.y = Math.floor(pixelCoor.y) - center.y + 300;
				
				diameter = processing.parseFloat(data[index].field[3].value);
				diameter += 1;
				diameter = Math.log(diameter);
				diameter += 1;
				diameter = Math.log(diameter);
				diameter *= 50;
				
				processing.noStroke();
				processing.fill(160);
				processing.ellipse(point.x,point.y,diameter,diameter);
				processing.stroke(0);
				processing.strokeWeight(5);
				processing.point(point.x,point.y);
				
				index ++;
				if(index == length){
					processing.image(map,0,0);
					processing.stroke(0);
					processing.strokeWeight(5);
					processing.point(point.x,point.y);
					processing.noLoop();
				}
			}
		}
		
		var canvas = document.getElementById("detail_canvas_" + tabIndex);
		var p = new Processing(canvas,sketchProc);
		
		Info.adjustHeight();
	}).fail(function(jqXHR,textStatus,errorThrown){
		console.log(jqXHR);
		console.log(textStatus);
		console.log(errorThrown);
		alert("Oops, we got an error...");
		return;
	});
};
*/

Info.ctrlMap = function(){
	var tabIndex = Tab.getIndex();
	var img = $("#info_control_window_" + tabIndex).find("img").eq(0);
	var processingInstance = Processing.getInstanceById("detail_canvas2_" + tabIndex);
	
	var value = img.attr("value");
	img.attr("value",1 - value);
	
	if(value == 0){
		img.attr("src","css/images/start_256x256.png");
		processingInstance.noLoop();
	}else{
		img.attr("src","css/images/pause_256x256.png");
		processingInstance.loop();
	}
};

Info.loadMap = function(){
	var tabIndex = Tab.getIndex();
	var imsi = $("#info_control_window_" + tabIndex).find("select").eq(0).val();
	var day = $("#info_control_window_" + tabIndex).find("select").eq(1).val();
	
	var canvasCntr = $("#detail_canvasCntr_" + tabIndex);
	canvasCntr.empty();
	
	if(parseInt(day) == 0){
		var mapCntr = $("<div></div>");
		mapCntr.appendTo(canvasCntr);
		mapCntr.attr("id","detail_map_" + tabIndex);
		mapCntr.attr("class","info_detail_map");
		mapCntr.css({
			"height": "640px",
			"width": "640px"
		});
		
		var mapOptions = {
			center: new google.maps.LatLng(39.9073,116.3911),
			zoom: 11,
			scaleControl: true,
			scaleControlOptions: {
				position: google.maps.ControlPosition.BOTTOM_LEFT
			},
			mapTypeId: google.maps.MapTypeId.ROADMAP
		};
		var map = new google.maps.Map(document.getElementById("detail_map_" + tabIndex),mapOptions);
		
		if(imsi == "460079145886069"){
			$.get("data/data-15/" + imsi + ".txt",function(data){
				var color = new Array("00ffff","808080","008000","ffff00");
				var temp = data.split("\n");
				var dayInfo = new Array(8,9,10,11,12,13,14,19,20,21,22,23,24,25,26,27,28);
				var timePeriod = new Array("20:00~06:59","07:00~09:59","10:00~16:59","17:00~19:59");
				for(var i = 0; i < 17; i++){
					for(var j = 0; j < 4; j++){
						var t = temp[i * 5 + j + 1].split(" ");
						if(t.length <= 2){
							continue;
						}
						var marker = new StyledMarker({
							styleIcon: new StyledIcon(StyledIconTypes.MARKER,{color: color[j],text: String(j + 1)}),
							position: new google.maps.LatLng(t[1],t[2]),
							map: map
						});/*
						
						var boxText = document.createElement("div");
						boxText.style.cssText = "border: 1px solid black; margin-top: 8px; background: #808080; padding: 5px;";
						boxText.innerHTML = "day: 6." + dayInfo[i] + "<br/>time period: " + timePeriod[j] + "<br/>traffic: ";
						
						var myOptions = {
							content: boxText,
							disableAutoPan: false,
							maxWidth: 0,
							pixelOffset: new google.maps.Size(-140, 0),
							zIndex: null,
							boxStyle: {
								background: "url('css/images/tipbox.gif') no-repeat",
								opacity: 0.75,
								width: "280px"
							},
							closeBoxMargin: "10px 2px 2px 2px",
							closeBoxURL: "http://www.google.com/intl/en_us/mapfiles/close.gif",
							infoBoxClearance: new google.maps.Size(1, 1),
							isHidden: false,
							pane: "floatPane",
							enableEventPropagation: false
						};
						
						google.maps.event.addListener(mrkrArr[i][j], "click", function (e) {
							ib.open(map,this);
						});
						
						var ib = new InfoBox(myOptions);*/
					}
				}
			});
		}
		return;
	}
	
	var canvas = $("<canvas></canvas>");
	canvas.appendTo(canvasCntr);
	canvas.attr("id","detail_canvas_" + tabIndex);
	canvas.attr("class","info_detail_canvas");
	
	canvas = $("<canvas></canvas>");
	canvas.appendTo(canvasCntr);
	canvas.attr("id","detail_canvas2_" + tabIndex);
	canvas.attr("class","info_detail_canvas");
	
	function sketchProc(processing){
		var index = new Object;
		var length = new Object;
		var point = new Array();
		
		processing.setup = function(){
			processing.size(640,640);
			
			var trace = processing.loadStrings("data/data-15/" + imsi + "_" + day + ".txt");
			length = trace.length;
			
			var lat_sum = 0;
			var lng_sum = 0;
			for(var i = 0; i < length; i++){
				var record = trace[i];
				var temp = record.split(" ");
				
				var lat = processing.parseFloat(temp[1]);
				var lng = processing.parseFloat(temp[2]);
				lat_sum += lat;
				lng_sum += lng;
			}
			
			var center = new Object;
			center.lat = lat_sum / length;
			center.lng = lng_sum / length;
			
			var img = new Image;
			var myCanvas = document.getElementById("detail_canvas_" + tabIndex);
			var ctx = myCanvas.getContext('2d');
			img.onload = function(){
				myCanvas.height = img.height;
				myCanvas.width = img.width;
				ctx.drawImage(img,0,0);
			}
			if(length == 0){
				img.src = "http://maps.googleapis.com/maps/api/staticmap?center=39.9073,116.3911&zoom=13&size=640x640&sensor=false";
				processing.noLoop();
			}else{
				img.src = "http://maps.googleapis.com/maps/api/staticmap?center=" + center.lat + "," + center.lng + "&zoom=13&size=640x640&sensor=false";
				if(imsi == "460079145886069"){
					var locCenter = processing.loadStrings("data/data-15/" + imsi + ".txt");
					var startIndex = $("#info_control_window_" + tabIndex).find("select").eq(1).prop("selectedIndex") * 5 + 1;
					var color = new Array("blue","gray","green","yellow");
					for(var i = 0; i < 4; i++){
						var temp = locCenter[startIndex + i].split(" ");
						if(temp.length <= 2){
							continue;
						}
						img.src += "&markers=color:" + color[i] + "|label:" + i + "|" + temp[1] + "," + temp[2];
					}
				}
			}
			
			var worldCoor = GMap.getWorldCoor(center.lat,center.lng);
			var pixelCoor = GMap.getPixelCoor(worldCoor,13);
			center.x = Math.floor(pixelCoor.x);
			center.y = Math.floor(pixelCoor.y);
			
			for(var i = 0; i < length; i++){
				var record = trace[i];
				var temp = record.split(" ");
				
				var lat = processing.parseFloat(temp[1]);
				var lng = processing.parseFloat(temp[2]);
				worldCoor = GMap.getWorldCoor(lat,lng);
				pixelCoor = GMap.getPixelCoor(worldCoor,13);
				
				point[i] = new Object;
				
				point[i].x = Math.floor(pixelCoor.x) - center.x + 320;
				point[i].y = Math.floor(pixelCoor.y) - center.y + 320;
				point[i].traffic = temp[0];
				point[i].time = temp[3] + "~" + temp[4];
			}
			
			index = 0;
		}
		
		processing.draw = function(){
			if(point.length != 0){
				rate = $("#info_control_window_" + tabIndex).find("select").eq(2).val();
				processing.frameRate(rate);
				
				processing.showTrace(index);
				index ++;
				if(index == length){
					index = 0;
				}
			}else{
				processing.background(255,255,255,0);
			}
		}
		
		processing.mousePressed = function(){
			var ctrl = $("#info_control_window_" + tabIndex).find("img").eq(0).attr("value");
			if(ctrl == 1){
				processing.showTrace(index);
				index++;
				if(index == length){
					index = 0;
				}
			}
		}
		
		processing.showTrace = function(i){
			processing.background(255,255,255,0);
			
			for(var j = 0; j <= i; j++){
				if(j == i){
					processing.stroke(255,0,0);
				}else{
					processing.stroke(0);
				}
				processing.strokeWeight(5);
				processing.point(point[j].x,point[j].y);
			}
			
			for(var j = 1; j <= i; j++){
				if(j == i){
					processing.stroke(255,0,0);
				}else{
					processing.stroke(0);
				}
				processing.strokeWeight(2);
				processing.line(point[j - 1].x,point[j - 1].y,point[j].x,point[j].y);
			}
			
			processing.noStroke();
			processing.fill(10,150,20,160);
			processing.beginShape();
			processing.vertex(point[i].x,point[i].y);
			processing.vertex(point[i].x + 8,point[i].y - 30);
			processing.vertex(point[i].x + 60,point[i].y - 30);
			processing.vertex(point[i].x + 60,point[i].y - 90);
			processing.vertex(point[i].x - 60,point[i].y - 90);
			processing.vertex(point[i].x - 60,point[i].y - 30);
			processing.vertex(point[i].x - 8,point[i].y - 30);
			processing.endShape(processing.CLOSE);
			
			processing.fill(255);
			processing.text("traffic : " + point[i].traffic,point[i].x - 55,point[i].y - 75);
			processing.text("time period : ",point[i].x - 55,point[i].y - 55);
			processing.text(point[i].time,point[i].x - 55,point[i].y - 35);
		}
	}
	canvas = document.getElementById("detail_canvas2_" + tabIndex);
	var p = new Processing(canvas,sketchProc);
	if(canvasCntr.width() < canvas.width){
		canvasCntr.css({
			"width": canvas.width
		});
	}
	canvasCntr.css({
		"height": canvas.height
	});
	
	Info.adjustHeight();
}

Info.loadChart = function(){
	var tabIndex = Tab.getIndex();
	
	var span = $("#detail_titleCntr_" + tabIndex).find("span").eq(1);
	var imsi = span.text();
	
	var canvasCntr = $("#detail_canvasCntr_2_" + tabIndex);
	canvasCntr.empty();
	var canvas = $("<canvas></canvas>");
	canvas.appendTo(canvasCntr);
	canvas.attr("id","detail_canvas_2_" + tabIndex);
	canvas.attr("class","info_detail_canvas");
	
	$("#info_float_window_" + tabIndex).empty();
	
	var query = "select * from t_gb_130610_period_traffic where imsi = " + imsi;
	$.getJSON(Common.runSqlUrl(),{
		db: "oracle",
		dbname: "orcl",
		user: "bj_gb",
		password: "root",
		sql: query
	}).done(function(data,textStatus,jqXHR){
		var scale = new Array();
		var total = 0;
		var message = new Array("00:00~03:59","04:00~07:59","08:00~11:59","12:00~15:59","16:00~19:59","20:00~23:59");
		var arr = "[";
		for(var i = 0; i < 6; i++){
			scale[i] = new Number(data[0].field[i + 1].value);
			total += scale[i];
			arr += "[\"" + message[i] + "\"," + scale[i] + "]";
			if(i == 5){
				arr += "]";
			}else{
				arr += ",";
			}
		}
		for(var i = 0; i < 6; i++){
			scale[i] /= total;
			if(scale[i] != 0){
				scale[i] += 1;
				scale[i] /= 2;
			}
		}
		
		google.load("visualization","1",{packages:["table"],"callback":drawTable});
		function drawTable(){
			var view = document.getElementById("info_float_window_" + tabIndex);
			var data = new google.visualization.DataTable();
			data.addColumn("string","Period");
			data.addColumn("number","Traffic");
			data.addRows($.parseJSON(arr));
			var options = {
				showRowNumber: true
			};
			var table = new google.visualization.Table(view);
			table.draw(data,options);
		}
		
		function sketchProc(processing){
			var r = new Object;
			var centerVertex = new Object;
			var radius = new Array();
			var outerVertex = new Array();
			var innerVertex = new Array();
			
			processing.setup = function(){
				processing.size(200,220);
				processing.background(255);
				
				r = 80;
				centerVertex.x = 100;
				centerVertex.y = 120;
				
				for(var i = 0; i < 6; i++){
					radius[i] = r * (i / 10 + 0.5);
				}
				
				for(var i = 0; i < 6; i++){
					outerVertex[i] = new Object;
					innerVertex[i] = new Object;
				}
				for(var i = 0; i < 6; i++){
					var theta = i * Math.PI / 3 + Math.PI / 18;
					outerVertex[i].x = centerVertex.x + Math.sin(theta) * r;
					outerVertex[i].y = centerVertex.y - Math.cos(theta) * r;
					innerVertex[i].x = centerVertex.x + Math.sin(theta) * r * scale[i];
					innerVertex[i].y = centerVertex.y - Math.cos(theta) * r * scale[i];
				}
				
				processing.hexagon();
			}
			
			processing.draw = function(){
				var distance = (processing.mouseX - centerVertex.x) * (processing.mouseX - centerVertex.x) +
					(processing.mouseY - centerVertex.y) * (processing.mouseY - centerVertex.y);
				if(distance <= 6400){
					$("#info_float_window_" + tabIndex).css("display","block");
				}else{
					$("#info_float_window_" + tabIndex).css("display","none");
					processing.noLoop();
				}
			}
			
			processing.mouseMoved = function(){
				processing.loop();
			}
			
			processing.hexagon = function(){
				processing.textAlign(processing.CENTER);
				processing.fill(0);
				processing.text("Traffic in six time period",centerVertex.x,20);
				
				for(var i = 0; i < 6; i++){
					processing.strokeWeight(0.2);
					processing.stroke(0);
					processing.noFill();
					processing.ellipse(centerVertex.x,centerVertex.y,2 * radius[i],2 * radius[i]);
				}
				
				for(var i = 0; i < 6; i++){
					processing.strokeWeight(0.2);
					processing.stroke(0);
					processing.line(outerVertex[i].x,outerVertex[i].y,centerVertex.x,centerVertex.y);
				}
				
				for(var i = 0; i < 6; i++){
					var width = processing.textWidth(message[i]) / 2 + 10;
					var theta = i * Math.PI / 3 + Math.PI / 18;
					
					processing.pushMatrix();
					processing.translate(centerVertex.x,centerVertex.y);
					processing.translate(width * Math.sin(theta),-width * Math.cos(theta));
					if(i < 3){
						processing.rotate(theta - Math.PI / 2);
					}else{
						processing.rotate(theta + Math.PI / 2);
					}
					processing.fill(0);
					processing.text(message[i],0,4);
					processing.popMatrix();
				}
				
				processing.noStroke();
				processing.fill(10,150,20,160);
				processing.beginShape();
				for(var i = 0; i < 6; i++){
					processing.vertex(innerVertex[i].x,innerVertex[i].y);
				}
				processing.endShape(processing.CLOSE);
			}
		}
		
		var canvas = document.getElementById("detail_canvas_2_" + tabIndex);
		var p = new Processing(canvas,sketchProc);
		
		Info.adjustHeight();
	}).fail(function(jqXHR,textStatus,errorThrown){
		console.log(jqXHR);
		console.log(textStatus);
		console.log(errorThrown);
		alert("Oops, we got an error...");
		return;
	});
};

Info.adjustHeight = function(){
	var tabIndex = Tab.getIndex();
	var imsiCntr_h = $("#imsiCntr_" + tabIndex).height();
	var detailImsi_h = 0;
	if($("#detailImsi_" + tabIndex).css("display") == "block"){
		detailImsi_h = $("#detailImsi_" + tabIndex).height();
	}
	var tab_h = new Object;
	if(imsiCntr_h > detailImsi_h){
		tab_h = imsiCntr_h;
	}else{
		tab_h = detailImsi_h;
	}
	$("#tab_" + tabIndex).css({
		"height": tab_h + 10
	});
	Tab.adjustHeight();
};
/*
Info. = function(){
	var tabIndex = Tab.getIndex();
	
	var canvasCntr = $("#detail_canvasCntr_" + tabIndex);
	canvasCntr.empty();
	var canvas = $("<canvas></canvas>");
	canvas.appendTo(canvasCntr);
	canvas.attr("id","detail_canvas_" + tabIndex);
	canvas.attr("class","info_detail_canvas");
	
	var worldCoor = GMap.getWorldCoor(39.9073,116.3911);
	var pixelCoor = GMap.getPixelCoor(worldCoor,11);
	var center = new Object;
	center.x = Math.floor(pixelCoor.x);
	center.y = Math.floor(pixelCoor.y);
	
	var query = "select latitude,longitude,requesttime,traffic " +
		"from t_gb_130610_specific_loc order by requesttime asc";
	$.getJSON(Common.runSqlUrl(),{
		db: "oracle",
		dbname: "orcl",
		user: "bj_gb",
		password: "root",
		sql: query
	}).done(function(data,textStatus,jqXHR){
		
	}).fail(function(jqXHR,textStatus,errorThrown){
		console.log(jqXHR);
		console.log(textStatus);
		console.log(errorThrown);
		alert("Oops, we got an error...");
		return;
	});
};*/