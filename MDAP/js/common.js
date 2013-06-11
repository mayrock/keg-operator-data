$(document).ready(function(){
	Lan.init();
});

Common = {};

Common.height = function(){return $(document).height();};
Common.width = function(){return $(document).width();};

Common.username = new Object;
Common.language = new Object;

Common.ip = function(){
	/**********aplha**********/
	return "http://10.1.1.55:8080/keg2/rest/";
	/**********release**********/
//	return "http://10.1.1.55:8080/keg/rest/";
};

/**********post**********/
Common.registerUrl = function(){return Common.ip() + "up/adduser";};
Common.addFavUrl = function(){return Common.ip() + "favp/addfav";};
Common.delFavUrl = function(){return Common.ip() + "favd/rmfav";};
Common.setLanUrl = function(){return Common.ip() + "up/setlanguage";};

/**********get**********/
Common.loginUrl = function(){return Common.ip() + "ug/login?jsoncallback=?";};
Common.dataViewUrl = function(){return Common.ip() + "dsg/gettabTypedvs?jsoncallback=?"};
Common.dvDataUrl = function(){return Common.ip() + "dsg/gettabTypedv?jsoncallback=?";};
Common.favListUrl = function(){return Common.ip() + "favg/getfavs?jsoncallback=?";};
Common.favDataUrl = function(){return Common.ip() + "favg/getfav?jsoncallback=?";};
Common.getLanUrl = function(){return Common.ip() + "ug/getlanguage?jsoncallback=?"};

Common.tabIndex = new Array();
Common.tabLimit = function(){return 7;};

Common.mapArr = new Array();
Common.mapInfoArr = new Array();

Common.chartIndex = new Array();
Common.chartType = new Array();
Common.yAxis = new Array();

/**********initialize page after set language**********/
Common.init = function(){
	Common.load();
	$("#main").css({
		"position": "absolute",
		"height": Common.height(),
		"width": Common.width()
	});
	Common.footer();
	Common.tabIndex[0] = 0;
	Common.header();
	$("#tabs").tabs();
};

/**********load page in the form of the language you set**********/
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
	
	$("#extraMenu").empty();
	$("<a href = 'javascript:void(0);' onclick = \"Tab.createFrame('geo');\">" + Lan.geo[index] + "</a><br/>" +
		"<a href = 'javascript:void(0);' onclick = \"Tab.createFrame('sta');\">" + Lan.sta[index] + "</a>")
		.appendTo("#extraMenu");
	
	$("#header").empty();
	$("<span class = 'title'>" + Lan.title[index] + "</span>").appendTo("#header");
};

/**********initialize css of header**********/
Common.header = function(){
	if(Common.tabIndex.length == 1){
		$("#header").css({
			"height": "100px",
			"background-position": "0 0,473px 0,812px 0px,905px 0,1047px 0,1210px 0"//(width:100)473 339 93 142 163
		});
		$(".ui-tabs").css({
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
	}
}

/**********initialize css of footer**********/
Common.footer = function(){
	$("#footer").css({
		"top": Common.height()
	});
};

/**********clear css of footer for re-initialize**********/
Common.clearFooter = function(){
	$("#footer").css({
		"top": 0
	});
};

/**********initialize css of background**********/
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

/**********show/hide drop-down list of new*********/
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

/**********show/hide drop-down list of favorite*********/
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