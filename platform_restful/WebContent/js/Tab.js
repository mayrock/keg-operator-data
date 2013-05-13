tabNum = 0;
tabCount = 0;
limit = 7;
getDatasetUrl = "http://10.1.1.55:8081/mdap/rest/dsg/get";

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
	setView(tabName);
	tabNum ++;
	tabCount ++;
}

function setOption(tabName,option){
//	url = getDatasetUrl + tabName + "dss?jsoncallback=?";
	url = getDatasetUrl + tabName + "dss";
	checkbox = document.createElement("div");
	checkbox.setAttribute("id","checkbox" + tabNum);
	$.getJSON(url,function(data){
		for(i = 0; i < data.length; i++){
			des = data[i].description;
			name = data[i].datasetName;
			if(tabName == "geo"){
				checkbox.innerHTML += "<input type = 'checkbox' onClick = \"" + tabName + "Information('" + name + "');\">" + des + "<br/>";
			}else{
				checkbox.innerHTML += "<input type = 'checkbox' onClick = \"" + tabName + "Information('" + name + "'," + tabNum + ");\">" + des + "<br/>";
			}
		}
	}).error(function(){
		alert("Oops, we got an error...");
	});
	select = document.createElement("div");
	select.setAttribute("class","select");
	select.innerHTML = "<input type = 'button' value = 'selectAll' onClick = \"selectAll(" + tabNum + ");\">" +
		"<input type = 'button' value = 'unselectAll' onClick = \"unselectAll(" + tabNum + ");\">";
	$(checkbox).appendTo(option);
	$(select).appendTo(option);
}

function setView(tabName){
	if(tabName == "geo"){
		mapInitialize(tabNum);
	}
}

function selectAll(num){
	input = document.getElementById("checkbox" + num).getElementsByTagName("input");
	for(i = 0; i < input.length; i++){
		if(input[i].checked == false){
			input[i].checked = true;
		}
	}
}

function unselectAll(num){
	input = document.getElementById("checkbox" + num).getElementsByTagName("input");
	for(i = 0; i < input.length; i++){
		if(input[i].checked == false){
			input[i].checked = true;
		}else{
			input[i].checked = false;
		}
	}
}