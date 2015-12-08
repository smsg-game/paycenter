<%@ page language="java" import="java.util.*"  contentType="text/html; charset=UTF-8"%>
<%@ page isELIgnored="false"%>
<%@page import="java.net.URLEncoder"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@page import="com.fantingame.pay.utils.MaskUtil"%>
<%
	String imgDomain = request.getContextPath();
	request.setAttribute("imgDomain", imgDomain);
	String appAgent = request.getHeader("App-Agent");
	String url = request.getScheme() + "://" + request.getServerName()+ request.getContextPath();
	String url2 = request.getScheme() + "://" + request.getServerName();
	if("AndroidSDKEasouPay".equals(appAgent)) {
		request.setAttribute("isWeb", false);
	}else {
		request.setAttribute("isWeb", true);
	}
%>
<%String maskChannel=MaskUtil.getMaskChannelByCondition(request.getParameter("appId")); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <meta charset="utf-8" />
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="apple-mobile-web-app-status-bar-style" content="black" />
    <title>梵付通收银台</title>
    <link rel="stylesheet" href="${imgDomain}/js/vendor/jquery-mobile/jquery.mobile-1.3.1.min.css" />
    <link rel="stylesheet" href="${imgDomain}/css/module.css?t=1002" />
      
    <script>
      try {

      } catch (error) {
        console.error("Your javascript has an error: " + error);
      }
    </script>
	<script type="text/javascript" src="${imgDomain}/js/zepto.min.js"></script>
	<script src="${imgDomain}/js/common-wap.js?t=20130223"></script>
	
  </head>
  <body id="body">
    <div id="index" data-role="page">
      <div data-role="header" style="display:none">
        <h1>梵付通收银台</h1>
      </div>
      <div data-role="content">
        <section id="index-info-section">
          <table>
            <tr>
              <td><strong>用户名</strong></td>
              <td><span class="font-status"><s:property value="#attr.user.name"/></span></td>
            </tr>
            <tr>
              <td><strong>商品名称</strong></td>
              <td><span class="font-status"><s:property value="payTrade.tradeName"/></span></td>
            </tr>
            <tr>
              <td><strong>应付金额</strong></td>
              <td><span class="font-striking"><s:property value="payTrade.reqFee"/></span>元</td>
            </tr>
          </table>
        </section>

        <section id="index-pay-section">
          <h4>直接购买支付</h4>
          <div>
            <div class="ui-grid-b">
              <div class="ui-block-a">
                <a href="javascript:startAli('${isWeb}','<%=url %>/module/ali.e?invoice=<s:property value="payTrade.invoice"/>','<%=url2%>/module/redirect!showResult.e?type=ali&invoice=<s:property value="payTrade.invoice"/>')" data-role="button" data-ajax="false">
                  <div class="span-btn-text">银行卡</div>
                </a>
              </div>
              <%if(!maskChannel.contains("alipay")) {%>
              <div class="ui-block-b">
                <a href="javascript:startAli('${isWeb}','<%=url %>/pc/pcali.e?invoice=<s:property value="payTrade.invoice"/>','<%=url2%>/module/redirect!showResult.e?type=ali&invoice=<s:property value="payTrade.invoice"/>')" data-role="button" data-ajax="false" >
                  <div class="span-btn-text">支付宝</div>
                </a>
              </div>
              <%}%>
              <%-- 
              <%if(!maskChannel.contains("mo9")) {%>
              <div class="ui-block-c">
                <a href="/module/mo9.e?invoice=<s:property value="payTrade.invoice"/>" data-role="button" data-ajax="false">
                  <div class="span-btn-text">先玩后付</div>
                </a>
              </div>
              <%}%>
              --%>
            </div>
          </div>
        </section>

        <section id="index-epay-section">
          <h4>充值e币支付 余额 <span class="font-striking">${eb}</span>e币 应付 <span class="font-striking"><s:property value="payTrade.reqEb"/></span>e币</h4>
          <div id="index-epay-subsection-1">
            <div class="ui-grid-b">
              <%if(!maskChannel.contains("sms")) {%>
              <div class="ui-block-a">
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
                <a href="#" data-role="button" onclick="checkMoney(15,'/module/sms.e?invoice=${payTrade.invoice}');" data-ajax="false">
                  <div class="span-btn-text">短信支付</div>
                </a>  
              </div>
              <%}%>
              <%if(!maskChannel.contains("mobilecard")) {%>
              <div class="ui-block-b ui-btn-five-chars">
                <a href="/module/card!selectCardPage.e?type=phonecard&invoice=<s:property value="payTrade.invoice"/>" data-role="button" data-ajax="false">
                  <div class="span-btn-text">手机充值卡</div>
                </a>   
              </div>
              <%}%>
              <%if(!maskChannel.contains("gamecard")) {%>
              <div class="ui-block-c">
                <a href="/module/card!selectCardPage.e?type=gamecard&invoice=<s:property value="payTrade.invoice"/>" data-role="button" data-ajax="false">
                  <div class="span-btn-text">游戏点卡</div>
                </a>
              </div>
              <%}%>
            </div>
          </div>
          <div id="index-epay-subsection-2">
            <div class="ui-grid-b">
              <div class="ui-block-a">
                <a href="/module/charge.e?appId=<s:property value="payTrade.appId"/>&backUrl=<%=URLEncoder.encode("/module/charge!toIndex.e")%>&invoice=<s:property value="payTrade.invoice"/>" data-role="button" data-ajax="false">
                  <div class="span-btn-text">e币余额</div>
                </a>
              </div>
              <div class="ui-block-b"></div>
              <div class="ui-block-c"></div>
            </div>
          </div>
        </section>
      </div>
    </div>
    <script src="${domain}/js/vendor/jquery/jquery-1.9.1.min.js"> </script> 
    <script src="${imgDomain}/js/vendor/jquery-mobile/jquery.mobile-1.3.1.min.js"> </script> 
    <script src="${imgDomain}/js/module.js?t=1002"></script>
  </body>
</html>
