<%@ page language="java" import="java.util.*"  contentType="text/html; charset=UTF-8"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page isELIgnored="false"%>
<%@ page import="com.fantingame.pay.utils.Constants"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<% String url = request.getScheme() + "://" + request.getServerName()+ request.getContextPath(); %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no,target-densitydpi=medium-dpi" />
<meta content="telephone=no" name="format-detection"/>
<title>梵付通(手机版) - 请选择充值金额</title>
<link type="text/css" rel="stylesheet"  href="/css/common.css"/>
<script type="text/javascript" src="/js/urlstore.js?t=2012121111"></script>
</head>

<body>
<div>
<div class="userInfo">
	<span ><span class="blue">充值账户：</span><s:property value="#attr.user.name"/></span>
</div>

<div class="mode">
	<h5>请选择充值金额（1元=<span class="red">100e币</span>）</h5>
	<div>
		<ul class="payMoney">
			<li  class="on">10元</li>
			<li>30元</li>
			<li>50元</li>
			<li>100元</li>
			<li>300元</li>
			<li>500元</li>
			<li>1000元</li>
			<li>3000元</li>
			<li>5000元</li>
		</ul>
		<div class="manualPay">
			<input id="money" class="loginInput left" name="" type="number" value="10" maxlength="5"/><div class="getPay left">你将充值：<span class="red"><span id="e">1000</span>e币</span></div>
		</div>
		
	</div>
	</div>
	<div class="div-blue"><input id="ok" class="button-blue" name="" type="button" value="确定充值" /></div>
</div>

<c:if test="${param.type=='select_amount_ali'}">
  <div class="company-info">*您将转入<span class="red">支付宝</span>官方页面进行充值</div>
</c:if>
<c:if test="${param.type=='select_amount_mo9'}">
  <div class="company-info">*您将转入<span class="red">摩9</span>官方页面进行充值</div>
</c:if>

<script src="/js/zepto.min.js"></script>
<script>
$(document).ready(function(){
	var li = $(".payMoney>li");
	var e = $("#e");
	var money = $("#money");
	var n = 100;
	li.live("click touchStart",function(){
		$(this).addClass("on").siblings().removeClass("on");
		var value = parseInt($(this).html());
		e.html(value*n);
		money.val(value);
	});
	money.live("focus",function(){
		li.removeClass("on");
		if(money.val()==="请输入金额/元"){
			money.val("")
		}
	});
	money.live("keyup",function(){
		var value =parseFloat($(this).val());
		if(isNaN(value)){
			value = 0;
		}
		e.html(parseInt(value*n));
	});
	$("#ok").live("click touchStart",function(){
		var baseUrl = "<%=url%>";
		if("<%=request.getParameter("channelId")%>"=="<%=Constants.CHANNEL_ID_ALIPAY%>"){
			var requestUrl=baseUrl+"/mobile/ali!aliCharge.e?money="+money.val();
			var returnUrl =baseUrl+"/mobile/redirect!showResult.e?type=ali";
			startAli(requestUrl,returnUrl);            
		}else{
			location.href= "/mobile/mo9!mo9Charge.e?money="+money.val();
		}
	})
})
</script>
<%@ include file="../common/common_back.jsp" %>
</body>
</html>
