<%@ page language="java" import="java.util.*"  contentType="text/html; charset=UTF-8"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page isELIgnored="false"%>
<%@page import="com.fantingame.pay.utils.Constants"%>
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
<title>梵付通(WAP) - 短信支付</title>
<link type="text/css" rel="stylesheet"  href="/css/common.css"/>
</head>

<body>
<div>
    <div class="userInfo">
	   <span><span class="blue">当前账户：</span><s:property value="#attr.user.name"/></span>
    </div>
	<div class="mode">
		<input type="hidden" value="5" name="money" id="money"/>
		<div>
			<ul class="payMoney">
				<li class="on">5元</li>
				<li>10元</li>
				<li>15元</li>
				<li>20元</li>
				<li>30元</li>
			</ul>
			<div class="gayText">
				手机号：<input id="phoneNum" class="loginInput" name="phoneNum" type="text" value="${phoneNo}"/>
			</div>
			<div>短信充值<span id="money1" style="color:red;">5</span>元=<span id="eb" style="color:red;">250</span>e币(<span id="money2" style="color:red;">2.5</span>元充值费用，<span id="money3" style="color:red;">2.5</span>元渠道费用)</div>
		</div>
		<div class="div-blue"><input id="ok" class="button-blue" name="" type="button" value="确定支付" /></div>
	    <h6>温馨提示：</h6>
	    <div>
			<p id="tit-6" class="gayText">
				1.短信充值目前只支持移动卡（新疆、宁夏、甘肃三省暂不支持）；<br />
				2.每天5元可以支付两次，其余一次；<br />
				3.每月支付总额不能超过70元；
		     </p>
	    </div>
	</div>
	
</div>

<script src="/js/zepto.min.js"></script>
<script src="/js/common-wap.js?t=20130223"></script>
<script>
$(document).ready(function(){
	var li = $(".payMoney>li");
	var money = $("#money");
	var phoneNum = $("#phoneNum");
	li.live("click touchStart",function(){
		$(this).addClass("on").siblings().removeClass("on");
		var value = parseInt($(this).html());
		money.val(value);
		$("#money1").html(value);
		$("#money2").html(value/2);
		$("#money3").html(value/2);
		$("#eb").html(value * 50);
	});
	phoneNum.live("focus",function(){
		if(phoneNum.val()==="请输入您的手机号"){
			phoneNum.val("")
		}
	});
	$("#ok").live("click touchStart",function(){
	    if(verify()){
	       location.href= "/wap/smsYhxf!smsCharge.e?money="+money.val()+"&phoneNum="+phoneNum.val();
	    } 
	})
	
	function verify(){
	    if(phoneNum.val()=="" || phoneNum.val()=="请输入您的手机号" || !new RegExp(/^(13|14|15|18)[0-9]{9}$/).test(phoneNum.val())){
	        showPrompt.init("请输入正确的手机号");
	        return false;
	    }
	    return true;
	}
	
})
</script>
</body>
</html>
