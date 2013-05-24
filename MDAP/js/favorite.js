Fav = {};

/**********initialize when loading page**********/
Fav.init = function(){
	fav_h = 200;
	fav_w = 300;
	$("#myFavInfo").css({
		"position": "absolute",
		"margin-top": (Common.height() - fav_h) / 2,
		"margin-right": (Common.width() - fav_w) / 2,
		"margin-bottom": (Common.height() - fav_h) / 2,
		"margin-left": (Common.width() - fav_w) / 2,
		"border": "1px solid #000000",
		"height": fav_h,
		"width": fav_w,
		"z-index": "1002",
		"background-color": "white",
		"display": "none"
	});
};

/**********revert one saved favorite**********/
Fav.revert = function(favIndex){
	var name = $.cookie("fav_name" + favIndex);
	var data = JSON.parse($.cookie("fav_data" + favIndex));
	var dsIndex = parseInt(data.dsIndex);
	var chartType = data.chartType;
	var staControl = data.staControl;
	Tab.createFrame("sta");
	var tabIndex = Common.tabIndex - 1;
	var chartIndex = Common.chartIndex[tabIndex][dsIndex];
	var len = staControl.length;
	for(var i = 0; i < len; i++){
		Common.staControl[tabIndex][dsIndex][chartIndex][i] = staControl[i];
	}
	Sta.createChart(tabIndex,dsIndex,chartType);
};

/**********rename**********/
Fav.rename = function(favIndex){
	/**********need to do**********/
};

/**********delete**********/
Fav.del = function(tabIndex,favIndex){
	var count = parseInt($.cookie("fav_count"));
	for(var i = 0; i < count - 1; i++){
		var name = $.cookie("fav_name" + (i + 1));
		var des = $.cookie("fav_des" + (i + 1));
		var data = JSON.parse($.cookie("fav_data" + (i + 1)));
		$.cookie("fav_name" + i,name,{expires: 7,path: "/"});
		$.cookie("fav_des" + i,des,{expires: 7,path: "/"});
		$.cookie("fav_data" + i,JSON.stringify(data),{expires: 7,path: "/"});
	}
	$.removeCookie("fav_name" + (count - 1),{path: "/"});
	$.removeCookie("fav_des" + (count - 1),{path: "/"});
	$.removeCookie("fav_data" + (count - 1),{path: "/"});
	$.cookie("fav_count",count - 1,{expires: 7,path: "/"});
	$("#favorite" + tabIndex).empty();
	Tab.load("fav",tabIndex);
};