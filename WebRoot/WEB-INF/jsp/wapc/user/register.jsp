<%@ page language="java" import="java.util.*"  contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no,target-densitydpi=medium-dpi" />
<meta content="telephone=no" name="format-detection">
<title>梵付通(WAP) - 欢迎登陆</title>
<link type="text/css" rel="stylesheet"  href="/css/common.css" />
 <script type="text/javascript">
  		function register(){
  			document.getElementById("frm").submit();
  		}
  </script>
</head>

<body >
<form  action="login.e" method="post">
<table class="loginTable"  border="0" cellspacing="0" cellpadding="0">
  <input type="hidden" id="bu"  name="bu" value="${bu}"/>
  <tr>
    <td width="100px">用户名：</td>
    <td><input class="loginInput w-200"  name="username"  value="" type="text" /></td>
  </tr>
  <tr>
    <td>密&nbsp;&nbsp;&nbsp;码：</td>
    <td><input class="loginInput w-200" name="password" value="" type="password" /></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td><input class="button-blue w-220 " name="" type="submit" value="登  录" /></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td><input class="button-blue  w-220" onclick="register();" name="" type="button" value="快速注册" /></td>
  </tr>
  <c:if test="${param.errMsg!=null}">
   <tr>
   <td>&nbsp;</td>
    <td ><a  class="left" style="color: red;">
    <c:if test="${param.errMsg=='login_error'}"> 用户名或密码错误，请重新输入！</c:if>
    <c:if test="${param.errMsg=='register_error'}"> 注册失败，请重新注册！</c:if>
    </a></td>
  </tr>
  </c:if>
   <tr>
    <td>&nbsp;</td>
    <td><a href="/pub/service.html" class="service left">服务协议</a><a href="/pub/forgetPwd.html" class="forgot right">忘记密码</a></td>
  </tr>
</table>
</form>
<form id="frm" action="register.e" method="post">
	<input type="hidden" name="bu" value="${bu}"/>
</form>

<div class="company-info">梵付通24小时服务热线：0755-83734900</div>

</body>
</html>
