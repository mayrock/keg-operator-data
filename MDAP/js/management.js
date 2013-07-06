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
			li = document.createElement("li");
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
			Mgt.load(tabIndex,i,name,type,subType);
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

Mgt.load = function(tabIndex,dsIndex,dsName,type,subType){
	var infoUrl = "";
	var fieldUrl = "";
	
	var infoTitle = document.createElement("div");
	infoTitle.setAttribute("id",type + "-" + subType + "-info-title-" + tabIndex + "-" + dsIndex);
	infoTitle.setAttribute("class","mgt-title");
	$(infoTitle).appendTo("#" + type + "-" + subType + "-mgt-content-" + tabIndex + "-" + dsIndex);
	
	var infoTable = document.createElement("div");
	infoTable.setAttribute("id",type + "-" + subType + "-info-" + tabIndex + "-" + dsIndex);
	infoTable.setAttribute("class","mgt-info");
	$(infoTable).appendTo("#" + type + "-" + subType + "-mgt-content-" + tabIndex + "-" + dsIndex);
	
	var fieldTitle = document.createElement("div");
	fieldTitle.setAttribute("id",type + "-" + subType + "-field-title-" + tabIndex + "-" + dsIndex);
	fieldTitle.setAttribute("class","mgt-title");
	$(fieldTitle).appendTo("#" + type + "-" + subType + "-mgt-content-" + tabIndex + "-" + dsIndex);
	
	var fieldTable = document.createElement("div");
	fieldTable.setAttribute("id",type + "-" + subType + "-field-" + tabIndex + "-" + dsIndex);
	fieldTable.setAttribute("class","mgt-field");
	$(fieldTable).appendTo("#" + type + "-" + subType + "-mgt-content-" + tabIndex + "-" + dsIndex);
	
	if(type == "dv"){
		infoUrl = Common.dvInfoUrl();
		fieldUrl = Common.dvFieldUrl();
		$("<span>data view information</span>").appendTo(infoTitle);
		$("<span>column information</span>").appendTo(fieldTitle);
		$.getJSON(infoUrl,{
			dataset: dsName
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
			var table = new google.visualization.Table(document.getElementById(type + "-" + subType + "-info-" + tabIndex + "-" + dsIndex));
			table.draw(tableData,{showRowNumber: false});
		}).error(function(){
			alert("Oops, we got an error...");
			return;
		});
	}else{
		infoUrl = Common.dsInfoUrl();
		fieldUrl = Common.dsFieldUrl();
		$("<span>data set information</span>").appendTo(infoTitle);
		$("<span>column information</span>").appendTo(fieldTitle);
		var dataTitle = document.createElement("div");
		dataTitle.setAttribute("id",type + "-" + subType + "-mgt-detail-data-title-" + tabIndex + "-" + dsIndex);
		dataTitle.setAttribute("class","mgt-detail-data-title");
		$(dataTitle).appendTo("#" + type + "-" + subType + "-mgt-content-" + tabIndex + "-" + dsIndex);
		$("<a herf = 'javascript:void(0);' onClick = \"Mgt.showTable('" + dsName + "');\">" +
			"show detail data of " + dsName + "</a>").appendTo(dataTitle);
		$(dataTitle).css({
			"margin-bottom": "5px",
			"border-bottom": "1px solid #282828"
		});
		$.getJSON(infoUrl,{
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
			var table = new google.visualization.Table(document.getElementById(type + "-" + subType + "-info-" + tabIndex + "-" + dsIndex));
			table.draw(tableData,{showRowNumber: false});
		}).error(function(){
			alert("Oops, we got an error...");
			return;
		});
	}
	$.getJSON(fieldUrl,{
		dataset: dsName
	},function(data){
		var len = data.length;
		var tableData = new google.visualization.DataTable();
		tableData.addColumn('string','description');
		tableData.addColumn('string','fieldName');
		tableData.addColumn('string','functionality');
		tableData.addColumn('boolean','isKey');
		tableData.addColumn('string','type');
		var arr = "[";
		for(var i = 0; i < len; i++){
			arr += "[\"" + data[i].description + "\",\"" + data[i].fieldName + "\",\"" + data[i].functionality + "\"," +
				data[i].isKey + ",\"" + data[i].type + "\"]";
			if(i == len - 1){
				arr += "]";
			}else{
				arr += ",";
			}
		}
		tableData.addRows($.parseJSON(arr));
		var table = new google.visualization.Table(document.getElementById(type + "-" + subType + "-field-" + tabIndex + "-" + dsIndex));
		table.draw(tableData,{showRowNumber: true});
	}).error(function(){
		alert("Oops, we got an error...");
		return;
	});
};

Mgt.adjustHeight = function(){
	console.log("Information:");
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
	/*
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
			*/
			var selectTitle = document.createElement("div");
			selectTitle.setAttribute("id",type + "-" + subType + "-mgt-select-column-title-" + tabIndex + "-" + dsIndex);
			selectTitle.setAttribute("class","mgt-select-column-title");
			$(selectTitle).appendTo("#" + type + "-" + subType + "-mgt-content-" + tabIndex + "-" + dsIndex);
			$("<a herf = 'javascript:void(0);' onClick = \"Mgt.showColumn('" + dsName + "');\">" +
				"create data view from " + dsName + "</a>").appendTo(selectTitle);
			
			Mgt.adjustHeight();/*
		}).error(function(){
			alert("Oops, we got an error...");
			return;
		});
	}).error(function(){
		alert("Oops, we got an error...");
	});*/
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
		console.log(geo + " " + sta);
		if((sta == 0) && (geo == 0)){
			$("#" + type + "-" + subType + "-mgt-select-column-title-" + tabIndex + "-" + dsIndex).empty();
			$("<span>this data set can't create any data view</span>").appendTo(selectTitle);
			return;
		}
		$("#" + type + "-" + subType + "-mgt-select-column-title-" + tabIndex + "-" + dsIndex).remove();
		var selectColumn = document.createElement("div");
		selectColumn.setAttribute("id",type + "-" + subType + "-mgt-select-column-" + tabIndex + "-" + dsIndex);
		selectColumn.setAttribute("class","mgt-select-column");
		$(selectColumn).appendTo("#" + type + "-" + subType + "-mgt-content-" + tabIndex + "-" + dsIndex);
		$.getJSON(Common.dsFieldUrl(),{
			dataset: dsName
		},function(data){
			if(sta == 1){
				var selector = document.createElement("div");
				selector.setAttribute("id",type + "-" + subType + "-mgt-sta-selector-" + tabIndex + "-" + dsIndex);
				selector.setAttribute("class","mgt-selector");
				$(selector).appendTo("#" + type + "-" + subType + "-mgt-content-" + tabIndex + "-" + dsIndex);
				
				var title = document.createElement("div");
				title.setAttribute("id",type + "-" + subType + "-mgt-sta-selector-title-" + tabIndex + "-" + dsIndex);
				title.setAttribute("class","mgt-selector-title");
				$(title).appendTo(selector);
				$("<span>create a stat data view<span>").appendTo(title);
				
				var key = document.createElement("div");
				key.setAttribute("id",type + "-" + subType + "-mgt-sta-selector-key-" + tabIndex + "-" + dsIndex);
				key.setAttribute("class","mgt-selector-key");
				$(key).appendTo(selector);
				$("<span>choose a key: <span>").appendTo(key);
				
				var value = document.createElement("div");
				value.setAttribute("id",type + "-" + subType + "-mgt-sta-selector-value-" + tabIndex + "-" + dsIndex);
				value.setAttribute("class","mgt-selector-value");
				$(value).appendTo(selector);
				$("<span>choose some values: <span>").appendTo(value);
				
				var button = document.createElement("div");
				button.setAttribute("id",type + "-" + subType + "-mgt-sta-selector-button-" + tabIndex + "-" + dsIndex);
				button.setAttribute("class","mgt-selector-button");
				$(button).appendTo(selector);
				$("<input type = 'button' value = 'show this data view' style = 'font-family: Times New Roman,\"楷体\";font-size: 16px;' " +
					"onclick = \"Mgt.staDataview('" + dsName + "');\"/>").appendTo(button);
				
				for(var i = 0; i < data.length; i++){
					if(data[i].functionality == "Identifier"){
						$("<input type = 'radio' name = 'key' value = '" + i + "'/><span>" + data[i].fieldName + " </span>").appendTo(key);
					}else{
						$("<input type = 'checkbox' value = '" + i + "'/><span>" + data[i].fieldName + " </span>").appendTo(value);
					}
				}
			}
			if(geo == 1){
				var selector = document.createElement("div");
				selector.setAttribute("id",type + "-" + subType + "-mgt-geo-selector-" + tabIndex + "-" + dsIndex);
				selector.setAttribute("class","mgt-selector");
				$(selector).appendTo("#" + type + "-" + subType + "-mgt-content-" + tabIndex + "-" + dsIndex);
				
				var title = document.createElement("div");
				title.setAttribute("id",type + "-" + subType + "-mgt-geo-selector-title-" + tabIndex + "-" + dsIndex);
				title.setAttribute("class","mgt-selector-title");
				$(title).appendTo(selector);
				$("<span>create a geo data view<span>").appendTo(title);
				
				var key = document.createElement("div");
				key.setAttribute("id",type + "-" + subType + "-mgt-geo-selector-key-" + tabIndex + "-" + dsIndex);
				key.setAttribute("class","mgt-selector-key");
				$(key).appendTo(selector);
				$("<span>keys:&nbsp;&nbsp;&nbsp;&nbsp;<span>").appendTo(key);
				
				var value = document.createElement("div");
				value.setAttribute("id",type + "-" + subType + "-mgt-geo-selector-value-" + tabIndex + "-" + dsIndex);
				value.setAttribute("class","mgt-selector-value");
				$(value).appendTo(selector);
				$("<span>choose some values: <span>").appendTo(value);
				
				var button = document.createElement("div");
				button.setAttribute("id",type + "-" + subType + "-mgt-geo-selector-button-" + tabIndex + "-" + dsIndex);
				button.setAttribute("class","mgt-selector-button");
				$(button).appendTo(selector);
				$("<input type = 'button' value = 'show this data view' style = 'font-family: Times New Roman,\"楷体\";font-size: 16px;' " +
					"onclick = \"Mgt.showDataview('geo','" + dsName + "');\"/>").appendTo(button);
				
				for(var i = 0; i < data.length; i++){
					if((data[i].functionality == "Longitude") || (data[i].functionality == "Latitude")){
						$("<span>" + data[i].fieldName + "&nbsp;&nbsp;&nbsp;&nbsp;<span>").appendTo(key);
					}else{
						$("<input type = 'checkbox' value = '" + i + "'/><span>" + data[i].fieldName + " </span>").appendTo(value);
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

Mgt.staDataview = function(dsName){
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
	var jsonArr = "[";
	
	var radio = $("#" + type + "-" + subType + "-mgt-sta-selector-key-" + tabIndex + "-" + dsIndex).find("input");
	for(var i = 0; i < radio.length; i++){
		if(radio.eq(i).is(":checked") == true){
			key = radio.eq(i).val();
		}
	}
	if(key == -1){
		alert("Choose one key!");
		return;
	}
	var checkbox = $("#" + type + "-" + subType + "-mgt-sta-selector-value-" + tabIndex + "-" + dsIndex).find("input");
	for(var i = 0; i < checkbox.length; i++){
		var j = value.length;
		if(checkbox.eq(i).is(":checked") == true){
			value[j] = checkbox.eq(i).val();
		}
	}
	if(value.length == 0){
		alert("Choose at least one value!");
		return;
	}
	if($("#" + type + "-" + subType + "-mgt-data-view-" + tabIndex + "-" + dsIndex).length != 0){
		$("#" + type + "-" + subType + "-mgt-data-view-" + tabIndex + "-" + dsIndex).remove();
	}
	var dataview = document.createElement("div");
	dataview.setAttribute("id",type + "-" + subType + "-mgt-data-view-" + tabIndex + "-" + dsIndex);
	dataview.setAttribute("class","mgt-data-view");
	$(dataview).appendTo("#" + type + "-" + subType + "-mgt-content-" + tabIndex + "-" + dsIndex);
	var dvContainer = document.createElement("div");
	dvContainer.setAttribute("id",type + "-" + subType + "-mgt-data-view-container-" + tabIndex + "-" + dsIndex);
	dvContainer.setAttribute("class","mgt-data-view-container");
	$(dvContainer).appendTo(dataview);
	var saveDataview = document.createElement("div");
	saveDataview.setAttribute("id",type + "-" + subType + "-mgt-save-data-view-" + tabIndex + "-" + dsIndex);
	saveDataview.setAttribute("class","mgt-save-data-view");
	$(saveDataview).appendTo(dataview);
	$(saveDataview).css({
		"width": $(dataview).width() - 500
	});
	var title = document.createElement("div");
	title.setAttribute("id",type + "-" + subType + "-mgt-save-data-view-title-" + tabIndex + "-" + dsIndex);
	title.setAttribute("class","mgt-save-data-view-title");
	$(title).appendTo(saveDataview);
	$("<span>save this data view</span><span>").appendTo(title);
	
	var text = document.createElement("div");
	text.setAttribute("id",type + "-" + subType + "-mgt-save-data-view-text-" + tabIndex + "-" + dsIndex);
	text.setAttribute("class","mgt-save-data-view-text");
	$(text).appendTo(saveDataview);
	$("<span>Name:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><input type = 'text' maxlength = '16' id = 'dvName'/><br/>").appendTo(text);
	$("<span>Description:</span><input type = 'text' id = 'dvDes'/>").appendTo(text);
	
	var button = document.createElement("div");
	button.setAttribute("id",type + "-" + subType + "-mgt-save-data-view-button-" + tabIndex + "-" + dsIndex);
	button.setAttribute("class","mgt-save-data-view-button");
	$(button).appendTo(saveDataview);
	
	Mgt.adjustHeight();
	
	$.getJSON(Common.dsFieldUrl(),{
		dataset: dsName
	},function(data){
		var len = data.length;
		var arr = "[";
		arr += "[\"" + data[key].fieldName + "\"";
		jsonArr += "\\\"" + data[key].fieldName + "\\\"";
		for(var i = 0; i < value.length; i++){
			arr += ",\"" + data[value[i]].fieldName + "\"";
			jsonArr += ",\\\"" + data[value[i]].fieldName + "\\\"";
		}
		arr += "],";
		jsonArr += "]";
		$("<input type = 'button' value = 'save data view' style = 'font-family: Times New Roman,\"楷体\";font-size: 16px;' " +
			"onclick = 'Mgt.saveDataview(\"sta\",\"" + dsName + "\",\"" + jsonArr + "\");'/>").appendTo(button);
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
				var view = document.getElementById(type + "-" + subType + "-mgt-data-view-container-" + tabIndex + "-" + dsIndex);
				var data = google.visualization.arrayToDataTable($.parseJSON(arr));
				var options = {};
				var chart = new google.visualization.ColumnChart(view);
				chart.draw(data,options);
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
	
	var text = $("#" + type + "-" + subType + "-mgt-save-data-view-text-" + tabIndex + "-" + dsIndex).find("input");
	if(text.eq(0).val() == ""){
		alert("Input a name of data view!");
		return;
	}
	
	console.log(dsName);
	dvName = text.eq(0).val();
	dvDes = text.eq(1).val();
	console.log(dvType);
	var jsonArr = $.parseJSON(arr);
	var key = "[\"" + jsonArr[0] + "\"]";
	console.log(key);
	var len = jsonArr.length;
	var value = "[";
	for(var i = 1; i < len - 1; i++){
		value += "\"" + jsonArr[i] + "\",";
	}
	value += "\"" + jsonArr[len - 1] + "\"]";
	console.log(value);
	var dfType = "";
	if(dvType == "sta"){
		dfType = "DistributionFeature";
	}
	$.post(Common.addDvUrl(),{
		dataset: dsName,
		dataview: dvName,
		description: dvDes,
		datafeaturetype: dfType,
		keys: key,
		values: value
	},function(response,status){
		console.log(response.status);
	}).error(function(){
		alert("Oops, we got an error...");
		return;
	});
};