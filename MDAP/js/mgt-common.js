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
		Mgt.subAcc(tabIndex,type,"other");
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
		}else if(subType == "geo"){
			$(head).html("geography data");
		}else{
			$(head).html("other data");
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
		}else if(subType == "geo"){
			msg = "{\"featuretype\":\"GeoFeature\"}";
		}else{
			msg = "{\"featuretype\":\"ValueFeature\"}";
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
				Mgt.loadDv(tabIndex,i,id,name,subType);
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

Mgt.loadDv = function(tabIndex,dsIndex,dvID,dvName,subType){
	var dataTitle = document.createElement("div");
	dataTitle.setAttribute("id","dv-" + subType + "-mgt-detail-data-title-" + tabIndex + "-" + dsIndex);
	dataTitle.setAttribute("class","mgt-detail-data-title");
	$(dataTitle).appendTo("#dv-" + subType + "-mgt-content-" + tabIndex + "-" + dsIndex);
	$("<a herf = 'javascript:void(0);' " +
		"onClick = \"Mgt.showDvTable(" + tabIndex + ",'" + subType + "'," + dsIndex + ",'" + dvID + "','" + dvName + "');\" " +
		"style = 'cursor: pointer;'>show detail data of " + dvName + "</a>").appendTo(dataTitle);
	$(dataTitle).css({
		"margin-bottom": "5px",
		"border-bottom": "1px solid #282828"
	});
	
	$.getJSON(Common.dvInfoUrl(),{
		id: dvID
	},function(data){
		var tableData = new google.visualization.DataTable();
		tableData.addColumn('string','Data Feature');
		tableData.addColumn('string','Name');
		tableData.addColumn('string','Description');
		tableData.addColumn('string','Identifier');
		tableData.addColumn('string','Value');
		
		var arr = $.parseJSON("[]");
		arr[0] = $.parseJSON("[]");
		arr[0][0] = data.datafeature;
		arr[0][1] = data.dataviewName;
		arr[0][2] = data.descriptionZh;
		var identifiers = "";
		if(data.hasOwnProperty("identifiers")){
			var i = 0;
			for(; i < data.identifiers.length - 1; i++){
				identifiers += data.identifiers[i] + ",";
			}
			identifiers += data.identifiers[i];
		}
		arr[0][3] = identifiers;
		var values = "";
		if(data.hasOwnProperty("values")){
			var i = 0;
			for(; i < data.values.length - 1; i++){
				values += data.values[i] + ",";
			}
			values += data.values[i];
		}
		arr[0][4] = values;
		
		tableData.addRows(arr);
		var table = new google.visualization.Table(document.getElementById("dv-" + subType + "-info-" + tabIndex + "-" + dsIndex));
		table.draw(tableData,{showRowNumber: false});
	}).error(function(){
		alert("Oops, we got an error...");
		return;
	});
	
	$.getJSON(Common.dvFieldUrl(),{
		id: dvID
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
			var des = "";
			if(data[i].hasOwnProperty("description")){
				des = data[i].description;
			}
			arr += "[\"" + data[i].datasetName + "\",\"" + data[i].datasetOwner + "\",\"" + des + "\",\"" +
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

Mgt.loadDs = function(tabIndex,dsIndex,dsID,dsName,subType){
	var dataTitle = document.createElement("div");
	dataTitle.setAttribute("id","ds-" + subType + "-mgt-detail-data-title-" + tabIndex + "-" + dsIndex);
	dataTitle.setAttribute("class","mgt-detail-data-title");
	$(dataTitle).appendTo("#ds-" + subType + "-mgt-content-" + tabIndex + "-" + dsIndex);
	$("<a herf = 'javascript:void(0);' " +
		"onClick = \"Mgt.showDsTable(" + tabIndex + ",'" + subType + "'," + dsIndex + ",'" + dsID + "','" + dsName + "');\" " +
		"style = 'cursor: pointer;'>show detail data of " + dsName + "</a>").appendTo(dataTitle);
	$(dataTitle).css({
		"margin-bottom": "5px",
		"border-bottom": "1px solid #282828"
	});
	
	$.getJSON(Common.dsInfoUrl(),{
		id: dsID
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
		id: dsID
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

Mgt.showDvTable = function(tabIndex,subType,dsIndex,dvID,dvName){
	var type = "dv";
	
	$("#" + type + "-" + subType + "-mgt-detail-data-title-" + tabIndex + "-" + dsIndex).empty();
	$("<span>show detail data of " + dvName + "</span>").appendTo("#" + type + "-" + subType + "-mgt-detail-data-title-" + tabIndex + "-" + dsIndex);
	
	$.getJSON(Common.dvInfoUrl(),{
		id: dvID
	},function(data){
		var keyLen = 0;
		if(data.hasOwnProperty("identifiers")){
			keyLen = data.identifiers.length;
		}
		var valueLen = 0;
		if(data.hasOwnProperty("values")){
			valueLen = data.values.length;
		}
		
		var tableData = new google.visualization.DataTable();
		for(var i = 0; i < keyLen; i++){
			tableData.addColumn("string",data.identifiers[i]);
		}
		for(var i = 0; i < valueLen; i++){
			tableData.addColumn("string",data.values[i]);
		}
		
		$.getJSON(Common.dvDataUrl(),{
			id: dvID
		},function(data){
			var l = data.length;
			$("#" + type + "-" + subType + "-mgt-detail-data-title-" + tabIndex + "-" + dsIndex).empty();
			if(l == 0){
				$("<span>this data view is empty</span>").appendTo("#" + type + "-" + subType + "-mgt-detail-data-title-" + tabIndex + "-" + dsIndex);
				Mgt.adjustHeight();
				return;
			}
			$("<span>detail data</span>").appendTo("#" + type + "-" + subType + "-mgt-detail-data-title-" + tabIndex + "-" + dsIndex);
			
			var detailData = document.createElement("div");
			detailData.setAttribute("id",type + "-" + subType + "-mgt-detail-data-" + tabIndex + "-" + dsIndex);
			detailData.setAttribute("class","mgt-detail-data");
			$(detailData).appendTo("#" + type + "-" + subType + "-mgt-content-" + tabIndex + "-" + dsIndex);
			
			var arr = $.parseJSON("[]");
			for(var i = 0; i < l; i++){
				var subArr = $.parseJSON("[]");
				arr[i] = subArr;
				for(var j = 0; j < keyLen; j++){
					subArr[j] = data[i].identifiers[j].value;
				}
				for(var j = 0; j < valueLen; j++){
					subArr[j + keyLen] = data[i].values[j].value;
				}
			}
			
			tableData.addRows(arr);
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

Mgt.showDsTable = function(tabIndex,subType,dsIndex,dsID,dsName){
	var type = "ds";
	
	$("#" + type + "-" + subType + "-mgt-detail-data-title-" + tabIndex + "-" + dsIndex).empty();
	$("<span>show detail data of " + dsName + "</span>").appendTo("#" + type + "-" + subType + "-mgt-detail-data-title-" + tabIndex + "-" + dsIndex);
	
	$.getJSON(Common.dsFieldUrl(),{
		id: dsID
	},function(data){
		var len = data.length;
		var tableData = new google.visualization.DataTable();
		for(var i = 0; i < len; i++){
			tableData.addColumn("string",data[i].fieldName);
		}
		
		$.getJSON(Common.dsDataUrl(),{
			id: dsID
		},function(data){
			var l = data.length;
			$("#" + type + "-" + subType + "-mgt-detail-data-title-" + tabIndex + "-" + dsIndex).empty();
			if(l == 0){
				$("<span>this data set is empty</span>").appendTo("#" + type + "-" + subType + "-mgt-detail-data-title-" + tabIndex + "-" + dsIndex);
				Mgt.adjustHeight();
				return;
			}
			$("<span>detail data</span>").appendTo("#" + type + "-" + subType + "-mgt-detail-data-title-" + tabIndex + "-" + dsIndex);
			
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
			
			var selectSQL = document.createElement("div");
			selectSQL.setAttribute("id",type + "-" + subType + "-mgt-sql-select-" + tabIndex + "-" + dsIndex);
			selectSQL.setAttribute("class","mgt-sql");
			$(selectSQL).appendTo("#" + type + "-" + subType + "-mgt-content-" + tabIndex + "-" + dsIndex);
			Mgt.selectSQL(tabIndex,subType,dsIndex,dsID);
			
			var sDataTitle = document.createElement("div");
			sDataTitle.setAttribute("id",type + "-" + subType + "-mgt-sql-select-detail-data-title-" + tabIndex + "-" + dsIndex);
			sDataTitle.setAttribute("class","mgt-detail-data-title");
			$(sDataTitle).appendTo("#" + type + "-" + subType + "-mgt-content-" + tabIndex + "-" + dsIndex);
			$(sDataTitle).css({
				"display": "none",
				"margin-bottom": 0,
				"border-bottom-width": 0
			});
			
			var sDetailData = document.createElement("div");
			sDetailData.setAttribute("id",type + "-" + subType + "-mgt-sql-select-detail-data-" + tabIndex + "-" + dsIndex);
			sDetailData.setAttribute("class","mgt-detail-data");
			$(sDetailData).appendTo("#" + type + "-" + subType + "-mgt-content-" + tabIndex + "-" + dsIndex);
			$(sDetailData).css({
				"display": "none"
			});
			
			var sqlTitle = document.createElement("div");
			sqlTitle.setAttribute("id",type + "-" + subType + "-mgt-sql-operation-title-" + tabIndex + "-" + dsIndex);
			sqlTitle.setAttribute("class","mgt-sql-operation-title");
			$(sqlTitle).appendTo("#" + type + "-" + subType + "-mgt-content-" + tabIndex + "-" + dsIndex);
			$("<a herf = 'javascript:void(0);' onClick = \"Mgt.showSQL(" + tabIndex + ",'" + subType + "'," + dsIndex + ",'" + dsID + "');\" " +
				"style = 'cursor: pointer;'>do some sql operations on " + dsName + "</a>").appendTo(sqlTitle);
			
			var groupBy = document.createElement("div");
			groupBy.setAttribute("id",type + "-" + subType + "-mgt-group-by-" + tabIndex + "-" + dsIndex);
			groupBy.setAttribute("class","mgt-sql");
			$(groupBy).appendTo("#" + type + "-" + subType + "-mgt-content-" + tabIndex + "-" + dsIndex);
			$(groupBy).css({
				"display": "none"
			});
			
			var selectTitle = document.createElement("div");
			selectTitle.setAttribute("id",type + "-" + subType + "-mgt-select-column-title-" + tabIndex + "-" + dsIndex);
			selectTitle.setAttribute("class","mgt-select-column-title");
			$(selectTitle).appendTo("#" + type + "-" + subType + "-mgt-content-" + tabIndex + "-" + dsIndex);
			$("<a herf = 'javascript:void(0);' onClick = \"Mgt.showColumn(" + tabIndex + ",'" + subType + "'," + dsIndex + ",'" + dsID + "');\" " +
				"style = 'cursor: pointer;'>create/show data view from " + dsName + "</a>").appendTo(selectTitle);
			
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
			}else if(subAcc == 1){
				subType = "geo";
			}else{
				subType = "other";
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
	var cntWidth = $("#" + type + "-" + subType + "-content-" + tabIndex).width();
	var tabsWidth = cntWidth - 40;
	
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
		
		var mgtCntWidth = tabsWidth - $("#" + type + "-" + subType + "-tabs-ul-" + tabIndex).width() - 50;
		$("#" + type + "-" + subType + "-mgt-content-" + tabIndex + "-" + dsIndex).css({
			"width": mgtCntWidth
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
	$("#" + type + "-" + subType + "-tabs-" + tabIndex).css({
		"width": tabsWidth
	});
	$("#" + type + "-content-" + tabIndex).css({
		"height": $("#" + type + "-accordion-" + tabIndex).height() + 10
	});
	Tab.adjustHeight(tabIndex);
};