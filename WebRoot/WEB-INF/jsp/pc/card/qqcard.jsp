<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@page import="com.fantingame.pay.utils.Constants"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no,target-densitydpi=medium-dpi" />
<meta content="telephone=no" name="format-detection" />
<title>梵付通(手机版) - Q币卡支付</title>
<link type="text/css" rel="stylesheet"  href="/css/common.css" />
<script type="text/javascript" src="/js/urlstore.js?t=2012121111"></script>
</head>


<body>
<input type="hidden" id="actualMoney" value="${payTrade.reqFee}"/>
<input type="hidden" id="invoice" value="${payTrade.invoice}"/>
<div class="userInfo">
	商品名称：${payTrade.tradeName}<br />
	需付金额：${payTrade.reqFee}元<br />
</div>

<div class="mode">
	<div class="tel-mode">
		<div class="q-money">
			<ul id="payMoney" class="payMoney">
					<li class="on">5元</li>
					<li>10元</li>
					<li>15元</li>
					<li>20元</li>
					<li>30元</li>
					<li>60元</li>
					<li>100元</li>
					<li>200元</li>
				</ul>
			
		
			<div class="gayText">卡号：<input id="input" class="loginInput" name="" type="number" value="" /><br />密码：<input id="password" class="loginInput" name="" type="password" value="" />
			</div>
			<div class="div-blue"><input id="ok" class="button-blue" name="" type="button" value="  确   认  " /></div>
			<h6>温馨提示：</h6>
			<div>
				<p   class="gayText">1、如果支付金额大于订单金额，多付的钱会自动充值到您账号的e币；<br/>2、只支持Q币卡卡密支付，不支持QQ账户内Q币或Q点支付；<br />3、请务必使用与您所选面额相同的Q币卡进行支付，否则您将承担因此而引起的交易失败或者交易金额丢失所造成的损失。</p>
				
			</div>
		</div>
		
	</div>
	

</div>


<div class="company-info">梵付通24小时服务热线：0755-83734900</div>

<script src="/js/zepto.min.js"></script>
<script src="/js/common-wap.js?t=20130223"></script>
<script>
$(document).ready(function(){
	
	var money = $(".payMoney>li");
	var input = $("#input");
	var password = $("#password");
	var ok = $("#ok");
	var url = "pay.e?";
	var m;
	var c="QQCARD";

	(function(){
		m=$(".payMoney>li.on").html();
		m=m.substring(0,m.length-1);
	}())
	
	money.live("click touchStart",function(){
		$(this).addClass("on").siblings().removeClass("on");
		m = $(this).html();
		m=m.substring(0,m.length-1);
	});
	
	<%if(Constants.isTest){%>
		$("#input").val("106364399");
		$("#password").val("595305232513");
	<%}%>

	
	input.live("blur",function(){
	 	verifyCard()
	})
	
	
	password.live("blur",function(){
		//移动卡
		passwordCard()
		//showPrompt.init("输入错误")
	});
	
	function verifyCard(){
		var l = input.val().length;
		//Q币卡：9位的阿拉伯数字，密码是12位的阿拉伯数字。
			if(l!==9){
				showPrompt.init("请输入有效的卡号");
				return;
			}
			return true;
	}
	
	function passwordCard(){
		var l = password.val().length;
		//Q币卡：9位的阿拉伯数字，密码是12位的阿拉伯数字。
			if(l!==12){
				showPrompt.init("请输入有效的密码");
				return;
			}
			return true;
	}
	
	ok.live("click touchStart",function(){
		var am = parseFloat($("#actualMoney").val());
		if(verifyCard() && passwordCard()){
		
		
			if(m<am){
				showPrompt.init("抱歉,您选择的充值卡金额不够支付此订单，请重新选择!");
			//相等
			}else{
				var commitUrl = "/mobile/card.e?invoice="+$("#invoice").val()+"&channelType=9&cardAmt="+m+"&cardNumber="+input.val()+"&cardPwd="+password.val();
				var checkUrl = "/mobile/tradeResult.e"; //往这接口取支付确切结果
				var successUrl= "/mobile/redirect!showResult.e"; //支付成功，导向到支付结果页面；
				var failUrl= "/mobile/redirect!showResult.e"; //支付失败，走页面驱动
				submitCardInfo(commitUrl , checkUrl , successUrl ,failUrl );
			}
		}
	})

})
</script>
<%@ include file="../common/common_back.jsp" %>
</body>
</html>