Mgt.showTable = function(tabIndex,subType,dsIndex,dsName){
	var type = "ds";
	
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
			$("<a herf = 'javascript:void(0);' onClick = \"Mgt.showColumn(" + tabIndex + ",'" + subType + "'," + dsIndex + ",'" + dsName + "');\" " +
				"style = 'cursor: pointer;'>create data view from " + dsName + "</a>").appendTo(selectTitle);
			
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

Mgt.showColumn = function(tabIndex,subType,dsIndex,dsName){
	var type = "ds";
	
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
				Mgt.selector(tabIndex,subType,dsIndex,"sta",dsName);
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
				Mgt.selector(tabIndex,subType,dsIndex,"geo",dsName);
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

Mgt.selector = function(tabIndex,subType,dsIndex,dvType,dsName){
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