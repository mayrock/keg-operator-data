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

Lan.init = function(){
	if($.cookie("language") == null){
		var index = 0;
		var language = window.navigator.language;
		for(var i = 0; i < Lan.list.length; i++){
			if(language == Lan.list[i]){
				index = i;
				break;
			}
		}
		$.cookie("language",index,{expires: 7,path: "/"});
	}
};

Lan.change = function(index,country){
	if(index == country){
		return;
	};
	$.cookie("language",country,{expires: 7,path: "/"});
	Common.load();
}