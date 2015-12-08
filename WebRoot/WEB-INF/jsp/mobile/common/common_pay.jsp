<%@ page language="java" import="java.util.*"  contentType="text/html; charset=UTF-8"%>
<%@page import="com.fantingame.pay.utils.Constants"%>
<%@page import="com.fantingame.pay.utils.MaskUtil"%>

<ul class="payMode">
	<%
		String maskChannel=MaskUtil.getMaskChannelByCondition(request.getParameter("appId")); 
		String url = request.getScheme() + "://" + request.getServerName()+ request.getContextPath();
		String url2 = request.getScheme() + "://" + request.getServerName();
	%>
	
	<%if(!maskChannel.contains("sms")) {%>
		<a href="#" onclick="checkMoney(15,'/mobile/sms.e?invoice=${payTrade.invoice}');">
			<li class="msg">
				<span class="pic"><span></span></span>
				<span class="text">短信支付</span>
			</li>
		</a>
	<%}%>
	<%-- 
	<%if(!maskChannel.contains("mo9")) {%>
		<a href="/mobile/mo9.e?invoice=<s:property value='payTrade.invoice'/>">
			<li class="mo9">
				<span class="pic"><span></span></span>
				<span class="text">mo9支付</span>
			</li>
		</a>
	<%}%>
	--%>
	<%if(!maskChannel.contains("alipay")) {%>
		<a href="<%=url %>/pc/pcali.e?invoice=<s:property value="payTrade.invoice"/>">
			<li class="alipay">
				<span class="pic"><span></span></span>
				<span class="text">支付宝</span>
			</li>
		</a> 
	<%}%>
	
	<%if(!maskChannel.contains("mobilecard")) {%>
		<a href="/mobile/card!selectCardPage.e?type=phonecard&invoice=<s:property value="payTrade.invoice"/>">
			<li class="tel">
				<span class="pic"><span></span></span>
				<span class="text">手机充值卡</span>
			</li>
		</a>
	<%}%>
	
	<%if(!maskChannel.contains("gamecard")) {%>
		<a href="/mobile/card!selectCardPage.e?type=gamecard&invoice=<s:property value="payTrade.invoice"/>">
			<li class="play">
				<span class="pic"><span></span></span>
				<span class="text">游戏点卡</span>
			</li>
		</a>
	<%}%>
	
	<%if(!maskChannel.contains("qbcard")) {%>
		<a href="/mobile/card!selectCardPage.e?type=qqcard&invoice=<s:property value="payTrade.invoice"/>">
			<li class="qqbi">
				<span class="pic"><span></span></span>
				<span class="text">Q币卡</span>
			</li>
		</a>
	<%}%>
</ul>
<script type="text/javascript" src="/js/zepto.min.js"></script>
<script src="/js/common-wap.js?t=20130223"></script>
<script type="text/javascript">
   var ac = ${payTrade.reqFee};
   function checkMoney(maxMoney,url){
        if(maxMoney<ac){
           showPrompt.init("短信支付暂不支持高额订单（15元以上），建议您选择支付宝/话费卡等其他支付方式。");
           return false;
        }else{
           location.href = url;
        }
   }
</script>