<%@ page language="java" import="java.util.*"  contentType="text/html; charset=UTF-8"%>
<%@ page   isELIgnored="false"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no,target-densitydpi=medium-dpi" />
<meta content="telephone=no" name="format-detection">
<title>梵付通(WAP)-注册成功</title>
<link type="text/css" rel="stylesheet"  href="/css/common.css" />
</head>

<body >


<div class="mode">
	<h5>注册成功，请记住您的用户/密码！</h5>
	<div>
		<span class="threeWords">用户</span>名：${username}<br /><span class="twoWords">密</span>码：${password}
	</div>
	<div class="div-blue">
	 <form action="login.e" method="post">
    	<input type="hidden" value="${username}" name="username" />
    	<input type="hidden" value="${password}" name="password" />
    	<input id="ok" class="button-blue" name="" type="submit" value="立即登录" />
    	<input   type="hidden" value="${bu}" name="bu"/>
    </form>
	</div>
</div>
<div class="company-info">梵付通24小时服务热线：0755-83734900</div>



</body>
</html>
