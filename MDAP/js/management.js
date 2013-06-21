Mgt = {};

Mgt.accordion = function(tabIndex,type){
	var head = document.createElement("h3");
	head.setAttribute("id",type + "Head" + tabIndex);
	head.setAttribute("class","head");
	$(head).appendTo("#accordion" + tabIndex);
	$(head).css({
		"padding-top": 0,
		"padding-right": 0,
		"padding-bottom": 0
	});
	if(type == "dv"){
		$(head).html("data view");
	}else{
		$(head).html("data set");
	}
	var content = document.createElement("div");
	content.setAttribute("id",type + "Content" + tabIndex);
	content.setAttribute("class","content");
	$(content).appendTo("#accordion" + tabIndex);
	$(content).css({
		"padding": 0
	});
	var tabs = document.createElement("div");
	tabs.setAttribute("id",type + "Tabs" + tabIndex);
	tabs.setAttribute("class",type + "Tabs");
	$(tabs).appendTo(content);
	var tabs_ul = document.createElement("ul");
	tabs_ul.setAttribute("id",type + "Tabs_ul" + tabIndex);
	tabs_ul.setAttribute("class",type + "Tabs_ul");
	$(tabs_ul).appendTo(tabs);
};

Mgt.loadSubTab = function(tabIndex,type){
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

Mgt.adjustHeight = function(){
	var activeTab = $("#tabs").tabs("option","active");
	var tabIndex = Common.tabIndex[activeTab];
	var activeAcc = $("#accordion" + tabIndex).accordion("option","active");
	var type = "";
	if(typeof(activeAcc) == "boolean"){
		Tab.adjustHeight(tabIndex);
		return;
	}
	if(activeAcc == 0){
		type = "dv";
	}else{
		type = "ds";
	}
	var subIndex = $("#" + type + "Tabs" + tabIndex).tabs("option","active");
	var tabHeight = $("#" + type + "Tab" + tabIndex + "_" + subIndex).height();
	var listHeight = $("#" + type + "Tabs_ul" + tabIndex).height();
	if(tabHeight >= listHeight){
		$("#" + type + "Content" + tabIndex).css({
			"height": tabHeight + 20
		});
	}else{
		$("#" + type + "Content" + tabIndex).css({
			"height": listHeight + 20
		});
	}
	Tab.adjustHeight(tabIndex);
};

Mgt.load = function(tabIndex,dsIndex,name,type){
	var infoUrl = "";
	var fieldUrl = "";
	if(type == "dv"){
		infoUrl = Common.dvInfoUrl();
		fieldUrl = Common.dvFieldUrl();
	}else{
		infoUrl = Common.dsInfoUrl();
		fieldUrl = Common.dsFieldUrl();
	}
	
	var infoTable = document.createElement("div");
	infoTable.setAttribute("id",type + "InfoTable" + tabIndex + "_" + dsIndex);
	infoTable.setAttribute("class",type + "InfoTable");
	$(infoTable).appendTo("#" + type + "DataMgt" + tabIndex + "_" + dsIndex);
	
	$.getJSON(infoUrl,{
		dataset: name
	},function(data){
		var len = data.length;
		var tableData = new google.visualization.DataTable();
		tableData.addColumn('string','datasetName');
		tableData.addColumn('string','description');
		var arr = "[[\"" + data.datasetName + "\",\"" + data.descriptionZh + "\"]]";
		tableData.addRows($.parseJSON(arr));
		
		var table = new google.visualization.Table(document.getElementById(type + "InfoTable" + tabIndex + "_" + dsIndex));
		table.draw(tableData,{showRowNumber: false});
	}).error(function(){
		alert("Oops, we got an error...");
		return;
	});
	
	var fieldTable = document.createElement("div");
	fieldTable.setAttribute("id",type + "FieldTable" + tabIndex + "_" + dsIndex);
	fieldTable.setAttribute("class",type + "FieldTable");
	$(fieldTable).appendTo("#" + type + "DataMgt" + tabIndex + "_" + dsIndex);
	
	$.getJSON(fieldUrl,{
		dataset: name
	},function(data){
		var len = data.length;
		var tableData = new google.visualization.DataTable();
		tableData.addColumn('string','description');
		tableData.addColumn('string','fieldName');
		tableData.addColumn('boolean','isKey');
		tableData.addColumn('string','type');
		var arr = "[";
		for(var i = 0; i < len; i++){
			arr += "[\"" + data[i].description + "\",\"" + data[i].fieldName + "\"," + data[i].isKey + ",\"" + data[i].type + "\"]";
			if(i == len - 1){
				arr += "]";
			}else{
				arr += ",";
			}
		}
		tableData.addRows($.parseJSON(arr));
		
		var table = new google.visualization.Table(document.getElementById(type + "FieldTable" + tabIndex + "_" + dsIndex));
		table.draw(tableData,{showRowNumber: true});
	}).error(function(){
		alert("Oops, we got an error...");
		return;
	});
};