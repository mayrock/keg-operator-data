Mgt.drawOptions = function(tabIndex,subType,dsIndex,dvType,dvName,dvDes,dsName){
	var type = "ds";
	
	$.getJSON(Common.dvFieldUrl(),{
		dataset: dvName
	},function(data){
		var checkbox = $("#" + type + "-" + subType + "-mgt-" + dvType + "-options-value-" + tabIndex + "-" + dsIndex).find("input");
		for(var i = 0; i < checkbox.length; i++){
			checkbox.eq(i).prop("checked",false);
		}
		if(dvType == "sta"){
			var radio = $("#" + type + "-" + subType + "-mgt-sta-options-key-" + tabIndex + "-" + dsIndex).find("input");
			for(var j = 0; j < radio.length; j++){
				radio.eq(j).prop("checked",false);
			}
			for(var i = 0; i < data.length; i++){
				if(data[i].functionality == "Identifier"){
					var span = $("#" + type + "-" + subType + "-mgt-sta-options-key-" + tabIndex + "-" + dsIndex).find("span");
					var keyName = data[i].fieldName;
					for(var j = 1; j < span.length; j++){
						if((keyName + " ") == span.eq(j).text()){
							radio.eq(j - 1).prop("checked",true);
							break;
						}
					}
				}else{
					var span = $("#" + type + "-" + subType + "-mgt-sta-options-value-" + tabIndex + "-" + dsIndex).find("span");
					var valueName = data[i].fieldName;
					for(var j = 1; j < span.length; j++){
						if((valueName + " ") == span.eq(j).text()){
							checkbox.eq(j - 1).prop("checked",true);
							break;
						}
					}
				}
			}
		}else{
			for(var i = 0; i < data.length; i++){
				if((data[i].functionality != "Latitude") && (data[i].functionality != "Longitude")){
					var span = $("#" + type + "-" + subType + "-mgt-geo-options-value-" + tabIndex + "-" + dsIndex).find("span");
					var valueName = data[i].fieldName;
					for(var j = 1; j < span.length; j++){
						if((valueName + " ") == span.eq(j).text()){
							checkbox.eq(j - 1).prop("checked",true);
							break;
						}
					}
				}
			}
		}
		
		Mgt.dataview(tabIndex,subType,dsIndex,dvType,dsName);
		
		var title = $("#" + type + "-" + subType + "-mgt-" + dvType + "-save-data-view-title-" + tabIndex + "-" + dsIndex);
		title.empty();
		$("<span>modify/delete this data view</span><span>").appendTo(title);
		var text = $("#" + type + "-" + subType + "-mgt-" + dvType + "-save-data-view-text-" + tabIndex + "-" + dsIndex).find("input");
		text.eq(0).val(dvName);
		text.eq(1).val(dvDes);
		$("#" + type + "-" + subType + "-mgt-" + dvType + "-save-data-view-button-" + tabIndex + "-" + dsIndex)
			.find("input").eq(0).val("modify");
		
		var button = $("#" + type + "-" + subType + "-mgt-" + dvType + "-save-data-view-button-" + tabIndex + "-" + dsIndex);
		$("<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>").appendTo(button);
		var input = $("<input/>");
		input.attr("type","button");
		input.attr("style","font-family: Times New Roman,\"楷体\";font-size: 16px;cursor: pointer;");
		input.val("delete");
		input.attr("onclick","Mgt.deleteDataview(" + tabIndex + ",'" + subType + "'," +
			dsIndex + ",'" + dvType + "','" + dvName + "','" + dsName + "')");
		input.appendTo(button);
	}).error(function(){
		alert("Oops, we got an error...");
		return;
	});
};

Mgt.dataview = function(tabIndex,subType,dsIndex,dvType,dsName){
	var type = "ds";
	
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
	
	var input = document.createElement("input");
	$(input).attr("type","button");
	$(input).attr("style","font-family: Times New Roman,\"楷体\";font-size: 16px;cursor: pointer;");
	$(input).val("save");
	$(input).appendTo(button);
	
	Mgt.adjustHeight();
	
	if(dvType == "sta"){
		Mgt.loadChart(tabIndex,subType,dsIndex,dsName);
	}else{
		Mgt.loadMap(tabIndex,subType,dsIndex,dsName);
	}
};

Mgt.loadChart = function(tabIndex,subType,dsIndex,dsName){
	var type = "ds";
	
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
		
		var input = $("#" + type + "-" + subType + "-mgt-sta-save-data-view-button-" + tabIndex + "-" + dsIndex).find("input").eq(0);
		var dvOperate = input.val();
		input.attr("onclick","Mgt.opDataview('" + dvOperate + "'," + tabIndex + ",'" + subType + "'," + dsIndex + ",'sta','" +
			dsName + "','" + jsonArr + "');");
		
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

Mgt.loadMap = function(tabIndex,subType,dsIndex,dsName){
	var type = "ds";
	
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
		console.log(jsonArr);
		var input = $("#" + type + "-" + subType + "-mgt-geo-save-data-view-button-" + tabIndex + "-" + dsIndex).find("input").eq(0);
		var dvOperate = input.val();
		console.log(dvOperate);
		input.attr("onclick","Mgt.opDataview('" + dvOperate + "'," + tabIndex + ",'" + subType + "'," + dsIndex + ",'geo','" +
			dsName + "','" + jsonArr + "');");
		
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

Mgt.opDataview = function(dvOperate,tabIndex,subType,dsIndex,dvType,dsName,arr){
	var type = "ds";
	
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
	console.log(dvName);
	console.log(dvDes);
	var jsonArr = $.parseJSON(arr);
	console.log(jsonArr);
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
	
	var url = "";
	var msg = JSON.parse("{}");
	if(dvOperate == "save"){
		url = Common.addDvUrl();
		msg.dataset = dsName;
		msg.dataview = dvName;
		msg.description = dvDes;
		msg.datafeaturetype = dfType;
		msg.keys = key;
		msg.values = value;
	}
	$.post(url,msg)
	.done(function(data,textStatus,jqXHR){
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
			Mgt.showDvList(tabIndex,subType,dsIndex,dvType,dsName);
			
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

Mgt.deleteDataview = function(tabIndex,subType,dsIndex,dvType,dvName,dsName){
	var type = "ds";
	
	$.post(Common.delDvUrl(),{
		dataset: dvName
	}).done(function(data,textStatus,jqXHR){
		if(dvType == "sta"){
			var radio = $("#" + type + "-" + subType + "-mgt-" + dvType + "-options-key-" + tabIndex + "-" + dsIndex).find("input");
			for(var i = 0; i < radio.length; i++){
				if(radio.eq(i).prop("checked") == true){
					radio.eq(i).prop("checked",false);
					break;
				}
			}
		}
		var checkbox = $("#" + type + "-" + subType + "-mgt-" + dvType + "-options-value-" + tabIndex + "-" + dsIndex).find("input");
		for(var i = 0; i < checkbox.length; i++){
			if(checkbox.eq(i).prop("checked") == true){
				checkbox.eq(i).prop("checked","false");
			}
		}
		
		var dataview = $("#" + type + "-" + subType + "-mgt-" + dvType + "-data-view-" + tabIndex + "-" + dsIndex);
		dataview.css("display","none")
		dataview.empty();
		
		Mgt.showDvList(tabIndex,subType,dsIndex,dvType,dsName);
	}).fail(function(jqXHR,textStatus,errorThrown){
		console.log(jqXHR);
		console.log(textStatus);
		console.log(errorThrown);
		alert("Oops, we got an error...");
		return;
	});
};