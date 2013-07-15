Mgt.showSQL = function(tabIndex,subType,dsIndex,dsID){
	var type = "ds";
	
	$.getJSON(Common.dsFieldUrl(),{
		id: dsID
	},function(data){
		var title = $("#" + type + "-" + subType + "-mgt-sql-title-" + tabIndex + "-" + dsIndex);
		title.empty();
		$("<span>group by operation</span>").appendTo(title);
		
		var container = document.createElement("div");
		container.setAttribute("id",type + "-" + subType + "-mgt-sql-container-" + tabIndex + "-" + dsIndex);
		container.setAttribute("class","mgt-sql-container");
		$(container).appendTo("#" + type + "-" + subType + "-mgt-content-" + tabIndex + "-" + dsIndex);
		
		var options = document.createElement("div");
		options.setAttribute("id",type + "-" + subType + "-mgt-sql-options-" + tabIndex + "-" + dsIndex);
		options.setAttribute("class","mgt-sql-options");
		$(options).appendTo(container);
		
		for(var i = 0; i < data.length; i++){
			$("<span id = '" + data[i].fieldName + "'>&nbsp;&nbsp;&nbsp;&nbsp;" + data[i].fieldName + "&nbsp;&nbsp;&nbsp;&nbsp;</span>")
				.appendTo(options);
			var select = $("<select></select>");
			select.appendTo(options);
			if((data[i].type == "Int") || (data[i].type == "Double")){
				$("<option value = 'GB'>group by</option>").appendTo(select);
				$("<option value = 'COUNT'>count</option>").appendTo(select);
				$("<option value = 'SUM'>sum</option>").appendTo(select);
			}else{
				$("<option value = 'GB'>group by</option>").appendTo(select);
				$("<option value = 'COUNT'>count</option>").appendTo(select);
			}
			$("<br/>").appendTo(options);
		}
		
		var text = document.createElement("div");
		text.setAttribute("id",type + "-" + subType + "-mgt-sql-text-" + tabIndex + "-" + dsIndex);
		text.setAttribute("class","mgt-sql-text");
		$(text).appendTo(container);
		$("<span>Name:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><input type = 'text' maxlength = '16' id = 'dvName'/><br/>").appendTo(text);
		$("<span>Description:</span><input type = 'text' id = 'dvDes'/>").appendTo(text);
		
		var button = document.createElement("div");
		button.setAttribute("id",type + "-" + subType + "-mgt-sql-button-" + tabIndex + "-" + dsIndex);
		button.setAttribute("class","mgt-sql-button");
		$(button).appendTo(container);
		$("<input type = 'button' value = 'do group by operation' style = 'font-family: Times New Roman,\"楷体\";font-size: 16px;cursor: pointer;' " +
			"onclick = \"Mgt.groupBy(" + tabIndex + ",'" + subType + "'," + dsIndex + ",'" + dsID + "');\"/>").appendTo(button);
		
		Mgt.adjustHeight();
	}).error(function(){
		alert("Oops, we got an error...");
		return;
	});
};

Mgt.groupBy = function(tabIndex,subType,dsIndex,dsID){
	var type = "ds";
	
	var fields = $.parseJSON("[]");
	var funcs = $.parseJSON("[]");
	var span = $("#" + type + "-" + subType + "-mgt-sql-options-" + tabIndex + "-" + dsIndex).find("span");
	var select = $("#" + type + "-" + subType + "-mgt-sql-options-" + tabIndex + "-" + dsIndex).find("select");
	var text = $("#" + type + "-" + subType + "-mgt-sql-text-" + tabIndex + "-" + dsIndex).find("input");
	for(var i = 0; i < select.length; i++){
		fields[i] = span.eq(i).attr("id");
		funcs[i] = select.eq(i).val();
	}
	
	$.post(Common.dvSQLUrl(),{
		datasetid: dsID,
		name: text.eq(0).val(),
		description: text.eq(1).val(),
		fields: JSON.stringify(fields),
		funcs: JSON.stringify(funcs)
	}).done(function(data,textStatus,jqXHR){
		console.log(data);
		console.log(textStatus);
		console.log(jqXHR);
		if(data == ""){
			var content = $("#dv-other-content-" + tabIndex);
			content.empty();
			
			var tabs = document.createElement("div");
			tabs.setAttribute("id","dv-other-tabs-" + tabIndex);
			tabs.setAttribute("class","mgt-tabs");
			$(tabs).appendTo(content);
			
			var tabs_ul = document.createElement("ul");
			tabs_ul.setAttribute("id","dv-other-tabs-ul-" + tabIndex);
			tabs_ul.setAttribute("class","mgt-tabs-ul");
			$(tabs_ul).appendTo(tabs);
			
			Mgt.subTab(tabIndex,"dv","other");
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

Mgt.showColumn = function(tabIndex,subType,dsIndex,dsID){
	var type = "ds";
	
	$.getJSON(Common.dsInfoUrl(),{
		id: dsID
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
			id: dsID
		},function(data){
			if(sta == 1){
				Mgt.selector(tabIndex,subType,dsIndex,"sta",dsID);
				$("<span>create/show a stat data view</span>")
					.appendTo("#" + type + "-" + subType + "-mgt-sta-selector-title-" + tabIndex + "-" + dsIndex);
				$("<span>choose a key: </span>").appendTo("#" + type + "-" + subType + "-mgt-sta-options-key-" + tabIndex + "-" + dsIndex);
				
				for(var i = 0; i < data.length; i++){
					if(data[i].functionality == "Identifier"){
						$("<input type = 'radio' name = 'key' value = '" + i + "' " +
							"onclick = \"Mgt.dataview(" + tabIndex + ",'" + subType + "'," + dsIndex + ",'" + dsID + "','sta');\"/><span>" +
							data[i].fieldName + " </span>").appendTo("#" + type + "-" + subType + "-mgt-sta-options-key-" + tabIndex + "-" + dsIndex);
					}else{
						$("<input type = 'checkbox' value = '" + i + "' " +
							"onclick = \"Mgt.dataview(" + tabIndex + ",'" + subType + "'," + dsIndex + ",'" + dsID + "','sta');\"/><span>" +
							data[i].fieldName + " </span>").appendTo("#" + type + "-" + subType + "-mgt-sta-options-value-" + tabIndex + "-" + dsIndex);
					}
				}
			}
			if(geo == 1){
				Mgt.selector(tabIndex,subType,dsIndex,"geo",dsID);
				$("<span>create/show a geo data view</span>")
					.appendTo("#" + type + "-" + subType + "-mgt-geo-selector-title-" + tabIndex + "-" + dsIndex);
				$("<span>keys:&nbsp;&nbsp;&nbsp;&nbsp;</span>")
					.appendTo("#" + type + "-" + subType + "-mgt-geo-options-key-" + tabIndex + "-" + dsIndex);
				
				for(var i = 0; i < data.length; i++){
					if(data[i].functionality == "Latitude"){
						$("<span class = '" + i + "'>" + data[i].fieldName + "&nbsp;&nbsp;&nbsp;&nbsp;</span>")
							.appendTo("#" + type + "-" + subType + "-mgt-geo-options-key-" + tabIndex + "-" + dsIndex);
					}
					if((data[i].functionality != "Latitude") && (data[i].functionality != "Longitude")){
						$("<input type = 'checkbox' value = '" + i + "' " +
							"onclick = \"Mgt.dataview(" + tabIndex + ",'" + subType + "'," + dsIndex + ",'" + dsID + "','geo');\"/><span>" +
							data[i].fieldName + " </span>").appendTo("#" + type + "-" + subType + "-mgt-geo-options-value-" + tabIndex + "-" + dsIndex);
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

Mgt.selector = function(tabIndex,subType,dsIndex,dvType,dsID){
	var type = "ds";
	
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
		"onclick = \"Mgt.showDataview(" + tabIndex + ",'" + subType + "'," + dsIndex + ",'" + dvType + "','" + dsID + "');\"/>").appendTo(button);
	
	var dataview = document.createElement("div");
	dataview.setAttribute("id",type + "-" + subType + "-mgt-" + dvType + "-data-view-" + tabIndex + "-" + dsIndex);
	dataview.setAttribute("class","mgt-data-view");
	$(dataview).appendTo(selector);
	$(dataview).css({
		"display": "none"
	});
	
	var dvContainer = document.createElement("div");
	dvContainer.setAttribute("id",type + "-" + subType + "-mgt-" + dvType + "-data-view-container-" + tabIndex + "-" + dsIndex);
	dvContainer.setAttribute("class","mgt-data-view-container");
	$(dvContainer).appendTo(dataview);
	
	var saveDv = document.createElement("div");
	saveDv.setAttribute("id",type + "-" + subType + "-mgt-" + dvType + "-save-data-view-" + tabIndex + "-" + dsIndex);
	saveDv.setAttribute("class","mgt-save-data-view");
	$(saveDv).appendTo(dataview);
	
	title = document.createElement("div");
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
	
	button = document.createElement("div");
	button.setAttribute("id",type + "-" + subType + "-mgt-" + dvType + "-save-data-view-button-" + tabIndex + "-" + dsIndex);
	button.setAttribute("class","mgt-save-data-view-button");
	$(button).appendTo(saveDv);
	
	var input = document.createElement("input");
	$(input).attr("type","button");
	$(input).attr("style","font-family: Times New Roman,\"楷体\";font-size: 16px;cursor: pointer;");
	$(input).val("save");
	$(input).appendTo(button);
	
	Mgt.showDvList(tabIndex,subType,dsIndex,dvType,dsID);
};

Mgt.showDvList = function(tabIndex,subType,dsIndex,dvType,dsID){
	var type = "ds";
	
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
		}
	});
	
	if(dvType == "sta"){
		dfType = "DistributionFeature";
	}else{
		dfType = "GeoFeature";
	}
	
	$.getJSON(Common.dataviewUrl(),{
		featuretype: dfType,
		id: dsID
	}).done(function(data,textStatus,jqXHR){
		var len = data.length;
		var list = $("#" + type + "-" + subType + "-mgt-" + dvType + "-dv-list-" + tabIndex + "-" + dsIndex);
		for(var i = 0; i < len; i++){
			var dvID = data[i].id;
			var des = data[i].descriptionZh;
			var dvName = data[i].dataviewName;
			
			var nameCtnr = document.createElement("div");
			nameCtnr.setAttribute("id",type + "-" + subType + "-mgt-" + dvType + "-dv-list-" + tabIndex + "-" + dsIndex + "-" + i);
			nameCtnr.setAttribute("class","mgt-dv-list-name");
			$(nameCtnr).appendTo(list);
			$("<a href = 'javascript:void(0);' onClick = \"Mgt.drawOptions(" +
				tabIndex + ",'" + subType + "'," + dsIndex + ",'" + dvType + "','" + dvID + "','" + dvName + "','" + des + "','" + dsID + "');\">" +
				dvName + "</a>").appendTo(nameCtnr);
		}
		var listWidth = list.width() + 20;
		var listHeight = 21 * len;
		list.css({
			"width": listWidth,
			"height": listHeight
		});
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