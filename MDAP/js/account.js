Account = {};

Account.createFrame = function(){
	$("#account_bg").css("display","block");
	$("#account").css("display","block");
}

Account.closeFrame = function(){
	$("#username_r").val("");
	$("#password_r").val("");
	$("#password_r2").val("");
	$("#password_l").val("");
	$("#checkbox_a").attr("checked",true);
	$("#account_bg").css("display","none");
	$("#account").css("display","none");
}

Account.upperRightMenu = function(logStatus,infoStatus){
	if(logStatus == "login"){
		if(infoStatus = "saved"){
			username = $.cookie("username");
		}else{
			username = infoStatus;
		}
		htmlString = "<a href = 'javascript:void(0);'>" + username + "</a>" +
			"<a href = 'javascript:void(0);'>new tab<input id = 'advanced' type = 'button' onclick = 'Common.advanced();'/></a>" +
			"<a href = 'javascript:void(0);'>my favorite</a>" +
			"<a href = 'javascript:void(0);'>my history</a>" +
			"<a href = 'javascript:void(0);' onclick = 'Account.logout();'>logout</a>";
	}else if(logStatus == "logout"){
		$.removeCookie('username',{path: '/'});
		$.removeCookie('password',{path: '/'});
		htmlString = "<a href = 'javascript:void(0);' onclick = 'Account.createFrame();'>register/login</a>";
	}else{
		alert("Oops, we got an error...");
		return;
	}
	$("#userInfo").html(htmlString);
}

Account.register = function(){
	username = $("#username_r").val();
	password = $("#password_r").val();
	password_c = $("#password_r2").val();
	if(username == ""){
		alert("Username cann't be empty!");
		return;
	}
	if((password == "") || (password_c == "")){
		alert("Password cann't be empty!");
		return;
	}
	if(password != password_c){
		alert("The two passwords you input don't match!");
		return;
	}
	$.post(url,{/**********to do**********/
		username: username,
		password: password
	},function(data){
		if(){/**********to do**********/
			$.cookie("username",username,{expires: 7,path: "/"});
			$.cookie("password",password,{expires: 7,path: "/"});
			Account.upperRightMenu("login","saved");
			Account.closeFrame();
		}else{
			alert("This username has been registered!");
			return;
		}
	});
};

Account.login = function(){
	username = $("#username_l").val();
	password = $("#password_l").val();
	if(Account.username == ""){
		alert("Username cann't be empty!");
		return;
	}
	if(Account.password == ""){
		alert("Password cann't be empty!");
		return;
	}
	$.post(url,{/**********to do**********/
		username: username,
		password: password
	},function(data){
		if(){/**********to do**********/
			if($("#checkbox_a").attr("checked") == true){
				$.cookie("username",username,{expires: 7,path: "/"});
				$.cookie("password",password,{expires: 7,path: "/"});
				Account.upperRightMenu("login","saved");
				Account.closeFrame();
			}else{
				Account.upperRightMenu("login",username);
				Account.closeFrame();
			}
		}else{
			alert("Username or password error!");
			return;
		}
	});
};

Account.logout = function(){
	Account.upperRightMenu("logout","");
	Account.closeFrame();
};