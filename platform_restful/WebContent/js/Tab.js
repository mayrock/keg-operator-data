tabNum = 0;
getDatasetUrl = "http://10.1.1.55:8081/mdap/rest/dsg/get";

function addNewTab(tabName){
	limit = 7;
	if(tabNum == limit){
		alert("Tabs cann't more than " + limit + "!");
		return;
	}
	content = document.getElementById("content");
	for(i = 0; i < tabNum; i++){
		tab = document.getElementById("tab" + i);
		tab.setAttribute("style","display: none");
	}
	if((tabName == "geo") || (tabName == "sta")){
		ul = document.getElementById("tab");
		l = ul.childNodes.length;
		$("ul").find("a").removeClass();
		li = document.createElement("li");
		if(tabName == "geo"){
			li.innerHTML = "<a href = 'javascript:void(0);' class = 'here' onclick = 'changeTab(" + tabNum + ",this)'>Geographic data</a>";
		}
		else{
			li.innerHTML = "<a href = 'javascript:void(0);' class = 'here' onclick = 'changeTab(" + tabNum + ",this)'>Statistical data</a>";
		}
		$("<img src = 'image/close.png' alt = 'close icon'/>")
			.appendTo(li)
			.hover(function(){
				var img = $(this);
				img.attr('src','image/close_hover.png');
			},function(){
				var img = $(this);
				img.attr('src','image/close.png');
			})
			.click(closeTab);
		ul.insertBefore(li,ul.childNodes[l]);
		div = document.createElement("div");
		div.setAttribute("id","tab" + tabNum);
		tabOption = document.createElement("div");
		tabContent = document.createElement("div");
		tabOption.setAttribute("class","option");
		tabContent.setAttribute("id","content" + tabNum);
		tabContent.setAttribute("class","content");
		div.appendChild(tabOption);
		div.appendChild(tabContent);
		content.appendChild(div);
		setTabOption(tabName,tabOption,tabNum);
		setTabContent(tabName,tabContent);
		tabNum ++;
	}
	else{
		alert("Oops, we got an error...");
	}
}

function setTabOption(tabName,option,num){
	option.innerHTML = "";
	url = getDatasetUrl + tabName + "dss";
	$.getJSON(url,function(data){
		for(i = 0; i < data.length; i++){
			des = data[i].description;
			name = data[i].datasetName;
			option.innerHTML += "<input type = 'checkbox' onClick = \"" + tabName + "Information('" + name + "'," + num + ");\">" + des + "<br/>";
		}
	}).error(function(){
		alert("Oops, we got an error...");
	});
	select = document.createElement("div");
	select.setAttribute("class","select");
	select.innerHTML = "<input type = 'button' value = 'selectAll' onClick = \"selectAll();\">" +
		"<input type = 'button' value = 'unselectAll' onClick = \"unselectAll();\">";
	option.appendChild(select);
}

function setTabContent(tabName,content){
	if(tabName == "geo"){
		mapInitialize(tabNum);
	}
}

function changeTab(num,tab){
	if($(tab).hasClass("here")){
		return;
	}
	$(tab).addClass("here");
	$(tab).parent().siblings().children("a").removeClass();
	ul = document.getElementById("tab");
	for(i = 0; i < tabNum; i++){
		tab = document.getElementById("tab" + i);
		tab.setAttribute("style","display: none");
	}
	tab = document.getElementById("tab" + num);
	tab.setAttribute("style","display: block");
}

function closeTab(){
}

function selectAll(){
}

function unselectAll(){
}