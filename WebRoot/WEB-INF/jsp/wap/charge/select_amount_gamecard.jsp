<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@page import="com.fantingame.pay.utils.Constants"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no,target-densitydpi=medium-dpi" />
<meta content="telephone=no" name="format-detection" />
<title>梵付通(WAP) - 游戏点卡支付</title>
<link type="text/css" rel="stylesheet"  href="/css/common.css" />
</head>


<body>

<div class="mode">
	<div class="tel-mode"><div id="menu">
		<ul id="telCompany" class="tel-company">
			<li class="on" card="6">盛大卡</li>
			<li card="8">征途卡</li>
			<li card="7">骏网一卡通</li>
			<li card="12">完美一卡通</li>
		</ul></div>
		
		<div class="tel-money">
			<div>
				<ul id="payMoney-6" class="payMoney">
					<li class="on">10元</li>
					<li>15元</li>
					<li>25元</li>
					<li>30元</li>
					<li>35元</li>
					<li>50元</li>
					<li>100元</li>
					<li>1000元</li>
				</ul>
				<ul id="payMoney-8" style="display:none" class="payMoney">
					<li class="on">5元</li>
					<li>10元</li>
					<li>20元</li>
					<li>30元</li>
					<li>50元</li>
					<li>100元</li>
					<li>250元</li>
					<li>300元</li>
					<li>500元</li>
				</ul>
				<ul id="payMoney-7" style="display:none" class="payMoney">
					<li class="on">10元</li>
					<li>20元</li>
					<li>30元</li>
					<li>50元</li>
					<li>100元</li>
					<li>200元</li>
					<li>300元</li>
					<li>500元</li>
				</ul>
			
				<ul id="payMoney-12" style="display:none" class="payMoney">
					<li class="on">15元</li>
					<li>30元</li>
					<li>50元</li>
					<li>100元</li>
				</ul>
			</div>
			<div class="gayText">卡号：<input id="input" class="loginInput" name="" type="text" value="" /><br />密码：<input id="password" class="loginInput" name="" type="password" value="" />
			</div>
		
			
			<div class="div-blue"><input id="ok" class="button-blue" name="" type="button" value="  确   认  " /></div>
			<h6>温馨提示：</h6>
			<div>
			<p  id="tit-6" class="gayText">1、请使用卡号以CSC5、CS、S、CA、CSB、YA、YB、YC、YD、80133开头的"盛大互动娱乐卡"进行支付；<br />2、选择充值卡面值时，请与您的卡实际面值保持一致，以免造成充值失败和充值卡失效。</p>
			<p  id="tit-8" style="display:none" class="gayText">1、全国官方征途游戏充值卡可用；<br />2、选择充值卡面值时，请与您的卡实际面值保持一致，以免造成充值失败和充值卡失效。</p>
			<p  id="tit-7" style="display:none"  class="gayText">1、不能使用特定游戏专属充值卡支付。 特定游戏包括大唐风云、传说、蜗牛、猫扑一卡通、九鼎、雅典娜、山河等游戏。<br />2、在此使用过的骏网一卡通，卡内剩余J点只能在易宝支付合作商家进行支付使用。</p>
			<p  id="tit-12" style="display:none"  class="gayText">1、全国官方完美游戏充值卡可用；<br />2、选择充值卡面值时，请与您的卡实际面值保持一致，以免造成充值失败和充值卡失效。</p>
			</div>
			
		</div>
		
	</div>
	

</div>


<div class="company-info">梵付通24小时服务热线：0755-83734900</div>

<script src="/js/zepto.min.js"></script>
<script src="/js/common-wap.js?t=20130223"></script>
<script>
$(document).ready(function(){
	var company = $("#telCompany>li");
	var money = $(".payMoney>li");
	var input = $("#input");
	var password = $("#password");
	var ok = $("#ok");
	var url = "pay.e?";
	var c,m;
	var n = 0;
	(function(){
		c=$("#telCompany>li.on").attr("card");
		m=$(".payMoney").eq(0).find("li.on").html();
		m=m.substring(0,m.length-1);
	}())
	
	
	company.live("click touchStart",function(){
		n = company.index($(this));
		$(this).addClass("on").siblings().removeClass("on");
		c = encodeURIComponent($(this).attr("card"));
	
		m=$(".payMoney").eq(n).find("li.on").html();
		m=m.substring(0,m.length-1);
		
		$("#payMoney-"+c).show().siblings().hide();
		$("#tit-"+c).show().siblings().hide();
		
		//盛大卡
		//卡号15位或16位的数字/字母
		if(c === "SNDACARD"){
			input.attr("type","text");
		}else{
			input.attr("type","number");
		}
		<%if(Constants.isTest){%>
		if(n==0){   //盛大卡
			$("#input").val("S110E0000604585");
			$("#password").val("30815826");
		}else if(n==1){//征途卡
			$("#input").val("0100001206785051");
			$("#password").val("70925644");
		}else if(n==2){  //骏网卡
			$("#input").val("1111151124744126");
			$("#password").val("3999556948085428");
		}else if(n==3){ //完美卡
			$("#input").val("6017299923");
			$("#password").val("141965193105388");
		}
		<%}%>
	
	});
	
	money.live("click touchStart",function(){
		$(this).addClass("on").siblings().removeClass("on");
		m = $(this).html();
		m=m.substring(0,m.length-1);
	});
	

	
	input.live("focus",function(){
		if($(this).val() === "在此输入序列号"){
			$(this).val("");
		}
	});
	
	input.live("blur",function(){
		verifyCard();
		//showPrompt.init("输入错误")
	})
	
	
	password.live("blur",function(){
		passwordCard();
		//showPrompt.init("输入错误")
	});
	
	function verifyCard(){
		
		var l = $.trim(input.val()).length;
		//盛大卡：卡号15位或16位的数字/字母，密码8位或9位的阿拉伯数字；
		if(c === "6"){
			if(l!==15 && l!==16){
				showPrompt.init("请输入有效的卡号");
				return false;
			}
			
		//征途卡：卡号16位阿拉伯数字，密码8位阿拉伯数字；
		}else if(c === "8"){
			if(l!== 16){
				showPrompt.init("请输入有效的卡号");
				return false;
			}
		//骏网卡：卡号、密码都是16位的阿拉伯数字；
		}else if(c === "7"){
			if(l!== 16){
				showPrompt.init("请输入有效的卡号");
				return false;
			}
		
		//完美卡：卡号10位、密码15位的阿拉伯数字；
		}else if(c === "12"){
			if(l!== 10){
				showPrompt.init("请输入有效的卡号");
				return false;
			}
		}
		return true;
	}
	
	function passwordCard(){
		//移动卡
		var l = password.val().length;
		//盛大卡：卡号15位或16位的数字/字母，密码8位或9位的阿拉伯数字；
		if(c === "6"){
			if(l!==8 && l!==9){
				showPrompt.init("请输入有效的密码");
				return false;
			}
			
		//征途卡：卡号16位阿拉伯数字，密码8位阿拉伯数字；
		}else if(c === "8"){
			if(l!== 8){
				showPrompt.init("请输入有效的密码");
				return false;
			}
		//骏网卡：卡号、密码都是16位的阿拉伯数字；
		}else if(c === "7"){
			if(l!== 16){
				showPrompt.init("请输入有效的密码");
				return false;
			}
		
		//完美卡：卡号10位、密码15位的阿拉伯数字；
		}else if(c === "12"){
			if(l!== 15){
				showPrompt.init("请输入有效的密码");
				return false;
			}
		}
		return true;
	}
	
	ok.live("click touchStart",function(){
		var am = parseFloat($("#actualMoney").val());
		
		if(verifyCard() && passwordCard()){
			//小于实际金额
			
			if(m<am){
				showPrompt.init("抱歉,您选择的充值卡金额不够支付此订单，请重新选择!");
			//相等
			}else{
				var commitUrl = "/wap/card!cardCharge.e?channelType="+c+"&cardAmt="+m+"&cardNumber="+input.val()+"&cardPwd="+password.val();
				var checkUrl = "/wap/tradeResult.e"; //往这接口取支付确切结果
				var successUrl= "/wap/redirect!showResult.e"; //支付成功，走页面驱动
				var failUrl= "/wap/redirect!showResult.e"; //支付失败，走页面驱动
				submitCardInfo(commitUrl , checkUrl , successUrl ,failUrl );
			}
		}
	})

})
</script>
</body>
</html>