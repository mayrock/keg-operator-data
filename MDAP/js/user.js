User = {};

/*****load register/login window*****/
User.load = function(){
	var index = Common.language;
	
	$("#userShell").empty();
	user = document.createElement("div");
	user.setAttribute("id","user");
	$(user).appendTo("#userShell");
	user_ul = document.createElement("ul");
	user_ul.setAttribute("id","user_ul");
	$(user_ul).appendTo("#user");
	$("<li><a href = '#register'>" + Lan.register[index] + "</a></li><li><a href = '#login'>" + Lan.login[index] + "</a></li>").appendTo(user_ul);
	register = document.createElement("div");
	register.setAttribute("id","register");
	$(register).appendTo("#user");
	$("<table><tr><td>" + Lan.inputName[index] + "</td><td><input type = 'text' id = 'username_r'/></td></tr>" +
		"<tr><td>" + Lan.inputCode[index] + "</td><td><input type = 'password' id = 'password_r'/></td></tr>" +
		"<tr><td>" + Lan.reInputCode[index] + "</td><td><input type = 'password' id = 'password_r2'/></td></tr></table>" +
		"<table><tr><td><input type = 'button' value = '" + Lan.register[index] + "' onclick = \"User.register();\"/></td>" +
		"<td><input type = 'button' value = '" + Lan.reset[index] + "' onclick = \"User.reset('register');\"/></td></tr></table>")
		.appendTo("#register");
	login = document.createElement("div");
	login.setAttribute("id","login");
	$(login).appendTo("#user");
	$("<table><tr><td>" + Lan.inputName[index] + "</td><td><input type = 'text' id = 'username_l'/></td></tr>" +
		"<tr><td>" + Lan.inputCode[index] + "</td><td><input type = 'password' id = 'password_l'/></td></tr></table>" +
		"<input type = 'checkbox' id = 'checkbox_l'/>" + Lan.saveInfo[index] +
		"<table><tr><td><input type = 'button' value = '" + Lan.login[index] + "' onclick = \"User.login();\"/></td>" +
		"<td><input type = 'button' value = '" + Lan.reset[index] + "' onclick = \"User.reset('login');\"/></td></tr></table>")
		.appendTo("#login");
	$("<img src = 'css/images/close_256x256.png' onclick = \"User.closeFrame();\"/>")
		.appendTo("#user")
		.css({
			"position": "absolute",
			"top": "5px",
			"right": "5px",
			"width": "16px"
		});
	$("#user").tabs({
		activate: function(event,ui){
			var active = $("#user").tabs("option","active");
			if(active == 0){
				User.reset("register");
			}else{
				User.reset("login");
			}
		}
	});
	
	$("#user table").attr({
		"width": Common.uTableWidth(),
		"style": "margin: auto"
	});
	$("#checkbox_l").prop("checked",true);
	
	if(Common.username == ""){
		User.infoMenu("logout","");
	}else{
		User.infoMenu("login","");
	}
};

/*****open window*****/
User.createFrame = function(){
	Common.background();
	Common.user();
	$("#background").css("display","block");
	$("#user").css("display","block");
	$("#user").tabs("option","active",1);
};

/*****close window*****/
User.closeFrame = function(){
	$("#background").css("display","none");
	$("#user").css("display","none");
};

/*****change #userInfo*****/
User.infoMenu = function(status,info){
	var index = Common.language;
	var htmlString = "";
	if(status == "login"){
		var username = Common.username;
		var permit = Common.permit;
		if(permit == 1){
			htmlString = "<a href = 'javascript:void(0);'>" + username + "</a>" +
				"<a href = 'javascript:void(0);' onClick = \"Common.extraMenu();\">" + Lan.create[index] +
				"<img src = 'css/images/down_arrow_64x64.png' style = 'width: 10px;margin-left: 2px'/></a>" +
				"<a href = 'javascript:void(0);' onClick = \"Common.extendedFav();\">" + Lan.favorite[index] +
				"<img src = 'css/images/down_arrow_64x64.png' style = 'width: 10px;margin-left: 2px'/></a>" +
				"<a href = 'javascript:void(0);' onclick = \"User.logout();\">" + Lan.logout[index] + "</a>";
		}else{
			htmlString = "<a href = 'javascript:void(0);'>" + username + "</a>" +
				"<a href = 'javascript:void(0);' onClick = \"Tab.createFrame('manage');\">" + Lan.manage[index] + "</a>" +
				"<a href = 'javascript:void(0);' onClick = \"Common.extraMenu();\">" + Lan.create[index] +
				"<img src = 'css/images/down_arrow_64x64.png' style = 'width: 10px;margin-left: 2px'/></a>" +
				"<a href = 'javascript:void(0);' onClick = \"Common.extendedFav();\">" + Lan.favorite[index] +
				"<img src = 'css/images/down_arrow_64x64.png' style = 'width: 10px;margin-left: 2px'/></a>" +
				"<a href = 'javascript:void(0);' onclick = \"User.logout();\">" + Lan.logout[index] + "</a>";
		}
		$("#userInfo").html(htmlString);
		Fav.loadDownList();
	}else{
		if(info == "clear"){
			$.removeCookie("username",{path: "/"});
			$.removeCookie("password",{path: "/"});
			Common.username = "";
			Common.permit = 0;
			$("#tabsShell").empty();
			tabs = document.createElement("div");
			tabs.setAttribute("id","tabs");
			$(tabs).appendTo("#tabsShell");
			tabs_ul = document.createElement("ul");
			tabs_ul.setAttribute("id","tabs_ul");
			$(tabs_ul).appendTo(tabs);
			Common.tabIndex = new Array();
			Common.mapArr = new Array();
			Common.mapInfoArr = new Array();
			Common.chartIndex = new Array();
			Common.chartType = new Array();
			Common.yAxis = new Array();
			Lan.init();
		}else{
			htmlString = "<a href = 'javascript:void(0);' onclick = \"User.createFrame();\">" + Lan.register[index] + "/" + Lan.login[index] + "</a>|";
			$("#userInfo").html(htmlString);
		}
	}
};

/*****register*****/
User.register = function(){
	var index = Common.language;
	var username = $("#username_r").val();
	var password = $("#password_r").val();
	var password_r = $("#password_r2").val();
	if(username == ""){
		alert(Lan.emptyName[index]);
		return;
	}
	if((password == "") || (password_r == "")){
		alert(Lan.emptyCode[index]);
		return;
	}
	if(password != password_r){
		alert(Lan.notSameCode[index]);
		return;
	}
	var language = Common.language;
	$.post(Common.registerUrl(),{
		userid: username,
		password: password,
		language: language
	},function(data){
		if(data.status == true){
			$.cookie("username",username,{expires: 7,path: "/"});
			$.cookie("password",password,{expires: 7,path: "/"});
			Common.username = username;
			User.closeFrame();
			User.infoMenu("login","");
		}else{
			alert(Lan.nameExist[index]);
			return;
		}
	},"json").error(function(){
		alert("Oops, we got an error...");
		return;
	});
};

/*****log in*****/
User.login = function(){
	var index = Common.language;
	var username = $("#username_l").val();
	var password = $("#password_l").val();
	if(username == ""){
		alert(Lan.emptyName[index]);
		return;
	}
	if(password == ""){
		alert(Lan.emptyCode[index]);
		return;
	}
	$.getJSON(Common.loginUrl(),{
		userid: username,
		password: password
	},function(data){
		if(data.status == true){
			if($("#checkbox_l").is(":checked") == true){
				$.cookie("username",username,{expires: 7,path: "/"});
				$.cookie("password",password,{expires: 7,path: "/"});
			}
			Common.username = username;
			Common.language = data.user.language;
			Common.permit = data.user.permission;
			User.closeFrame();
			Common.load();
		}else{
			alert(Lan.nameOrCodeError[index]);
			return;
		}
	}).error(function(){
		alert("Oops, we got an error...");
		return;
	});
};

/*****log out*****/
User.logout = function(){
	$("#extraMenu").css("display","none");
	$("#extendedFav").css("display","none");
	User.infoMenu("logout","clear");
};

/*****reset information*****/
User.reset = function(status){
	if(status == "register"){
		$("#username_r").val("");
		$("#password_r").val("");
		$("#password_r2").val("");
	}else if(status == "login"){
		$("#username_l").val("");
		$("#password_l").val("");
		$("#checkbox_l").prop("checked",true);
	}
};