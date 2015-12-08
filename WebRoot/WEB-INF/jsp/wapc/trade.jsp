<%@ page language="java" import="java.util.*"  contentType="application/xhtml+xml; charset=UTF-8"%>
<%@ page isELIgnored="false"%>
<%@page import="java.net.URLEncoder"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="application/xhtml+xml;charset=UTF-8"/>
<meta http-equiv="Cache-Control" content="no-cache"/>
<title>梵付通</title>
<link type="text/css" rel="stylesheet"  href="/css/common.css"/>
</head>

<body>

<div class="userInfo">
	<span class="user-name">用户名：<a href="/wapc/userDetail.e?appId=<s:property value="payTrade.appId"/>"><s:property value="#attr.user.name"/></a></span>
</div>

<div class="mode">
<h5>订单信息</h5>
	<div>
	    <span>余额：${eb}&nbsp;e币 &nbsp;&nbsp;<a href="/wapc/charge.e?appId=<s:property value="payTrade.appId"/>&amp;backUrl=<%=URLEncoder.encode("/wapc/trade!toIndex.e")%>">充值</a></span>
	    <span  class="user-money">
		<s:if test="payTrade != null">
		     需支付：<s:property value="payTrade.reqEb"/>&nbsp;e币<br />
		</s:if>
		</span>
		<s:if test="eb >=payTrade.reqEb">
		   <div class="div-blue"><a href="/wapc/eb.e?invoice=<s:property value="payTrade.invoice"/>"><input class="button-blue" name="" type="button" value="确定e币支付" /></a></div>
		</s:if>
	</div>
</div>



<div class="mode">
    <s:if test="eb >= payTrade.reqEb">
	  <h5>其他支付方式</h5>
	</s:if>
	<s:else>
	  <h5>请选择支付方式</h5>
	</s:else>
	<div>
	   <%@ include file="common/common_pay.jsp" %>
	</div>
</div>

<%@ include file="common/common_footer.jsp" %>
</body>
</html>
