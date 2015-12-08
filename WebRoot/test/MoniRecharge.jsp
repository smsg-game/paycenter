<%@ page language="java" import="java.util.*"  contentType="text/html; charset=UTF-8"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

  <head>
    <title>梵付通</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
  </head>
  
  <style>
  	body{
  		padding:0;
  		margin:0;
  	}
  </style>

  
  <body>
    <div style="text-align: center;background-color: #82DCF6;height:30px;line-height:30px;">easou梵付通</div>
    
    <s:if test="data != null">
    	<s:property value="data.code"/>
    </s:if>
    
    <s:if test="#session.user != null">
    <s:property value="#session.user.id"/>
    	用户名:<s:property value="#session.user.name"/> &nbsp;&nbsp; e币:<s:property value="#session.user.eb"/>
    	<div style="background-color: #E8EEF7;height:30px;line-height:30px;">
	    	订单信息
	    </div>
	    <div>
	    	商品名称:${param.goodsName}<br/>
	    	原价:${param.price}<br/>
	    	<a href="order.e?invoice=<s:property value="data.invoice"/>&channelId=1000100020001009">确认e币支付</a>
	    </div>
	    <div>
	    	其他支付方式:<br/>
	    	<a href="order.e?invoice=<s:property value="data.invoice"/>&channelId=1000200010000000">魔9</a><br/>
	    	<a href="order.e?invoice=<s:property value="data.invoice"/>&channelId=1000200020000000">支付宝</a><br/>
	    	<a href="order.e?invoice=<s:property value="data.invoice"/>&channelId=">财付通</a><br/>
	    	<a href="order.e?invoice=<s:property value="data.invoice"/>&channelId=">话费卡</a><br/>
	    	<a href="order.e?invoice=<s:property value="data.invoice"/>&channelId=">网银</a><br/>
	    </div>
    </s:if>
    
    
    
    <s:if test="#session.user == null">
    	<div style="background-color: #E8EEF7;height:30px;line-height:30px;">
	    	订单信息
	    </div>
	    <div>
	    	商品名称:${param.goodsName}<br/>
	    	原价:${param.price}<br/>
	    </div>
	    <div>
	    	请选择支付方式:<br/>
	    	<a href="order.e?invoice=<s:property value="data.invoice"/>&channelId=1000100020001009">e币支付</a>
	    	<a href="order.e?invoice=<s:property value="data.invoice"/>&channelId=1000200010000000">魔9</a><br/>
	    	<a href="order.e?invoice=<s:property value="data.invoice"/>&channelId=1000200020000000">支付宝</a><br/>
	    	<a href="order.e?invoice=<s:property value="data.invoice"/>&channelId=">财付通</a><br/>
	    	<a href="order.e?invoice=<s:property value="data.invoice"/>&channelId=">话费卡</a><br/>
	    	<a href="order.e?invoice=<s:property value="data.invoice"/>&channelId=">网银</a><br/>
	    </div>
    </s:if>
    
    
    <div>
    	优惠活动
    </div>
    <div>
	    1.感恩节e币大放送，任何渠道充100送100<br/>
	    2.圣诞节支付宝e币充值充100返利10<br/>
    </div>
    
    <div>
    	梵付通24小时服务热线:0755-83734900
    </div>
  </body>
  
</html>
