$(document).ready(function(){
	Common.loading();
	Common.staInit();
	Common.favInit();
	Common.bgInit();
	$("#main").css({
		"position": "absolute",
		"height": Common.height(),
		"width": Common.width()
	});
	$("#tabs").tabs();
});

Common = {};

Common.height = function(){return $(document).height();};
Common.width = function(){return $(document).width();};

/**********如何识别浏览器的语言需要好好考虑,现在使用的是en-US和zh-CN**********/
Common.language = function(){
	var language = window.navigator.language;
	if($.cookie("language") == null){
		$.cookie("language",language,{expires: 7,path: "/"});
	}else{
		language = $.cookie("language");
	}
	return language;
};

Common.switchLan = function(language){
	$.cookie("language",language,{expires: 7,path: "/"});
	Common.loading();
}

/**********应该把需要切换语言的文字作为变量**********/
Common.loading = function(){
	if(Common.language() == "en-US"){
		$("#userShell").empty();
		user = document.createElement("div");
		user.setAttribute("id","user");
		$(user).appendTo("#userShell");
		ul = document.createElement("ul");
		ul.setAttribute("id","user_ul");
		$(ul).appendTo("#user");
		$("<li><a href = '#register'>Register</a></li><li><a href = '#login'>Login</a></li>").appendTo("#user_ul");
		register = document.createElement("div");
		register.setAttribute("id","register");
		$(register).appendTo("#user");
		$("<table><tr><td>Input Username:</td><td><input type = 'text' id = 'username_r'/></td></tr><tr><td>Input Password:</td>" +
			"<td><input type = 'password' id = 'password_r'/></td></tr><tr><td>Password Again:</td>" +
			"<td><input type = 'password' id = 'password_r2'/></td></tr></table>" +
			"<table><tr><td><input type = 'button' value = 'register' onclick = \"User.register();\"/></td>" +
			"<td><input type = 'button' value = 'reset' onclick = \"User.reset('register');\"/></td></tr></table>").appendTo("#register");
		login = document.createElement("div");
		login.setAttribute("id","login");
		$(login).appendTo("#user");
		$("<table><tr><td>Input Username:</td><td><input type = 'text' id = 'username_l'/></td></tr><tr><td>Input Password:</td>" +
			"<td><input type = 'password' id = 'password_l'/></td></tr></table>" +
			"<input type = 'checkbox' id = 'checkbox_l'/>Save username and password in 7 days" +
			"<table><tr><td><input type = 'button' value = 'login' onclick = \"User.login();\"/></td>" +
			"<td><input type = 'button' value = 'reset' onclick = \"User.reset('login');\"/></td></tr></table>").appendTo("#login");
		Common.userInit();
		User.init();
		$("#lanOption").empty();
		$("<span>language:<span><input name = 'language' type = 'radio' value = 'En' Onclick = \"Common.switchLan('en-US');\" checked/>English" +
			"<input name = 'language' type = 'radio' value = 'Chs' Onclick = \"Common.switchLan('zh-CN');\"/>Chinese Simplified<br/>").appendTo("#lanOption");
		Common.userLoading();
		$("#extraMenu").empty();
		$("<a href = 'javascript:void(0);' onclick = \"Tab.createFrame('geo');\">geo data</a><br/>" +
			"<a href = 'javascript:void(0);' onclick = \"Tab.createFrame('sta');\">sta data</a>").appendTo("#extraMenu");
		$("#header").empty();
		$("<span class = 'title'>Mobile Data Analysis Platform</span>").appendTo("#header");
	}else if(Common.language() == "zh-CN"){
		$("#userShell").empty();
		user = document.createElement("div");
		user.setAttribute("id","user");
		$(user).appendTo("#userShell");
		ul = document.createElement("ul");
		ul.setAttribute("id","user_ul");
		$(ul).appendTo("#user");
		$("<li><a href = '#register'>注册</a></li><li><a href = '#login'>登录</a></li>").appendTo("#user_ul");
		register = document.createElement("div");
		register.setAttribute("id","register");
		$(register).appendTo("#user");
		$("<table><tr><td>请输入用户名:</td><td><input type = 'text' id = 'username_r'/></td></tr><tr><td>请输入密码:</td>" +
			"<td><input type = 'password' id = 'password_r'/></td></tr><tr><td>请再次输入密码:</td>" +
			"<td><input type = 'password' id = 'password_r2'/></td></tr></table>" +
			"<table><tr><td><input type = 'button' value = '注册' onclick = \"User.register();\"/></td>" +
			"<td><input type = 'button' value = '重置' onclick = \"User.reset('register');\"/></td></tr></table>").appendTo("#register");
		login = document.createElement("div");
		login.setAttribute("id","login");
		$(login).appendTo("#user");
		$("<table><tr><td>请输入用户名:</td><td><input type = 'text' id = 'username_l'/></td></tr><tr><td>请输入密码:</td>" +
			"<td><input type = 'password' id = 'password_l'/></td></tr></table>" +
			"<input type = 'checkbox' id = 'checkbox_l'/>保存用户名和密码7天" +
			"<table><tr><td><input type = 'button' value = '登录' onclick = \"User.login();\"/></td>" +
			"<td><input type = 'button' value = '重置' onclick = \"User.reset('login');\"/></td></tr></table>").appendTo("#login");
		Common.userInit();
		User.init();
		$("#lanOption").empty();
		$("<span>语言:<span><input name = 'language' type = 'radio' value = 'En' Onclick = \"Common.switchLan('en-US');\"/>英语" +
			"<input name = 'language' type = 'radio' value = 'Chs' Onclick = \"Common.switchLan('zh-CN');\" checked/>简体中文<br/>").appendTo("#lanOption");
		Common.userLoading();
		$("#extraMenu").empty();
		$("<a href = 'javascript:void(0);' onclick = \"Tab.createFrame('geo');\">地理数据</a><br/>" +
			"<a href = 'javascript:void(0);' onclick = \"Tab.createFrame('sta');\">统计数据</a>").appendTo("#extraMenu");
		$("#header").empty();
		$("<span class = 'title'>移动数据分析平台</span>").appendTo("#header");
	}else{
		/**********need to do**********/
	}
};

Common.userLoading = function(){
	if(($.cookie("username") != null) && ($.cookie("password") != null) && ($.cookie("favData") != null)){
		User.upperRightMenu("login","saved");
		Fav.downList();
	}else{
		User.upperRightMenu("logout","init");
	}
};

Common.func = function(){
	/**********aplha**********/
	return 0;
	/**********release**********/
//	return 1;
};

Common.ip = function(){
	if(Common.func() == 0){
		return "http://10.1.1.55:8080/platform_restful/rest/";
	}else if(Common.func() == 1){
		return "http://10.1.1.55:8080/platform_restful2/rest/";
	}else{
		/**********need to do**********/
	}
};

Common.registerUrl = function(){return Common.ip() + "up/adduser?jsoncallback=?";};
Common.loginUrl = function(){return Common.ip() + "ug/verifyuser/username/password?jsoncallback=?";};
Common.datasetUrl = function(){return Common.ip() + "dsg/gettabTypedvs?jsoncallback=?"};
Common.dsDataUrl = function(){return Common.ip() + "dsg/gettabTypedv/dsName?jsoncallback=?";};

Common.tabCount = 0;
Common.tabLimit = function(){return 7;};
Common.tabIndex = 0;

Common.mapArr = new Array();
Common.mapInfoArr = new Array();

Common.chartIndex = new Array();
Common.chartType = new Array();
Common.yAxis = new Array();

/**********initialize #user**********/
Common.userInit = function(){
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

/**********initialize # related to sta**********/
Common.staInit = function(){
	$("#largeChart").css({
		"position": "absolute",
		"margin-top": (Common.height() - Sta.lcHeight()) / 2,
		"margin-right": (Common.width() - Sta.lcWidth()) / 2,
		"margin-bottom": (Common.height() - Sta.lcHeight()) / 2,
		"margin-left": (Common.width() - Sta.lcWidth()) / 2,
		"border": "1px solid #000000",
		"height": Sta.lcHeight(),
		"width": Sta.lcWidth(),
		"z-index": "1000",
		"background-color": "white",
		"display": "none"
	});
	$("#lcContainer").css({
		"position": "absolute",
		"top": 0,
		"left": 0,
		"height": Sta.lcHeight(),
		"width": Sta.lcCntrWidth()
	});
	$("#lcCheckbox").css({
		"position": "absolute",
		"right": 0,
		"bottom": 0,
		"height": Sta.lcHeight() - 40,
		"width": Sta.lcWidth() - Sta.lcCntrWidth()
	});
};

/**********initialize #favInfo**********/
Common.favInit = function(){
	$("#favInfo").css({
		"position": "absolute",
		"margin-top": (Common.height() - Fav.height()) / 2,
		"margin-right": (Common.width() - Fav.weight()) / 2,
		"margin-bottom": (Common.height() - Fav.height()) / 2,
		"margin-left": (Common.width() - Fav.weight()) / 2,
		"border": "1px solid #000000",
		"height": Fav.height(),
		"width": Fav.weight(),
		"z-index": "1000",
		"background-color": "white",
		"display": "none"
	});
};

/**********initialize #background**********/
Common.bgInit = function(){
	$("#background").css({
		"position": "absolute",
		"height": Common.height(),
		"width": Common.width(),
		"filter": "alpha(opacity = 60)",
		"opacity": "0.6",
		"z-index": "999",
		"background-color": "gray",
		"display": "none"
	});
};

Common.extraMenu = function(){
	if($("#extraMenu").css("display") == "none"){
		$("#extraMenu").css("display","block");
	}else{
		$("#extraMenu").css("display","none");
	}
};

Common.extendedFav = function(){
	if($("#extendedFav").css("display") == "none"){
		$("#extendedFav").css("display","block");
	}else{
		$("#extendedFav").css("display","none");
	}
};