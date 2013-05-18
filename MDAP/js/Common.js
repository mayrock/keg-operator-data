$(document).ready(function(){
	account_height = 240;
	account_width = 360;
	table_width = 320;
	$("#account").css({
		"position": "absolute",
		"margin-top": (Common.height() - account_height) / 2,
		"margin-right": (Common.width() - account_width) / 2,
		"margin-bottom": (Common.height() - account_height) / 2,
		"margin-left": (Common.width() - account_width) / 2,
		"border": "1px solid #000000",
		"height": account_height,
		"width": account_width,
		"z-index": "1000",
		"background-color": "white",
		"display": "none"
	});
	$("#account table").attr({
		"width": table_width,
		"style": "margin: auto"
	});
	$("#account_bg").css({
		"position": "absolute",
		"height": height,
		"width": width,
		"filter": "alpha(opacity = 60)",
		"opacity": "0.6",
		"z-index": "999",
		"background-color": "gray",
		"display": "none"
	});
	$("#main").css({
		"position": "absolute",
		"height": height,
		"width": width
	});
	$("#account").tabs();
	$("#tabs").tabs();
	$("#checkbox_l").attr("checked",true);
	$("<img src = 'css/images/close.png'/>")
		.appendTo("#account")
		.css({
			"position": "absolute",
			"top": "5px",
			"right": "5px"
		}).hover(
			function(){$(this).attr("src","css/images/close_hover.png");},
			function(){$(this).attr("src","css/images/close.png");}
		).click(
			function(){Account.closeFrame();}
		);
/*	if(($.cookie("username") != null) && ($.cookie("password") != null)){
		Account.upperRightMenu("login","saved");
	}else{
		Account.upperRightMenu("logout","init");
	}*/
	Account.upperRightMenu("login","Ariesnix");
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
}

Common.tabNum = 0;

Common.tabCount = 0;

Common.mrkrArr = new Array();

Common.tabLimit = function(){
	return 7;
}

Common.advanced = function(){
	if($("#extended").css("display") == "none"){
		$("#extended").css("display","block");
	}else{
		$("#extended").css("display","none");
	}
};