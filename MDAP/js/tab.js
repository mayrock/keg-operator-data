Tab = {};

Tab.createFrame = function(tabType){
	if(!((tabType == "geo") || (tabType == "sta") || (tabType == "fav"))){
		alert("Oops, we got an error...");
		return;
	}
	$("#extraMenu").css("display","none");
	var tabCount = Common.tabCount;
	var tabIndex = Common.tabIndex;
	/**********limit the number of tab**********/
	if(tabCount == Common.tabLimit()){
		alert("Tabs cann't be more than " + Common.tabLimit() + "!");
		return;
	}
	Common.tabCount ++;
	Common.tabIndex ++;
	/**********add a tab**********/
	li = document.createElement("li");
	li.setAttribute("id","tabs_li" + tabIndex);
	$(li).appendTo("#tabs_ul");
	if(tabType == "geo"){
		li.innerHTML = "<a href='#tab" + tabIndex + "'>geo data</a>";
	}else if(tabType == "sta"){
		li.innerHTML = "<a href='#tab" + tabIndex + "'>stat data</a>";
	}else if(tabType == "fav"){
		li.innerHTML = "<a href='#tab" + tabIndex + "'>my favorite</a>";
	}else{
		/**********need to do**********/
	}
	tab = document.createElement("div");
	tab.setAttribute("id","tab" + tabIndex);
	tab.setAttribute("class","tab");
	$(tab).appendTo("#tabs");
	if((tabType == "geo") || (tabType == "sta")){
		option = document.createElement("div");
		option.setAttribute("id","option" + tabIndex);
		option.setAttribute("class","option");
		$(option).appendTo(tab);
		view = document.createElement("div");
		view.setAttribute("id","view" + tabIndex);
		view.setAttribute("class","view");
		$(view).appendTo(tab);
	}else if(tabType == "fav"){
		favorite = document.createElement("div");
		favorite.setAttribute("id","favorite" + tabIndex);
		favorite.setAttribute("class","favorite");
		$(favorite).appendTo(tab);
		$("<img src = 'css/images/refresh.png'/>")
			.appendTo(li)
			.click(
				/**********refresh tab**********/
				function(){
					$("#favorite" + tabIndex).empty();
					Tab.load(tabType,tabIndex);
				}
			);
	}else{
		/**********need to do**********/
	}
	if(tabType == "sta"){
		$("<img src = 'css/images/save.png' onclick = \"Fav.createFrame(" + tabIndex + ");\"/>").appendTo(li);
	}
	$("<img src = 'css/images/close.png'/>")
		.appendTo(li)
		.hover(
			function(){
				$(this).attr("src","css/images/close_hover.png");
			},function(){
				$(this).attr("src","css/images/close.png");
			}
		).click(
			function(){
				Tab.close(tabType,tabIndex);
			}
		);
	Tab.load(tabType,tabIndex);
	$("#tabs").tabs("refresh");
	$("#tabs").tabs("option","active",tabCount);
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
		$.getJSON(Common.datasetUrl().replace("tabType",tabType),function(data){
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
		$.getJSON(Common.datasetUrl().replace("tabType",tabType),function(data){
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
	}else if(tabType == "fav"){
		table = document.createElement("table");
		$(table).appendTo("#favorite" + tabIndex);
		$(table).attr({
			"border": 1,
			"cellpadding": 10
		});
		$("<thead><tr><th width = '50'><span>index</span></th><th width = '100'><span>name</span></th><th width = '50'><span>options</span></th></tr></thead>").appendTo(table);
		var favData = JSON.parse($.cookie("favData"));
		var len = favData.length;
		for(var i = 0; i < len; i++){
			var tabData = favData[i];
			var name = tabData.name;
			var datasetData = tabData.dataset;
			tr = document.createElement("tr");
			$(tr).appendTo(table);
			td = document.createElement("td");
			$("<span>" + (i + 1) + "</span>").appendTo(td);
			$(td).appendTo(tr);
			td = document.createElement("td");
			$("<a herf = 'javascript:void(0);' onClick = \"Fav.revertSta(" + i + ");\">" + name + "</a>").appendTo(td);
			$(td).appendTo(tr);
			td = document.createElement("td");
			$("<a herf = 'javascript:void(0);' onClick = \"Fav.delSta(" + i + ");\">delete</a>").appendTo(td);
			$(td).appendTo(tr);
		}
	}else{
		/**********need to do**********/
	}
};

/**********close a tab**********/
Tab.close = function(tabType,tabIndex){
	$("#tabs_li" + tabIndex).remove();
	$("#tab" + tabIndex).remove();
	Common.tabCount --;
	$("#tabs").tabs("refresh");
};