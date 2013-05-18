Tab = {};

Tab.createFrame = function(tabName){
	if(!((tabName == "geo") || (tabName == "sta"))){
		alert("Oops, we got an error...");
		return;
	}
	$("#extended").css("display","none");
	tabNum = Common.tabNum;
	tabCount = Common.tabCount;
	//限制标签页个数
	if(tabCount == Common.tabLimit()){
		alert("Tabs cann't be more than " + Tab.limit + "!");
		return;
	}
	Common.tabNum ++;
	Common.tabCount ++;
	//添加一个标签页
	li = document.createElement("li");
	$(li).appendTo("#tabs_ul");
	li.innerHTML = "<a href='#tab" + tabNum + "'>" + tabName + " data</a>";
	$("<img src = 'css/images/close.png'/>")
		.appendTo(li)
		.hover(
			function(){
				$(this).attr("src","css/images/close_hover.png");
			},function(){
				$(this).attr("src","css/images/close.png");
			}
		).click(
			//点击关闭按钮删除标签页
			function(){
				$(this).parent().remove();
				divName = $(this).parent().children("a").attr("href");
				$(divName).remove();
				Common.tabCount --;
				$("#tabs").tabs("refresh");
			}
		);
	tab = document.createElement("div");
	tab.setAttribute("id","tab" + tabNum);
	tab.setAttribute("class","tab");
	$(tab).appendTo("#tabs");
	option = document.createElement("div");
	option.setAttribute("id","option" + tabNum);
	option.setAttribute("class","option");
	$(option).appendTo(tab);
	view = document.createElement("div");
	view.setAttribute("id","view" + tabNum);
	view.setAttribute("class","view");
	$(view).appendTo(tab);
	//设置标签页内容
	setTab();
	$("#tabs").tabs("refresh");
	$("#tabs").tabs("option","active",tabCount);
/*	$("#checkbox" + tabNum).jstree({
		"plugins": ["checkbox"]
	});
	$("#checkbox" + tabNum).jstree("hide_dots");
	$("#checkbox" + tabNum).jstree("hide_icons");*/

	function setTab(){
		if(tabName == "geo"){
			map = Map.initialize(tabNum);
		}
		checkbox = document.createElement("div");
		checkbox.setAttribute("id","checkbox" + tabNum);
		checkbox.setAttribute("class","checkbox");
		$(checkbox).appendTo("#option" + tabNum);
/*		ul_lv1 = document.createElement("ul");
		$(ul_lv1).appendTo(checkbox);*/
		url = getDatasetUrl + tabName + "dss";
		$.ajaxSettings.async = false;
		$.getJSON(url,function(data){
			len = data.length;
			for(i = 0; i < len; i++){
				des = data[i].description;
				if(tabName == "geo"){
					name = data[i].datasetName;
					type = "";
					schema = data[i].schema;
					type = "points";
					Map.loadData(tabNum,i,type);
					$("<input type = 'checkbox' id = 'checkbox" + tabNum + "_" + i + "' onclick = 'Map.clickEvent(" + tabNum + "," + i + "," + map + "," + type + ");'/>" + des).appendTo(checkbox);
				}else if(tabName == "sta"){
					$("<input type = 'checkbox' id = 'checkbox" + tabNum + "_" + i + "' onclick = 'Chart.clickEvent(" + tabNum + "," + i + ");'/>" + des).appendTo(checkbox);
				}
/*				li_lv1 = document.createElement("li");
				li_lv1.setAttribute("id","jstree" + tabNum + "_" + i);
				$(li_lv1).appendTo(ul_lv1);
				li_lv1.innerHTML = "<a href='#'>" + des + "</a>";
				ul_lv2 = document.createElement("ul");
				$(ul_lv2).appendTo(li_lv1);
				for(j = 0; j < len_sub; j++){
					des_sub = schema[i];
					li_lv2 = document.createElement("li");
					li_lv2.setAttribute("id","jstree" + tabNum + "_" + i + "_" + j);
					$(li_lv2).appendTo(ul_lv2);
					li_lv2.innerHTML = "<a href='#'>" + des_sub + "</a>";
				}*/
			}
		}).error(function(){
			alert("Oops, we got an error...");
		});
	}
}
/*
function selectAll(tabName,tabNum){
	input = document.getElementById("checkbox" + tabNum).getElementsByTagName("input");
	for(i = 0; i < input.length; i++){
		if(input[i].checked == false){
			input[i].checked = true;
			if(tabName == "geo"){
				setGeoChecked(tabName,i);
			}else{
				setStaChecked(tabName,tabNum,i);
			}
		}
	}
}

function unselectAll(tabName,tabNum){
	input = document.getElementById("checkbox" + tabNum).getElementsByTagName("input");
	for(i = 0; i < input.length; i++){
		if(input[i].checked == false){
			input[i].checked = true;
			if(tabName == "geo"){
				setGeoChecked(tabName,i);
			}else{
				setStaChecked(tabName,tabNum,i);
			}
		}else{
			input[i].checked = false;
			if(tabName == "geo"){
				setGeoUnchecked(i);
			}else{
				setStaUnchecked(tabNum,i);
			}
		}
	}
}*/