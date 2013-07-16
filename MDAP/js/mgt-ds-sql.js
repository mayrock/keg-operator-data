Mgt.showSQL = function(tabIndex,subType,dsIndex,dsID){
	var type = "ds";
	
	$.getJSON(Common.dsFieldUrl(),{
		id: dsID
	},function(data){
		$("#" + type + "-" + subType + "-mgt-sql-operation-title-" + tabIndex + "-" + dsIndex).remove();
		
		var groupBy = $("#" + type + "-" + subType + "-mgt-group-by-" + tabIndex + "-" + dsIndex);
		$(groupBy).css({
			"display": "block"
		});
		
		var title = document.createElement("div");
		title.setAttribute("id",type + "-" + subType + "-mgt-group-by-title-" + tabIndex + "-" + dsIndex);
		title.setAttribute("class","mgt-sql-title");
		$(title).appendTo(groupBy);
		$("<span>group by operation</span>").appendTo(title);
		/*
		var dvList = document.createElement("div");
	dvList.setAttribute("id",type + "-" + subType + "-mgt-" + dvType + "-data-view-list-" + tabIndex + "-" + dsIndex);
	dvList.setAttribute("class","mgt-dv-list");
	$(dvList).appendTo(selector);
		*/
		var options = document.createElement("div");
		options.setAttribute("id",type + "-" + subType + "-mgt-sql-options-" + tabIndex + "-" + dsIndex);
		options.setAttribute("class","mgt-sql-options");
		$(options).appendTo(groupBy);
		
		var table = $("<table></table>");
		table.appendTo(options);
		for(var i = 0; i < data.length; i++){
			var tr = $("<tr></tr>");
			tr.appendTo(table);
			var td = $("<td></td>");
			td.appendTo(tr);
			td.css({
				"padding-left": "20px"
			});
			$("<span id = '" + data[i].fieldName + "'>" + data[i].fieldName + "</span>").appendTo(td);
			td = $("<td></td>");
			td.css({
				"padding-left": "5px"
			});
			td.appendTo(tr);
			var select = $("<select></select>");
			select.appendTo(td);
			select.css({
				"font-family": "Times New Roman,\"楷体\"",
				"font-size": "16px"
			});
			if((data[i].type == "Int") || (data[i].type == "Double")){
				$("<option value = 'GB'>group by</option>").appendTo(select);
				$("<option value = 'COUNT'>count</option>").appendTo(select);
				$("<option value = 'SUM'>sum</option>").appendTo(select);
			}else{
				$("<option value = 'GB'>group by</option>").appendTo(select);
				$("<option value = 'COUNT'>count</option>").appendTo(select);
			}
		}
		
		var text = document.createElement("div");
		text.setAttribute("id",type + "-" + subType + "-mgt-sql-text-" + tabIndex + "-" + dsIndex);
		text.setAttribute("class","mgt-sql-text");
		$(text).appendTo(groupBy);
		
		table = $("<table></table>");
		table.appendTo(text);
		for(var i = 0; i < 2; i++){
			var tr = $("<tr></tr>");
			tr.appendTo(table);
			var td = $("<td></td>");
			td.appendTo(tr);
			td.css({
				"padding-left": "20px"
			});
			if(i == 0){
				$("<span>Name</span>").appendTo(td);
			}else{
				$("<span>Description</span>").appendTo(td);
			}
			td = $("<td></td>");
			td.css({
				"padding-left": "5px"
			});
			td.appendTo(tr);
			if(i == 0){
				$("<input type = 'text' maxlength = '16' id = 'dvName' style = 'font-family: Times New Roman,\"楷体\";font-size: 16px;'/><br/>")
					.appendTo(td);
			}else{
				$("<input type = 'text' id = 'dvDes' style = 'font-family: Times New Roman,\"楷体\";font-size: 16px;'/>").appendTo(td);
			}
		}
		
		var button = document.createElement("div");
		button.setAttribute("id",type + "-" + subType + "-mgt-sql-button-" + tabIndex + "-" + dsIndex);
		button.setAttribute("class","mgt-sql-button");
		$(button).appendTo(groupBy);
		$("<input type = 'button' value = 'group by' style = 'font-family: Times New Roman,\"楷体\";font-size: 16px;cursor: pointer;' " +
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
	var count = 0;
	var span = $("#" + type + "-" + subType + "-mgt-sql-options-" + tabIndex + "-" + dsIndex).find("span");
	var select = $("#" + type + "-" + subType + "-mgt-sql-options-" + tabIndex + "-" + dsIndex).find("select");
	var text = $("#" + type + "-" + subType + "-mgt-sql-text-" + tabIndex + "-" + dsIndex).find("input");
	for(var i = 0; i < select.length; i++){
		fields[i] = span.eq(i).attr("id");
		funcs[i] = select.eq(i).val();
		if(select.eq(i).val() == "GB"){
			count++;
		}
	}
	if(count == 0){
		alert("A column must be selected for group by!");
		return;
	}
	if(text.eq(0).val() == 0)
	
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