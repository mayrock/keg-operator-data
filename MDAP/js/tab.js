Tab = {};

Tab.adjustHeight = function(tabIndex){
	var tabHeight = $("#tab" + tabIndex).height();
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
	if(!((tabType == "geo") || (tabType == "sta") || (tabType == "mgt"))){
		alert("Oops, we got an error...");
		return;
	}
	$("#extraMenu").css("display","none");
	$("#extendedFav").css("display","none");
	length = Common.tabIndex.length - 1;
	var tabIndex = Common.tabIndex[length];
	/*****limit the number of tab*****/
	if(length == Common.tabLimit()){
		alert("Tabs cann't be more than " + Common.tabLimit() + "!");
		return;
	}
	Common.tabIndex[length + 1] = tabIndex + 1;
	li = document.createElement("li");
	li.setAttribute("id","tabs_li" + tabIndex);
	li.setAttribute("class","tabs_li");
	$(li).appendTo("#tabs_ul");
	if(tabType == "geo"){
		li.innerHTML = "<a href = '#tab" + tabIndex + "'>geo data</a>";
	}else if(tabType == "sta"){
		li.innerHTML = "<a href = '#tab" + tabIndex + "'>stat data</a>";
	}else{
		li.innerHTML = "<a href = '#tab" + tabIndex + "'>manage</a>";
	}
	
	var tab = document.createElement("div");
	tab.setAttribute("id","tab" + tabIndex);
	tab.setAttribute("class","tab");
	$(tab).appendTo("#tabs");
	$(tab).css({
		"padding-top": "10px",
		"padding-right": "10px",
		"padding-bottom": 0,
		"padding-left": "10px"
	});
	
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
	}else{
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
	if(tabType == "sta"){
		$("<img src = 'css/images/save_256x256.png' onclick = \"Fav.createFrame(" + tabIndex + ");\"/>")
			.appendTo(li)
			.css({
				"width": "16px",
				"margin-right": "5px"
			});
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
	}
	$("<img src = 'css/images/close_256x256.png' onclick = \"Tab.close('" + tabType + "'," + tabIndex + ");\"/>")
		.appendTo(li)
		.css({
			"width": "16px",
			"margin-top": "8px",
			"margin-right": "5px"
		});
	$("#tabs").tabs("refresh");
	$("#tabs").tabs("option","active",length);
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
			var des = data[i].descriptionZh;
			var dvName = data[i].dataviewName;
			var keys = data[i].identifiers;
			var type = "points";
			if(keys[0] == "Region"){
				type = "regions";
			}
			Geo.loadData(tabIndex,i,dvName,type);
			$("<input type = 'checkbox' id = 'geo-checkbox-" + tabIndex + "-" + i + "' " +
				"onclick = \"Geo.clickEvent(" + tabIndex + "," + i + ",'" + type + "');\"/>" + des + "<br/>").appendTo(checkbox);
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
			
			var des = data[i].descriptionZh;
			var dvName = data[i].dataviewName;
			
			var nameCtnr = document.createElement("div");
			nameCtnr.setAttribute("id","sta-dvName-" + tabIndex + "-" + i);
			nameCtnr.setAttribute("class","sta-dvName");
			$(nameCtnr).appendTo("#sta-dv-list-" + tabIndex);
			$("<a href = 'javascript:void(0);' onClick = \"Sta.guide(" + tabIndex + "," + i + ",'" + dvName + "');\">" +
				des + "</a>").appendTo(nameCtnr);
			
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
			$(titleContent).html(des);
			
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
		
		var ctnrHeight = 25 * len;
		$("#sta-dv-content-" + tabIndex).css({
			"height": ctnrHeight + 5,
			"border": "1px solid blue"
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

/*****close one tab*****/
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