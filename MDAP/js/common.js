$(document).ready(function(){
	User.init();
	Common.staInit();
	Common.favInit();
	Common.bgInit();
	$("#main").css({
		"position": "absolute",
		"height": Common.height(),
		"width": Common.width()
	});
	$("#tabs").tabs();
	Common.loading();
});

Common = {};

Common.height = function(){return $(document).height();};
Common.width = function(){return $(document).width();};

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

Common.loading = function(){
	if(Common.language() == "en"){
		$("#lanOption").empty();
		$("<span>language:<span><input name = 'language' type = 'radio' value = 'En' Onclick = \"Common.switchLan('en');\" checked/>English" +
			"<input name = 'language' type = 'radio' value = 'Chs' Onclick = \"Common.switchLan('zh-CN');\"/>Chinese Simplified<br/>").appendTo("#lanOption");
		Common.userLoading();
	}else if(Common.language() == "zh-CN"){
		$("#lanOption").empty();
		$("<span>语言:<span><input name = 'language' type = 'radio' value = 'En' Onclick = \"Common.switchLan('en');\"/>英语" +
			"<input name = 'language' type = 'radio' value = 'Chs' Onclick = \"Common.switchLan('zh-CN');\" checked/>简体中文<br/>").appendTo("#lanOption");
		Common.userLoading();
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
//	return 0;
	/**********release**********/
	return 1;
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