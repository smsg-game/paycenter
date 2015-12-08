<%@ page language="java" import="java.util.*"  contentType="text/html; charset=UTF-8"%>
<%@page import="com.fantingame.pay.utils.Constants"%>
<%@page import="com.fantingame.pay.utils.MaskUtil"%>

<ul class="payMode">
	<%
		String maskChannel=MaskUtil.getMaskChannelByCondition(request.getParameter("appId")); 
		String url = request.getScheme() + "://" + request.getServerName()+ request.getContextPath();
		String url2 = request.getScheme() + "://" + request.getServerName();
	%>
	
	<%if(!maskChannel.contains("alipay")) {%>
		<a href="<%=url %>/pc/pcali.e?id=<s:property value="payEb.id"/>">
			<li class="alipay">
				<span class="pic"><span></span></span>
				<span class="text">支付宝</span>
			</li>
		</a> 
	<%}%>
	<%if(!maskChannel.contains("tenpay")) {%>
		<a href="<%=url %>/pc/pctenpay.e?id=<s:property value="payEb.id"/>">
			<li class="tenpay">
				<span class="pic"><span></span></span>
				<span class="text">财付通</span>
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