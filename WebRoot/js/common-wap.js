/*
*显示弹出层
*/
var showPrompt={
		init:function(title,isTimer){
			if($("#mask").length !== 0){
				return;
				}
			this.isTimer = isTimer;
			this.creatHtml();
			this.setValue(title);
			this.setPost();
			this.addEvent();
			
		},
		
		//添加事件
		addEvent:function(){
			var _t = this,tMask;
			var time = 2000;
			
			if(!this.isTimer){
				this.mask.live("touchstart click",function(e){
					clearTimeout(tMask);								 
					$("#mask").remove();
				});
				
				clearTimeout(tMask);
				tMask = setTimeout(function(){
					$("#mask").remove();
				},time);
			}
				window.addEventListener("orientationchange", function(){
				//$(".number").val(_t.bodyWidth);
					setTimeout(function(){
						_t.setPost();	 
					},200)
																  
				}, false); 
		},
		
		//创建一个HTML代码 插入在BODY中
		creatHtml:function(){
			var html  = "<div id='mask'><div class='maskBg'></div><div class='maskContent'></div><span class='maskText'></span></div>";
			$("body").append(html);
			this.mask = $("#mask");
			
		},
		
		//设置里面的图片和文字
		setValue:function(title){
			$(".maskText").html(title);
		},
		
		//设置位置
		setPost:function(){
			var top = document.documentElement.scrollTop || document.body.scrollTop;
			var height = $(window).height();
			var width = $(window).width();
			var maskHeight = $(".maskContent").height();
			var maskWidth = $(".maskContent").width();
			var maskTextHeight = $(".maskText").height();
			var maskTextWidth = $(".maskText").width();
			
			
			this.mask.find(".maskContent").css({
				left:(width-maskWidth)/2,
				height:maskTextHeight+40
			});
			
			this.mask.find(".maskText").css({
				top:(height-maskTextHeight)/2+top,
				left:(width-maskTextWidth)/2
			});
			
			this.mask.find(".maskContent").css({
				top:(height-$(".maskContent").height())/2+top
			});
			
		}
	}


function submitCardInfo(commitUrl,checkUrl,successUrl,failUrl){
	showPrompt.init("正在提交订单信息...",true);
	var invoice = "";
	var time = time || 30;
	var maskText = $("#mask .maskText");
	commitUrl = commitUrl + "&ti="+new Date().getTime();
	$.getJSON(commitUrl, function(data){
		var status = data.status;
		var msg = data.msg;
		invoice = data.invoice;
		if(status=="success"){
			maskText.html("√ 提交成功!<br/>正在处理您的支付订单...（<span id='markTime'>30</span>秒）");
			t();
			ajaxService();
		}else{
			location.href = failUrl+"?status=fail&invoice="+invoice+"&msg="+encodeURIComponent(msg);
		}
	});  //提交信息
			 
	function t(){
		var timer = setTimeout(function(){
		if(time>0){
			time-=1;
			$("#markTime").html(time);
			t();
		}else{
			clearTimeout(timer);
			location.href = failUrl+"?status=unknow&invoice="+invoice+"&msg="+encodeURIComponent("网络无响应");
		}
	},1000)}; //倒计时函数
			
	function ajaxService(){
		var timer = setTimeout(function(){
			$.getJSON(checkUrl+"?ti="+new Date().getTime()+"&invoice="+invoice, function(data){
				var dataResult = data.status;
				var dataMsg =data.msg;
				if(dataResult == 1){
					clearTimeout(timer);
					location.href = successUrl+"?status=success&invoice="+invoice;
				}else if(dataResult == 0){
					ajaxService();
				}else{
					clearTimeout(timer);
					location.href = failUrl+"?status=fail&invoice="+invoice+"&msg="+encodeURIComponent(dataMsg);
				}
			})
	},3000)};  //连续请求支付结果
}
