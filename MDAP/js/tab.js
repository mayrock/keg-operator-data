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
	$(li).appendTo("#tabs_ul");
	if((tabType == "geo") || (tabType == "sta")){
		li.innerHTML = "<a href='#tab" + tabIndex + "'>" + tabType + " data</a>";
	}else if(tabType == "fav"){
		li.innerHTML = "<a href='#tab" + tabIndex + "'>my " + tabType + "orite</a>";
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
	$("<img src = 'css/images/close.png'/>")
		.appendTo(li)
		.hover(
			function(){
				$(this).attr("src","css/images/close_hover.png");
			},function(){
				$(this).attr("src","css/images/close.png");
			}
		).click(
			/**********close a tab**********/
			function(){
				$(this).parent().remove();
				tabId = $(this).parent().children("a").attr("href");
				$(tabId).remove();
				Common.tabCount --;
				$("#tabs").tabs("refresh");
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
		$.ajaxSettings.async = false;
		$.getJSON(Common.datasetUrl() + tabType + "dss",function(data){
			var len = data.length;
			for(var i = 0; i < len; i++){
				var des = data[i].description;
				var name = data[i].datasetName;
				var schema = data[i].schema;
				var type = "points";
				if(schema[0] == "Region"){
					type = "regions";
				}
				Geo.loadData(tabIndex,i,name,type);
				$("<input type = 'checkbox' id = 'checkbox" + tabIndex + "_" + i + "' onclick = \"Geo.clickEvent(" + tabIndex + "," + i + ",'" + type + "');\"/>" + des + "<br/>").appendTo(checkbox);
			}
			$("<input type = 'button' style = 'font-family: Times New Roman;font-size: 16px' value = 'selectAll' onclick = \"Geo.selectAll(" + tabIndex + "," + len + ")\"/>").appendTo(select);
			$("<span>&nbsp;&nbsp;</span><input type = 'button' style = 'font-family: Times New Roman;font-size: 16px;' value = 'invertAll' onclick = \"Geo.invertAll(" + tabIndex + "," + len + ")\"/>").appendTo(select);
		}).error(function(){
			alert("Oops, we got an error...");
			return;
		});
	}else if(tabType == "sta"){
		Common.chartCount[tabIndex] = new Array();
		Common.chartIndex[tabIndex] = new Array();
		Common.staControl[tabIndex] = new Array();
		$.ajaxSettings.async = false;
		$.getJSON(Common.datasetUrl() + tabType + "dss",function(data){
			var len = data.length;
			for(var i = 0; i < len; i++){
				Common.chartCount[tabIndex][i] = 0;
				Common.chartIndex[tabIndex][i] = 0;
				Common.staControl[tabIndex][i] = new Array();
				var des = data[i].description;
				var schema = data[i].schema;
				/**********y axis information of the first chart should be initialize before chart container is created,
				so we can easily reset it when revert from my favorite**********/
				var chartIndex = Common.chartIndex[tabIndex][i];
				var len = schema.length;
				Common.staControl[tabIndex][i][chartIndex] = new Array();
				for(var j = 0; j < len - 1; j++){
					Common.staControl[tabIndex][i][chartIndex][j] = true;
				}
				var name = data[i].datasetName;
				if((name == "RegionInfo2") || (name == "RegionInfo3") || (name == "WebsiteId_URL")){
					continue;
				}
				var chartType = "lineChart";
				$("<span>&nbsp;</span><img src = 'css/images/add.png'/><span>&nbsp;</span><a href = 'javascript:void(0);' onClick = \"Sta.createChart(" + tabIndex + "," + i + ",'" + chartType + "');\">" + des + "</a><br/>").appendTo("#option" + tabIndex);
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
		if($.cookie("fav_count") != null){
			table = document.createElement("table");
			$(table).appendTo("#favorite" + tabIndex);
			$(table).attr({
				"border": 1,
				"cellpadding": 10
			});
			$("<thead><tr><th width = '50'><span>index</span></th><th width = '100'><span>name</span></th><th width = '450'><span>description</span></th><th width = '50'><span>options</span></th></tr></thead>").appendTo(table);
			var count = $.cookie("fav_count");
			for(var i = 0; i < count; i++){
				var name = $.cookie("fav_name" + i);
				var des = $.cookie("fav_des" + i);
				tr = document.createElement("tr");
				$(tr).appendTo(table);
				td = document.createElement("td");
				$("<span>" + (i + 1) + "</span>").appendTo(td);
				$(td).appendTo(tr);
				td = document.createElement("td");
				$("<a herf = 'javascript:void(0);' onClick = \"Fav.revert(" + i + ");\">" + name + "</a>").appendTo(td);
				$(td).appendTo(tr);
				td = document.createElement("td");
				$("<span>" + des + "</span>").appendTo(td);
				$(td).appendTo(tr);
				td = document.createElement("td");
				$("<a herf = 'javascript:void(0);' onClick = \"Fav.del(" + tabIndex + "," + i + ");\">delete</a>").appendTo(td);
				$(td).appendTo(tr);
			}
		}
	}else{
		/**********need to do**********/
	}
};