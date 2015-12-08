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
<title>梵付通(WAP) - 游戏点卡支付</title>
<link type="text/css" rel="stylesheet"  href="/css/common.css" />
</head>


<body>
<div class="userInfo">
	<span><span class="blue">充值账户：</span><s:property value="#attr.user.name"/></span>
</div>
<div class="mode">
	<div class="tel-mode"><div id="menu">
		<ul id="telCompany" class="tel-company">
			<li class="on">盛大卡</li>
			<li><a href="/wapc/charge!selectAmount.e?type=select_amount_zhengtu">征途卡</a></li>
			<li><a href="/wapc/charge!selectAmount.e?type=select_amount_junwang">骏网一卡通</a> </li>
			<li><a href="/wapc/charge!selectAmount.e?type=select_amount_wanmei">完美一卡通</a></li>
		</ul>
		</div>
		<div class="tel-money">
			<form action="card!cardCharge.e"  method="get">
				<input type="hidden" name="channelType" value="6"/>
				<div>
					<span class="radio"><input type="radio" name="cardAmt" value="10" checked="checked"/>10元</span>
					<span class="radio"><input type="radio" name="cardAmt" value="15" />15元</span>
					<span class="radio"><input type="radio" name="cardAmt" value="25" />25元</span>
					<span class="radio"><input type="radio" name="cardAmt" value="30"/>30元</span>
					<br/>
					<span class="radio"><input type="radio" name="cardAmt" value="35" />35元</span>
					<span class="radio"><input type="radio" name="cardAmt" value="50" />50元</span>
					<span class="radio"><input type="radio" name="cardAmt" value="100" />100元</span>
					<span class="radio"><input type="radio" name="cardAmt" value="1000" />1000元</span>
				</div>
				
				<div class="gayText">
					<span class="threeWords">序列</span>号：<input  class="loginInput" name="cardNumber" type="text" value="" /><br/>
					<span class="twoWords">密</span>码：<input  class="loginInput" name="cardPwd" type="password" value="" />
				</div>
				<div class="div-blue"><input  class="button-blue"  type="submit" value="  确   认  " /></div>
			</form>
			<h6>温馨提示：</h6>
			<div>
				<p  class="gayText">1、请使用卡号以CSC5、CS、S、CA、CSB、YA、YB、YC、YD、80133开头的"盛大互动娱乐卡"进行支付；<br />2、选择充值卡面值时，请与您的卡实际面值保持一致，以免造成充值失败和充值卡失效。</p>
			</div>
		</div>
	</div>
</div>

<div class="company-info">梵付通24小时服务热线：0755-83734900</div>

</body>
</html>