Tab = {};

Tab.adjustHeight = function(){
	var tabIndex = Tab.getIndex();
	var tabHeight = $("#tab_" + tabIndex).height();
	if((tabHeight + 25) > Common.tabHeight){
		$("#tab_bg").css({
			"height": tabHeight + 25
		});
	}else{
		$("#tab_bg").css({
			"height": Common.tabHeight
		});
	}
};

/*****add one tab*****/
Tab.createFrame = function(tabType){
	if(!((tabType == "geo") || (tabType == "sta") || (tabType == "mgt") || (tabType == "info"))){
		alert("Oops, we got an error...");
		return;
	}
	
	$("#extraMenu").css("display","none");
	$("#extendedFav").css("display","none");
	
//	length = Common.tabIndex.length - 1;
//	var tabIndex = Common.tabIndex[length];
	
	/*****limit the number of tab*****/
	var ul = $("#tabs_ul");
	var length = ul.find("li").length;
	if(length == Common.tabLimit()){
		alert("Tabs cann't be more than " + Common.tabLimit() + "!");
		return;
	}
	
	var date = new Date();
	var tabIndex = date.getTime();
	
//	Common.tabIndex[length + 1] = tabIndex + 1;
	var li = document.createElement("li");
	li.setAttribute("id",tabIndex);
	li.setAttribute("class","tabs_li");
	$(li).appendTo(ul);
	if(tabType == "geo"){
		li.innerHTML = "<a href = '#tab" + tabIndex + "'>geo data</a>";
	}else if(tabType == "sta"){
		li.innerHTML = "<a href = '#tab" + tabIndex + "'>stat data</a>";
	}else if(tabType == "mgt"){
		li.innerHTML = "<a href = '#tab" + tabIndex + "'>manage</a>";
	}else if(tabType == "info"){
		li.innerHTML = "<a href = '#tab_" + tabIndex + "'>user info</a>";
	}
	
	var tab = document.createElement("div");
	tab.setAttribute("id","tab_" + tabIndex);
	tab.setAttribute("class","tab");
	$(tab).appendTo("#tabs");
	$(tab).css({
		"padding-top": "20px",
		"padding-right": "10px",
		"padding-bottom": 0,
		"padding-left": "20px"
	});
	/*
	if(tabType == "geo"){
		var option = document.createElement("div");
		option.setAttribute("id","geo-option-" + tabIndex);
		option.setAttribute("class","geo-option");
		$(option).appendTo(tab);
		
		var view = document.createElement("div");
		view.setAttribute("id","geo-view-" + tabIndex);
		view.setAttribute("class","geo-view");
		$(view).appendTo(tab);
		
		Tab.loadGeo(tabIndex);
	}else if(tabType == "sta"){
		var staList = document.createElement("div");
		staList.setAttribute("id","staList" + tabIndex);
		staList.setAttribute("class","staList");
		$(staList).appendTo(tab);
		
		var accordion = document.createElement("div");
		accordion.setAttribute("id","accordion" + tabIndex);
		accordion.setAttribute("class","accordion");
		$(accordion).appendTo(staList);
		
		var head = document.createElement("h3");
		head.setAttribute("id","sta-dv-head-" + tabIndex);
		head.setAttribute("class","head");
		$(head).appendTo("#accordion" + tabIndex);
		$(head).css({
			"padding-top": 0,
			"padding-right": "10px",
			"padding-bottom": 0,
		});
		$(head).html("data view");
		
		var content = document.createElement("div");
		content.setAttribute("id","sta-dv-content-" + tabIndex);
		content.setAttribute("class","content");
		$(content).appendTo("#accordion" + tabIndex);
		$(content).css({
			"padding-top": "5px",
			"padding-right": "10px",
			"padding-bottom": 0,
			"padding-left": "25px"
		});
		
		var list = document.createElement("div");
		list.setAttribute("id","sta-dv-list-" + tabIndex);
		list.setAttribute("class","sta-list");
		$(list).appendTo(content);
		
		$("#accordion" + tabIndex).accordion({
			collapsible: true,
			activate: function(event,ui){
				var activeTab = $("#tabs").tabs("option","active");
				var tabIndex = Common.tabIndex[activeTab];
				var listHeight = $("#staList" + tabIndex).height();
				var viewHeight = $("#staView" + tabIndex).height();
				if(listHeight > viewHeight){
					$("#tab" + tabIndex).css({
						"height": listHeight + 10
					});
				}else{
					$("#tab" + tabIndex).css({
						"height": viewHeight + 10
					});
				}
				Tab.adjustHeight(tabIndex);
			}
		});
		
		var staView = document.createElement("div");
		staView.setAttribute("id","staView" + tabIndex);
		staView.setAttribute("class","staView");
		$(staView).appendTo(tab);
		
		Tab.loadSta(tabIndex);
		
		$("<img src = 'css/images/save_256x256.png' onclick = \"Fav.createFrame(" + tabIndex + ");\"/>")
			.appendTo(li)
			.css({
				"width": "16px",
				"margin-right": "5px"
			});
	}else if(tabType == "mgt"){
		var accordion = document.createElement("div");
		accordion.setAttribute("id","accordion" + tabIndex);
		accordion.setAttribute("class","accordion");
		$(accordion).appendTo(tab);
		
		Mgt.accordion(tabIndex,"dv");
		Mgt.accordion(tabIndex,"ds");
		
		google.load("visualization","1",{packages:["table"],"callback":drawTable});
		function drawTable(){
			Mgt.subTab(tabIndex,"dv","sta");
			Mgt.subTab(tabIndex,"dv","geo");
			Mgt.subTab(tabIndex,"dv","other");
			Mgt.subTab(tabIndex,"ds","pub");
			Mgt.subTab(tabIndex,"ds","lim");
			Mgt.subTab(tabIndex,"ds","own");
		}
		
		$("#accordion" + tabIndex).accordion({
			collapsible: true,
			activate: function(event,ui){
				Mgt.adjustHeight();
			}
		});
		$("#accordion" + tabIndex).accordion("option","active",false);
	}
	
	var permit = Common.permit;
	if(permit == 2){
		if((tabType == "sta") || (tabType == "geo")){
			$("<img src = 'css/images/refresh_512x512.png' onclick = \"alert('refresh!');\"/>")
				.appendTo(li)
				.css({
					"width": "16px",
					"margin-right": "5px"
				});
		}
	}*/
	$("<img src = 'css/images/close_256x256.png' onclick = \"Tab.close(" + tabIndex + ");\"/>")
		.appendTo(li)
		.css({
			"width": "16px",
			"margin-top": "8px",
			"margin-right": "5px"
		});
	
	$("#tabs").tabs("refresh");
	$("#tabs").tabs("option","active",length);
	if(tabType == "info"){
		Tab.info.imsiListCntr();
		Tab.info.detailImsiCntr();
		Tab.info.lv2Tab0();
		Tab.info.lv2Tab1();
		Tab.info.lv2Tab2();
		
		Info.loadImsi(0);
	}
};

Tab.loadGeo = function(tabIndex){
	var checkbox = document.createElement("div");
	checkbox.setAttribute("id","geo-checkbox" + tabIndex);
	checkbox.setAttribute("class","geo-checkbox");
	$(checkbox).appendTo("#geo-option-" + tabIndex);
	
	var select = document.createElement("div");
	select.setAttribute("id","geo-select" + tabIndex);
	select.setAttribute("class","geo-select");
	$(select).appendTo("#geo-option-" + tabIndex);
	
	$.getJSON(Common.dataviewUrl(),{
		featuretype: "GeoFeature"
	},function(data){
		var len = data.length;
		if(len == 0){
			$("#geo-view-" + tabIndex).css({
				"left": "40px"
			});
			Geo.initMap(tabIndex);
			return;
		}
		$("#geo-option-" + tabIndex).css({
			"height": "400px"
		});
		
		Common.mapInfoArr[tabIndex] = new Array();
		for(var i = 0; i < len; i++){
			var id = data[i].id;
			var dvName = data[i].dataviewName;
			var keys = data[i].identifiers;
			var type = "points";
			if(keys[0] == "Region"){
				type = "regions";
			}
			Geo.loadData(tabIndex,i,id,type);
			$("<input type = 'checkbox' id = 'geo-checkbox-" + tabIndex + "-" + i + "' " +
				"onclick = \"Geo.clickEvent(" + tabIndex + "," + i + ",'" + type + "');\"/>" + dvName + "<br/>").appendTo(checkbox);
		}
		$("<input type = 'button' style = 'font-family: Times New Roman;font-size: 16px' value = 'selectAll' " +
			"onclick = \"Geo.selectAll(" + tabIndex + "," + len + ")\"/>").appendTo(select);
		$("<span>&nbsp;&nbsp;</span><input type = 'button' style = 'font-family: Times New Roman;font-size: 16px;' value = 'invertAll' " +
			"onclick = \"Geo.invertAll(" + tabIndex + "," + len + ")\"/>").appendTo(select);
		
		if($("#geo-option-" + tabIndex).width() < 160){
			$("#geo-option-" + tabIndex).css({
				"width": "160px",
				"border": "1px solid #000000"
			});
		}
		$("#geo-view-" + tabIndex).css({
			"left": $("#geo-option-" + tabIndex).width() + 40
		});
		Geo.initMap(tabIndex);
		
		$("#tab" + tabIndex).css({
			"height": "400px"
		});
		Tab.adjustHeight(tabIndex);
	}).error(function(){
		alert("Oops, we got an error...");
		return;
	});
};

Tab.loadSta = function(tabIndex){
	Common.chartIndex[tabIndex] = new Array();
	Common.chartType[tabIndex] = new Array();
	Common.yAxis[tabIndex] = new Array();
	$.getJSON(Common.dataviewUrl(),{
		featuretype: "DistributionFeature"
	},function(data){
		var len = data.length;
		for(var i = 0; i < len; i++){
			Common.chartIndex[tabIndex][i] = new Array();
			Common.chartIndex[tabIndex][i][0] = 0;
			Common.chartType[tabIndex][i] = new Array();
			Common.yAxis[tabIndex][i] = new Array();
			
			var dvName = data[i].dataviewName;
			
			var nameCtnr = document.createElement("div");
			nameCtnr.setAttribute("id","sta-dvName-" + tabIndex + "-" + i);
			nameCtnr.setAttribute("class","sta-dvName");
			$(nameCtnr).appendTo("#sta-dv-list-" + tabIndex);
			$("<a href = 'javascript:void(0);' onClick = \"Sta.guide(" + tabIndex + "," + i + ");\">" +
				dvName + "</a>").appendTo(nameCtnr);
			
			var view_dv = document.createElement("div");
			view_dv.setAttribute("id","sta-view-dv-" + tabIndex + "-" + i);
			view_dv.setAttribute("class","view_dv");
			$(view_dv).appendTo("#staView" + tabIndex);
			$(view_dv).css({
				"display": "none"
			});
			
			var dvTitle = document.createElement("div");
			dvTitle.setAttribute("id","dvTitle-" + tabIndex + "-" + i);
			dvTitle.setAttribute("class","dvTitle");
			$(dvTitle).appendTo(view_dv);
			
			var titleContent = document.createElement("div");
			titleContent.setAttribute("id","titleContent-" + tabIndex + "-" + i);
			titleContent.setAttribute("class","titleContent");
			$(titleContent).appendTo(dvTitle);
			$(titleContent).html(dvName);
			
			$("<img src = 'css/images/close_256x256.png' onclick = \"Sta.closeAllChart(" + tabIndex + "," + i + ");\"/>")
				.appendTo(dvTitle)
				.css({
					"position": "absolute",
					"right": "5px",
					"bottom": "5px",
					"width": "16px"
				});
			
			var dvContainer = document.createElement("div");
			dvContainer.setAttribute("id","dvContainer-" + tabIndex + "-" + i);
			dvContainer.setAttribute("class","dvContainer");
			$(dvContainer).appendTo(view_dv);
		}
		
		var ctnrHeight = 24 * len;
		$("#sta-dv-content-" + tabIndex).css({
			"height": ctnrHeight + 5,
			"border-right": "1px solid #282828",
			"border-bottom": "1px solid #282828",
			"border-left": "1px solid #282828"
		});
		var listWidth = $("#staList" + tabIndex).width();
		$("#staList" + tabIndex).css({
			"width": listWidth
		});
		var viewWidth = Common.width() - listWidth - 100;
		$("#staView" + tabIndex).css({
			"width": viewWidth
		});
		var listHeight = $("#staList" + tabIndex).height();
		$("#tab" + tabIndex).css({
			"height": listHeight + 10
		});
		Tab.adjustHeight(tabIndex);
	}).error(function(){
		alert("Oops, we got an error...");
		return;
	});
};



Tab.info = {};

Tab.info.imsiListCntr = function(){
	var tabIndex = Tab.getIndex();
	
	var imsiCntr = $("<div></div>");
	imsiCntr.appendTo("#tab_" + tabIndex);
	imsiCntr.attr("id","imsiCntr_" + tabIndex);
	imsiCntr.attr("class","info_imsiCntr");
	
	var window = $("<div></div>");
	window.appendTo("#tab_" + tabIndex);
	window.attr("id","info_float_window_" + tabIndex);
	window.attr("class","info_float_window");
	window.css({
		"display": "none"
	});
	
	var titleCntr = $("<div></div>");
	titleCntr.appendTo(imsiCntr);
	titleCntr.attr("id","imsi_titleCntr_" + tabIndex);
	titleCntr.attr("class","info_imsi_titleCntr");
	titleCntr.append("<span>Imsi List</span>");
	
	var tableCntr = $("<div></div>");
	tableCntr.appendTo(imsiCntr);
	tableCntr.attr("id","imsi_tableCntr_" + tabIndex);
	tableCntr.attr("class","info_imsi_tableCntr");
	
	var pageCntr = $("<div></div>");
	pageCntr.appendTo(imsiCntr);
	pageCntr.attr("id","imsi_pageCntr_" + tabIndex);
	pageCntr.attr("class","info_imsi_pageCntr");
	
	var frontPage = $("<input/>");
	frontPage.appendTo(pageCntr);
	frontPage.attr("id","imsi_frontPage_" + tabIndex);
	frontPage.attr("class","info_imsi_page");
	frontPage.attr("type","button");
	frontPage.attr("value","frontPage");
	frontPage.prop("disabled",true);
	
	var nextPage = $("<input/>");
	nextPage.appendTo(pageCntr);
	nextPage.attr("id","imsi_nextPage_" + tabIndex);
	nextPage.attr("class","info_imsi_page");
	nextPage.attr("type","button");
	nextPage.attr("value","nextPage");
	nextPage.attr("onClick","Info.turnPage('imsi','next',1);");
};

Tab.info.detailImsiCntr = function(){
	var tabIndex = Tab.getIndex();
	
	var detailImsi = $("<div></div>");
	detailImsi.appendTo("#tab_" + tabIndex);
	detailImsi.attr("id","detailImsi_" + tabIndex);
	detailImsi.attr("class","info_detailImsi");
	detailImsi.css({
		"display": "none"
	});
	
	var titleCntr = $("<div></div>");
	titleCntr.appendTo(detailImsi);
	titleCntr.attr("id","detail_titleCntr_" + tabIndex);
	titleCntr.attr("class","info_detail_titleCntr");
	
	var tabs = $("<div></div>");
	tabs.appendTo(detailImsi);
	tabs.attr("id","lv2_tabs_" + tabIndex);
	tabs.attr("class","info_lv2_tabs");
	
	var ul = $("<ul></ul>");
	ul.appendTo(tabs);
	ul.attr("id","lv2_tabs_ul_" + tabIndex);
	ul.attr("class","info_lv2_tabs_ul");
	
	var li = new Object;
	var tab = new Object;
	for(var i = 0; i < 3; i++){
		li = $("<li></li>");
		li.appendTo(ul);
		li.attr("id","lv2_tabs_li_" + i + "_" + tabIndex);
		li.attr("class","info_lv2_tabs_li");
		
		tab = $("<div></div>");
		tab.appendTo(tabs);
		tab.attr("id","lv2_tab_" + i + "_" + tabIndex);
		tab.attr("class","info_lv2_tab");
	}
	
	$("#lv2_tabs_li_0_" + tabIndex).html("<a href = '#lv2_tab_0_" + tabIndex + "'>Table</a>");
	$("#lv2_tabs_li_1_" + tabIndex).html("<a href = '#lv2_tab_1_" + tabIndex + "'>Map</a>");
	$("#lv2_tabs_li_2_" + tabIndex).html("<a href = '#lv2_tab_2_" + tabIndex + "'>Chart</a>");
	
	tabs.tabs({
		activate: function(){
			Info.adjustHeight();
		}
	});
	tabs.tabs("option","active",1);
};

Tab.info.lv2Tab0 = function(){
	var tabIndex = Tab.getIndex();
	var tab = $("#lv2_tab_0_" + tabIndex);
	
	var tableCntr = $("<div></div>");
	tableCntr.appendTo(tab);
	tableCntr.attr("id","detail_tableCntr_" + tabIndex);
	tableCntr.attr("class","info_detail_tableCntr");
	
	var pageCntr = $("<div></div>");
	pageCntr.appendTo(tab);
	pageCntr.attr("id","detail_pageCntr_" + tabIndex);
	pageCntr.attr("class","info_detail_pageCntr");
	
	var frontPage = $("<input/>");
	frontPage.appendTo(pageCntr);
	frontPage.attr("id","detail_frontPage_" + tabIndex);
	frontPage.attr("class","info_detail_page");
	frontPage.attr("type","button");
	frontPage.attr("value","frontPage");
	frontPage.prop("disabled",true);
	
	var nextPage = $("<input/>");
	nextPage.appendTo(pageCntr);
	nextPage.attr("id","detail_nextPage_" + tabIndex);
	nextPage.attr("class","info_detail_page");
	nextPage.attr("type","button");
	nextPage.attr("value","nextPage");
	nextPage.attr("onClick","Info.turnPage('detail','next',1);");
};

Tab.info.lv2Tab1 = function(){
	var tabIndex = Tab.getIndex();
	var tab = $("#lv2_tab_1_" + tabIndex);
	
	var window = $("<div></div>");
	window.appendTo(tab);
	window.attr("id","info_control_window_" + tabIndex);
	window.attr("class","info_control_window");
	
	$("<span>choose a imsi:</span>").appendTo(window);
	var select = $("<select></select>");
	select.appendTo(window);
	select.change(function(){
		var tabIndex = Tab.getIndex();
		var selectIndex = $("#info_control_window_" + tabIndex).find("select").eq(1).prop("selectedIndex");
		if(selectIndex != 17){
			var processingInstance = Processing.getInstanceById("detail_canvas2_" + tabIndex);
			processingInstance.exit();
		}
		
		var img = $("#info_control_window_" + tabIndex).find("img").eq(0);
		var value = img.attr("value");
		if(value == 1){
			img.attr("src","css/images/pause_256x256.png");
			img.attr("value",1 - value);
		}
		Info.loadMap();
	});
	select.css({
		"font-family": "Times New Roman,\"楷体\"",
		"font-size": "16px"
	});
	
	$.getJSON("data/data-15/imsi.json")
	.done(function(data,textStatus,jqXHR){
		select = window.find("select").eq(0);
		for(var i = 0; i < data.imsi.length; i++){
			var option = $("<option></option>");
			option.appendTo(select);
			option.attr("value",data.imsi[i]);
			option.text(data.imsi[i]);
		}
	}).fail(function(jqXHR,textStatus,errorThrown){
		console.log(jqXHR);
		console.log(textStatus);
		console.log(errorThrown);
		alert("Oops, we got an error...");
		return;
	});
	
	$("<span>choose a date:</span>").appendTo(window);
	select = $("<select></select>");
	select.appendTo(window);
	select.change(function(){
		var tabIndex = Tab.getIndex();
		var selectIndex = $("#info_control_window_" + tabIndex).find("select").eq(1).prop("selectedIndex");
		if(selectIndex != 17){
			var processingInstance = Processing.getInstanceById("detail_canvas2_" + tabIndex);
			processingInstance.exit();
		}
		
		var img = $("#info_control_window_" + tabIndex).find("img").eq(0);
		var value = img.attr("value");
		if(value == 1){
			img.attr("src","css/images/pause_256x256.png");
			img.attr("value",1 - value);
		}
		Info.loadMap();
	});
	select.css({
		"font-family": "Times New Roman,\"楷体\"",
		"font-size": "16px"
	});
	
	var day = new Array(8,9,10,11,12,13,14,19,20,21,22,23,24,25,26,27,28);
	for(var i = 0; i < day.length; i++){
		var option = $("<option></option>");
		option.appendTo(select);
		option.attr("value",day[i]);
		option.text("\xa0" + day[i] + "\xa0");
	}
	var option = $("<option></option>");
	option.appendTo(select);
	option.attr("value",0);
	option.text("\xa0All\xa0");
	
	$("<span>frame rate:</span>").appendTo(window);
	select = $("<select></select>");
	select.appendTo(window);
	select.css({
		"font-family": "Times New Roman,\"楷体\"",
		"font-size": "16px"
	});
	
	for(var i = 0; i < 6; i++){
		var option = $("<option></option>");
		option.appendTo(select);
		option.attr("value",1 << i);
		option.text("\xa0" + (1 << i) + "\xa0");
	}
	
	var imgCntr = $("<div></div>");
	imgCntr.appendTo(window);
	imgCntr.css({
		"float": "right",
		"margin-top": "5px",
		"margin-right": "20px",
		"margin-left": "10px"
	});
	
	$("<img src = 'css/images/pause_256x256.png' onclick = \"Info.ctrlMap();\">")
		.appendTo(imgCntr)
		.attr("value","0")
		.css({
			"width": "20px"
		});
	
	var canvasCntr = $("<div></div>");
	canvasCntr.appendTo(tab);
	canvasCntr.attr("id","detail_canvasCntr_" + tabIndex);
	canvasCntr.attr("class","info_detail_canvasCntr");
};

Tab.info.lv2Tab2 = function(){
	var tabIndex = Tab.getIndex();
	var tab = $("#lv2_tab_2_" + tabIndex);
	
	var canvasCntr = $("<div></div>");
	canvasCntr.appendTo(tab);
	canvasCntr.attr("id","detail_canvasCntr_2_" + tabIndex);
	canvasCntr.attr("class","info_detail_canvasCntr");
};

Tab.close = function(tabIndex){
	$("#" + tabIndex).remove();
	$("#tab_" + tabIndex).remove();
	$("#tabs").tabs("refresh");
}

/*****close one tab*****/
/*
Tab.close = function(tabType,tabIndex){
	$("#tabs_li" + tabIndex).remove();
	$("#tab" + tabIndex).remove();
	if(tabType == "sta"){
		Common.chartIndex[tabIndex] = new Object;
		Common.chartType[tabIndex] = new Object;
		Common.yAxis[tabIndex] = new Object;
	}else if(tabType == "geo"){
		Common.mapArr[tabIndex] = new Object;
		Common.mapInfoArr[tabIndex] = new Object;
	}
	Common.tabIndex.splice(tabIndex,1);
	$("#tabs").tabs("refresh");
};

/**********refresh one tab**********/
Tab.refresh = function(tabIndex){
	$("#option" + tabIndex).empty();
	$("#view" + tabIndex).empty();
	$.getJSON(Common.dataviewUrl().replace("tabType","sta"),function(data){
		var len = data.length;
		google.load("visualization","1",{packages:["corechart"],"callback":drawChart});
		function drawChart(){
			for(var i = 0; i < len; i++){
				var des = data[i].descriptionZh;
				$("<span>&nbsp;</span><img src = 'css/images/add.png'/><span>&nbsp;</span><a href = 'javascript:void(0);' " +
					"onClick = \"Sta.guide(" + tabIndex + "," + i + ");\">" + des + "</a><br/>").appendTo("#option" + tabIndex);
				view_ds = document.createElement("div");
				view_ds.setAttribute("id","view_ds" + tabIndex + "_" + i);
				view_ds.setAttribute("class","view_ds");
				$(view_ds).appendTo("#view" + tabIndex);
				$("<div id = 'special" + tabIndex + "_" + i + "' style = 'clear:both;'></div>").appendTo(view_ds);
				$(view_ds).css("display","none");
				var l = Common.chartIndex[tabIndex][i].length;
				if(l == 1){
					continue;
				}
				$(view_ds).css("display","block");
				var cType = new Array();
				var yAxisData = new Array();
				for(var j = 0; j < l - 1; j++){
					var chartIndex = Common.chartIndex[tabIndex][i][j];
					cType[j] = Common.chartType[tabIndex][i][chartIndex];
					yAxisData[j] = new Array();
					var yAxisLen = Common.yAxis[tabIndex][i][chartIndex].length;
					for(var k = 0; k < yAxisLen; k++){
						yAxisData[j][k] = Common.yAxis[tabIndex][i][chartIndex][k];
					}
					Common.chartIndex[tabIndex][i][j] = j;
				}
				Common.chartIndex[tabIndex][i][j] = j;
				Common.chartType[tabIndex][i] = new Array();
				Common.yAxis[tabIndex][i] = new Array();
				for(var j = 0; j < l - 1; j++){
					Common.chartType[tabIndex][i][j] = cType[j];
					Common.yAxis[tabIndex][i][j] = new Array();
					var yAxisLen = yAxisData[j].length;
					for(var k = 0; k < yAxisLen; k++){
						Common.yAxis[tabIndex][i][j][k] = yAxisData[j][k];
					}
				}
				for(var j = 0; j < l - 1; j++){
					Sta.createChart(tabIndex,i,j);
				}
			}
		}
	}).error(function(){
		alert("Oops, we got an error...");
	});
};

Tab.getIndex = function(){
	var index = $("#tabs").tabs("option","active");
	var ul = $("#tabs_ul");
	var li = ul.find("li").eq(index);
	var tabIndex = li.attr("id");
	return tabIndex;
}
