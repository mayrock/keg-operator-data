Tab = {};

/**********add a tab**********/
Tab.createFrame = function(tabType){
	if(!((tabType == "geo") || (tabType == "sta"))){
		alert("Oops, we got an error...");
		return;
	}
	$("#extraMenu").css("display","none");
	$("#extendedFav").css("display","none");
	length = Common.tabIndex.length - 1;
	var tabIndex = Common.tabIndex[length];
	/**********limit the number of tab**********/
	if(length == Common.tabLimit()){
		alert("Tabs cann't be more than " + Common.tabLimit() + "!");
		return;
	}
	Common.tabIndex[length + 1] = tabIndex + 1;
	li = document.createElement("li");
	li.setAttribute("id","tabs_li" + tabIndex);
	$(li).appendTo("#tabs_ul");
	if(tabType == "geo"){
		li.innerHTML = "<a href='#tab" + tabIndex + "'>geo data</a>";
	}else if(tabType == "sta"){
		li.innerHTML = "<a href='#tab" + tabIndex + "'>stat data</a>";
	}else{
		/**********need to do**********/
	}
	tab = document.createElement("div");
	tab.setAttribute("id","tab" + tabIndex);
	tab.setAttribute("class","tab");
	$(tab).appendTo("#tabs");
	option = document.createElement("div");
	option.setAttribute("id","option" + tabIndex);
	option.setAttribute("class","option");
	$(option).appendTo(tab);
	view = document.createElement("div");
	view.setAttribute("id","view" + tabIndex);
	view.setAttribute("class","view");
	$(view).appendTo(tab);
	if(tabType == "sta"){
		$("<img src = 'css/images/save.png' onclick = \"Fav.createFrame(" + tabIndex + ");\"/>").appendTo(li);
//		$("<img src = 'css/images/save.png' onclick = \"Tab.refresh(" + tabIndex + ");\"/>").appendTo(li);
	}
	$("<img src = 'css/images/close.png'/>")
		.appendTo(li)
		.hover(
			function(){
				$(this).attr("src","css/images/close_hover.png");
			},function(){
				$(this).attr("src","css/images/close.png");
			}
		).css({
			"margin-top": "12px",
			"margin-right": "5px"
		}).click(
			function(){
				Tab.close(tabType,tabIndex);
			}
		);
	Tab.load(tabType,tabIndex);
	$("#tabs").tabs("refresh");
	$("#tabs").tabs("option","active",length);
};

/**********initialize a tab**********/
Tab.load = function(tabType,tabIndex){
	if(tabType == "geo"){
		Geo.initMap(tabIndex);
		checkbox = document.createElement("div");
		checkbox.setAttribute("id","checkbox" + tabIndex);
		checkbox.setAttribute("class","checkbox");
		$(checkbox).appendTo("#option" + tabIndex);
		select = document.createElement("div");
		select.setAttribute("id","select" + tabIndex);
		select.setAttribute("class","select");
		$(select).appendTo("#option" + tabIndex);
		$.getJSON(Common.dataViewUrl().replace("tabType",tabType),function(data){
			var len = data.length;
			for(var i = 0; i < len; i++){
				var des = data[i].description;
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
		}).error(function(){
			alert("Oops, we got an error...");
			return;
		});
	}else if(tabType == "sta"){
		Common.chartIndex[tabIndex] = new Array();
		Common.chartType[tabIndex] = new Array();
		Common.yAxis[tabIndex] = new Array();
		$.getJSON(Common.dataViewUrl().replace("tabType",tabType),function(data){
			var len = data.length;
			for(var i = 0; i < len; i++){
				Common.chartIndex[tabIndex][i] = new Array();
				Common.chartIndex[tabIndex][i][0] = 0;
				Common.chartType[tabIndex][i] = new Array();
				Common.yAxis[tabIndex][i] = new Array();
				var des = data[i].description;
				$("<span>&nbsp;</span><img src = 'css/images/add.png'/><span>&nbsp;</span><a href = 'javascript:void(0);' " +
					"onClick = \"Sta.guide(" + tabIndex + "," + i + ");\">" + des + "</a><br/>").appendTo("#option" + tabIndex);
				view_ds = document.createElement("div");
				view_ds.setAttribute("id","view_ds" + tabIndex + "_" + i);
				view_ds.setAttribute("class","view_ds");
				$(view_ds).appendTo("#view" + tabIndex);
				$("<div id = 'special" + tabIndex + "_" + i + "' style = 'clear:both;'></div>").appendTo(view_ds);
				$(view_ds).css("display","none");
			}
		}).error(function(){
			alert("Oops, we got an error...");
			return;
		});
	}else{
		/**********need to do**********/
	}
};

/**********close a tab**********/
Tab.close = function(tabType,tabIndex){
	$("#tabs_li" + tabIndex).remove();
	$("#tab" + tabIndex).remove();
	Common.tabIndex.splice(tabIndex,1);
	$("#tabs").tabs("refresh");
};

Tab.refresh = function(tabIndex){
	$("#option" + tabIndex).empty();
	$("#view" + tabIndex).empty();
	$.getJSON(Common.dataViewUrl().replace("tabType","sta"),function(data){
		var len = data.length;
		google.load("visualization","1",{packages:["corechart"],"callback":drawChart});
		function drawChart(){
			for(var i = 0; i < len; i++){
				var des = data[i].description;
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