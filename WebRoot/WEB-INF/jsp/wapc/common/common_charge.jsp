<%@ page language="java" import="java.util.*"  contentType="application/xhtml+xml; charset=UTF-8"%>
<%@page import="com.fantingame.pay.utils.Constants"%>
<%@page import="com.fantingame.pay.utils.MaskUtil"%>
<%String maskChannel=MaskUtil.getMaskChannelByCondition(request.getParameter("appId")); %>

<ul class="payMode">

	<%if(!maskChannel.contains("sms")) {%>
		<a href="/wapc/charge!selectAmount.e?type=select_amount_sms">
			<li class="msg">
				<span class="pic"><span></span></span>
				<span class="text">短信支付</span>
			</li>
		</a>
	<%}%>	
		
	<%if(!maskChannel.contains("mo9")) {%>
		<a href="/wapc/charge!selectAmount.e?type=select_amount_mo9">
			<li class="mo9">
				<span class="pic"><span></span></span>
				<span class="text">mo9支付</span>
			</li>
		</a>
	<%}%>	
		
	<%if(!maskChannel.contains("alipay")) {%>
		<a href="/wapc/charge!selectAmount.e?type=select_amount_ali">
			<li class="alipay">
				<span class="pic"><span></span></span>
				<span class="text">支付宝</span>
			</li>
		</a>
	<%}%>	
		
	<%if(!maskChannel.contains("mobilecard")) {%>
		<a href="/wapc/charge!selectAmount.e?type=select_amount_yidong">
			<li class="tel">
				<span class="pic"><span></span></span>
				<span class="text">话费卡</span>
			</li>
		</a>
	<%}%>	
		
	<%if(!maskChannel.contains("gamecard")) {%>
		<a href="/wapc/charge!selectAmount.e?type=select_amount_shengda">
			<li class="play">
				<span class="pic"><span></span></span>
				<span class="text">游戏点卡</span>
			</li>
		</a>
	<%}%>
		
	<%if(!maskChannel.contains("qbcard")) {%>
		<a href="/wapc/charge!selectAmount.e?type=select_amount_qq">
			<li class="qqbi">
				<span class="pic"><span></span></span>
				<span class="text">Q币卡</span>
			</li>
		</a>
	<%}%>
</ul>