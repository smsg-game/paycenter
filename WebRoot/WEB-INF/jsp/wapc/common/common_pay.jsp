<%@ page language="java" import="java.util.*"  contentType="application/xhtml+xml; charset=UTF-8"%>
<%@page import="com.fantingame.pay.utils.Constants"%>
<%@page import="com.fantingame.pay.utils.MaskUtil"%>
<%String maskChannel=MaskUtil.getMaskChannelByCondition(request.getParameter("appId")); %>

<ul class="payMode">
	<%if(!maskChannel.contains("sms")) {%>
	    <a href="/wapc/sms.e?invoice=<s:property value="payTrade.invoice"/>">
			<li class="msg">
				<span class="pic"><span></span></span>
				<span class="text">短信支付</span>
			</li>
		</a>
	<%}%>
	<%-- 
	<%if(!maskChannel.contains("mo9")) {%>
		<a href="/wapc/mo9.e?invoice=<s:property value="payTrade.invoice"/>">
			<li class="mo9">
				<span class="pic"><span></span></span>
				<span class="text">mo9支付</span>
			</li>
		</a>
	<%}%>	
	--%>	
	<%if(!maskChannel.contains("alipay")) {%>
		<a href="/wapc/ali.e?invoice=<s:property value="payTrade.invoice"/>">
			<li class="alipay">
				<span class="pic"><span></span></span>
				<span class="text">支付宝</span>
			</li>
		</a>
	<%}%>
	
	<%if(!maskChannel.contains("mobilecard")) {%>
		<a href="/wapc/card!selectCardPage.e?type=yidong&amp;invoice=<s:property value="payTrade.invoice"/>">
			<li class="tel">
				<span class="pic"><span></span></span>
				<span class="text">手机充值卡</span>
			</li>
		</a>
	<%}%>
	
	<%if(!maskChannel.contains("gamecard")) {%>	
		<a href="/wapc/card!selectCardPage.e?type=shengda&amp;invoice=<s:property value="payTrade.invoice"/>">
			<li class="play">
				<span class="pic"><span></span></span>
				<span class="text">游戏点卡</span>
			</li>
		</a>
	<%}%>
	
	<%if(!maskChannel.contains("qbcard")) {%>	
		<a href="/wapc/card!selectCardPage.e?type=qq&amp;invoice=<s:property value="payTrade.invoice"/>">
			<li class="qqbi">
				<span class="pic"><span></span></span>
				<span class="text">Q币卡</span>
			</li>
		</a>
	<%}%>
</ul>