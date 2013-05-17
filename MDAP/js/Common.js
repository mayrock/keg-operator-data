$(document).ready(function(){
	hegiht = $(document).height();
	width = $(document).width();
	account_height = 270;
	account_width = 400;
	$("#account_bg").css({
		"position": "absolute",
		"height": hegiht,
		"width": width,
		"filter": "alpha(opacity = 60)",
		"opacity": "0.6",
		"z-index": "999",
		"background-color": "gray",
		"display": "none"
	});
	$("#account").css({
		"position": "absolute",
		"margin-top": (hegiht - account_height) / 2,
		"margin-right": (width - account_width) / 2,
		"margin-bottom": (hegiht - account_height) / 2,
		"margin-left": (width - account_width) / 2,
		"border": "1px solid #000000",
		"height": account_height,
		"width": account_width,
		"z-index": "1000",
		"background-color": "white",
		"display": "none"
	});
	$("#account table").attr({
		"width": "350px",
		"style": "margin: auto"
	});
	$("#account").tabs();
	$("#checkbox_a").attr("checked",true);
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
		Account.upperRightMenu("logout","");
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