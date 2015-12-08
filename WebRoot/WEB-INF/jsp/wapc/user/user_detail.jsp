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
	现有 e 币：${eb} e币(1元=100e币) <a href="charge.e?appId=${param.appId}&amp;backUrl=<%=URLEncoder.encode("/wapc/trade!toIndex.e")%>" class="green">充值>></a>
	</div>	
</div>

<div id="payRecord" style="" class="mode">
	<h5>e币交易记录(最新10条)</h5>
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
			<a href="userDetail.e?page=<s:property value="#request.dealflows.thisPage+1"/>" class="button-blue no-input"  type="button" >查看下一页</a>
		</s:if>
		<s:else>
			<a href="trade!toIndex.e" class="button-blue no-input"  type="button" >返回</a>
		</s:else>
	</div>
</div>


<div class="company-info">梵付通24小时服务热线：0755-83734900</div>


</body>
</html>