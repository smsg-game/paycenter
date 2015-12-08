<%@ page language="java" import="java.util.*"  contentType="application/xhtml+xml; charset=UTF-8"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page isELIgnored="false"%>
<%@page import="com.fantingame.pay.utils.StringTools"%>
<%@page import="com.fantingame.pay.manager.PayTradeManager"%>
<%@page import="com.fantingame.pay.entity.PayTrade"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.apache.struts2.ServletActionContext"%>
<%@page import="com.fantingame.pay.session.WapServletRequestWrapper"%>
<%@page import="org.apache.struts2.dispatcher.StrutsRequestWrapper"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="application/xhtml+xml;charset=UTF-8"/>
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no,target-densitydpi=medium-dpi" />
<meta content="telephone=no" name="format-detection"/>
<title>梵付通(WAP) - 出错!</title>
<link type="text/css" rel="stylesheet"  href="/css/common.css"/>
</head>

<body >


<div class="mode-noBg">
	
	<div>
		<br />原因：<s:property value="errorStr"/> 
		<br/>
		<%
			String backUrl = "";
			StrutsRequestWrapper strutsRequest = (StrutsRequestWrapper) ServletActionContext.getRequest();
			WapServletRequestWrapper wapRequest = (WapServletRequestWrapper) strutsRequest.getRequest();
		    String invoice = (String)wapRequest.getWapSession().getAttribute("firstTradeInvoice");
		    PayTradeManager payTradeManager = (PayTradeManager)
		    	(WebApplicationContextUtils.getRequiredWebApplicationContext(ServletActionContext.getServletContext()).getBean("payTradeManager"));
			PayTrade trade = payTradeManager.getEntityByInvoice(invoice);
			if(trade!=null){
				backUrl = trade.getRedirectUrl();
			}
		 %>
	</div>
	<div class="div-blue"><a href="/wap/redirect.e?invoice=${invoice}"><input class="button-blue" name="" type="button" value="返回"/></a></div>

</div>
<div class="company-info">梵付通24小时服务热线：0755-83734900</div>

</body>
</html>

