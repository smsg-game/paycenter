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
	
function cardPayResult(href,time,successHref){
		showPrompt.init("√ 提交成功!<br/>正在处理您的支付订单...（<span id='markTime'>30</span>秒）",true);
//		document.title = "正在提交支付请求";
//		window.AndroidWindow.disableCloseButton("正在处理支付请求,请稍后！");
//		window.AndroidWindow.disableBackButton("正在处理支付请求,请稍后！");
		href=href+"&_t_"+new Date().getTime();
		$.getJSON(href, function(data){
				var result = data.data.result;
				var msg = encodeURIComponent(data.data.msg);
				//document.title = "支付请求已完成";
//			 	window.AndroidWindow.enableCloseButton();
//			 	window.AndroidWindow.enableBackButton();
				if(result=="success"){
					if(successHref==undefined || successHref==null){
						location.href = "/service/cardPayResult.e?result=success";
					}else{
						location.href = successHref;
					}
				}else if(result=="fail"){
						 location.href = "/service/cardPayResult.e?result=fail&msg="+msg;
				 }else if(result=="unknow"){
						 location.href = "/service/cardPayResult.e?result=unknow&msg="+msg;
				 }
			})
				 
			function t(){
				var timer = setTimeout(function(){
				if(time>0){
					time-=1;
					$("#markTime").html(time);
					t();
				}else{
					clearTimeout(timer);
//					 window.AndroidWindow.enableCloseButton();
//					 window.AndroidWindow.enableBackButton();
					location.href = "/service/cardPayResult.e?result=unknow&msg="+encodeURIComponent("网络无响应");
				}
			},1000)}
			t();
	}