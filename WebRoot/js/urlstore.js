document.addEventListener("DOMContentLoaded", function(){
	var goingUrl = "@@"+location.href;
	var supportSessionStorage = window.sessionStorage!=undefined?true:false;
	var urls = supportSessionStorage?sessionStorage["urls"]:window.MapStorage.get("urls");
	if(urls == undefined || urls==null){  //第一次访问，放入当前URL
		urls = goingUrl;
	}else{  //第二次访问
		urls=""+urls;
		var pos = urls.indexOf(goingUrl);
		if(pos!=-1){
			urls= urls.substring(0,pos);
		}
		urls = urls+goingUrl;
	}
	if(supportSessionStorage){
		sessionStorage["urls"]=urls;
	}else{
		window.MapStorage.put("urls",urls);
	}
	var urlObj = urls.split("@@");
	var x=null;
	if(urlObj.length>1){
		x = urlObj[urlObj.length-2];
	}
	var xx = document.getElementById("android_com_easou_pay_per_url");
	if(xx!=undefined){
		xx.value=x;
	}
}, false);



function startAli(requestUrl,resultUrl){
	$.getJSON(requestUrl, function(data){
		if(data!=undefined && data.data!=undefined && data.data.url!=undefined){
			window.AndroidPay.startAlipay(data.data.url,resultUrl);
		}else{
			alert("参数有误!");
		}
	});
}
