tabNum = 0;
tabCount = 0;
limit = 7;
getDatasetUrl = "http://10.1.1.55:8088/platform_restful/rest/dsg/get";

function addNewTab(tabName){
	if(!((tabName == "geo") || (tabName == "sta"))){
		alert("Oops, we got an error...");
		return;
	}
	$("#extendedTab").css("display","none");
	//限制标签页个数
	if(tabCount == limit){
		alert("Tabs cann't be more than " + limit + "!");
		return;
	}
	//添加一个标签页
	main = $("#main").tabs();
	ul = main.find("ul");
	li = document.createElement("li");
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
				contentName = $(this).parent().children("a").attr("href");
				$(contentName).remove();
				tabCount --;
				main.tabs("refresh");
			}
		);
	$(li).appendTo(ul);
	content = document.createElement("div");
	content.setAttribute("id","tab" + tabNum);
	option = document.createElement("div");
	view = document.createElement("div");
	option.setAttribute("class","option");
	view.setAttribute("id","view" + tabNum);
	view.setAttribute("class","view");
	$(option).appendTo(content);
	$(view).appendTo(content);
	$(content).appendTo(main);
	main.tabs("refresh");
	main.tabs("option","active",tabCount);
	//设置标签页内容
	setOption(tabName,option);
	tabNum ++;
	tabCount ++;
}

function setOption(tabName,option){
	checkbox = document.createElement("div");
	checkbox.setAttribute("id","checkbox" + tabNum);
	url = getDatasetUrl + tabName + "dss";
	$.ajaxSettings.async = false;
	$.getJSON(url,function(data){
		len = data.length;
		if(tabName == "geo"){
			mapInitialize(tabNum,len);
		}
		for(i = 0; i < len; i++){
			des = data[i].description;
			checkbox.innerHTML += "<input type = 'checkbox' onClick = \"" + tabName + "Information('" + tabName + "'," + tabNum + "," + i + ");\"/>" + des + "<br/>";
		}
	}).error(function(){
		alert("Oops, we got an error...");
	});
	select = document.createElement("div");
	select.setAttribute("class","select");
	select.innerHTML = "<input type = 'button' value = 'selectAll' onClick = \"selectAll('" + tabName + "'," + tabNum + ");\"/>" +
		"<input type = 'button' value = 'unselectAll' onClick = \"unselectAll('" + tabName + "'," + tabNum + ");\"/>";
	$(checkbox).appendTo(option);
	$(select).appendTo(option);
}

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
}