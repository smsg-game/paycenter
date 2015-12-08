<%@ page language="java" import="java.util.*"  contentType="text/html; charset=UTF-8"%>
<%@page import="com.fantingame.pay.utils.Constants"%>

<ul class="payMode">
	<%-- 
	<a href="/wap/mo9.e?invoice=<s:property value="payTrade.invoice"/>">
		<li class="mo9">
			<span class="pic"><span></span></span>
			<span class="text">mo9支付</span>
		</li>
	</a>
	--%>
	<a href="/wap/ali.e?invoice=<s:property value="payTrade.invoice"/>">
		<li class="alipay">
			<span class="pic"><span></span></span>
			<span class="text">支付宝</span>
		</li>
	</a>

	<a href="/wap/card!selectCardPage.e?type=gamecard&invoice=<s:property value="payTrade.invoice"/>">
		<li class="play">
			<span class="pic"><span></span></span>
			<span class="text">游戏点卡</span>
		</li>
	</a>
	
	<a href="/wap/card!selectCardPage.e?type=qqcard&invoice=<s:property value="payTrade.invoice"/>">
		<li class="qqbi">
			<span class="pic"><span></span></span>
			<span class="text">Q币卡</span>
		</li>
	</a>
	
	<a href="/wap/card!selectCardPage.e?type=phonecard&invoice=<s:property value="payTrade.invoice"/>">
		<li class="tel">
			<span class="pic"><span></span></span>
			<span class="text">手机充值卡</span>
		</li>
	</a>
</ul>