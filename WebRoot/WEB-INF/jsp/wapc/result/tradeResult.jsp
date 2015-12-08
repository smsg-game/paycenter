<%@ page language="java" import="java.util.*"  contentType="application/xhtml+xml; charset=UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="application/xhtml+xml;charset=UTF-8"/>
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no,target-densitydpi=medium-dpi" />
<meta content="telephone=no" name="format-detection" />
<title>梵付通(WAP) - 支付结果</title>
<link type="text/css" rel="stylesheet"  href="/css/common.css" />
</head>
  
 <body>
	<div class="mode-noBg">
		<div>
		   <s:if test="status==1">
			  √ 该订单已经支付成功！
			  <a href="/wapc/redirect.e?invoice=${invoice}"><input class="button-blue" name="" type="button" value="返回"/></a>
		   </s:if>
		   <s:elseif test="status==0">
		       ${msg}<br/>
		       <div class="div-blue">
	              <a href="/wapc/tradeResult.e?invoice=<s:property value="invoice"/>"><input class="button-blue" name="" type="button" value="刷新"/></a>
	              <a href="/wapc/redirect.e?invoice=${invoice}"><input class="button-blue" name="" type="button" value="返回"/></a>
	           </div>
		   </s:elseif>
		   <s:else>
		      ${msg}<br/>
			 <div class="div-blue">
	            <a href="/wapc/redirect.e?invoice=${invoice}"><input class="button-blue" name="" type="button" value="返回"/></a>
	         </div>
		   </s:else>
		</div>
	</div>
    <div class="company-info">梵付通24小时服务热线：0755-83734900</div>
 </body>

</html>
