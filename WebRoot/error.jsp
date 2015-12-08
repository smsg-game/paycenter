<%@ page language="java" import="java.util.*"  contentType="text/html; charset=UTF-8"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page isELIgnored="false"%>
<%@page import="com.fantingame.pay.utils.StringTools"%>
<%@page import="org.apache.struts2.ServletActionContext"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no,target-densitydpi=medium-dpi" />
<meta content="telephone=no" name="format-detection">
<title>出错!</title>
<link type="text/css" rel="stylesheet"  href="/css/common.css"/>
</head>

<body >


<div class="mode-noBg">
	
	<div>
		<br />原因：<s:property value="errorStr"/> 
		<br/>
		<%
		   Object obj = ServletActionContext.getValueStack((HttpServletRequest)pageContext.getRequest()).findValue("errorStr");
			String msg = "异常!";
			if(obj!=null){
				msg = obj.toString() ;
			}
		 %>
	</div>
	<div class="div-blue"><input  class="button-blue" name="" type="button" value="返回" onclick="javascript:window.AndroidWindow.close()" /></div>
	<input type="hidden" id="android_com_easou_pay_code" name="android_com_easou_pay_code" value="6"/>
	<input type="hidden" id="android_com_easou_pay_msg" name="android_com_easou_pay_msg" value="<%=msg%>"/>
	<input type="hidden" id="android_com_easou_pay_per_url" value="javascript:window.AndroidWindow.close();" />
	<input type="hidden" id="android_com_easou_pay_pagetag"  name="android_com_easou_pay_pagetag" value=""/>
		

</div>
<div class="company-info">梵付通24小时服务热线：0755-83734900</div>

</body>
</html>

