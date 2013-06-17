Tab = {};

/*****add one tab*****/
Tab.createFrame = function(tabType){
	if(!((tabType == "geo") || (tabType == "sta") || (tabType == "manage"))){
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
	if((tabType == "sta") || (tabType == "geo")){
		option = document.createElement("div");
		option.setAttribute("id","option" + tabIndex);
		option.setAttribute("class","option");
		$(option).appendTo(tab);
		view = document.createElement("div");
		view.setAttribute("id","view" + tabIndex);
		view.setAttribute("class","view");
		$(view).appendTo(tab);
	}else{
		subTabs = document.createElement("div");
		subTabs.setAttribute("id","subTabs" + tabIndex);
		subTabs.setAttribute("class","subTabs");
		$(subTabs).appendTo(tab);
		subTabs_ul = document.createElement("ul");
		subTabs_ul.setAttribute("id","subTabs_ul" + tabIndex);
		$(subTabs_ul).appendTo(subTabs);
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
	Tab.load(tabType,tabIndex);
	$("#tabs").tabs("refresh");
	$("#tabs").tabs("option","active",length);
};

/**********load one tab**********/
Tab.load = function(tabType,tabIndex){
	if(tabType == "geo"){
		checkbox = document.createElement("div");
		checkbox.setAttribute("id","checkbox" + tabIndex);
		checkbox.setAttribute("class","checkbox");
		$(checkbox).appendTo("#option" + tabIndex);
		select = document.createElement("div");
		select.setAttribute("id","select" + tabIndex);
		select.setAttribute("class","select");
		$(select).appendTo("#option" + tabIndex);
		$(select).css({})
		$.getJSON(Common.dataviewUrl().replace("tabType",tabType),function(data){
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
	}else if(tabType == "sta"){
		Common.chartIndex[tabIndex] = new Array();
		Common.chartType[tabIndex] = new Array();
		Common.yAxis[tabIndex] = new Array();
		$.getJSON(Common.dataviewUrl().replace("tabType",tabType),function(data){
			var len = data.length;
			for(var i = 0; i < len; i++){
				Common.chartIndex[tabIndex][i] = new Array();
				Common.chartIndex[tabIndex][i][0] = 0;
				Common.chartType[tabIndex][i] = new Array();
				Common.yAxis[tabIndex][i] = new Array();
				var des = data[i].descriptionZh;
				dataview = document.createElement("div");
				dataview.setAttribute("class","dataview");
				$(dataview).appendTo("#option" + tabIndex);
				$("<a href = 'javascript:void(0);' onClick = \"Sta.guide(" + tabIndex + "," + i + ");\">" + des + "</a>").appendTo(dataview);
				view_ds = document.createElement("div");
				view_ds.setAttribute("id","view_ds" + tabIndex + "_" + i);
				view_ds.setAttribute("class","view_ds");
				$(view_ds).appendTo("#view" + tabIndex);
				$("<div id = 'special" + tabIndex + "_" + i + "' style = 'clear:both;'></div>").appendTo(view_ds);
				$(view_ds).css("display","none");
			}
			$("#view" + tabIndex).css({
				"left": $("#option" + tabIndex).width() + 40
			});
		}).error(function(){
			alert("Oops, we got an error...");
			return;
		});
	}else{
		$.getJSON(Common.allDataviewUrl(),function(data){
			var len = data.length;
/*
			dv = document.createElement("div");
			dv.setAttribute("class","dvTitle");
			$(dv).appendTo("#option" + tabIndex);
			$("<span>----------dataview----------</span>").appendTo(dv);*/
			google.load("visualization","1",{packages:["table"],"callback":drawTable});
			
			function drawTable(){
				for(var i = 0; i < len; i++){
					var name = data[i].datasetName;
					var des = data[i].descriptionZh;
					li = document.createElement("li");
					li.setAttribute("id","subTabs_li" + tabIndex + "_" + i);
					li.setAttribute("class","subTabs_li");
					$(li).appendTo("#subTabs_ul" + tabIndex);
					li.innerHTML = "<a href = '#subTab" + tabIndex + "_" + i + "'>" + des + "</a>";
					$("<img src = 'css/images/close_256x256.png' onClick = \"alert('delete');\"/>")
						.appendTo(li)
						.css({
							"float": "right",
							"width": "16px",
							"margin-top": "5px",
							"margin-right": "5px"
						});
					subTab = document.createElement("div");
					subTab.setAttribute("id","subTab" + tabIndex + "_" + i);
					subTab.setAttribute("class","subTab");
					$(subTab).appendTo("#subTabs" + tabIndex);
					dataMgt = document.createElement("div");
					dataMgt.setAttribute("id","dataMgt" + tabIndex + "_" + i);
					dataMgt.setAttribute("class","dataMgt");
					$(dataMgt).appendTo("#subTab" + tabIndex + "_" + i);
					Mgt.load(tabIndex,i,name,"dv");
				}
				$.getJSON(Common.allDatasetUrl(),function(data){
					var length = data.length;/*
					ds = document.createElement("div");
					ds.setAttribute("class","dsTitle");
					$(ds).appendTo("#option" + tabIndex);
					$("<span>----------dataset----------</span>").appendTo(ds);*/
					for(var i = len; i < len + length; i++){
						var name = data[i - len].datasetName;
						var des = data[i - len].descriptionZh;
						li = document.createElement("li");
						li.setAttribute("id","subTabs_li" + tabIndex + "_" + i);
						li.setAttribute("class","subTabs_li");
						$(li).appendTo("#subTabs_ul" + tabIndex);
						li.innerHTML = "<a href = '#subTab" + tabIndex + "_" + i + "'>" + des + "</a>";
						$("<img src = 'css/images/close_256x256.png' onClick = \"alert('delete');\"/>")
							.appendTo(li)
							.css({
								"float": "right",
								"width": "16px",
								"margin-top": "5px",
								"margin-right": "5px"
							});
						subTab = document.createElement("div");
						subTab.setAttribute("id","subTab" + tabIndex + "_" + i);
						subTab.setAttribute("class","subTab");
						$(subTab).appendTo("#subTabs" + tabIndex);
						dataMgt = document.createElement("div");
						dataMgt.setAttribute("id","dataMgt" + tabIndex + "_" + i);
						dataMgt.setAttribute("class","dataMgt");
						$(dataMgt).appendTo("#subTab" + tabIndex + "_" + i);
						Mgt.load(tabIndex,i,name,"ds");
					}
					$("#subTabs" + tabIndex).tabs().addClass("ui-tabs-vertical ui-helper-clearfix");
					$("#subTabs" + tabIndex + " li").removeClass("ui-corner-top").addClass("ui-corner-left");
					$("#subTabs" + tabIndex + " .ui-widget-header").css({
						"border-right": "1px solid #000000"
					});
					var subTabWidth = $("#subTabs" + tabIndex + " ul").width() + 36;
					$(".dataMgt").css({
						"left": subTabWidth,
						"width": Common.width() - subTabWidth - 400
					});
				}).error(function(){
					alert("Oops, we got an error...");
					return;
				});
			}
		}).error(function(){
			alert("Oops, we got an error...");
			return;
		});
	}
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