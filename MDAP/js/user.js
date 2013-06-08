User = {};

User.height = function(){return 240;};
User.width = function(){return 360;};
User.tableWidth = function(){return 320;};

/**********initialize css of register/login window**********/
User.init = function(){
	$("#user").css({
		"position": "absolute",
		"margin-top": (Common.height() - User.height()) / 2,
		"margin-right": (Common.width() - User.width()) / 2,
		"margin-bottom": (Common.height() - User.height()) / 2,
		"margin-left": (Common.width() - User.width()) / 2,
		"border": "1px solid #000000",
		"height": User.height(),
		"width": User.width(),
		"z-index": "1000",
		"background-color": "white",
		"display": "none"
	});
};

/**********load register/login window in the form of the language you set**********/
User.load = function(){
	var index = Common.language;
	
	$("#userShell").empty();
	user = document.createElement("div");
	user.setAttribute("id","user");
	$(user).appendTo("#userShell");
	ul = document.createElement("ul");
	ul.setAttribute("id","user_ul");
	$(ul).appendTo("#user");
	$("<li><a href = '#register'>" + Lan.register[index] + "</a></li><li><a href = '#login'>" + Lan.login[index] + "</a></li>").appendTo("#user_ul");
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
	$("<img src = 'css/images/close.png'/>")
		.appendTo("#user")
		.css({
			"position": "absolute",
			"top": "5px",
			"right": "5px"
		}).hover(
			function(){$(this).attr("src","css/images/close_hover.png");},
			function(){$(this).attr("src","css/images/close.png");}
		).click(
			function(){User.closeFrame();}
		);
	$("#checkbox_l").attr("checked",true);
	$("#user").tabs();
	
	User.init();
	$("#user table").attr({
		"width": User.tableWidth(),
		"style": "margin: auto"
	});
	
	if(($.cookie("username") != null) && ($.cookie("password") != null)){
		User.upperRightMenu("login","");
	}else{
		User.upperRightMenu("logout","");
	}
};

/**********open register/login window**********/
User.createFrame = function(){
	Common.background();
	User.init();
	$("#background").css("display","block");
	$("#user").css("display","block");
	$("#user").tabs("option","active",1);
};

/**********close register/login window**********/
User.closeFrame = function(){
	$("#username_r").val("");
	$("#password_r").val("");
	$("#password_r2").val("");
	$("#password_l").val("");
	$("#checkbox_l").attr("checked",true);
	$("#background").css("display","none");
	$("#user").css("display","none");
};

/**********change the upper-right block**********/
User.upperRightMenu = function(status,info){
	var index = Common.language;
	var htmlString = "";
	if(status == "login"){
		var username = Common.username;
		htmlString = "<a href = 'javascript:void(0);'>" + username + "</a>" +
			"<a href = 'javascript:void(0);' onClick = \"Common.extraMenu();\">" + Lan.create[index] +
			"<img src = 'css/images/down_arrow.png'/></a>" +
			"<a href = 'javascript:void(0);' onClick = \"Common.extendedFav();\">" + Lan.favorite[index] +
			"<img src = 'css/images/down_arrow.png'/></a>" +
			"<a href = 'javascript:void(0);' onclick = \"User.logout();\">" + Lan.logout[index] + "</a>";
		Fav.loadDownList();
	}else if(status == "logout"){
		htmlString = "<a href = 'javascript:void(0);' onclick = \"User.createFrame();\">" + Lan.register[index] + "/" + Lan.login[index] + "</a>";
		if(info == "clear"){
			$.removeCookie("username",{path: "/"});
			$.removeCookie("password",{path: "/"});
			$.removeCookie("language",{path: "/"});
			Lan.init();
		}
	}else{
		/**********need to do**********/
	}
	$("#userInfo").html(htmlString);
};

/**********register**********/
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
			User.upperRightMenu("login","");
			User.closeFrame();
		}else if(data.status == false){
			alert(Lan.nameExist[index]);
			return;
		}else{
			/**********need to do**********/
		}
	},"json").error(function(){
		alert("Oops, we got an error...");
		return;
	});
};

/**********log in**********/
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
			User.upperRightMenu("login","");
			User.closeFrame();
			$.getJSON(Common.getLanUrl(),{
				userid: username
			},function(data){
				Common.language = data;
				Common.load();
			}).error(function(){
				alert("Oops, we got an error...");
				return;
			});
		}else if(data.status == false){
			alert(Lan.nameOrCodeError[index]);
			return;
		}else{
			/**********need to do**********/
		}
	}).error(function(){
		alert("Oops, we got an error...");
		return;
	});
};

/**********log out**********/
User.logout = function(){
	$("#extraMenu").css("display","none");
	$("#extendedFav").css("display","none");
	User.upperRightMenu("logout","clear");
};

/**********reset info in the register/login window**********/
User.reset = function(status){
	if(status == "register"){
		$("#username_r").val("");
		$("#password_r").val("");
		$("#password_r2").val("");
	}else if(status == "login"){
		$("#username_l").val("");
		$("#password_l").val("");
	}else{
		/**********need to do**********/
	}
};