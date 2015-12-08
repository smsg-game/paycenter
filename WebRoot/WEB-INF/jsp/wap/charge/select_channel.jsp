<%@ page language="java" import="java.util.*"  contentType="text/html; charset=UTF-8"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page isELIgnored="false"%>
<%@ page import="com.fantingame.pay.utils.Constants"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no,target-densitydpi=medium-dpi" />
<meta content="telephone=no" name="format-detection"/>
<title>梵付通(WAP) - 请选择充值方式</title>
<link type="text/css" rel="stylesheet"  href="/css/common.css"/>
</head>

<body >

<div class="userInfo">
	<span class="user-name"><%if(Constants.isTest){%><span style="color:red;">(测试)</span><%}%>用户名：<s:property value="#attr.user.name"/></span>
</div>

<div class="mode">
	<h5>请选择充值方式</h5>
	<div>
		<%@ include file="../common/common_charge.jsp" %>
	</div>
	<h6>温馨提示：</h6>
	<div>
		<p id="tit-6" class="gayText">
			e币是梵町发行虚拟货币，可用于梵町产品（包括游戏、小说、音乐等）的消费。1元=100e币。
		</p>
	</div>
</div>

<%@ include file="../common/common_footer.jsp" %>
</body>
</html>
