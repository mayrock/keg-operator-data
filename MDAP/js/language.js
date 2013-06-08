Lan = {};

Lan.list = new Array("en-US","zh-CN");

Lan.register = new Array("Register","注册");
Lan.login = new Array("Login","登录");
Lan.inputName = new Array("Input Username:","请输入用户名:");
Lan.inputCode = new Array("Input Password:","请输入密码:");
Lan.reInputCode = new Array("Password Again:","请确认密码:");
Lan.reset = new Array("reset","重置");
Lan.saveInfo = new Array("Save username and password in 7 days","保存用户名和密码7天");
Lan.geo = new Array("geo data","地理数据");
Lan.sta = new Array("stat data","统计数据");
Lan.title = new Array("Mobile Data Analysis Platform","移动数据分析平台");
Lan.create = new Array("new","新建");
Lan.favorite = new Array("favorite","收藏");
Lan.logout = new Array("logout","登出");
Lan.emptyName = new Array("Username cann't be empty!","用户名不能为空!");
Lan.emptyCode = new Array("Password cann't be empty!","密码不能为空!");
Lan.notSameCode = new Array("The two passwords you input don't match!","两次密码不一致!");
Lan.nameExist = new Array("This username has been registered!","存在该用户名!");
Lan.nameOrCodeError = new Array("Username or password error!","用户名或密码错误!")

/**********set language at first when loading page**********/
Lan.init = function(){
	if(($.cookie("username") != null) && ($.cookie("password") != null)){
		var username = $.cookie("username");
		Common.username = username;
		$.getJSON(Common.getLanUrl(),{
			userid: username
		},function(data){
			Common.language = data;
			Common.init();
		}).error(function(){
			alert("Oops, we got an error...");
			return;
		});
	}else{
		var index = 0;
		var language = window.navigator.language;
		for(var i = 0; i < Lan.list.length; i++){
			if(language == Lan.list[i]){
				index = i;
				break;
			}
		}
		Common.language = index;
		Common.init();
	}
};

/**********change language and reload page**********/
Lan.change = function(index,country){
	var username = Common.username;
	if(index == country){
		return;
	}
	$.post(Common.setLanUrl(),{
		userid: username,
		language: country
	},function(data){
		if(data.status == true){
			Common.language = country;
			Common.load();
		}else if(data.status == false){
			alert("Oops, we got an error...");
			return;
		}else{
			/**********need to do**********/
		}
	},"json").error(function(){
		alert("Oops, we got an error...");
		return;
	});
}