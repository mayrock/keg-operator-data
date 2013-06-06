User = {};

User.height = function(){return 240;};
User.width = function(){return 360;};
User.tableWidth = function(){return 320;};

/**********initialize #user**********/
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
}

User.load = function(){
	$("#user table").attr({
		"width": User.tableWidth(),
		"style": "margin: auto"
	});
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
	$("#user").tabs();
	$("#checkbox_l").attr("checked",true);
	
	if(($.cookie("username") != null) && ($.cookie("password") != null) && ($.cookie("favData") != null)){
		User.upperRightMenu("login","saved");
		Fav.downList();
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

/**********close window**********/
User.closeFrame = function(){
	$("#username_r").val("");
	$("#password_r").val("");
	$("#password_r2").val("");
	$("#password_l").val("");
	$("#checkbox_l").attr("checked",true);
	$("#background").css("display","none");
	$("#user").css("display","none");
};

/**********change info in the upper-right page**********/
User.upperRightMenu = function(status,info){
	var index = $.cookie("language");
	var htmlString = "";
	if(status == "login"){
		if(info == "saved"){
			username = $.cookie("username");
		}else{
			username = info;
		}
		htmlString = "<a href = 'javascript:void(0);'>" + username + "</a>" +
			"<a href = 'javascript:void(0);' onClick = \"Common.extraMenu();\">" + Lan.create[index] +
			"<img src = 'css/images/down_arrow.png'/></a>" +
			"<a href = 'javascript:void(0);' onClick = \"Common.extendedFav();\">" + Lan.favorite[index] +
			"<img src = 'css/images/down_arrow.png'/></a>" +
			"<a href = 'javascript:void(0);' onclick = \"User.logout();\">" + Lan.logout[index] + "</a>";
	}else if(status == "logout"){
		htmlString = "<a href = 'javascript:void(0);' onclick = \"User.createFrame();\">" + Lan.register[index] + "/" + Lan.login[index] + "</a>";
		if(info == "clear"){
			$.removeCookie("username",{path: "/"});
			$.removeCookie("password",{path: "/"});
			$.removeCookie("favData",{path: "/"});
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
	var index = $.cookie("language");
	username = $("#username_r").val();
	password = $("#password_r").val();
	password_r = $("#password_r2").val();
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
	$.post(Common.registerUrl(),{
		username: username,
		password: password
	},function(data){
		if(data.status == true){
			$.cookie("username",username,{expires: 7,path: "/"});
			$.cookie("password",password,{expires: 7,path: "/"});
			User.upperRightMenu("login","saved");
			User.closeFrame();
		}else if(data.status == false){
			alert("This username has been registered!");
			return;
		}else{
			/**********need to do**********/
		}
	}).error(function(){
		alert("Oops, we got an error...");
		return;
	});
};

/**********log in**********/
User.login = function(){
	var index = $.cookie("language");
	username = $("#username_l").val();
	password = $("#password_l").val();
	if(username == ""){
		alert(Lan.emptyName[index]);
		return;
	}
	if(password == ""){
		alert(Lan.emptyCode[index]);
		return;
	}
	$.ajaxSettings.async = false;
	$.getJSON(Common.loginUrl().replace("username",username).replace("password",password),function(data){
		if(data.status == true){
			if($("#checkbox_l").is(":checked") == true){
				$.cookie("username",username,{expires: 7,path: "/"});
				$.cookie("password",password,{expires: 7,path: "/"});
				$.cookie("favData",JSON.stringify("[]"),{expires: 7,path: "/"});
				User.upperRightMenu("login","saved");
				User.closeFrame();
			}else if($("#checkbox_l").is(":checked") == false){
				User.upperRightMenu("login",username);
				User.closeFrame();
			}else{
				/**********need to do**********/
			}
		}else if(data.status == false){
			alert("Username or password error!");
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
	User.closeFrame();
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