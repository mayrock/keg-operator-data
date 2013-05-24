User = {};

/**********initialize when loading page**********/
User.init = function(){
	user_h = 240;
	user_w = 360;
	user_tw = 320;
	$("#user").css({
		"position": "absolute",
		"margin-top": (Common.height() - user_h) / 2,
		"margin-right": (Common.width() - user_w) / 2,
		"margin-bottom": (Common.height() - user_h) / 2,
		"margin-left": (Common.width() - user_w) / 2,
		"border": "1px solid #000000",
		"height": user_h,
		"width": user_w,
		"z-index": "1000",
		"background-color": "white",
		"display": "none"
	});
	$("#user table").attr({
		"width": user_tw,
		"style": "margin: auto",
		"border": "1px"
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
};

/**********open register/login window**********/
User.createFrame = function(){
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
	if(status == "login"){
		if(info == "saved"){
			username = $.cookie("username");
		}else{
			username = info;
		}
		htmlString = "<a href = 'javascript:void(0);'>" + username + "</a>" +
			"<a href = 'javascript:void(0);' onClick = \"Common.extraMenu();\">new<img src = 'css/images/down_arrow.png'/></a>" +
			"<a href = 'javascript:void(0);' onClick = \"Tab.createFrame('fav');\">my favorite</a>" +
			"<a href = 'javascript:void(0);' onclick = \"User.logout();\">logout</a>";
	}else if(status == "logout"){
		if(info == ""){
			$.removeCookie("username",{path: "/"});
			$.removeCookie("password",{path: "/"});
			if($.cookie("fav_count") != null){
				var count = parseInt($.cookie("fav_count"));
				$.removeCookie("fav_count",{path: "/"});
				for(var i = 0; i < count; i++){
					$.removeCookie("fav_name" + i,{path: "/"});
					$.removeCookie("fav_des" + i,{path: "/"});
					$.removeCookie("fav_data" + i,{path: "/"});
				}
			}
		}
		htmlString = "<a href = 'javascript:void(0);' onclick = \"User.createFrame();\">register/login</a>";
	}else{
		/**********need to do**********/
	}
	$("#userInfo").html(htmlString);
};

/**********register**********/
User.register = function(){
	username = $("#username_r").val();
	password = $("#password_r").val();
	password_r = $("#password_r2").val();
	if(username == ""){
		alert("Username cann't be empty!");
		return;
	}
	if((password == "") || (password_r == "")){
		alert("Password cann't be empty!");
		return;
	}
	if(password != password_r){
		alert("The two passwords you input don't match!");
		return;
	}
	$.ajaxSettings.async = false;
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
	username = $("#username_l").val();
	password = $("#password_l").val();
	if(username == ""){
		alert("Username cann't be empty!");
		return;
	}
	if(password == ""){
		alert("Password cann't be empty!");
		return;
	}
	$.ajaxSettings.async = false;
	$.getJSON(Common.loginUrl() + username + "/" + password,function(data){
		if(data.status == true){
			if($("#checkbox_l").is(":checked") == true){
				$.cookie("username",username,{expires: 7,path: "/"});
				$.cookie("password",password,{expires: 7,path: "/"});
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
	User.upperRightMenu("logout","");
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