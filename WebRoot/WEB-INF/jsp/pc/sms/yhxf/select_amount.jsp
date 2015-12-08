<%@ page language="java" import="java.util.*"  contentType="text/html; charset=UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<% String url = request.getScheme() + "://" + request.getServerName()+ request.getContextPath();%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no,target-densitydpi=medium-dpi" />
<meta content="telephone=no" name="format-detection"/>
<title>梵付通(APP) - 短信支付</title>
<link type="text/css" rel="stylesheet"  href="/css/common.css"/>
</head>

<body>
<div>
    <div class="userInfo">
	   <span><span class="blue">当前账户：</span><s:property value="#attr.user.name"/></span>
    </div>
	<div class="mode">
		<h5>订单金额：${payTrade.reqFee}元</h5>
		<input type="hidden" value="" name="money" id="money"/>
		<input type="hidden" value="${payTrade.invoice}" name="invoice" id="invoice"/>
		<input type="hidden" value="${payTrade.reqFee}" name="reqFee" id="reqFee"/>
		<input type="hidden" value="${rate}" name="rate" id="rate"/>
		<div>使用短信支付您需要支付<span style="color:red;">${payTrade.reqFee/rate}</span>元（<span style="color:red;">${payTrade.reqFee/(rate*2)}</span>元订单费用，<span style="color:red;">${payTrade.reqFee/(rate*2)}</span>元渠道费用）</div>
		<div>
			<ul class="payMoney">
				<li>5元</li>
				<li>10元</li>
				<li>15元</li>
				<li>20元</li>
				<li>30元</li>
			</ul>
			<div class="gayText">
				手机号：<input id="phoneNum" class="loginInput" name="phoneNum" type="text" placeholder="请输入您的手机号" value="${phoneNo}" maxlength="11"/>
			</div>
		</div>
		<div class="div-blue"><input id="ok" class="button-blue" name="" type="button" value="确定支付" /></div>
		<h6>温馨提示：</h6>
		<div>
			<p id="tit-6" class="gayText">
			    1.如果支付金额大于订单金额，多付的钱会自动充值到您账号的e币；<br />
				2.短信充值目前只支持移动卡（新疆、宁夏、甘肃三省暂不支持）；<br />
				3.每天5元可以支付两次，其余一次；<br />
				4.每月支付总额不能超过70元；
		     </p>
		</div>
	</div>
</div>

<script src="/js/zepto.min.js"></script>
<script src="/js/common-wap.js?t=20130223"></script>
<script>
$(document).ready(function(){
	var li = $(".payMoney>li");
	var money = $("#money");
	var phoneNum = $("#phoneNum");
	var reqFee = parseFloat($("#reqFee").val());
	var rate = parseFloat($("#rate").val());
	
	//计算并自动选择金额
	for (var i=0;i<li.length;i++){
	    var me = parseInt(li.eq(i).html());
	    if(me*rate>=reqFee){
	        money.val(me);
	        li.eq(i).addClass("on").siblings().removeClass("on");
	        break;
	    }
	}

	li.live("click touchStart",function(){
		$(this).addClass("on").siblings().removeClass("on");
		var value = parseInt($(this).html());
		money.val(value);
	});
	phoneNum.live("focus",function(){
		if(phoneNum.val()==="请输入您的手机号"){
			phoneNum.val("")
		}
	});
	$("#ok").live("click touchStart",function(){
	    if(verify()){
	       //ajax获取结果
	       var href = "/mobile/smsYhxf.e?money="+money.val()+"&phoneNum="+phoneNum.val()+"&invoice="+$("#invoice").val();
	       $.getJSON(href,function(data) {
	           //调android接口,data.status,data.msg,data.number,redirectUrl
	           if(data.status=='success'){
	               window.AndroidSmsPayForJavaScript.startSmsPay(data.msg,data.number,'${redirectUrl}');
	           }else{
	               location.href = "${redirectUrl}&status="+data.status+"&msg="+encodeURIComponent(data.msg);
	           }
	       });
	    } 
	})
	
	function verify(){
	    if(phoneNum.val()=="" || phoneNum.val()=="请输入您的手机号" || !new RegExp(/^(13|14|15|18)[0-9]{9}$/).test(phoneNum.val())){
	        showPrompt.init("请输入正确的手机号");
	        return false;
	    }
	    var ac = money.val() * rate;
		if(ac<reqFee){
		   showPrompt.init("抱歉,您选择的充值卡金额不够支付此订单，请重新选择!");
		   return false;
	    }
	    return true;
	}
	
})
</script>
</body>
</html>
