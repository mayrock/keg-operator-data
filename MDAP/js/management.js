Mgt = {};

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

Mgt.createFrame = function(tabIndex,type){
	var head = document.createElement("h3");
	head.setAttribute("id",type + "Head" + tabIndex);
	head.setAttribute("class","head");
	$(head).appendTo("#accordion" + tabIndex);
	$(head).css({
		"padding-top": "10px",
		"padding-right": 0,
		"padding-bottom": "10px"
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

Mgt.adjustHeight = function(){
	var activeTab = $("#tabs").tabs("option","active");
	var tabIndex = Common.tabIndex[activeTab];
	var active = $("#accordion" + tabIndex).accordion("option","active");
	var type = "";
	if(typeof(active) == "boolean"){
		var tabHeight = $("#tab" + tabIndex).height();
		if((tabHeight + 25) > Common.tabHeight){
			$("#tab_bg").css({
				"height": tabHeight + 25
			});
		}else{
			$("#tab_bg").css({
				"height": Common.tabHeight
			});
		}
		return;
	}
	if(active == 0){
		type = "dv";
	}else{
		type = "ds";
	}
	var activeSubTab = $("#" + type + "Tabs" + tabIndex).tabs("option","active");
	var height = $("#" + type + "Tab" + tabIndex + "_" + activeSubTab).height();
	var ulHeight = $("#" + type + "Tabs_ul" + tabIndex).height();
	if(height >= ulHeight){
		$("#" + type + "Content" + tabIndex).css({
			"height": height + 20
		});
	}else{
		$("#" + type + "Content" + tabIndex).css({
			"height": ulHeight + 20
		});
	}
	var tabHeight = $("#tab" + tabIndex).height();
	if((tabHeight + 25) > Common.tabHeight){
		$("#tab_bg").css({
			"height": tabHeight + 25
		});
	}else{
		$("#tab_bg").css({
			"height": Common.tabHeight
		});
	}
};