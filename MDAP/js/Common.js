$(document).ready(function(){
	height = $(document).height();
	width = $(document).width();
	account_height = 240;
	account_width = 360;
	table_width = 320;
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
	$("#account").css({
		"position": "absolute",
		"margin-top": (height - account_height) / 2,
		"margin-right": (width - account_width) / 2,
		"margin-bottom": (height - account_height) / 2,
		"margin-left": (width - account_width) / 2,
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
	$("#account").tabs();
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
	if(($.cookie("username") != null) && ($.cookie("password") != null)){
		Account.upperRightMenu("login","saved");
	}else{
		Account.upperRightMenu("logout","init");
	}
});

Common = {};

Common.advanced = function(){
	if($("#extended").css("display") == "none"){
		$("#extended").css("display","block");
	}else{
		$("#extended").css("display","none");
	}
};