<%@ page language="java" import="java.util.*"  contentType="text/html; charset=UTF-8"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page isELIgnored="false"%>
<%@ page import="com.fantingame.pay.utils.Constants"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no,target-densitydpi=medium-dpi" />
<meta content="telephone=no" name="format-detection"/>
<title>梵付通-短信支付结果</title>
<link type="text/css" rel="stylesheet"  href="/css/common.css"/>
<script type="text/javascript" src="/js/zepto.min.js"></script>
<script type="text/javascript">
        function checkStatus(invoice){
            $("#loading").toggle();
            $("#btnText").toggle();
            $.getJSON("/mobile/tradeResult.e?ti="+new Date().getTime()+"&invoice="+invoice, function(data){
				var dataResult = data.status;
				var dataMsg =data.msg;
				if(dataResult == 1){//支付成功
					 document.getElementById('payResult').innerHTML = '支付结果：<span style="color:red;">支付成功！</span>'
				     $("#loading").toggle();
					 $("#backBtn").toggle();
				}else if(dataResult == 0){//未支付
					 document.getElementById('payResult').innerHTML = '支付结果：<span style="color:red;">支付未完成！</span>'
				     $("#loading").toggle();
                     $("#btnText").toggle();
				}else{//支付失败
					 document.getElementById('payResult').innerHTML = '支付结果：<span style="color:red;">支付失败！['+dataMsg+']</span>'
				     $("#loading").toggle();
					 $("#backBtn").toggle();
				}
	       })
        }
</script>
</head>
<body>
<div class="mode">
	<h5>提示</h5>
	<div id="tipText">
		<s:if test="#parameters.status[0]=='success'">
		      短信支付请求提交成功，正在处理支付请求，请刷新查看支付结果。！
		</s:if>
		<s:else>
		      支付失败!${param.msg};
		</s:else>
	</div>
	<div id="payResult"></div>
	<div class="div-blue">
	    <a id="sendBtn" onclick="checkStatus('${param.invoice}');"><input class="button-blue" name="" id="btnText" type="button" value="刷新"/></a>
	    <a id="backBtn" href="/mobile/redirect.e?invoice=${param.invoice}" style="display: none;"><input class="button-blue" name="" id="btnText" type="button" value="返回"/></a>
	    <img id="loading" style="display: none;" src='http://m123-p2.fantingame.com/200/app/upload/2012/11/27/loading.gif' alt='.'/>
	</div>
</div>
<%@ include file="../../common/common_footer.jsp" %>
</body>
</html>
