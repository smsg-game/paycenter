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
	商品名称：${payTrade.tradeName}<br />
	需付金额：${payTrade.reqFee}元<br />
</div>

<div class="mode">
	
	<div class="tel-mode"><div id="menu">
		<ul id="telCompany" class="tel-company">
			<li><a href="/wapc/card!selectCardPage.e?type=shengda&amp;invoice=${payTrade.invoice}">盛大卡</a> </li>
			<li><a href="/wapc/card!selectCardPage.e?type=zhengtu&amp;invoice=${payTrade.invoice}">征途卡</a></li>
			<li  class="on">骏网一卡通</li>
			<li><a href="/wapc/card!selectCardPage.e?type=wanmei&amp;invoice=${payTrade.invoice}">完美一卡通</a></li>
		</ul>
		</div>
		<div class="tel-money">
			<form action="card.e"  method="get">
				<input type="hidden" name="invoice" value="${payTrade.invoice}"/>
				<input type="hidden" name="channelType" value="7"/>
				
				<div>
					<span class="radio"><input type="radio" name="cardAmt" value="10" checked="checked"/>10元</span>
					<span class="radio"><input type="radio" name="cardAmt" value="20" />20元</span>
					<span class="radio"><input type="radio" name="cardAmt" value="30" />30元</span>
					<span class="radio"><input type="radio" name="cardAmt" value="50"/>50元</span>
					<br/>
					<span class="radio"><input type="radio" name="cardAmt" value="100" />100元</span>
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
				<p  class="gayText">1、如果支付金额大于订单金额，多付的钱会自动充值到您账号的e币；<br/>2、不能使用特定游戏专属充值卡支付。 特定游戏包括大唐风云、传说、蜗牛、猫扑一卡通、九鼎、雅典娜、山河等游戏。<br />3、选择充值卡面值时，请与您的卡实际面值保持一致，以免造成充值失败和充值卡失效。</p>
			</div>
		</div>
	</div>
</div>

<div class="company-info">梵付通24小时服务热线：0755-83734900</div>

</body>
</html>