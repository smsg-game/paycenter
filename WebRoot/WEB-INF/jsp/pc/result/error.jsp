<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8"%>
<%@ page isELIgnored="false"%>
<%@page import="java.net.URLEncoder"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
  <meta name="apple-mobile-web-app-capable" content="yes" />
  <meta name="apple-mobile-web-app-status-bar-style" content="black" />
  <title>梵付通收银台 - e币支付 - 支付成功</title>
  <link rel="stylesheet" href="${imgDomain}/js/vendor/jquery-mobile/jquery.mobile-1.3.1.min.css" />
  <link rel="stylesheet" href="${imgDomain}/css/module.css" />
  <script src="${domain}/js/vendor/jquery/jquery-1.9.1.min.js"> </script> 
  <script src="${imgDomain}/js/vendor/jquery-mobile/jquery.mobile-1.3.1.min.js"> </script> 
  <script src="${imgDomain}/js/module.js"></script>

</head>
<body>
<div data-role="page" id="failure">
  <div data-role="header" style="display:none" data-theme="b">
    <h1>支付失败</h1>
  </div>
  <div data-role="content">
    <section>
      <table>
        <tr>
          <td><strong>用户名</strong></td>
          <td><span class="font-status"><s:property value="#attr.user.name"/></span></td>
        </tr>
        <tr>
          <td><strong>商品名称</strong></td>
          <td><span class="font-status">e币充值</span></td>
        </tr>
        <tr>
          <td><strong>支付金额</strong></td>
          <td><span class="font-striking"><s:property value="payEb.reqFee"/></span> 元</td>
        </tr>
        <tr>
          <td><strong>e币余额</strong></td>
          <td><span class="font-striking">${eb}</span> e币</td>
        </tr>
      </table>
    </section>

    <a class="pay" href="/module/trade!toIndex.e?tradeInvoice=<s:property value="payTrade.invoice"/>" data-role="button">重新支付</a>
  </div>
  
</div>
</body>
</html>