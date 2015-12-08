<%@ page language="java" import="java.util.*"  contentType="text/html; charset=UTF-8"%>
<%@page import="java.net.URLEncoder"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no,target-densitydpi=medium-dpi" />
<meta content="telephone=no" name="format-detection">
<title>梵付通(WAP) - E币交易明细</title>
<link type="text/css" rel="stylesheet"  href="/css/common.css">
</head>
<body >

<div class="mode">
	<div>
	<span class="threeWords">用户</span>名：<s:property value="#attr.user.name"/><br />
	<span class="threeWords">用户</span>ID：<s:property value="#attr.user.id"/><br />
	现有 e 币：${eb} e币(1元=100e币) <a href="charge.e?appId=${param.appId}&backUrl=<%=URLEncoder.encode("/wap/trade!toIndex.e")%>" class="green">充值>></a>
	</div>	
	<div class="div-blue"><input id="buttonRecord" class="button-blue" name="" type="button" value="e币交易记录" /></div>
</div>

<div id="payRecord" style="" class="mode">
	<h5>e币交易记录</h5>
	<ul class="pay-record">
		<s:iterator value="#request.dealflows.dataContext" id="flow" >
			<li>
				<span><s:date name="createDatetime" /></span><br />
				<span><s:property value="tradeName"/> : <s:property value="paidFee"/>    e币</span>
			</li>
		</s:iterator>
	</ul>
	<div class="div-blue">
		<s:if test="#request.dealflows.hasNext">
			<a id="load_more" class="button-blue no-input" name="" type="button" value="加载更多">加载更多</a>
		</s:if>
	</div>
</div>


<div class="company-info">梵付通24小时服务热线：0755-83734900</div>


<script src="/js/zepto.min.js"></script>
<script>
$(document).ready(function(){
	var page=2;
	var buttonRecord = $("#buttonRecord");
	var payRecord = $("#payRecord");
	buttonRecord.live("click touchStart",function(){
		payRecord.toggle();
	})
	$("#load_more").bind("click touchStart",function(){
		$(this).html("<img src='http://m123-p1.fantingame.com/game/data/2011/12/9/loading.gif' alt='.'/>正在加载中");
		$.getJSON("/wap/dealHistory.e?page="+page,function(data) {
		if(data!= undefined && data.dealflows!=undefined && data.dealflows.dataContext!=undefined){
			var dh = [];
			$.each(data.dealflows.dataContext,function(i, value) {
				dh.push("<li><span>");
				dh.push(value.createDatetime);
				dh.push("</span><br><span>");
				dh.push(value.tradeName);
				dh.push(" : ");
				dh.push(value.paidFee);
				dh.push(" e币</span></li>");
			});
			$("#load_more").html("加载更多");
			if(!data.dealflows.hasNext){
				$(".div-blue").hide();
			}
			$(".pay-record").append(dh.join(""));
			page++;
		}
		});
	});
})
</script>
</body>
</html>