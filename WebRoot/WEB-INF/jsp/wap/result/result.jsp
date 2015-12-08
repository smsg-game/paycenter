<%@ page language="java" contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
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
<title>梵付通(WAP) - 支付结果</title>
<link type="text/css" rel="stylesheet"  href="/css/common.css" />
</head>
  
 <body>
	<div class="mode-noBg">
		<div>
		   <s:if test="#request.status=='success'">
		       √ 支付成功！
		   </s:if>
		   <s:else>
		       ${msg}
		   </s:else>
		</div>
		<div class="div-blue"><a href="/wap/redirect.e?invoice=${invoice}"><input class="button-blue" name="" type="button" value="返回"/></a></div>
	</div>
    <div class="company-info">梵付通24小时服务热线：0755-83734900</div>
 </body>

</html>
