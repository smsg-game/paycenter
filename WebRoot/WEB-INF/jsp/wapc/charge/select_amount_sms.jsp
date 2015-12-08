<%@ page language="java" import="java.util.*"  contentType="application/xhtml+xml; charset=UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<% String url = request.getScheme() + "://" + request.getServerName()+ request.getContextPath(); %>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="application/xhtml+xml;charset=UTF-8"/>
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no,target-densitydpi=medium-dpi" />
<meta content="telephone=no" name="format-detection"/>
<title>梵付通(WAP) - 短信支付</title>
<link type="text/css" rel="stylesheet"  href="/css/common.css"/>
</head>

<body>
<div>
    <div class="userInfo">
	   <span><span class="blue">当前账户：</span><s:property value="#attr.user.name"/></span>
    </div>
	<div class="mode">
		<input type="hidden" value="5" name="money" id="money"/>
		<form action="/wapc/smsYhxf!smsCharge.e"  method="get">
			<div>
				<span class="radio"><input type="radio" name="money" value="5" checked="checked"/>5元</span>
				<span class="radio"><input type="radio" name="money" value="10"/>10元</span>
				<span class="radio"><input type="radio" name="money" value="15"/>15元</span>
				<span class="radio"><input type="radio" name="money" value="20"/>20元</span>
				<span class="radio"><input type="radio" name="money" value="30"/>30元</span>
			</div>	
			<div class="gayText">
				手机号：<input id="phoneNum" class="loginInput" name="phoneNum" type="text" value="${phoneNo}"/>
				<div>短信充值<span id="money1" style="color:red;">5</span>元=<span id="eb" style="color:red;">250</span>e币(<span id="money2" style="color:red;">2.5</span>元充值费用，<span id="money3" style="color:red;">2.5</span>元渠道费用)</div>
			</div>
			<div class="div-blue"><input  class="button-blue"  type="submit" value="  确   认  " /></div>
	    </form>
	</div>
		
	<h6>温馨提示：</h6>
	<div>
	    <p id="tit-6" class="gayText">
		    1.短信充值目前只支持移动卡（新疆、宁夏、甘肃三省暂不支持）；<br/>
			2.每天5元可以支付两次，其余一次；<br/>
			3.每月支付总额不能超过70元；
		</p>
	</div>
</div>
</body>
</html>
