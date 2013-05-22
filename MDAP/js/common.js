$(document).ready(function(){
	User.init();
	Sta.init();
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
	$("#main").css({
		"position": "absolute",
		"height": Common.height(),
		"width": Common.width()
	});
	$("#tabs").tabs();
	if(($.cookie("username") != null) && ($.cookie("password") != null)){
		User.upperRightMenu("login","saved");
	}else{
		User.upperRightMenu("logout","init");
	}
});

Common = {};

Common.height = function(){
	return $(document).height();
}

Common.width = function(){
	return $(document).width();
}

Common.ip = function(){
	return "http://10.1.1.55:8088/platform_restful/rest/";
};

Common.registerUrl = function(){
	return Common.ip() + "up/adduser";
};

Common.loginUrl = function(){
	return Common.ip() + "ug/verifyuser/";
};

Common.datasetUrl = function(){
	return Common.ip() + "dsg/get";
};

Common.latlngUrl = function(){
	return Common.ip() + "dsg/getgeods/";
};

Common.staDataUrl = function(){
	return Common.ip() + "dsg/getstads/";
};

Common.tabCount = 0;

Common.tabLimit = function(){
	return 7;
};

Common.tabIndex = 0;

Common.mapArr = new Array();

Common.mapInfoArr = new Array();

Common.chartCount = new Array();

Common.chartIndex = new Array();

Common.staControl = new Array();

Common.extraMenu = function(){
	if($("#extraMenu").css("display") == "none"){
		$("#extraMenu").css("display","block");
	}else{
		$("#extraMenu").css("display","none");
	}
};