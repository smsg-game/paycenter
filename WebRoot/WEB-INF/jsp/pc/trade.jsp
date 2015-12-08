<%@ page language="java" import="java.util.*"  contentType="text/html; charset=UTF-8"%>
<%@ page isELIgnored="false"%>
<%@page import="java.net.URLEncoder"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no,target-densitydpi=medium-dpi" />
<meta content="telephone=no" name="format-detection"/>
<title>梵付通</title>
<link type="text/css" rel="stylesheet"  href="/css/common.css"/>
<script type="text/javascript" src="/js/zepto.min.js"></script>
<script type="text/javascript" src="/js/urlstore.js?t=2012121111"></script>
</head>

<body>

<div class="userInfo">
	<span class="user-name"><%if(Constants.isTest){%><span style="color:red;"></span><%}%>用户名：<a href="/mobile/userDetail.e?appId=<s:property value="payTrade.appId"/>"><s:property value="#attr.user.name"/></a></span>
</div>

<div class="mode">
<h5>订单信息</h5>
	<div>
		<span>余额：${eb}&nbsp;e币 &nbsp;&nbsp;</span>
	    <span  class="user-money">
		<s:if test="payEb != null">
		     充值：<s:property value="payEb.reqFee*100"/>&nbsp;e币<br />
		</s:if>
		</span>
	</div>
</div>



<div class="mode">
	<s:else>
	  <h5>请选择支付方式</h5>
	</s:else>
	<div>
		<%@ include file="common/common_pay.jsp" %>
	</div>
</div>

<%@ include file="common/common_footer.jsp" %>
<%@ include file="common/common_back.jsp" %>
</body>
</html>
