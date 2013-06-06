$(document).ready(function(){
	Lan.init();
	Common.load();
	$("#main").css({
		"position": "absolute",
		"height": Common.height(),
		"width": Common.width()
	});
	Common.footer();
	Common.tabIndex[0] = 0;
	$("#tabs").tabs();
});

Common = {};

Common.height = function(){return $(document).height();};
Common.width = function(){return $(document).width();};

Common.func = function(){
	/**********aplha**********/
	return 0;
	/**********release**********/
//	return 1;
};

Common.ip = function(){
	if(Common.func() == 0){
		return "http://10.1.1.55:8080/keg2/rest/";
	}else if(Common.func() == 1){
		return "http://10.1.1.55:8080/keg/rest/";
	}else{
		/**********need to do**********/
	}
};

Common.registerUrl = function(){return Common.ip() + "up/adduser?jsoncallback=?";};
Common.loginUrl = function(){return Common.ip() + "ug/login/username/password?jsoncallback=?";};
Common.datasetUrl = function(){return Common.ip() + "dsg/gettabTypedvs?jsoncallback=?"};
Common.dsDataUrl = function(){return Common.ip() + "dsg/gettabTypedv/dsName?jsoncallback=?";};

Common.tabIndex = new Array();
Common.tabLimit = function(){return 7;};

Common.mapArr = new Array();
Common.mapInfoArr = new Array();

Common.chartIndex = new Array();
Common.chartType = new Array();
Common.yAxis = new Array();

Common.footer = function(){
	$("#footer").css({
		"top": Common.height()
	});
};

Common.clearFooter = function(){
	$("#footer").css({
		"top": 0
	});
};

/**********initialize #background**********/
Common.background = function(){
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

Common.load = function(index){
	var index = $.cookie("language");
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
	User.init();
	User.load();
	
	if(index == 0){
		$("#extraMenu").css({
			"right": "120px"
		});
		$("#extendedFav").css({
			"right": "20px",
			"width": "150px"
		});
	}else if(index == 1){
		$("#extraMenu").css({
			"right": "100px"
		});
		$("#extendedFav").css({
			"right": "5px",
			"width": "150px"
		});
	}else{
		/**********need to do**********/
	}
	
	$("#lanOption").empty();
	$("<img src = 'css/images/American.png'/>")
		.appendTo("#lanOption")
		.click(
			function(){
				Lan.change(index,0);
			}
		);
	$("<img src = 'css/images/China.png'/>")
		.appendTo("#lanOption")
		.css({
			"margin-left": "10px"
		}).click(
			function(){
				Lan.change(index,1);
			}
		);
	
	$("#extraMenu").empty();
	$("<a href = 'javascript:void(0);' onclick = \"Tab.createFrame('geo');\">" + Lan.geo[index] + "</a><br/>" +
		"<a href = 'javascript:void(0);' onclick = \"Tab.createFrame('sta');\">" + Lan.sta[index] + "</a>")
		.appendTo("#extraMenu");
	
	$("#header").empty();
	$("<span class = 'title'>" + Lan.title[index] + "</span>").appendTo("#header");
};

Common.extraMenu = function(){
	if($("#extendedFav").css("display") == "block"){
		$("#extendedFav").css("display","none");
	}
	if($("#extraMenu").css("display") == "none"){
		$("#extraMenu").css("display","block");
	}else{
		$("#extraMenu").css("display","none");
	}
};

Common.extendedFav = function(){
	if($("#extraMenu").css("display") == "block"){
		$("#extraMenu").css("display","none");
	}
	if($("#extendedFav").css("display") == "none"){
		$("#extendedFav").css("display","block");
	}else{
		$("#extendedFav").css("display","none");
	}
};