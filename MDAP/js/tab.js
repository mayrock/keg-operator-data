Tab = {};

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
	Common.adjunct();
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
	tab = document.createElement("div");
	tab.setAttribute("id","tab" + tabIndex);
	tab.setAttribute("class","tab");
	$(tab).appendTo("#tabs");
	$(tab).css({
		"padding-top": "10px",
		"padding-right": 0,
		"padding-bottom": 0,
		"padding-left": 0
	});
	if(tabType == "geo"){
		var option = document.createElement("div");
		option.setAttribute("id","option" + tabIndex);
		option.setAttribute("class","option");
		$(option).appendTo(tab);
		var view = document.createElement("div");
		view.setAttribute("id","view" + tabIndex);
		view.setAttribute("class","view");
		$(view).appendTo(tab);
		Tab.loadGeo(tabIndex);
	}else if(tabType == "sta"){
		var accordion = document.createElement("div");
		accordion.setAttribute("id","accordion" + tabIndex);
		accordion.setAttribute("class","accordion");
		$(accordion).appendTo(tab);
		Tab.loadSta(tabIndex);
	}else{
		var accordion = document.createElement("div");
		accordion.setAttribute("id","accordion" + tabIndex);
		accordion.setAttribute("class","accordion");
		$(accordion).appendTo(tab);
		Mgt.createFrame(tabIndex,"dv");
		Mgt.createFrame(tabIndex,"ds");
		$("#accordion" + tabIndex).accordion({
			collapsible: true,
			activate: function(event,ui){
				Mgt.adjustHeight();
			}
		});
		google.load("visualization","1",{packages:["table"],"callback":drawTable});
		
		function drawTable(){
			Tab.loadMgt(tabIndex,"dv");
			Tab.loadMgt(tabIndex,"ds");
		}
		
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
	checkbox = document.createElement("div");
	checkbox.setAttribute("id","checkbox" + tabIndex);
	checkbox.setAttribute("class","checkbox");
	$(checkbox).appendTo("#option" + tabIndex);
	select = document.createElement("div");
	select.setAttribute("id","select" + tabIndex);
	select.setAttribute("class","select");
	$(select).appendTo("#option" + tabIndex);
	$.getJSON(Common.dataviewUrl().replace("tabType","geo"),function(data){
		var len = data.length;
		if(len == 0){
			$("#view" + tabIndex).css({
				"left": "40px"
			});
			Geo.initMap(tabIndex);
			return;
		}
		$("#option" + tabIndex).css({
			"height": "400px"
		});
		for(var i = 0; i < len; i++){
			var des = data[i].descriptionZh;
			var name = data[i].datasetName;
			var keys = data[i].keys;
			var type = "points";
			if(keys[0] == "Region"){
				type = "regions";
			}
			Geo.loadData(tabIndex,i,name,type);
			$("<input type = 'checkbox' id = 'checkbox" + tabIndex + "_" + i + "' " +
				"onclick = \"Geo.clickEvent(" + tabIndex + "," + i + ",'" + type + "');\"/>" + des + "<br/>").appendTo(checkbox);
		}
		$("<input type = 'button' style = 'font-family: Times New Roman;font-size: 16px' value = 'selectAll' " +
			"onclick = \"Geo.selectAll(" + tabIndex + "," + len + ")\"/>").appendTo(select);
		$("<span>&nbsp;&nbsp;</span><input type = 'button' style = 'font-family: Times New Roman;font-size: 16px;' value = 'invertAll' " +
			"onclick = \"Geo.invertAll(" + tabIndex + "," + len + ")\"/>").appendTo(select);
		if($("#option" + tabIndex).width() < 160){
			$("#option" + tabIndex).css({
				"width": "160px",
				"border": "1px solid #000000"
			});
		}
		$("#view" + tabIndex).css({
			"left": $("#option" + tabIndex).width() + 40
		});
		Geo.initMap(tabIndex);
	}).error(function(){
		alert("Oops, we got an error...");
		return;
	});
};

Tab.loadSta = function(tabIndex){
	Common.chartIndex[tabIndex] = new Array();
	Common.chartType[tabIndex] = new Array();
	Common.yAxis[tabIndex] = new Array();
	$.getJSON(Common.dataviewUrl().replace("tabType","sta"),function(data){
		var len = data.length;
		for(var i = 0; i < len; i++){
			Common.chartIndex[tabIndex][i] = new Array();
			Common.chartIndex[tabIndex][i][0] = 0;
			Common.chartType[tabIndex][i] = new Array();
			Common.yAxis[tabIndex][i] = new Array();
			var des = data[i].descriptionZh;
			
			var head = document.createElement("h3");
			head.setAttribute("id","head" + tabIndex + "_" + i);
			head.setAttribute("class","head");
			$(head).appendTo("#accordion" + tabIndex);
			$(head).css({
				"padding-top": "10px",
				"padding-right": 0,
				"padding-bottom": "10px"
			});
			$("<img src = 'css/images/add_256x256.png' onClick = \"Sta.guide(" + tabIndex + "," + i + ");\"/>")
				.appendTo(head)
				.css({
					"width": "16px",
					"margin-right": "20px"
				});
			$("<span>" + des + "</span>").appendTo(head);
			var content = document.createElement("div");
			content.setAttribute("id","content" + tabIndex + "_" + i);
			content.setAttribute("class","content");
			$(content).appendTo("#accordion" + tabIndex);
			$(content).css({
				"padding-top": 0,
				"padding-right": 0,
				"padding-bottom": 0,
				"padding-left": "20px"
			});
			
			view_ds = document.createElement("div");
			view_ds.setAttribute("id","view_ds" + tabIndex + "_" + i);
			view_ds.setAttribute("class","view_ds");
			$(view_ds).appendTo(content);
			$(view_ds).css({
				"width": 0
			})
		}
		$("#accordion" + tabIndex).accordion({
			collapsible: true,
			activate: function(event,ui){
				var activeTab = $("#tabs").tabs("option","active");
				var tabIndex = Common.tabIndex[activeTab];
				var active = $("#accordion" + tabIndex).accordion("option","active");
				if(typeof(active) == "boolean"){
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
					return;
				}
				var l = Common.chartIndex[tabIndex][active].length;
				if(l == 1){
					$("#content" + tabIndex + "_" + active).css({
						"height": 0
					});
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
					return;
				}
				var dsWidth = $("#view_ds" + tabIndex + "_" + active).width();
				var width = $("#content" + tabIndex + "_" + active).width();
				var dsHeight = $("#view_ds" + tabIndex + "_" + active).height();
				if(dsWidth <= width){
					$("#content" + tabIndex + "_" + active).css({
						"padding-top": "10px",
						"height": dsHeight + 10
					});
				}else{
					$("#content" + tabIndex + "_" + active).css({
						"padding-top": "10px",
						"height": dsHeight + 25
					});
				}
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
			}
		});
		$("#accordion" + tabIndex).accordion("option","active",false);
	}).error(function(){
		alert("Oops, we got an error...");
		return;
	});
};

Tab.loadMgt = function(tabIndex,type){
	var url = "";
	if(type == "dv"){
		url = Common.allDataviewUrl();
	}else{
		url = Common.allDatasetUrl();
	}
	$.getJSON(url,function(data){
		var len = data.length;
		for(var i = 0; i < len; i++){
			var name = data[i].datasetName;
			var des = data[i].descriptionZh;
			li = document.createElement("li");
			li.setAttribute("id",type + "Tabs_li" + tabIndex + "_" + i);
			li.setAttribute("class",type + "Tabs_li");
			$(li).appendTo("#" + type + "Tabs_ul" + tabIndex);
			li.innerHTML = "<a href = '#" + type + "Tab" + tabIndex + "_" + i + "'>" + des + "</a>";
			$("#" + type + "Tabs_li" + tabIndex + "_" + i + " a").css({
				"padding-top": "4px",
				"padding-bottom": "4px"
			});
			$("<img src = 'css/images/close_256x256.png' onClick = \"alert('delete');\"/>")
				.appendTo(li)
				.css({
					"float": "right",
					"width": "16px",
					"margin-top": "5px",
					"margin-right": "5px"
				});
			subTab = document.createElement("div");
			subTab.setAttribute("id",type + "Tab" + tabIndex + "_" + i);
			subTab.setAttribute("class",type + "Tab");
			$(subTab).appendTo("#" + type + "Tabs" + tabIndex);
			$(subTab).css({
				"padding": 0
			});
			dataMgt = document.createElement("div");
			dataMgt.setAttribute("id",type + "DataMgt" + tabIndex + "_" + i);
			dataMgt.setAttribute("class",type + "DataMgt");
			$(dataMgt).appendTo("#" + type + "Tab" + tabIndex + "_" + i);
			$(dataMgt).css({
				"width": 400
			});
			Mgt.load(tabIndex,i,name,type);
		}
		$("#" + type + "Tabs" + tabIndex).tabs({
			activate: function(event,ui){
				Mgt.adjustHeight();
			}
		}).addClass("ui-tabs-vertical ui-helper-clearfix");
		$("#" + type + "Tabs" + tabIndex + " li").removeClass("ui-corner-top").addClass("ui-corner-left");
		$("#" + type + "Tabs" + tabIndex + " .ui-widget-header").css({
			"border-right": "1px solid #000000"
		});
	}).error(function(){
		alert("Oops, we got an error...");
		return;
	});
};

/*****close one tab*****/
Tab.close = function(tabType,tabIndex){
	$("#tabs_li" + tabIndex).remove();
	$("#tab" + tabIndex).remove();
	Common.tabIndex.splice(tabIndex,1);
	Common.adjunct();
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