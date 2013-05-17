IP = "http://10.1.1.55:8088/platform_restful/rest/";

Account = {};

Account.registerUrl = IP + "up/adduser";

Account.loginUrl = IP + "ug/verifyuser/";

Account.createFrame = function(){
	$("#account_bg").css("display","block");
	$("#account").css("display","block");
	$("#account").tabs("option","active",1);
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

Account.upperRightMenu = function(status,info){
	if(status == "login"){
		if(info = "saved"){
			username = $.cookie("username");
		}else{
			username = info;
		}
		htmlString = "<a href = 'javascript:void(0);'>" + username + "</a>" +
			"<a href = 'javascript:void(0);'>new tab<input id = 'advanced' type = 'button' onclick = 'Common.advanced();'/></a>" +
			"<a href = 'javascript:void(0);'>my favorite</a>" +
			"<a href = 'javascript:void(0);'>my history</a>" +
			"<a href = 'javascript:void(0);' onclick = 'Account.logout();'>logout</a>";
	}else if(status == "logout"){
		if(info != "init"){
			$.removeCookie("username",{path: "/"});
			$.removeCookie("password",{path: "/"});
		}
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
	$.post(Account.registerUrl,{
		username: username,
		password: password
	},function(data){
		if(data.status == true){
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
	Account.loginUrl += username + "/" + password;
	$.getJSON(Account.loginUrl,function(data){
		if(data.status == true){
			if($("#checkbox_l").is(":checked") == true){
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
	}).error(function(){
		alert("Oops, we got an error...");
	});
};

Account.logout = function(){
	$("#extended").css("display","none");
	Account.upperRightMenu("logout","");
	Account.closeFrame();
};

Account.reset = function(status){
	if(status == "register"){
		$("#username_r").val("");
		$("#password_r").val("");
		$("#password_r2").val("");
	}else if(status == "login"){
		$("#username_l").val("");
		$("#password_l").val("");
	}else{
		alert("Oops, we got an error...");
	}
};