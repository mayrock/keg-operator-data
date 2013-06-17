Mgt = {};

Mgt.load = function(tabIndex,dsIndex,name,type){
	var url1 = "";
	var url2 = "";
	if(type == "ds"){
		url1 = Common.dsInfoUrl();
		url2 = Common.dsFieldUrl();
	}else{
		url1 = Common.dvInfoUrl();
		url2 = Common.dvFieldUrl();
	}
	infoTable = document.createElement("div");
	infoTable.setAttribute("id","infoTable" + tabIndex + "_" + dsIndex);
	infoTable.setAttribute("class","infoTable");
	$(infoTable).appendTo("#dataMgt" + tabIndex + "_" + dsIndex);
	fieldTable = document.createElement("div");
	fieldTable.setAttribute("id","fieldTable" + tabIndex + "_" + dsIndex);
	fieldTable.setAttribute("class","fieldTable");
	$(fieldTable).appendTo("#dataMgt" + tabIndex + "_" + dsIndex);
	$.getJSON(url1,{
		dataset: name
	},function(data){
		var len = data.length;
		var tableData = new google.visualization.DataTable();
		tableData.addColumn('string','datasetName');
		tableData.addColumn('string','description');
		var arr = "[[\"" + data.datasetName + "\",\"" + data.descriptionZh + "\"]]";
		tableData.addRows($.parseJSON(arr));
		
		var table = new google.visualization.Table(document.getElementById('infoTable' + tabIndex + "_" + dsIndex));
		table.draw(tableData,{showRowNumber: false});
	}).error(function(){
		alert("Oops, we got an error...");
		return;
	});
	$.getJSON(url2,{
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
		
		var table = new google.visualization.Table(document.getElementById('fieldTable' + tabIndex + "_" + dsIndex));
		table.draw(tableData,{showRowNumber: true});
	}).error(function(){
		alert("Oops, we got an error...");
		return;
	});
};