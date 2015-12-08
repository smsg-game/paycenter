(function($, window, document) {
	
  $(function() {
    initPage(); // First page action.
    $(document).bind('pagecreate', initPage); // AJAX action.
    $(document).bind('pageshow', adjustPostionOfPayButton);
  });

  var pageHeight;
  var visibleHeight;

  function initPage() {
    showHeaderIfUserAgentIsNotMobile();
    initPostionOfPayButton();
  }

  function showHeaderIfUserAgentIsNotMobile() {
    if (!/Android|webOS|iPhone|iPad|iPod|Phone|BlackBerry|Nokia/i.test(navigator.userAgent)) {
      $('div[data-role=header]').css('display', 'block');
    }
  }

  function  initPostionOfPayButton() {
//    console.log('init');
//    console.log($(document).height());
//    console.log($(window).height());
//    console.log(getActivePageHeight());
//    console.log(getActiveVisibleHeight());
    
    visibleHeight = getActiveVisibleHeight();
    if (getActiveVisibleHeight() < $(window).height()) {
	    $('.pay').addClass('pay-go-bottom');
	    $.mobile.activePage.height($(document).height());
	  } else {
	    $('.pay').removeClass('pay-go-bottom');
	  }
  }

  function adjustPostionOfPayButton() {
//    console.log('adjust');
//    console.log($(document).height());
//    console.log($(window).height());
//    console.log(getActivePageHeight());
//    console.log(getActiveVisibleHeight());

    if (getActiveVisibleHeight() < $(window).height()) {
      $('.pay').addClass('pay-go-bottom');
      $.mobile.activePage.height($(document).height());
    } else {
      $('.pay').removeClass('pay-go-bottom');
    }
  }

  function getActivePageHeight() { 
    return $.mobile.activePage.height(); 
  }
  function getActiveVisibleHeight() {
    return $.mobile.activePage.children('[data-role=header]').height() 
         + $.mobile.activePage.children('[data-role=content]').height() 
         + $.mobile.activePage.children('[data-role=footer]').height();
  }

})(jQuery, window, document);

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
				this.mask.on("touchstart click",function(e){
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
				height:maskTextHeight+70,
				top: (height-(maskTextHeight+70))/2
			});
			
			this.mask.find(".maskText").css({
				top:(height-maskTextHeight)/2+top,
				left:(width-maskTextWidth)/2
			});
			
			this.mask.find(".maskContent").css({
				top:(height-$(".maskContent").height())/2+top
			});
			
		}
	};


function submitCardInfo(commitUrl,checkUrl,successUrl,failUrl){
	showPrompt.init("正在提交订单信息...",true);
	var invoice = "";
	var time = time || 60;
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

function submitCardInfo2(commitUrl,checkUrl,successUrl,failUrl){
	
	var popHTML = 
	"<div id=\"waiting-wrapper\" style=\"display:none\">"
	  + "<div id=\"waiting\">"
	   + "<div id=\"waiting-content\">" 
	    + "<div class=\"title\">正在查询支付结果</div>"
	     + "<div class=\"content\">"
	     + "（<span id='markTime'>30</span>秒）"
	     + "</div>"
	   + "</div>"
	   + "</div>"
	+ "</div>";
	
	var invoice = "";
	var ajaxTimer = null;
	var time = time || 30;
//	var maskText = $("#mask .maskText");
	commitUrl = commitUrl + "&ti="+new Date().getTime();
	$.getJSON(commitUrl, function(data){
		var status = data.status;
		var msg = data.msg;
		invoice = data.invoice;
		if(status=="success"){
			$(document.body).append(popHTML);
			waitPaymentCompleted();
			t();
			ajaxService();
		}else{
			location.href = failUrl+"?status=fail&invoice="+invoice+"&msg="+encodeURIComponent(msg);
		}
	});  //提交信息
	
	function waitPaymentCompleted() {
	    $('#waiting-wrapper').show();

	    var top = document.documentElement.scrollTop || document.body.scrollTop;
	    var height = $(window).height();
	    var width = $(window).width();

	    var waitingHeight = $("#waiting").height();
	    var waitingWidth = $("#waiting").width();
	    var waitingTextHeight = $("#waiting-content").height();
	    var waitingTextWidth = $("#waiting-content").width();
	    
	    $("#waiting").css({
	      left:(width-waitingWidth)/2,
	      height:170,
	      top:(height-(170))/2
	    });
	    
	    $("#waiting-content").css({
	      top:(height-waitingTextHeight)/2+top,
	      left:(width-waitingTextWidth)/2
	    });
	  }
	function t(){
		var timer = setTimeout(function(){
		if(time>0){
			time-=1;
			$("#markTime").html(time);
			t();
		}else{
//			clearTimeout(timer);
//			location.href = failUrl+"?status=unknow&invoice="+invoice+"&msg="+encodeURIComponent("网络无响应");
			clearTimeout(timer);
			clearTimeout(ajaxTimer);
			$(".title").html("<span class='font-striking'>未查询到支付结果</span><br><span class='font-striking'>是否继续查询</span>");
			$(".content").html("<a class='pay' id='jixu' href='javascript:void(0)'>继续查询结果</a>");
			$("#jixu").on('click touchStart',function(){
				time = 30;
				$(".title").html("正在查询支付结果");
				$(".content").html("（<span id='markTime'>30</span>秒）");
				t();
				ajaxService();
			});
		}
	},1000)}; //倒计时函数
	function ajaxService(){
		ajaxTimer = setTimeout(function(){
			$.getJSON(checkUrl+"?ti="+new Date().getTime()+"&invoice="+invoice, function(data){
				var dataResult = data.status;
				var dataMsg =data.msg;
				if(dataResult == 1){
					clearTimeout(ajaxTimer);
					location.href = successUrl+"?status=success&invoice="+invoice;
				}else if(dataResult == 0){
					ajaxService();
				}else{
					clearTimeout(ajaxTimer);
					location.href = failUrl+"?status=fail&invoice="+invoice+"&msg="+encodeURIComponent(dataMsg);
				}
			})
	},3000)
	};  //连续请求支付结果
}

function startAli(isWeb,requestUrl,resultUrl){
	if(isWeb == "true") {
		location.href = requestUrl + "&ty=web";
	} else {
		if(window.AndroidPayForJS.check()) {
			$.getJSON(requestUrl + "&ty=android", function(data){
				if(data!=undefined && data.data!=undefined && data.data.url!=undefined){
					window.AndroidPayForJS.pay(data.data.url,resultUrl);
				}else{
					showPrompt.init("参数有误");
				}
			});
		} else {
			location.href = requestUrl + "&ty=web";
		}
	}
}
function startSms(isWeb,requestUrl,redirectUrl) {
	if(isWeb == 'true') {
		location.href = requestUrl + "&isWeb=1";
	} else {
		$.getJSON(requestUrl,function(data) {
			//调android接口,data.status,data.msg,data.number,redirectUrl
			if(data.status=='success'){
				window.AndroidSmsPayForJavaScript.startSmsPay(data.msg,data.number, redirectUrl);
			} else {
				location.href = redirectUrl + "&status="+data.status+"&msg="+encodeURIComponent(data.msg);
			}
		});
	}
}