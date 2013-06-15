$(document).ready(function(){
	alert("Version Test Environment: Chrome27");
	Common.tabHeight = Common.height() - 160;
	Lan.init();
});

Common = {};

Common.username = "";
Common.language = -1;
Common.permit = 1;

Common.width = function(){return $(document).width();};
Common.height = function(){return $(document).height();};

Common.userHeight = function(){return 210;};
Common.userWidth = function(){return 360;};
Common.uTableWidth = function(){return 320;};

Common.tabHeight = 0;

Common.lcHeight = function(){return 480;};
Common.lcWidth = function(){return 800;};
Common.lcCntrWidth = function(){return 600;};

Common.favHeight = function(){return 75;};
Common.favWidth = function(){return 200;};

Common.ip = function(){
	/*****aplha*****/
	return "http://10.1.1.55:8080/keg2/rest/";
	/*****release*****/
//	return "http://10.1.1.55:8080/keg/rest/";
};

/*****post*****/
Common.registerUrl = function(){return Common.ip() + "up/adduser";};

Common.addFavUrl = function(){return Common.ip() + "favp/addfav";};
Common.delFavUrl = function(){return Common.ip() + "favd/rmfav";};

Common.setLanUrl = function(){return Common.ip() + "up/setlanguage";};

/*****get*****/
Common.loginUrl = function(){return Common.ip() + "ug/login?jsoncallback=?";};

Common.dataviewUrl = function(){return Common.ip() + "dsg/gettabTypedvs?jsoncallback=?"};
Common.dvDataUrl = function(){return Common.ip() + "dsg/gettabTypedv?jsoncallback=?";};
Common.datasetUrl = function(){return Common.ip() + "dsg/gettabTypedss?jsoncallback=?"};
Common.dsDataUrl = function(){return Common.ip() + "dsg/gettabTypeds?jsoncallback=?";};

Common.favListUrl = function(){return Common.ip() + "favg/getfavs?jsoncallback=?";};
Common.favDataUrl = function(){return Common.ip() + "favg/getfav?jsoncallback=?";};

Common.tabLimit = function(){return 7;};
Common.tabIndex = new Array();

Common.mapArr = new Array();
Common.mapInfoArr = new Array();

Common.chartIndex = new Array();
Common.chartType = new Array();
Common.yAxis = new Array();

/*****initialize page after set language*****/
Common.init = function(){
	Common.load();
	Common.tabIndex[0] = 0;
	Common.adjunct();
	$("#tab_bg").css({
		"height": Common.tabHeight
	});
	$("#tabs").tabs({
		activate: function(event,ui){
			var active = $("#tabs").tabs("option","active");
			var tabIndex = Common.tabIndex[active];
			var viewHeight = $("#view" + tabIndex).height();
			if((viewHeight + 25) > Common.tabHeight){
				$("#tab_bg").css({
					"height": viewHeight + 25
				});
			}else{
				$("#tab_bg").css({
					"height": Common.tabHeight
				});
			}
		}
	});
};

/*****load page*****/
Common.load = function(){
	var index = Common.language;
	
	User.load();
	
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
	
	if(index == 0){
		$("#extraMenu").css({
			"right": "180px"
		});
		$("#extendedFav").css({
			"right": "75px",
			"width": "150px"
		});
	}else if(index == 1){
		$("#extraMenu").css({
			"right": "160px"
		});
		$("#extendedFav").css({
			"right": "60px",
			"width": "150px"
		});
	}
	
	$("#extraMenu").empty();
	$("<a href = 'javascript:void(0);' onclick = \"Tab.createFrame('geo');\">" + Lan.geo[index] + "</a><br/>" +
		"<a href = 'javascript:void(0);' onclick = \"Tab.createFrame('sta');\">" + Lan.sta[index] + "</a>")
		.appendTo("#extraMenu");
	
	$("#header").empty();
	$("<span class = 'title'>" + Lan.title[index] + "</span>").appendTo("#header");
};

/*****initialize css of #header, .ui-tabs & #tabBar_bg*****/
Common.adjunct = function(){
	if(Common.tabIndex.length == 1){
		$("#header").css({
			"height": "100px",
			"background-position": "0 0,473px 0,812px 0px,905px 0,1047px 0,1210px 0"//(width:100)473 339 93 142 163
		});
		$(".ui-tabs").css({
			"height": 0
		});
		$("#tabBar_bg").css({
			"height": 0
		});
	}else{
		$("#header").css({
			"height": "65px",
			"background-position": "0 0,307.5px 0,527.5px 0,588px 0,680.5px 0,786.5px 0"//(width:65)307.5 220 60.5 92.5 106
		});
		$(".ui-tabs").css({
			"height": "35px"
		});
		$("#tabBar_bg").css({
			"height": "35px"
		});
	}
}

/*****initialize css of #background*****/
Common.background = function(){
	$("#background").css({
		"position": "absolute",
		"top": 0,
		"left": 0,
		"height": Common.height(),
		"width": Common.width(),
		"filter": "alpha(opacity = 60)",
		"opacity": "0.6",
		"z-index": "999",
		"background-color": "gray",
		"display": "none"
	});
};

/*****initialize css of #user*****/
Common.user = function(){
	$("#user").css({
		"position": "absolute",
		"margin-top": (Common.height() - Common.userHeight()) / 2,
		"margin-right": (Common.width() - Common.userWidth()) / 2,
		"margin-bottom": (Common.height() - Common.userHeight()) / 2,
		"margin-left": (Common.width() - Common.userWidth()) / 2,
		"border": "1px solid #000000",
		"width": Common.userWidth(),
		"height": Common.userHeight(),
		"z-index": "1000",
		"background-color": "white",
		"display": "none"
	});
};

/*****initialize css of #largeChart*****/
Common.largeChart = function(){
	$("#largeChart").css({
		"position": "absolute",
		"margin-top": (Common.height() - Common.lcHeight()) / 2,
		"margin-right": (Common.width() - Common.lcWidth()) / 2,
		"margin-bottom": (Common.height() - Common.lcHeight()) / 2,
		"margin-left": (Common.width() - Common.lcWidth()) / 2,
		"border": "1px solid #000000",
		"width": Common.lcWidth(),
		"height": Common.lcHeight(),
		"z-index": "1000",
		"background-color": "white",
		"display": "none"
	});
	$("#lcContainer").css({
		"position": "absolute",
		"top": 0,
		"left": 0,
		"width": Common.lcCntrWidth(),
		"height": Common.lcHeight()
	});
	$("#lcCheckbox").css({
		"position": "absolute",
		"right": 0,
		"bottom": 0,
		"width": Common.lcWidth() - Common.lcCntrWidth(),
		"height": Common.lcHeight() - 40
	});
};

/*****initialize css of #favInfo*****/
Common.favInfo = function(){
	$("#favInfo").css({
		"position": "absolute",
		"margin-top": (Common.height() - Common.favHeight()) / 2,
		"margin-right": (Common.width() - Common.favWidth()) / 2,
		"margin-bottom": (Common.height() - Common.favHeight()) / 2,
		"margin-left": (Common.width() - Common.favWidth()) / 2,
		"border": "1px solid #000000",
		"width": Common.favWidth(),
		"height": Common.favHeight(),
		"z-index": "1000",
		"background-color": "white",
		"display": "none"
	});
};

/*****show/hide drop-down list of new*****/
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

/*****show/hide drop-down list of favorite*****/
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