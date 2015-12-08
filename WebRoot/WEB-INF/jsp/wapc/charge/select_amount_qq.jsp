<%@ page language="java" import="java.util.*"  contentType="application/xhtml+xml; charset=UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@page import="com.fantingame.pay.utils.Constants"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="application/xhtml+xml;charset=UTF-8"/>
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no,target-densitydpi=medium-dpi" />
<meta content="telephone=no" name="format-detection" />
<title>梵付通(WAP) - QQ点卡支付</title>
<link type="text/css" rel="stylesheet"  href="/css/common.css" />
</head>


<body>
<div class="userInfo">
	<span><span class="blue">充值账户：</span><s:property value="#attr.user.name"/></span>
</div>
<div class="mode">
	
	<div class="tel-mode">
		<div class="tel-money">
			<form action="card!cardCharge.e"  method="get">
				<input type="hidden" name="channelType" value="9"/>
				<div>
					<span class="radio"><input type="radio" name="cardAmt" value="5"  checked="checked"/>5元</span>
					<span class="radio"><input type="radio" name="cardAmt" value="10"/>10元</span>
					<span class="radio"><input type="radio" name="cardAmt" value="15"/>15元</span>
					<span class="radio"><input type="radio" name="cardAmt" value="20"/>20元</span>
					<br/>
					<span class="radio"><input type="radio" name="cardAmt" value="30"/>30元</span>
					<span class="radio"><input type="radio" name="cardAmt" value="60"/>60元</span>
					<span class="radio"><input type="radio" name="cardAmt" value="100"/>100元</span>
					<span class="radio"><input type="radio" name="cardAmt" value="200"/>200元</span>
				</div>
				
				<div class="gayText">
					<span class="threeWords">序列</span>号：<input  class="loginInput" name="cardNumber" type="text" value="" /><br/>
					<span class="twoWords">密</span>码：<input  class="loginInput" name="cardPwd" type="password" value="" />
				</div>
				<div class="div-blue"><input  class="button-blue"  type="submit" value="  确   认  " /></div>
			</form>
			<h6>温馨提示：</h6>
			<div>
				<p class="gayText">1、只支持Q币卡卡密支付，不支持QQ账户内Q币或Q点支付；<br />2、请务必使用与您所选面额相同的Q币卡进行支付，否则您将承担因此而引起的交易失败或者交易金额丢失所造成的损失。</p>
			</div>
		</div>
	</div>
</div>

<div class="company-info">梵付通24小时服务热线：0755-83734900</div>

</body>
</html>