getDatasetUrl = "http://10.1.1.55:8081/mdap/rest/dsg/get";

function initialize(){
//	mapInitialize();
//	width = document.documentElement.clientWidth;
	url = getDatasetUrl + 
	//"geo" + 
	"dss?jsoncallback=?";
	$.getJSON(url,function(data){
alert(data);
	}).error(function(){
		alert("Oops, we got an error...");
	});
}