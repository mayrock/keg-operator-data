Tab = {};

Tab.createFrame = function(tabType){
	if(!((tabType == "geo") || (tabType == "sta"))){
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
	li.innerHTML = "<a href='#tab" + tabIndex + "'>" + tabType + " data</a>";
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
	/**********initialize a tab**********/
	setTab();
	$("#tabs").tabs("refresh");
	$("#tabs").tabs("option","active",tabCount);
	
	function setTab(){
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
				$("<input type = 'button' value = 'selectAll' onclick = \"Geo.selectAll(" + tabIndex + "," + len + ")\"/>").appendTo(select);
				$("<input type = 'button' value = 'invertAll' onclick = \"Geo.invertAll(" + tabIndex + "," + len + ")\"/>").appendTo(select);
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
					var name = data[i].datasetName;
					if((name == "RegionInfo2") || (name == "RegionInfo3") || (name == "WebsiteId_URL")){
						continue;
					}
					$("<a href = 'javascript:void(0);' onClick = \"Sta.createChart(" + tabIndex + "," + i + ");\">" + des + "<img src = 'css/images/add.png'/></a><br/>").appendTo("#option" + tabIndex);
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
	}
}