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
		for(var i = 0; i < len; i++){
			var name = "";
			if(type == "dv"){
				name = data[i].dataviewName;
			}else{
				name = data[i].datasetName;
			}
			var des = data[i].descriptionZh;
			
			var li = document.createElement("li");
			li.setAttribute("id",type + "-" + subType + "-tabs-li-" + tabIndex + "-" + i);
			li.setAttribute("class","mgt-tabs-li");
			$(li).appendTo("#" + type + "-" + subType + "-tabs-ul-" + tabIndex);
			li.innerHTML = "<a href = '#" + type + "-" + subType + "-tab-" + tabIndex + "-" + i + "'>" + des + "</a>";
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
				Mgt.loadDv(tabIndex,i,name,subType);
			}else{
				$("<span>data set information</span>").appendTo(infoTitle);
				$("<span>column information</span>").appendTo(fieldTitle);
				Mgt.loadDs(tabIndex,i,name,subType);
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

Mgt.loadDv = function(tabIndex,dsIndex,dvName,subType){
	$.getJSON(Common.dvInfoUrl(),{
		dataset: dvName
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
		dataset: dvName
	},function(data){
		var len = data.length;
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

Mgt.loadDs = function(tabIndex,dsIndex,dsName,subType){
	var dataTitle = document.createElement("div");
	dataTitle.setAttribute("id","ds-" + subType + "-mgt-detail-data-title-" + tabIndex + "-" + dsIndex);
	dataTitle.setAttribute("class","mgt-detail-data-title");
	$(dataTitle).appendTo("#ds-" + subType + "-mgt-content-" + tabIndex + "-" + dsIndex);
	$("<a herf = 'javascript:void(0);' onClick = \"Mgt.showTable('" + dsName + "');\" style = 'cursor: pointer;'>" +
		"show detail data of " + dsName + "</a>").appendTo(dataTitle);
	$(dataTitle).css({
		"margin-bottom": "5px",
		"border-bottom": "1px solid #282828"
	});
	
	$.getJSON(Common.dsInfoUrl(),{
		dataset: dsName
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
		dataset: dsName
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

Mgt.showTable = function(dsName){
	var activeTab = $("#tabs").tabs("option","active");
	var tabIndex = Common.tabIndex[activeTab];
	var type = "ds";
	var subAcc = $("#" + type + "-accordion-" + tabIndex).accordion("option","active");
	var subType = "";
	if(subAcc == 0){
		subType = "pub";
	}else if(subAcc == 1){
		subType = "lim";
	}else{
		subType = "own";
	}
	var dsIndex = $("#" + type + "-" + subType + "-tabs-" + tabIndex).tabs("option","active");
	
	$("#" + type + "-" + subType + "-mgt-detail-data-title-" + tabIndex + "-" + dsIndex).empty();
	$("<span>show detail data of " + dsName + "</span>").appendTo("#" + type + "-" + subType + "-mgt-detail-data-title-" + tabIndex + "-" + dsIndex);
	
	$.getJSON(Common.dsFieldUrl(),{
		dataset: dsName
	},function(data){
		var len = data.length;
		var tableData = new google.visualization.DataTable();
		for(var i = 0; i < len; i++){
			tableData.addColumn("string",data[i].fieldName);
		}
		
		$.getJSON(Common.dsDataUrl(),{
			dataset: dsName
		},function(data){
			var l = data.length;
			$("#" + type + "-" + subType + "-mgt-detail-data-title-" + tabIndex + "-" + dsIndex).empty();
			if(l == 0){
				$("<span>this data set is empty</span>").appendTo("#" + type + "-" + subType + "-mgt-detail-data-title-" + tabIndex + "-" + dsIndex);
				Mgt.adjustHeight();
				return;
			}
			$("<a herf = 'javascript:void(0);' onClick = \"Mgt.showTable('" + dsName + "');\">detail data</a>")
				.appendTo("#" + type + "-" + subType + "-mgt-detail-data-title-" + tabIndex + "-" + dsIndex);
			
			var detailData = document.createElement("div");
			detailData.setAttribute("id",type + "-" + subType + "-mgt-detail-data-" + tabIndex + "-" + dsIndex);
			detailData.setAttribute("class","mgt-detail-data");
			$(detailData).appendTo("#" + type + "-" + subType + "-mgt-content-" + tabIndex + "-" + dsIndex);
			
			var arr = "[";
			for(var i = 0; i < l; i++){
				arr += "[";
				for(var j = 0; j < len; j++){
					arr += "\"" + data[i].field[j].value + "\"";
					if(j == len - 1){
						arr += "]";
					}else{
						arr += ",";
					}
				}
				if(i == l - 1){
					arr += "]";
				}else{
					arr += ",";
				}
			}
			
			tableData.addRows($.parseJSON(arr));
			var table = new google.visualization.Table(document.getElementById(type + "-" + subType + "-mgt-detail-data-" + tabIndex + "-" + dsIndex));
			table.draw(tableData,{showRowNumber: true});
			
			var dataWidth = $("#" + type + "-" + subType + "-mgt-detail-data-" + tabIndex + "-" + dsIndex + " .google-visualization-table-table").width();
			$("#" + type + "-" + subType + "-mgt-detail-data-title-" + tabIndex + "-" + dsIndex).css({
				"width": dataWidth,
				"margin-bottom": 0,
				"border-bottom-width": 0
			});
			$("#" + type + "-" + subType + "-mgt-detail-data-" + tabIndex + "-" + dsIndex).css({
				"width": dataWidth
			});
			
			var selectTitle = document.createElement("div");
			selectTitle.setAttribute("id",type + "-" + subType + "-mgt-select-column-title-" + tabIndex + "-" + dsIndex);
			selectTitle.setAttribute("class","mgt-select-column-title");
			$(selectTitle).appendTo("#" + type + "-" + subType + "-mgt-content-" + tabIndex + "-" + dsIndex);
			$("<a herf = 'javascript:void(0);' onClick = \"Mgt.showColumn('" + dsName + "');\" style = 'cursor: pointer;'>" +
				"create data view from " + dsName + "</a>").appendTo(selectTitle);
			
			Mgt.adjustHeight();
		}).error(function(){
			alert("Oops, we got an error...");
			return;
		});
	}).error(function(){
		alert("Oops, we got an error...");
		return;
	});
};

Mgt.showColumn = function(dsName){
	var activeTab = $("#tabs").tabs("option","active");
	var tabIndex = Common.tabIndex[activeTab];
	var type = "ds";
	var subAcc = $("#" + type + "-accordion-" + tabIndex).accordion("option","active");
	var subType = "";
	if(subAcc == 0){
		subType = "pub";
	}else if(subAcc == 1){
		subType = "lim";
	}else{
		subType = "own";
	}
	var dsIndex = $("#" + type + "-" + subType + "-tabs-" + tabIndex).tabs("option","active");
	
	$.getJSON(Common.dsInfoUrl(),{
		dataset: dsName
	},function(data){
		var feature = data.datafeature;
		var sta = 0;
		var geo = 0;
		for(var i = 0; i < feature.length; i++){
			if(feature[i] == "GeoFeature"){
				geo = 1;
			}else if(feature[i] == "DistributionFeature"){
				sta = 1;
			}
		}
		
		if((sta == 0) && (geo == 0)){
			$("#" + type + "-" + subType + "-mgt-select-column-title-" + tabIndex + "-" + dsIndex).empty();
			$("<span>this data set can't create any data view</span>").appendTo(selectTitle);
			return;
		}
		$("#" + type + "-" + subType + "-mgt-select-column-title-" + tabIndex + "-" + dsIndex).remove();
		
		$.getJSON(Common.dsFieldUrl(),{
			dataset: dsName
		},function(data){
			if(sta == 1){
				Mgt.selector("sta",dsName);
				$("<span>create a stat data view</span>").appendTo("#" + type + "-" + subType + "-mgt-sta-selector-title-" + tabIndex + "-" + dsIndex);
				$("<span>choose a key: </span>").appendTo("#" + type + "-" + subType + "-mgt-sta-options-key-" + tabIndex + "-" + dsIndex);
				
				for(var i = 0; i < data.length; i++){
					if(data[i].functionality == "Identifier"){
						$("<input type = 'radio' name = 'key' value = '" + i + "'/><span>" + data[i].fieldName + " </span>")
							.appendTo("#" + type + "-" + subType + "-mgt-sta-options-key-" + tabIndex + "-" + dsIndex);
					}else{
						$("<input type = 'checkbox' value = '" + i + "'/><span>" + data[i].fieldName + " </span>")
							.appendTo("#" + type + "-" + subType + "-mgt-sta-options-value-" + tabIndex + "-" + dsIndex);
					}
				}
			}
			if(geo == 1){
				Mgt.selector("geo",dsName);
				$("<span>create a geo data view</span>").appendTo("#" + type + "-" + subType + "-mgt-geo-selector-title-" + tabIndex + "-" + dsIndex);
				$("<span>keys:&nbsp;&nbsp;&nbsp;&nbsp;</span>")
					.appendTo("#" + type + "-" + subType + "-mgt-geo-options-key-" + tabIndex + "-" + dsIndex);
				
				for(var i = 0; i < data.length; i++){
					if(data[i].functionality == "Latitude"){
						$("<span class = '" + i + "'>" + data[i].fieldName + "&nbsp;&nbsp;&nbsp;&nbsp;</span>")
							.appendTo("#" + type + "-" + subType + "-mgt-geo-options-key-" + tabIndex + "-" + dsIndex);
					}
					if((data[i].functionality != "Latitude") && (data[i].functionality != "Longitude")){
						$("<input type = 'checkbox' value = '" + i + "'/><span>" + data[i].fieldName + " </span>")
							.appendTo("#" + type + "-" + subType + "-mgt-geo-options-value-" + tabIndex + "-" + dsIndex);
					}
				}
				for(var i = 0; i < data.length; i++){
					if(data[i].functionality == "Longitude"){
						$("<span class = '" + i + "'>" + data[i].fieldName + "&nbsp;&nbsp;&nbsp;&nbsp;</span>")
							.appendTo("#" + type + "-" + subType + "-mgt-geo-options-key-" + tabIndex + "-" + dsIndex);
					}
				}
			}
			
			Mgt.adjustHeight();
		}).error(function(){
			alert("Oops, we got an error...");
			return;
		});
	}).error(function(){
		alert("Oops, we got an error...");
		return;
	});
};

Mgt.selector = function(dvType,dsName){
	var activeTab = $("#tabs").tabs("option","active");
	var tabIndex = Common.tabIndex[activeTab];
	var type = "ds";
	var subAcc = $("#" + type + "-accordion-" + tabIndex).accordion("option","active");
	var subType = "";
	if(subAcc == 0){
		subType = "pub";
	}else if(subAcc == 1){
		subType = "lim";
	}else{
		subType = "own";
	}
	var dsIndex = $("#" + type + "-" + subType + "-tabs-" + tabIndex).tabs("option","active");
	
	var selector = document.createElement("div");
	selector.setAttribute("id",type + "-" + subType + "-mgt-" + dvType + "-selector-" + tabIndex + "-" + dsIndex);
	selector.setAttribute("class","mgt-selector");
	$(selector).appendTo("#" + type + "-" + subType + "-mgt-content-" + tabIndex + "-" + dsIndex);
	
	var title = document.createElement("div");
	title.setAttribute("id",type + "-" + subType + "-mgt-" + dvType + "-selector-title-" + tabIndex + "-" + dsIndex);
	title.setAttribute("class","mgt-selector-title");
	$(title).appendTo(selector);
	
	var dvList = document.createElement("div");
	dvList.setAttribute("id",type + "-" + subType + "-mgt-" + dvType + "-data-view-list-" + tabIndex + "-" + dsIndex);
	dvList.setAttribute("class","mgt-dv-list");
	$(dvList).appendTo(selector);
	
	var options = document.createElement("div");
	options.setAttribute("id",type + "-" + subType + "-mgt-" + dvType + "-options-" + tabIndex + "-" + dsIndex);
	options.setAttribute("class","mgt-options");
	$(options).appendTo(selector);
	
	var key = document.createElement("div");
	key.setAttribute("id",type + "-" + subType + "-mgt-" + dvType + "-options-key-" + tabIndex + "-" + dsIndex);
	key.setAttribute("class","mgt-options-key");
	$(key).appendTo(options);
	
	var value = document.createElement("div");
	value.setAttribute("id",type + "-" + subType + "-mgt-" + dvType + "-options-value-" + tabIndex + "-" + dsIndex);
	value.setAttribute("class","mgt-options-value");
	$(value).appendTo(options);
	$("<span>choose some values: </span>").appendTo(value);
	
	var button = document.createElement("div");
	button.setAttribute("id",type + "-" + subType + "-mgt-" + dvType + "-options-button-" + tabIndex + "-" + dsIndex);
	button.setAttribute("class","mgt-options-button");
	$(button).appendTo(options);
	$("<input type = 'button' value = 'show this data view' style = 'font-family: Times New Roman,\"楷体\";font-size: 16px;cursor: pointer;' " +
		"onclick = \"Mgt.dataview('" + dvType + "','" + dsName + "');\"/>").appendTo(button);
	
	var dataview = document.createElement("div");
	dataview.setAttribute("id",type + "-" + subType + "-mgt-" + dvType + "-data-view-" + tabIndex + "-" + dsIndex);
	dataview.setAttribute("class","mgt-data-view");
	$(dataview).appendTo(selector);
	$(dataview).css({
		"display": "none"
	});
	
	Mgt.showDvList(dvType,dsName);
};

Mgt.showDvList = function(dvType,dsName){
	var activeTab = $("#tabs").tabs("option","active");
	var tabIndex = Common.tabIndex[activeTab];
	var type = "ds";
	var subAcc = $("#" + type + "-accordion-" + tabIndex).accordion("option","active");
	var subType = "";
	if(subAcc == 0){
		subType = "pub";
	}else if(subAcc == 1){
		subType = "lim";
	}else{
		subType = "own";
	}
	var dsIndex = $("#" + type + "-" + subType + "-tabs-" + tabIndex).tabs("option","active");
	
	var dvList = $("#" + type + "-" + subType + "-mgt-" + dvType + "-data-view-list-" + tabIndex + "-" + dsIndex);
	dvList.empty();
	
	var accordion = document.createElement("div");
	accordion.setAttribute("id",type + "-" + subType + "-mgt-" + dvType + "-dv-accordion-" + tabIndex + "-" + dsIndex);
	accordion.setAttribute("class","accordion");
	$(accordion).appendTo(dvList);
	
	var head = document.createElement("h3");
	head.setAttribute("id",type + "-" + subType + "-mgt-" + dvType + "-dv-head-" + tabIndex + "-" + dsIndex);
	head.setAttribute("class","head");
	$(head).appendTo(accordion);
	$(head).css({
		"padding-top": 0,
		"padding-right": "10px",
		"padding-bottom": 0,
	});
	$(head).html("data view");
	
	var content = document.createElement("div");
	content.setAttribute("id",type + "-" + subType + "-mgt-" + dvType + "-dv-content-" + tabIndex + "-" + dsIndex);
	content.setAttribute("class","content");
	$(content).appendTo(accordion);
	$(content).css({
		"padding-top": "5px",
		"padding-right": "10px",
		"padding-bottom": "5px",
		"padding-left": "25px",
		"border-right": "1px solid #282828",
		"border-bottom": "1px solid #282828",
		"border-left": "1px solid #282828"
	});
	
	var list = document.createElement("div");
	list.setAttribute("id",type + "-" + subType + "-mgt-" + dvType + "-dv-list-" + tabIndex + "-" + dsIndex);
	list.setAttribute("class","mgt-data-view-list");
	$(list).appendTo(content);
	
	$(accordion).accordion({
		collapsible: true,
		activate: function(event,ui){
			Mgt.adjustSubAcc(dvType);
		}
	});
	
	if(dvType == "sta"){
		dfType = "DistributionFeature";
	}else{
		dfType = "GeoFeature";
	}
	
	$.getJSON(Common.dataviewUrl(),{
		featuretype: dfType,
		dataset: dsName
	}).done(function(data,textStatus,jqXHR){
		var len = data.length;
		var list = $("#" + type + "-" + subType + "-mgt-" + dvType + "-dv-list-" + tabIndex + "-" + dsIndex);
		for(var i = 0; i < len; i++){
			var des = data[i].descriptionZh;
			var dvName = data[i].dataviewName;
			
			var nameCtnr = document.createElement("div");
			nameCtnr.setAttribute("id",type + "-" + subType + "-mgt-" + dvType + "-dv-list-" + tabIndex + "-" + dsIndex + "-" + i);
			nameCtnr.setAttribute("class","mgt-dv-list-name");
			$(nameCtnr).appendTo(list);
			$("<a href = 'javascript:void(0);' onClick = \"Mgt.drawOptions('" + dvType + "','" + dvName + "','" + dsName + "');\">" +
				des + "</a>").appendTo(nameCtnr);
		}
		var listWidth = list.width() + 20;
		var listHeight = 21 * len;
		list.css({
			"width": listWidth,
			"height": listHeight
		});
		Mgt.adjustSubAcc(dvType);
		$(accordion).accordion("option","active",false);
		$(accordion).accordion("option","active",0);
	}).fail(function(jqXHR,textStatus,errorThrown){
		console.log(jqXHR);
		console.log(textStatus);
		console.log(errorThrown);
		alert("Oops, we got an error...");
		return;
	});
};

Mgt.adjustSubAcc = function(dvType){
	var activeTab = $("#tabs").tabs("option","active");
	var tabIndex = Common.tabIndex[activeTab];
	var type = "ds";
	var subAcc = $("#" + type + "-accordion-" + tabIndex).accordion("option","active");
	var subType = "";
	if(subAcc == 0){
		subType = "pub";
	}else if(subAcc == 1){
		subType = "lim";
	}else{
		subType = "own";
	}
	var dsIndex = $("#" + type + "-" + subType + "-tabs-" + tabIndex).tabs("option","active");
	
	var list = $("#" + type + "-" + subType + "-mgt-" + dvType + "-dv-list-" + tabIndex + "-" + dsIndex);
	var listWidth = list.width();
	var listHeight = list.height();
	var cntWidth = listWidth;
	var cntHeight = listHeight;
	if($("#" + type + "-" + subType + "-mgt-" + dvType + "-data-view-" + tabIndex + "-" + dsIndex).css("display") == "none"){
		if(listHeight > 45){
			cntWidth += 15;
			cntHeight = 45;
		}
	}
	$("#" + type + "-" + subType + "-mgt-" + dvType + "-dv-content-" + tabIndex + "-" + dsIndex).css({
		"height": cntHeight,
		"width": cntWidth
	});
};

Mgt.drawOptions = function(dvType,dvName,dsName){
	var activeTab = $("#tabs").tabs("option","active");
	var tabIndex = Common.tabIndex[activeTab];
	var type = "ds";
	var subAcc = $("#" + type + "-accordion-" + tabIndex).accordion("option","active");
	var subType = "";
	if(subAcc == 0){
		subType = "pub";
	}else if(subAcc == 1){
		subType = "lim";
	}else{
		subType = "own";
	}
	var dsIndex = $("#" + type + "-" + subType + "-tabs-" + tabIndex).tabs("option","active");
	
	$.getJSON(Common.dvFieldUrl(),{
		dataset: dvName
	},function(data){
		for(var i = 0; i < data.length; i++){
			if(data[i].functionality == "Identifier"){
				if(dvType == "sta"){
					var radio = $("#" + type + "-" + subType + "-mgt-sta-options-key-" + tabIndex + "-" + dsIndex).find("input");
					var span = $("#" + type + "-" + subType + "-mgt-sta-options-key-" + tabIndex + "-" + dsIndex).find("span");
					var keyName = data[i].fieldName;
					for(var j = 1; j < span.length; j++){
						if((keyName + " ") == span.eq(j).text()){
							radio.eq(j - 1).prop("checked",true);
							break;
						}
					}
				}
			}else{
				var checkbox = $("#" + type + "-" + subType + "-mgt-" + dvType + "-options-value-" + tabIndex + "-" + dsIndex).find("input");
				var span = $("#" + type + "-" + subType + "-mgt-" + dvType + "-options-value-" + tabIndex + "-" + dsIndex).find("span");
				var valueName = data[i].fieldName;
				for(var j = 1; j < span.length; j++){
					if((valueName + " ") == span.eq(j).text()){
						checkbox.eq(j - 1).prop("checked",true);
						break;
					}
				}
			}
		}
		Mgt.dataview(dvType,dsName);
	}).error(function(){
		alert("Oops, we got an error...");
		return;
	});
};

Mgt.dataview = function(dvType,dsName){
	var activeTab = $("#tabs").tabs("option","active");
	var tabIndex = Common.tabIndex[activeTab];
	var type = "ds";
	var subAcc = $("#" + type + "-accordion-" + tabIndex).accordion("option","active");
	var subType = "";
	if(subAcc == 0){
		subType = "pub";
	}else if(subAcc == 1){
		subType = "lim";
	}else{
		subType = "own";
	}
	var dsIndex = $("#" + type + "-" + subType + "-tabs-" + tabIndex).tabs("option","active");
	
	var key = -1;
	var value = -1;
	
	if(dvType == "sta"){
		var radio = $("#" + type + "-" + subType + "-mgt-sta-options-key-" + tabIndex + "-" + dsIndex).find("input");
		for(var i = 0; i < radio.length; i++){
			if(radio.eq(i).is(":checked") == true){
				key = 0;
				break;
			}
		}
		if(key == -1){
			alert("Choose one key!");
			return;
		}
	}
	
	var checkbox = $("#" + type + "-" + subType + "-mgt-" + dvType + "-options-value-" + tabIndex + "-" + dsIndex).find("input");
	for(var i = 0; i < checkbox.length; i++){
		if(checkbox.eq(i).is(":checked") == true){
			value = 0;
		}
	}
	if(value == -1){
		alert("Choose at least one value!");
		return;
	}
	
	var acc = $("#" + type + "-" + subType + "-mgt-" + dvType + "-dv-accordion-" + tabIndex + "-" + dsIndex).accordion("option","active");
	if(typeof(acc) == "number"){
		var list = $("#" + type + "-" + subType + "-mgt-" + dvType + "-dv-list-" + tabIndex + "-" + dsIndex);
		var listWidth = list.width();
		var listHeight = list.height();
		$("#" + type + "-" + subType + "-mgt-" + dvType + "-dv-content-" + tabIndex + "-" + dsIndex).css({
			"height": listHeight,
			"width": listWidth
		});
	}
	
	var dataview = $("#" + type + "-" + subType + "-mgt-" + dvType + "-data-view-" + tabIndex + "-" + dsIndex);
	if(dataview.css("display") == "none"){
		dataview.css("display","block")
	};
	dataview.empty();
	
	var dvContainer = document.createElement("div");
	dvContainer.setAttribute("id",type + "-" + subType + "-mgt-" + dvType + "-data-view-container-" + tabIndex + "-" + dsIndex);
	dvContainer.setAttribute("class","mgt-data-view-container");
	$(dvContainer).appendTo(dataview);
	
	var saveDv = document.createElement("div");
	saveDv.setAttribute("id",type + "-" + subType + "-mgt-" + dvType + "-save-data-view-" + tabIndex + "-" + dsIndex);
	saveDv.setAttribute("class","mgt-save-data-view");
	$(saveDv).appendTo(dataview);
	
	var title = document.createElement("div");
	title.setAttribute("id",type + "-" + subType + "-mgt-" + dvType + "-save-data-view-title-" + tabIndex + "-" + dsIndex);
	title.setAttribute("class","mgt-save-data-view-title");
	$(title).appendTo(saveDv);
	$("<span>save this data view</span><span>").appendTo(title);
	
	var text = document.createElement("div");
	text.setAttribute("id",type + "-" + subType + "-mgt-" + dvType + "-save-data-view-text-" + tabIndex + "-" + dsIndex);
	text.setAttribute("class","mgt-save-data-view-text");
	$(text).appendTo(saveDv);
	$("<span>Name:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><input type = 'text' maxlength = '16' id = 'dvName'/><br/>").appendTo(text);
	$("<span>Description:</span><input type = 'text' id = 'dvDes'/>").appendTo(text);
	
	var button = document.createElement("div");
	button.setAttribute("id",type + "-" + subType + "-mgt-" + dvType + "-save-data-view-button-" + tabIndex + "-" + dsIndex);
	button.setAttribute("class","mgt-save-data-view-button");
	$(button).appendTo(saveDv);
	
	Mgt.adjustHeight();
	
	if(dvType == "sta"){
		Mgt.loadChart(dsName);
	}else{
		Mgt.loadMap(dsName);
	}
};

Mgt.loadChart = function(dsName){
	var activeTab = $("#tabs").tabs("option","active");
	var tabIndex = Common.tabIndex[activeTab];
	var type = "ds";
	var subAcc = $("#" + type + "-accordion-" + tabIndex).accordion("option","active");
	var subType = "";
	if(subAcc == 0){
		subType = "pub";
	}else if(subAcc == 1){
		subType = "lim";
	}else{
		subType = "own";
	}
	var dsIndex = $("#" + type + "-" + subType + "-tabs-" + tabIndex).tabs("option","active");
	
	var key = -1;
	var value = new Array();
	var radio = $("#" + type + "-" + subType + "-mgt-sta-options-key-" + tabIndex + "-" + dsIndex).find("input");
	for(var i = 0; i < radio.length; i++){
		if(radio.eq(i).is(":checked") == true){
			key = radio.eq(i).val();
		}
	}
	var checkbox = $("#" + type + "-" + subType + "-mgt-sta-options-value-" + tabIndex + "-" + dsIndex).find("input");
	for(var i = 0; i < checkbox.length; i++){
		var j = value.length;
		if(checkbox.eq(i).is(":checked") == true){
			value[j] = checkbox.eq(i).val();
		}
	}
	
	$.getJSON(Common.dsFieldUrl(),{
		dataset: dsName
	},function(data){
		var len = data.length;
		var arr = "[";
		var jsonArr = "[";
		arr += "[\"" + data[key].fieldName + "\"";
		jsonArr += "\\\"" + data[key].fieldName + "\\\"";
		for(var i = 0; i < value.length; i++){
			arr += ",\"" + data[value[i]].fieldName + "\"";
			jsonArr += ",\\\"" + data[value[i]].fieldName + "\\\"";
		}
		arr += "],";
		jsonArr += "]";
		
		$("<input type = 'button' value = 'save data view' style = 'font-family: Times New Roman,\"楷体\";font-size: 16px;cursor: pointer;' " +
			"onclick = 'Mgt.saveDataview(\"sta\",\"" + dsName + "\",\"" + jsonArr + "\");'/>")
			.appendTo("#" + type + "-" + subType + "-mgt-sta-save-data-view-button-" + tabIndex + "-" + dsIndex);
		
		$.getJSON(Common.dsDataUrl(),{
			dataset: dsName
		},function(data){
			var l = data.length;
			for(var i = 0; i < l; i++){
				arr += "[\"" + data[i].field[key].value + "\"";
				for(var j = 0; j < value.length; j++){
					arr += "," + data[i].field[value[j]].value;
				}
				arr += "]";
				if(i == l - 1){
					arr += "]";
				}else{
					arr += ",";
				}
			}
			
			google.load("visualization","1",{packages:["corechart"],"callback":drawChart});
			function drawChart(){
				var view = document.getElementById(type + "-" + subType + "-mgt-sta-data-view-container-" + tabIndex + "-" + dsIndex);
				var data = google.visualization.arrayToDataTable($.parseJSON(arr));
				var options = {};
				var chart = new google.visualization.ColumnChart(view);
				chart.draw(data,options);
				Mgt.adjustHeight();
			}
		}).error(function(){
			alert("Oops, we got an error...");
			return;
		});
	}).error(function(){
		alert("Oops, we got an error...");
		return;
	});
};

Mgt.loadMap = function(dsName){
	var activeTab = $("#tabs").tabs("option","active");
	var tabIndex = Common.tabIndex[activeTab];
	var type = "ds";
	var subAcc = $("#" + type + "-accordion-" + tabIndex).accordion("option","active");
	var subType = "";
	if(subAcc == 0){
		subType = "pub";
	}else if(subAcc == 1){
		subType = "lim";
	}else{
		subType = "own";
	}
	var dsIndex = $("#" + type + "-" + subType + "-tabs-" + tabIndex).tabs("option","active");
	
	var key = new Array();
	var value = new Array();
	var span = $("#" + type + "-" + subType + "-mgt-geo-options-key-" + tabIndex + "-" + dsIndex).find("span");
	for(var i = 1; i < span.length; i++){
		key[i - 1] = span.eq(i).attr("class");
	}
	var checkbox = $("#" + type + "-" + subType + "-mgt-geo-options-value-" + tabIndex + "-" + dsIndex).find("input");
	for(var i = 0; i < checkbox.length; i++){
		var j = value.length;
		if(checkbox.eq(i).is(":checked") == true){
			value[j] = checkbox.eq(i).val();
		}
	}
	
	$.getJSON(Common.dsFieldUrl(),{
		dataset: dsName
	},function(fieldData){
		var len = fieldData.length;
		var mapOptions = {
			center: new google.maps.LatLng(39.90960456049752,116.3972282409668),
			zoom: 12,
			scaleControl: true,
			scaleControlOptions: {
				position: google.maps.ControlPosition.BOTTOM_LEFT
			},
			mapTypeId: google.maps.MapTypeId.ROADMAP
		};
		var map = new google.maps.Map(
			document.getElementById(type + "-" + subType + "-mgt-geo-data-view-container-" + tabIndex + "-" + dsIndex),mapOptions);
		
		var jsonArr = "[";
		jsonArr += "\\\"" + fieldData[key[0]].fieldName + "\\\",\\\"" + fieldData[key[1]].fieldName + "\\\"";
		for(var i = 0; i < value.length; i++){
			jsonArr += ",\\\"" + fieldData[value[i]].fieldName + "\\\"";
		}
		jsonArr += "]";
		
		$("<input type = 'button' value = 'save data view' style = 'font-family: Times New Roman,\"楷体\";font-size: 16px;cursor: pointer;' " +
			"onclick = 'Mgt.saveDataview(\"geo\",\"" + dsName + "\",\"" + jsonArr + "\");'/>")
			.appendTo("#" + type + "-" + subType + "-mgt-geo-save-data-view-button-" + tabIndex + "-" + dsIndex);
		
		$.getJSON(Common.dsDataUrl(),{
			dataset: dsName
		},function(data){
			var l = data.length;
			var mrkrArr = new Array();
			for(var i = 0; i < l; i++){
				var arr = "";
				for(var j = 0; j < value.length; j++){
					arr += fieldData[value[j]].fieldName + " :" + data[i].field[value[j]].value + "\n";
				}
				mrkrArr[i] = new google.maps.Marker({
					position : new google.maps.LatLng(data[i].field[key[0]].value,data[i].field[key[1]].value),
					title : arr,
					map: map
				});
			}
			
			Mgt.adjustHeight();
		}).error(function(){
			alert("Oops, we got an error...");
			return;
		});
	}).error(function(){
		alert("Oops, we got an error...");
		return;
	});
};

Mgt.saveDataview = function(dvType,dsName,arr){
	var activeTab = $("#tabs").tabs("option","active");
	var tabIndex = Common.tabIndex[activeTab];
	var type = "ds";
	var subAcc = $("#" + type + "-accordion-" + tabIndex).accordion("option","active");
	var subType = "";
	if(subAcc == 0){
		subType = "pub";
	}else if(subAcc == 1){
		subType = "lim";
	}else{
		subType = "own";
	}
	var dsIndex = $("#" + type + "-" + subType + "-tabs-" + tabIndex).tabs("option","active");
	
	var text = $("#" + type + "-" + subType + "-mgt-" + dvType + "-save-data-view-text-" + tabIndex + "-" + dsIndex).find("input");
	if(text.eq(0).val() == ""){
		alert("Input a name of data view!");
		return;
	}
	if(text.eq(1).val() == ""){
		alert("Input a description of data view!");
		return;
	}
	
	var dvName = text.eq(0).val();
	var dvDes = text.eq(1).val();
	var jsonArr = $.parseJSON(arr);
	var key = "";
	var value = "";
	var dfType = "";
	if(dvType == "sta"){
		key = "[\"" + jsonArr[0] + "\"]";
		var len = jsonArr.length;
		value = "[";
		for(var i = 1; i < len - 1; i++){
			value += "\"" + jsonArr[i] + "\",";
		}
		value += "\"" + jsonArr[len - 1] + "\"]";
		dfType = "DistributionFeature";
	}else{
		key = "[\"" + jsonArr[0] + "\",\"" + jsonArr[1] + "\"]";
		var len = jsonArr.length;
		value = "[";
		for(var i = 2; i < len - 1; i++){
			value += "\"" + jsonArr[i] + "\",";
		}
		value += "\"" + jsonArr[len - 1] + "\"]";
		dfType = "GeoFeature";
	}
	
	$.post(Common.addDvUrl(),{
		dataset: dsName,
		dataview: dvName,
		description: dvDes,
		datafeaturetype: dfType,
		keys: key,
		values: value
	}).done(function(data,textStatus,jqXHR){
		console.log(data);
		console.log(textStatus);
		console.log(jqXHR);
		console.log(data == "");
		if(data == ""){
			$("#dv-" + dvType + "-content-" + tabIndex).empty();
			
			var tabs = document.createElement("div");
			tabs.setAttribute("id","dv-" + dvType + "-tabs-" + tabIndex);
			tabs.setAttribute("class","mgt-tabs");
			$(tabs).appendTo("#dv-" + dvType + "-content-" + tabIndex);
			
			var tabs_ul = document.createElement("ul");
			tabs_ul.setAttribute("id","dv-" + dvType + "-tabs-ul-" + tabIndex);
			tabs_ul.setAttribute("class","mgt-tabs-ul");
			$(tabs_ul).appendTo(tabs);
			
			Mgt.subTab(tabIndex,"dv",dvType);
			Mgt.showDvList(dvType,dsName);
			
			return;
		}
		data = $.parseJSON(data);
		if(data.hasOwnProperty("error")){
			alert(data.error);
			return;
		}
	}).fail(function(jqXHR,textStatus,errorThrown){
		console.log(jqXHR);
		console.log(textStatus);
		console.log(errorThrown);
		alert("Oops, we got an error...");
		return;
	});
};