Mgt = {};

Mgt.accordion = function(tabIndex,type){
	var head = document.createElement("h3");
	head.setAttribute("id",type + "-head-" + tabIndex);
	head.setAttribute("class","head");
	$(head).appendTo("#accordion" + tabIndex);
	$(head).css({
		"padding-top": 0,
		"padding-right": 0,
		"padding-bottom": 0
	});
	
	var content = document.createElement("div");
	content.setAttribute("id",type + "-content-" + tabIndex);
	content.setAttribute("class","content");
	$(content).appendTo("#accordion" + tabIndex);
	$(content).css({
		"padding-top": 0,
		"padding-right": 0,
		"padding-bottom": 0,
		"padding-left": "10px"
	});
	
	var accordion = document.createElement("div");
	accordion.setAttribute("id",type + "-accordion-" + tabIndex);
	accordion.setAttribute("class","accordion");
	$(accordion).appendTo(content);
	
	if(type == "dv"){
		$(head).html("data view");
		Mgt.subAcc(tabIndex,type,"sta");
		Mgt.subAcc(tabIndex,type,"geo");
	}else{
		$(head).html("data set");
		Mgt.subAcc(tabIndex,type,"pub");
		Mgt.subAcc(tabIndex,type,"lim");
		Mgt.subAcc(tabIndex,type,"own");
	}
	
	$(accordion).accordion({
		collapsible: true,
		activate: function(event,ui){
			Mgt.adjustHeight();
		}
	});
	$(accordion).accordion("option","active",false);
};

Mgt.subAcc = function(tabIndex,type,subType){
	var head = document.createElement("h3");
	head.setAttribute("id",type + "-" + subType + "-head-" + tabIndex);
	head.setAttribute("class","head");
	$(head).appendTo("#" + type + "-accordion-" + tabIndex);
	$(head).css({
		"padding-top": 0,
		"padding-right": 0,
		"padding-bottom": 0
	});
	
	if(type == "dv"){
		if(subType == "sta"){
			$(head).html("statistics data");
		}else{
			$(head).html("geography data");
		}
	}else{
		if(subType == "pub"){
			$(head).html("public data");
		}else if(subType == "lim"){
			$(head).html("limit data");
		}else{
			$(head).html("own data");
		}
	}
	
	var content = document.createElement("div");
	content.setAttribute("id",type + "-" + subType + "-content-" + tabIndex);
	content.setAttribute("class","content");
	$(content).appendTo("#" + type + "-accordion-" + tabIndex);
	$(content).css({
		"padding": 0
	});
	
	var tabs = document.createElement("div");
	tabs.setAttribute("id",type + "-" + subType + "-tabs-" + tabIndex);
	tabs.setAttribute("class","mgt-tabs");
	$(tabs).appendTo(content);
	
	var tabs_ul = document.createElement("ul");
	tabs_ul.setAttribute("id",type + "-" + subType + "-tabs-ul-" + tabIndex);
	tabs_ul.setAttribute("class","mgt-tabs-ul");
	$(tabs_ul).appendTo(tabs);
};

Mgt.subTab = function(tabIndex,type,subType){
	var url = "";
	var msg = "{}";
	var username = Common.username;
	
	if(type == "dv"){
		url = Common.dataviewUrl();
		if(subType == "sta"){
			msg = "{\"featuretype\":\"DistributionFeature\"}";
		}else{
			msg = "{\"featuretype\":\"GeoFeature\"}";
		}
	}else{
		if(subType == "pub"){
			url = Common.pubDsUrl();
		}else if(subType == "lim"){
			url = Common.limDsUrl();
			msg = "{\"userid\":\"" + username + "\"}";
		}else{
			url = Common.ownDsUrl();
			msg = "{\"userid\":\"" + username + "\"}";
		}
	}
	
	$.getJSON(url,$.parseJSON(msg),function(data){
		var len = data.length;
		if(len == 0){
			return;
		}
		for(var i = 0; i < len; i++){
			var id = data[i].id;
			var name = "";
			if(type == "dv"){
				name = data[i].dataviewName;
			}else{
				name = data[i].datasetName;
			}
			
			var li = document.createElement("li");
			li.setAttribute("id",type + "-" + subType + "-tabs-li-" + tabIndex + "-" + i);
			li.setAttribute("class","mgt-tabs-li");
			$(li).appendTo("#" + type + "-" + subType + "-tabs-ul-" + tabIndex);
			li.innerHTML = "<a href = '#" + type + "-" + subType + "-tab-" + tabIndex + "-" + i + "'>" + name + "</a>";
			$("#" + type + "-" + subType + "-tabs-li-" + tabIndex + "-" + i + " a").css({
				"padding-top": "4px",
				"padding-bottom": "4px"
			});
			
			var subTab = document.createElement("div");
			subTab.setAttribute("id",type + "-" + subType + "-tab-" + tabIndex + "-" + i);
			subTab.setAttribute("class","mgt-tab");
			$(subTab).appendTo("#" + type + "-" + subType + "-tabs-" + tabIndex);
			$(subTab).css({
				"padding": 0
			});
			
			var content = document.createElement("div");
			content.setAttribute("id",type + "-" + subType + "-mgt-content-" + tabIndex + "-" + i);
			content.setAttribute("class","mgt-content");
			$(content).appendTo(subTab);
			
			var infoTitle = document.createElement("div");
			infoTitle.setAttribute("id",type + "-" + subType + "-info-title-" + tabIndex + "-" + i);
			infoTitle.setAttribute("class","mgt-title");
			$(infoTitle).appendTo(content);
			
			var infoTable = document.createElement("div");
			infoTable.setAttribute("id",type + "-" + subType + "-info-" + tabIndex + "-" + i);
			infoTable.setAttribute("class","mgt-info");
			$(infoTable).appendTo(content);
			
			var fieldTitle = document.createElement("div");
			fieldTitle.setAttribute("id",type + "-" + subType + "-field-title-" + tabIndex + "-" + i);
			fieldTitle.setAttribute("class","mgt-title");
			$(fieldTitle).appendTo(content);
			
			var fieldTable = document.createElement("div");
			fieldTable.setAttribute("id",type + "-" + subType + "-field-" + tabIndex + "-" + i);
			fieldTable.setAttribute("class","mgt-field");
			$(fieldTable).appendTo(content);
			
			if(type == "dv"){
				$("<span>data view information</span>").appendTo(infoTitle);
				$("<span>column information</span>").appendTo(fieldTitle);
				Mgt.loadDv(tabIndex,i,id,subType);
			}else{
				$("<span>data set information</span>").appendTo(infoTitle);
				$("<span>column information</span>").appendTo(fieldTitle);
				Mgt.loadDs(tabIndex,i,id,name,subType);
			}
		}
		
		$("#" + type + "-" + subType + "-tabs-" + tabIndex).tabs({
			activate: function(event,ui){
				Mgt.adjustHeight();
			}
		}).addClass("ui-tabs-vertical ui-helper-clearfix");
		$("#" + type + "-" + subType + "-tabs-" + tabIndex + " li").removeClass("ui-corner-top").addClass("ui-corner-left");
		if(len != 0){
			$("#" + type + "-" + subType + "-tabs-" + tabIndex + " .ui-widget-header").css({
				"border-right": "1px solid #000000"
			});
		}
	}).error(function(){
		alert("Oops, we got an error...");
		return;
	});
};

Mgt.loadDv = function(tabIndex,dsIndex,id,subType){
	$.getJSON(Common.dvInfoUrl(),{
		id: id
	},function(data){
		var tableData = new google.visualization.DataTable();
		tableData.addColumn('string','Data Feature');
		tableData.addColumn('string','Name');
		tableData.addColumn('string','Description');
		tableData.addColumn('string','Identifier');
		tableData.addColumn('string','Value');
		
		var arr = "[[\"" + data.datafeature + "\",\"" + data.dataviewName + "\",\"" + data.descriptionZh + "\",\"";
		var temp = data.identifiers;
		var i = 0;
		for(; i < temp.length - 1; i++){
			arr += temp[i] + ",";
		}
		arr += temp[i] + "\",\"";
		var temp = data.values;
		i = 0;
		for(; i < temp.length - 1; i++){
			arr += temp[i] + ",";
		}
		arr += temp[i] + "\"]]";
		
		tableData.addRows($.parseJSON(arr));
		var table = new google.visualization.Table(document.getElementById("dv-" + subType + "-info-" + tabIndex + "-" + dsIndex));
		table.draw(tableData,{showRowNumber: false});
	}).error(function(){
		alert("Oops, we got an error...");
		return;
	});
	
	$.getJSON(Common.dvFieldUrl(),{
		id: id
	},function(data){
		var len = data.length;
		if(len == 0){
			return;
		}
		var tableData = new google.visualization.DataTable();
		tableData.addColumn('string','Data Set Name');
		tableData.addColumn('string','Data Set Owner');
		tableData.addColumn('string','Description');
		tableData.addColumn('string','Field Name');
		tableData.addColumn('string','Functionality');
		tableData.addColumn('boolean','Key');
		tableData.addColumn('string','Type');
		
		var arr = "[";
		for(var i = 0; i < len; i++){
			arr += "[\"" + data[i].datasetName + "\",\"" + data[i].datasetOwner + "\",\"" + data[i].description + "\",\"" +
				data[i].fieldName + "\",\"" + data[i].functionality + "\"," + data[i].isKey + ",\"" + data[i].type + "\"]";
			if(i == len - 1){
				arr += "]";
			}else{
				arr += ",";
			}
		}
		
		tableData.addRows($.parseJSON(arr));
		var table = new google.visualization.Table(document.getElementById("dv-" + subType + "-field-" + tabIndex + "-" + dsIndex));
		table.draw(tableData,{showRowNumber: true});
	}).error(function(){
		alert("Oops, we got an error...");
		return;
	});
};

Mgt.loadDs = function(tabIndex,dsIndex,id,dsName,subType){
	var dataTitle = document.createElement("div");
	dataTitle.setAttribute("id","ds-" + subType + "-mgt-detail-data-title-" + tabIndex + "-" + dsIndex);
	dataTitle.setAttribute("class","mgt-detail-data-title");
	$(dataTitle).appendTo("#ds-" + subType + "-mgt-content-" + tabIndex + "-" + dsIndex);
	$("<a herf = 'javascript:void(0);' " +
		"onClick = \"Mgt.showTable(" + tabIndex + ",'" + subType + "'," + dsIndex + ",'" + id + "','" + dsName + "');\" style = 'cursor: pointer;'>" +
		"show detail data of " + dsName + "</a>").appendTo(dataTitle);
	$(dataTitle).css({
		"margin-bottom": "5px",
		"border-bottom": "1px solid #282828"
	});
	
	$.getJSON(Common.dsInfoUrl(),{
		id: id
	},function(data){
		var tableData = new google.visualization.DataTable();
		tableData.addColumn('string','Data Feature');
		tableData.addColumn('string','Name');
		tableData.addColumn('string','Description');
		tableData.addColumn('string','Key');
		tableData.addColumn('string','Other Field');
		tableData.addColumn('string','Owner');
		tableData.addColumn('string','Permission');
		
		var arr = "[[\"";
		var temp = data.datafeature;
		var i = 0;
		for(; i < temp.length - 1; i++){
			arr += temp[i] + ",";
		}
		arr += temp[i] + "\",";
		arr += "\"" + data.datasetName + "\",\"" + data.descriptionZh + "\",\"";
		var temp = data.keyFields;
		i = 0;
		for(; i < temp.length - 1; i++){
			arr += temp[i] + ",";
		}
		arr += temp[i] + "\",\"";
		var temp = data.otherFields;
		i = 0;
		for(; i < temp.length - 1; i++){
			arr += temp[i] + ",";
		}
		arr += temp[i] + "\",\"" + data.owner + "\",\"" + data.permission + "\"]]";
		
		tableData.addRows($.parseJSON(arr));
		var table = new google.visualization.Table(document.getElementById("ds-" + subType + "-info-" + tabIndex + "-" + dsIndex));
		table.draw(tableData,{showRowNumber: false});
	}).error(function(){
		alert("Oops, we got an error...");
		return;
	});
	
	$.getJSON(Common.dsFieldUrl(),{
		id: id
	},function(data){
		var len = data.length;
		var tableData = new google.visualization.DataTable();
		tableData.addColumn('string','Description');
		tableData.addColumn('string','Field Name');
		tableData.addColumn('string','Functionality');
		tableData.addColumn('boolean','Key');
		tableData.addColumn('string','Type');
		
		var arr = "[";
		for(var i = 0; i < len; i++){
			arr += "[\"" + data[i].description + "\",\"" + data[i].fieldName + "\",\"" +
				data[i].functionality + "\"," + data[i].isKey + ",\"" + data[i].type + "\"]";
			if(i == len - 1){
				arr += "]";
			}else{
				arr += ",";
			}
		}
		
		tableData.addRows($.parseJSON(arr));
		var table = new google.visualization.Table(document.getElementById("ds-" + subType + "-field-" + tabIndex + "-" + dsIndex));
		table.draw(tableData,{showRowNumber: true});
	}).error(function(){
		alert("Oops, we got an error...");
		return;
	});
};

Mgt.adjustHeight = function(){
	console.log("Manage Tab Adjust Height Information:");
	var activeTab = $("#tabs").tabs("option","active");
	var tabIndex = Common.tabIndex[activeTab];
	console.log("tabIndex:" + tabIndex);
	
	var activeAcc = $("#accordion" + tabIndex).accordion("option","active");
	console.log("activeAcc:" + activeAcc);
	var type = "";
	if(typeof(activeAcc) == "boolean"){
		Tab.adjustHeight(tabIndex);
		return;
	}else{
		if(activeAcc == 0){
			type = "dv";
		}else{
			type = "ds";
		}
	}
	
	var subAcc = $("#" + type + "-accordion-" + tabIndex).accordion("option","active");
	console.log("subAcc:" + subAcc);
	var subType = "";
	if(typeof(subAcc) == "boolean"){
		$("#" + type + "-content-" + tabIndex).css({
			"height": $("#" + type + "-accordion-" + tabIndex).height() + 10
		});
		Tab.adjustHeight(tabIndex);
		return;
	}else{
		if(activeAcc == 0){
			if(subAcc == 0){
				subType = "sta";
			}else{
				subType = "geo";
			}
		}else{
			if(subAcc == 0){
				subType = "pub";
			}else if(subAcc == 1){
				subType = "lim";
			}else{
				subType = "own";
			}
		}
	}
	
	var dsIndex = $("#" + type + "-" + subType + "-tabs-" + tabIndex).tabs("option","active");
	console.log("dsIndex:" + dsIndex);
	var height = 0;
	if(typeof(dsIndex) == "number"){
		var infoWidth = $("#" + type + "-" + subType + "-info-" + tabIndex + "-" + dsIndex + " .google-visualization-table-table").width();
		var fieldWidth = $("#" + type + "-" + subType + "-field-" + tabIndex + "-" + dsIndex + " .google-visualization-table-table").width();
		$("#" + type + "-" + subType + "-info-title-" + tabIndex + "-" + dsIndex).css({
			"width": infoWidth
		});
		$("#" + type + "-" + subType + "-info-" + tabIndex + "-" + dsIndex).css({
			"width": infoWidth
		});
		$("#" + type + "-" + subType + "-field-title-" + tabIndex + "-" + dsIndex).css({
			"width": fieldWidth
		});
		$("#" + type + "-" + subType + "-field-" + tabIndex + "-" + dsIndex).css({
			"width": fieldWidth
		});
		var width = Common.width() - $("#" + type + "-" + subType + "-tabs-ul-" + tabIndex).width();
		$("#" + type + "-" + subType + "-mgt-content-" + tabIndex + "-" + dsIndex).css({
			"width": width - 150
		});
		var tabHeight = $("#" + type + "-" + subType + "-tab-" + tabIndex + "-" + dsIndex).height();
		var ulHeight = $("#" + type + "-" + subType + "-tabs-ul-" + tabIndex).height();
		if(tabHeight >= ulHeight){
			height = tabHeight;
		}else{
			height = ulHeight;
		}
	}
	$("#" + type + "-" + subType + "-content-" + tabIndex).css({
		"height": height + 20
	});
	$("#" + type + "-content-" + tabIndex).css({
		"height": $("#" + type + "-accordion-" + tabIndex).height() + 10
	});
	Tab.adjustHeight(tabIndex);
};