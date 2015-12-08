<%@ page language="java" import="java.util.*"  contentType="application/xhtml+xml; charset=UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="application/xhtml+xml;charset=UTF-8"/>
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no,target-densitydpi=medium-dpi" />
<meta content="telephone=no" name="format-detection"/>
<title>梵付通-短信支付结果</title>
<link type="text/css" rel="stylesheet"  href="/css/common.css"/>
<style>
	#sendsms{
		color:#000;
		text-decoration:underline;
	}
</style>
</head>
<body>
<div class="mode">
	<h5>请用手机<span style="color:red;">${phoneNum}</span>发送以下内容完成支付</h5>
	<div id="tipText">
	<%
		String ua = request.getHeader("USER-AGENT");
		if(ua!=null){
			ua = ua.replaceAll("\\s+", "").toLowerCase();
			if(ua.contains("ucapplewebkit") || ua.contains("juc") ||  ua.contains("ucbrowser") || ua.contains("ucweb") || ua.contains("qqbrowser")){
				out.print("1.编辑短信<a id=\"sendsms\" href=\"sms:"+request.getAttribute("number")+"?body="+request.getAttribute("msg")+"\"><span style=\"color:red;\">"+request.getAttribute("msg")+"</span><span style=\"color:#3c3b3b;\">发送到</span><span style=\"color:red;\">"+request.getAttribute("number")+"</span></a>");
				out.print("&nbsp;&nbsp;&nbsp;&nbsp;<a id=\"sendsms\" href=\"sms:"+request.getAttribute("number")+"?body="+request.getAttribute("msg")+"\"><span style=\"color:red;\">点此发送</span></a><br/>");
				out.print("2.根据收到内容回复对应数字确认支付；<br/>");
				out.print("3.发送成功后，在本页面刷新查看支付结果。<br/>");
				out.print("4.如果\"点此发送\"有问题,请手工编辑短信并发送；");
			}else{
				out.print("1.编辑短信<span style=\"color:red;\">"+request.getAttribute("msg")+"</span><span style=\"color:#3c3b3b;\">发送到</span><span style=\"color:red;\">"+request.getAttribute("number")+"</span>；<br/>");
				out.print("2.根据收到内容回复对应数字确认支付；<br/>");
				out.print("3.发送成功后，在本页面刷新查看支付结果。");
			}
		}else{
			out.print("1.编辑短信<span style=\"color:red;\">"+request.getAttribute("msg")+"</span><span style=\"color:#3c3b3b;\">发送到</span><span style=\"color:red;\">"+request.getAttribute("number")+"</span>；<br/>");
			out.print("2.根据收到内容回复对应数字确认支付；<br/>");
			out.print("3.发送成功后，在本页面刷新查看支付结果。");
		}
	%>
	</div>
	<div id="payResult"></div>
	<div class="div-blue">
	   <a href="/wapc/tradeResult.e?invoice=<s:property value="invoice"/>"><input class="button-blue" name="" type="button" value="查看支付状态"/></a>
	   <a href="/wapc/redirect.e?invoice=${invoice}"><input class="button-blue" name="" type="button" value="返回"/></a>
	</div>
</div>
<%@ include file="../../common/common_footer.jsp" %>
</body>
</html>
