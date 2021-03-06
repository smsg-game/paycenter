<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@page import="com.fantingame.pay.utils.Constants"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
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
<title>梵付通(手机版) - 手机充值卡支付</title>
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
	
	<div class="tel-mode"><div id="menu">
		<ul id="telCompany" class="tel-company">
			<li class="on" card="3">移动充值卡</li>
			<li card="4">联通充值卡</li>
			<li card="5">电信充值卡</li>
		</ul>
		</div>
		<div class="tel-money">
			<div>
				<ul id="payMoney-3" class="payMoney">
					<li class="on">10元</li>
					<li>20元</li>
					<li>30元</li>
					<li>50元</li>
					<li>100元</li>
					<li>200元</li>
					<li>300元</li>
					<li>500元</li>
					
				</ul>
				<ul id="payMoney-4" style="display:none" class="payMoney">
					<li class="on">20元</li>
					<li>30元</li>
					<li>50元</li>
					<li>100元</li>
					<li>300元</li>
					<li>500元</li>
					
				</ul>
				<ul id="payMoney-5"  style="display:none"  class="payMoney">
					<li class="on">50元</li>
					<li>100元</li>
					
				</ul>
			</div>
			
		    <!-- 
			<div class="gayText"><span class="threeWords">序列</span>号：<input id="input" class="loginInput" name="" type="number" value="在此输入序列号" /><br /><span class="twoWords">密</span>码：<input id="password" class="loginInput" name="" type="password" value="" />
			</div>
			-->
			<div class="gayText"><span class="threeWords">序列</span>号：<input id="input" class="loginInput" name="" type="number" value="" /><br/><span class="twoWords">密</span>码：<input id="password" class="loginInput" name="" type="password" value="" />
			</div>
			<div class="div-blue"><input id="ok" class="button-blue" name="" type="button" value="  确   认  " /></div>
			<h6>温馨提示：</h6>
			<div>
				<p  id="tit-3" class="gayText">1、如果支付金额大于订单金额，多付的钱会自动充值到您账号的e币；<br/>2、移动充值卡支持全国卡和浙江、福建地方卡，福建卡只支持50元、100元；<br />3、选择充值卡面值时，请与您的卡实际面值保持一致，以免造成充值失败和充值卡失效。</p>
				<p  id="tit-4"  style="display:none"  class="gayText">1、如果支付金额大于订单金额，多付的钱会自动充值到您账号的e币；<br/>2、联通充值卡只支持全国卡；<br />3、选择充值卡面值时，请与您的卡实际面值保持一致，以免造成充值失败和充值卡失效。</p>
				<p  id="tit-5"  style="display:none"  class="gayText">1、如果支付金额大于订单金额，多付的钱会自动充值到您账号的e币；<br/>2、电信充值卡支持全国卡和广东卡，序列号第四位为“1”的卡为全国卡，为“2”的则为地方卡；<br />3、选择充值卡面值时，请与您的卡实际面值保持一致，以免造成充值失败和充值卡失效。</p>
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
		<%if(Constants.isTest){%>
			if(n==1){   //联通卡
				$("#input").val("981201456548870");
				$("#password").val("9801134331059130706");
			}else if(n==0){  //移动卡
				$("#input").val("12427140118694573");
				$("#password").val("141286736524346279");
			}else if(n==2){  //电信卡
				$("#input").val("8511001208020063803");
				$("#password").val("858221002391390366");
			}
		<%}%>
		m=$(".payMoney").eq(n).find("li.on").html();
		m=m.substring(0,m.length-1);
		
		$(this).addClass("on").siblings().removeClass("on");
		c = encodeURIComponent($(this).attr("card"));
		
		$("#payMoney-"+c).show().siblings().hide();
		$("#tit-"+c).show().siblings().hide();
	});
	money.live("click touchStart",function(){
		$(this).addClass("on").siblings().removeClass("on");
		m =$(this).html();
		m=m.substring(0,m.length-1);
	});
	
	input.live("focus",function(){
		if($(this).val() === "在此输入序列号"){
			$(this).val("");
		}
	});
	
	input.live("blur",function(){
		verifyCard()
		//showPrompt.init("输入错误")
	})
	
	
	password.live("blur",function(){
		passwordCard()
		//showPrompt.init("输入错误")
	})
	
	function verifyCard(){
	
		var l = input.val().length;
		//移动卡：序列号：输入位数大于等于10，小于等于17.密码：输入位数大于等于8，小于等于18；
		if(c === "3"){
		
			if(l<10 || l>17){
				showPrompt.init("请输入有效的序列号");
				return false;
			}
		//联通卡：序列号15位阿拉伯数字，密码19位阿拉伯数字；
		}else if(c === "4"){
			if(l!== 15){
				showPrompt.init("请输入有效的序列号");
				return false;
			}
		//电信卡：卡号19位阿拉伯数字，密码18位阿拉伯数字；
		}else if(c === "5"){
			if(l!== 19){
				showPrompt.init("请输入有效的序列号");
				return false;
			}
		}
		return true;
	}
	
	
	function passwordCard(){
		//移动卡
		var l = password.val().length;
		
		//移动卡：序列号：输入位数大于等于10，小于等于17.密码：输入位数大于等于8，小于等于18；
		if(c === "3"){
			if(l<8 || l>18){
				showPrompt.init("请输入有效的密码");
				return false;
			}
		//联通卡：序列号15位阿拉伯数字，密码19位阿拉伯数字；
		}else if(c === "4"){
			if(l!== 19){
				showPrompt.init("请输入有效的密码");
				return false;
			}
		//电信卡：卡号19位阿拉伯数字，密码18位阿拉伯数字；
		}else if(c === "5"){
			if(l!== 18){
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
				var commitUrl = "/mobile/card.e?invoice="+$("#invoice").val()+"&channelType="+c+"&cardAmt="+m+"&cardNumber="+input.val()+"&cardPwd="+password.val();
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