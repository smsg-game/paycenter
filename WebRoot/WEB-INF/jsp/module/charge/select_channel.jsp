<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8"%>
<%@ page isELIgnored="false"%>
<%@page import="java.net.URLEncoder"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.fantingame.pay.utils.Constants"%>
<%@page import="com.fantingame.pay.utils.MaskUtil"%>
<%String maskChannel=MaskUtil.getMaskChannelByCondition(request.getParameter("appId")); %>
<%
	String imgDomain = request.getContextPath();
	request.setAttribute("imgDomain", imgDomain);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
  <meta name="apple-mobile-web-app-capable" content="yes" />
  <meta name="apple-mobile-web-app-status-bar-style" content="black" />
  <title>e币支付</title>
  <link rel="stylesheet" href="${imgDomain}/js/vendor/jquery-mobile/jquery.mobile-1.3.1.min.css" />
  <link rel="stylesheet" href="${imgDomain}/css/module.css" />
  <script src="${domain}/js/vendor/jquery/jquery-1.9.1.min.js"> </script> 
  <script src="${imgDomain}/js/vendor/jquery-mobile/jquery.mobile-1.3.1.min.js"> </script> 
  <script src="${imgDomain}/js/module.js"></script>

</head>
<body>
<div data-role="page" id="e-pay">
  <div data-role="header" style="display:none">
    <h1>e币支付</h1>
  </div>
  <div data-role="content">
    <section>
      <table>
        <tr>
          <td><strong>用户名</strong></td>
          <td><span class="font-status"><s:property value="#attr.user.name"/></span></td>
        </tr>
        <tr>
          <td><strong>e币余额</strong></td>
          <td><span class="font-striking">${eb}</span> e币</td>
        </tr>
        <tr>
          <td><strong>应付e币</strong></td>
          <td><span class="font-striking"><s:property value="payTrade.reqEb"/></span> e币</td>
        </tr>
        <tr>
          <td><strong>应付金额</strong></td>
          <td><span class="font-striking"><s:property value="payTrade.reqFee"/></span> 元</td>
        </tr>
      </table>
    </section>

    <section id="epay-pay-section" style="display:none">
      <h4>请选择充值方式</h4>
      <div id="epay-pay-subsection-1" class="ui-grid-b">
      	<%if(!maskChannel.contains("sms")) {%>
        <div class="ui-block-a">
          <a href="/module/charge!selectAmount.e?type=select_amount_sms&channelId=<%=Constants.CHANNEL_ID_SMSYHXF%>" data-role="button" data-ajax="false">
            <div class="span-btn-text">短信支付</div>
          </a>
        </div>
        <%}%>
        <%if(!maskChannel.contains("mobilecard")) {%>
        <div class="ui-block-b">
          <a href="/module/charge!selectAmount.e?type=select_amount_phonecard" data-role="button">
            <div class="span-btn-text">手机充值支付</div>
          </a>
        </div>
        <%}%>
        <%if(!maskChannel.contains("gamecard")) {%>
        <div class="ui-block-c">
          <a href="/module/charge!selectAmount.e?type=select_amount_gamecard" data-role="button">
            <div class="span-btn-text">游戏点卡</div>
          </a>
        </div>
        <%}%>
      </div>
      <div id="epay-pay-subsection-2" class="ui-grid-b">
        <div class="ui-block-a">
          <a href="/module/charge!selectAmount.e?type=select_amount_ali&channelId=<%=Constants.CHANNEL_ID_ALIPAY_WAP%>" data-role="button">
            <div class="span-btn-text">银行卡</div>
          </a>
        </div>
        <%if(!maskChannel.contains("alipay")) {%>
        <div class="ui-block-b">
          <a href="/module/charge!selectAmount.e?type=select_amount_ali&channelId=<%=Constants.CHANNEL_ID_ALIPAY_WAP%>" data-role="button">
            <div class="span-btn-text">支付宝</div>
          </a>
        </div>
        <%}%>
        <%-- 
        <%if(!maskChannel.contains("mo9")) {%>
        <div class="ui-block-c">
          <a href="/module/charge!selectAmount.e?type=select_amount_mo9&channelId=<%=Constants.CHANNEL_ID_MO9%>" data-role="button">
            <div class="span-btn-text">先玩后付</div>
          </a>
        </div>
        <%}%>
        --%>
      </div>
    </section>
    
    <a class="pay" href="/module/eb.e?invoice=<s:property value="payTrade.invoice"/>" data-role="button">马上支付</a>
  </div>
  <script> 
    var $paymentApproaches = $('#epay-pay-section');
    var $pay = $('.pay');

    function checkBalance(balance, amount) {
      if (balance < amount) {
        $paymentApproaches.show();
        $pay.text('余额不足，请充值后支付').click(function() {
          return false;
        });
        $pay.css('background', 'none');
        $pay.css('background-color', '#ccc');
      } else {
        $paymentApproaches.hide();
        $pay.text('马上支付').click(function() {
					
        });
      }
    }
    
    checkBalance('${eb}',"<s:property value="payTrade.reqFee"/>" * 100);
    //checkBalance(0,"<s:property value="payTrade.reqFee"/>");
  </script>
</div>
</body>

</html>