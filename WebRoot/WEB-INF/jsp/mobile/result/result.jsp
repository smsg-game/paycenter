<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@page import="java.net.URLEncoder"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<title>支付结果-${msg}</title>
<link type="text/css" rel="stylesheet"  href="/css/common.css" />
</head>
  
  <body>
	<div class="mode-noBg">
		<div>
			<c:if test="${type=='ali'}">
				<c:if test="${code=='9000'}">
					√ 支付成功
					<input type="hidden" id="android_com_easou_pay_code" name="android_com_easou_pay_code" value="999"/>
					<input type="hidden" id="android_com_easou_pay_msg" name="android_com_easou_pay_msg" value="支付成功"/>
				</c:if>
				<c:if test="${code!='9000'}">
					支付失败 : ${msg}
					<input type="hidden" id="android_com_easou_pay_code" name="android_com_easou_pay_code" value="${code}"/>
					<input type="hidden" id="android_com_easou_pay_msg" name="android_com_easou_pay_msg" value="${msg}"/>
				</c:if>
			</c:if>
			<c:if test="${type!='ali'}">
				<c:if test="${status=='success'}">
					√ 支付成功
					<input type="hidden" id="android_com_easou_pay_code" name="android_com_easou_pay_code" value="999"/>
					<input type="hidden" id="android_com_easou_pay_msg" name="android_com_easou_pay_msg" value="支付成功"/>
				</c:if>
				<c:if test="${status!='success'}">
					支付失败 : ${msg}
					<input type="hidden" id="android_com_easou_pay_code" name="android_com_easou_pay_code" value="6"/>
					<input type="hidden" id="android_com_easou_pay_msg" name="android_com_easou_pay_msg" value="${msg}"/>
				</c:if>
			</c:if>
		</div>
		
		<div class="div-blue"><a href="javascript:AndroidWindow.close();"><input class="button-blue" name="" type="button" value="返回"/></a></div>
		<input type="hidden" id="android_com_easou_pay_per_url" value="javascript:AndroidWindow.close();" />
		<input type="hidden" id="android_com_easou_pay_pagetag"  name="android_com_easou_pay_pagetag" value=""/>
	</div>
    <div class="company-info">梵付通24小时服务热线：0755-83734900</div>
  </body>
</html>
