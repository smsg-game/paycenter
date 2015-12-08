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
<title>梵付通-短信支付结果</title>
<link type="text/css" rel="stylesheet"  href="/css/common.css"/>
</head>
<body>
<div class="mode">
	<h5>提示</h5>
	<div id="tipText">
		<s:if test="#parameters.status[0]=='success'">
		      短信支付成功。
		</s:if>
		<s:else>
		   ${param.msg}
		</s:else>
	</div>
	<div id="payResult"></div>
	<div class="div-blue">
	   <a id="backBtn" href="/mobile/redirect.e?invoice=${param.invoice}"><input class="button-blue" name="" id="btnText" type="button" value="返回"/></a>
	</div>
</div>
<%@ include file="../../common/common_footer.jsp" %>
</body>
</html>
