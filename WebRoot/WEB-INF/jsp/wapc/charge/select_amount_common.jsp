<%@ page language="java" import="java.util.*"  contentType="application/xhtml+xml; charset=UTF-8"%>
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
<meta http-equiv="Content-Type" content="application/xhtml+xml;charset=UTF-8"/>
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no,target-densitydpi=medium-dpi" />
<meta content="telephone=no" name="format-detection"/>
<title>梵付通(WAP) - 请选择充值金额</title>
<link type="text/css" rel="stylesheet"  href="/css/common.css"/>
</head>

<body>
<div class="userInfo">
	<span><span class="blue">充值账户：</span><s:property value="#attr.user.name"/></span>
</div>
<form class="mode"  <c:if test="${param.type=='select_amount_mo9'}">action="mo9!mo9Charge.e" </c:if>  <c:if test="${param.type=='select_amount_ali'}">action="ali!aliCharge.e" </c:if>method="get">
	<h5>请选择充值金额（1元=<span class="red">100e币</span>）</h5>
	<div>
		<span class="radio"><input type="radio" name="money" value="10" checked="checked"/>10元</span>
		<span class="radio"><input type="radio" name="money" value="30"/>30元</span>
		<span class="radio"><input type="radio" name="money" value="50"/>50元</span>
		<span class="radio"><input type="radio" name="money" value="100"/>100元</span>
		<span class="radio"><input type="radio" name="money" value="300"/>300元</span>
		<br/>
		<span class="radio"><input type="radio" name="money" value="500"/>500元</span>
		<span class="radio"><input type="radio" name="money" value="1000"/>1000元</span>
		<span class="radio"><input type="radio" name="money" value="3000"/>3000元</span>
		<span class="radio"><input type="radio" name="money" value="5000"/>5000元</span>
	</div>	
	<div class="manualPay">
		其他金额 ：<input class="loginInput" name="otherMoney" type="text" value="" maxlength="5"/>
	</div>
	<div class="div-blue"><input  class="button-blue"  type="submit" value="提交" /></div>
</form>

<c:if test="${param.type=='select_amount_ali'}">
  <div class="company-info">*您将转入<span class="red">支付宝</span>官方页面进行充值</div>
</c:if>
<c:if test="${param.type=='select_amount_mo9'}">
  <div class="company-info">*您将转入<span class="red">魔9</span>官方页面进行充值</div>
</c:if>
</body>
</html>
