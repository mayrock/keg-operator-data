Lan = {};

Lan.list = new Array("en-US","zh-CN");

Lan.register = new Array("Register","注册");
Lan.login = new Array("Login","登录");
Lan.inputName = new Array("Input Username:","请输入用户名:");
Lan.inputCode = new Array("Input Password:","请输入密码:");
Lan.reInputCode = new Array("Password Again:","请确认密码:");
Lan.saveInfo = new Array("Save username and password in 7 days","保存用户名和密码7天");
Lan.reset = new Array("reset","重置");

Lan.mainTitle = new Array("MOBILE DATA","移 动 数 据");
Lan.subtitle = new Array("ANALYSIS PLATFORM","分 析 平 台");

Lan.manage = new Array("Manage","管理");
Lan.create = new Array("New","新建");
Lan.geo = new Array("Geo Data","地理数据");
Lan.sta = new Array("Stat Data","统计数据");
Lan.favorite = new Array("Favorite","收藏");
Lan.logout = new Array("Logout","登出");

Lan.emptyName = new Array("Username cann't be empty!","用户名不能为空!");
Lan.emptyCode = new Array("Password cann't be empty!","密码不能为空!");
Lan.notSameCode = new Array("The two passwords you input don't match!","两次密码不一致!");
Lan.nameExist = new Array("This username has been registered!","存在该用户名!");
Lan.nameOrCodeError = new Array("Username or password error!","用户名或密码错误!")
Lan.cookieInvalid = new Array("The username and password in cookie is invalid!","cookie中的用户名和密码无效!")

/*****set language and verify username & password when first loading page*****/
Lan.init = function(){
	if(($.cookie("username") != null) && ($.cookie("password") != null)){
		var username = $.cookie("username");
		var password = $.cookie("password");
		$.getJSON(Common.loginUrl(),{
			userid: username,
			password: password
		},function(data){
			if(data.status == true){
				Common.username = username;
				Common.language = data.user.language;
				Common.permit = data.user.permission;
			}else{
				$.removeCookie("username",{path: "/"});
				$.removeCookie("password",{path: "/"});
				var index = 0;
				var language = window.navigator.language;
				for(var i = 0; i < Lan.list.length; i++){
					if(language == Lan.list[i]){
						index = i;
						break;
					}
				}
				alert(Lan.cookieInvalid[index]);
				Common.language = index;
				Common.init();
			}
			Common.init();
		}).error(function(){
			alert("Oops, we get an error...");
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

/*****change language and reload page*****/
Lan.change = function(index,country){
	var username = Common.username;
	if(index == country){
		return;
	}
	if(username == ""){
		Common.language = country;
		Common.load();
		return;
	}
	$.post(Common.setLanUrl(),{
		userid: username,
		language: country
	},function(data){
		if(data.status == true){
			Common.language = country;
			Common.load();
		}else{
			alert("Oops, we get an error...");
			return;
		}
	},"json").error(function(){
		alert("Oops, we get an error...");
		return;
	});
};