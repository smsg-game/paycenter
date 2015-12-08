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
<title>梵付通</title>
<link type="text/css" rel="stylesheet"  href="/css/common.css"/>
<script type="text/javascript" src="/js/zepto.min.js"></script>
<script type="text/javascript">
    function sendSms(invoice){
        document.getElementById('btnText').value = '刷新';
        document.getElementById('tipText').innerHTML = '如果您已将<span style="color:red;">${msg}</span>发送到<span style="color:red;">${to}</span>，请点击刷新查看支付结果';
        document.getElementById('sendBtn').onclick = function checkStatus(){
            $.getJSON("/wap/tradeResult.e?ti="+new Date().getTime()+"&invoice="+invoice, function(data){
				var dataResult = data.status;
				var dataMsg =data.msg;
				if(dataResult == 1){//支付成功
					 document.getElementById('payResult').innerHTML = '<span style="color:red;">支付结果：支付成功！</span>'
				}else if(dataResult == 0){//未支付
					 document.getElementById('payResult').innerHTML = '<span style="color:red;">支付结果：支付未完成！</span>'
				}else{//支付失败
					 document.getElementById('payResult').innerHTML = '<span style="color:red;">支付结果：支付失败！['+dataMsg+']</span>'
				}
	       })
        }
        location.href = 'sms:${to}';
    }
    
    
</script>
</head>
<body>
<div class="mode">
	<h5>提示</h5>
	<div id="tipText">
		1.将要发送短信<span style="color:red;">${msg}</span>到<span style="color:red;">${to}</span>；<br/>
        2.发送成功后，返回本页面刷新后查看支付结果；<br/>
	</div>
	<div id="payResult"></div>
	<div class="div-blue"><a id="sendBtn" onclick="sendSms('${invoice}');"><input class="button-blue" name="" id="btnText" type="button" value="发送短信" /></a></div>
</div>
<%@ include file="../../common/common_footer.jsp" %>
</body>
</html>
