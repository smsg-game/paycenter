<%@ page language="java" import="java.util.*"  contentType="application/xhtml+xml; charset=UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@page import="com.fantingame.pay.utils.Constants"%>

<!DOCTYPE html PUBLIC "-//WAPFORUM//DTD XHTML Mobile 1.0//EN" "http://www.wapforum.org/DTD/xhtml-mobile10.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="application/xhtml+xml;charset=UTF-8"/>
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no,target-densitydpi=medium-dpi" />
<meta http-equiv="Cache-Control" content="no-cache"/>
<title>梵付通(WAP简) - 移动充值卡支付</title>
<link type="text/css" rel="stylesheet"  href="/css/common.css" />
</head>


<body>
<div class="userInfo">
	<span><span class="blue">充值账户：</span><s:property value="#attr.user.name"/></span>
</div>
<div class="mode">
	
	<div class="tel-mode"><div id="menu">
		<ul id="telCompany" class="tel-company">
			<li class="on" >移动充值卡</li>
			<li><a href="/wapc/charge!selectAmount.e?type=select_amount_liantong">联通充值卡</a></li>
			<li><a href="/wapc/charge!selectAmount.e?type=select_amount_dianxin">电信充值卡</a> </li>
		</ul>
		</div>
		<div class="tel-money">
			<form action="card!cardCharge.e"  method="get">
				<input type="hidden" name="channelType" value="3"/>
				<div>
					<span class="radio"><input type="radio" name="cardAmt" value="20" checked="checked"/>20元</span>
					<span class="radio"><input type="radio" name="cardAmt" value="30" />30元</span>
					<span class="radio"><input type="radio" name="cardAmt" value="50" />50元</span>
					<span class="radio"><input type="radio" name="cardAmt" value="100"/>100元</span>
					<br/>
					<span class="radio"><input type="radio" name="cardAmt" value="200" />200元</span>
					<span class="radio"><input type="radio" name="cardAmt" value="300" />300元</span>
					<span class="radio"><input type="radio" name="cardAmt" value="500" />500元</span>
				</div>
				
				<div class="gayText">
					<span class="threeWords">序列</span>号：<input  class="loginInput" name="cardNumber" type="text" value="" /><br/>
					<span class="twoWords">密</span>码：<input  class="loginInput" name="cardPwd" type="password" value="" />
				</div>
				<div class="div-blue"><input  class="button-blue"  type="submit" value="  确   认  " /></div>
			</form>
			<h6>温馨提示：</h6>
			<div>
				<p  class="gayText">1、移动充值卡支持全国卡和浙江、福建地方卡，福建卡只支持50元、100元；<br />2、选择充值卡面值时，请与您的卡实际面值保持一致，以免造成充值失败和充值卡失效。</p>
			</div>
		</div>
	</div>
</div>

<div class="company-info">梵付通24小时服务热线：0755-83734900</div>

</body>
</html>