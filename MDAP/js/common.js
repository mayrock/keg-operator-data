$(document).ready(function(){
	chart_height = 400;
	chart_width = 750;
	lcContainer_width = 600;
	User.init();
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
	$("#largeChart").css({
		"position": "absolute",
		"margin-top": (Common.height() - chart_height) / 2,
		"margin-right": (Common.width() - chart_width) / 2,
		"margin-bottom": (Common.height() - chart_height) / 2,
		"margin-left": (Common.width() - chart_width) / 2,
		"border": "1px solid #000000",
		"height": chart_height,
		"width": chart_width,
		"z-index": "1000",
		"background-color": "white",
		"display": "none"
	});
	$("#lcContainer").css({
		"position": "absolute",
		"top": 0,
		"left": 0,
		"height": chart_height,
		"width": lcContainer_width
	});
	$("#lcCheckbox").css({
		"position": "absolute",
		"right": 0,
		"bottom": 0,
		"height": chart_height - 25,
		"width": chart_width - lcContainer_width
	});
	$("#main").css({
		"position": "absolute",
		"height": Common.height(),
		"width": Common.width()
	});
	$("#tabs").tabs();
	$("<img src = 'css/images/close.png'/>")
		.appendTo("#largeChart")
		.css({
			"position": "absolute",
			"top": "5px",
			"right": "5px"
		}).hover(
			function(){$(this).attr("src","css/images/close_hover.png");},
			function(){$(this).attr("src","css/images/close.png");}
		).click(
			function(){Chart.closeFrame();}
		);
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

Common.staControl = new Array();

Common.extraMenu = function(){
	if($("#extraMenu").css("display") == "none"){
		$("#extraMenu").css("display","block");
	}else{
		$("#extraMenu").css("display","none");
	}
};