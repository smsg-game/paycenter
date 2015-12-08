<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>游戏商向梵町下单</title>
<script type="text/javascript" src="zepto.min.js"></script>
<script>
  function getSign(){
	var inputs = $("input");
	var params ="?"
	$.each(inputs,function(i, value) {
		if(value.name!=undefined && value.name!=null && value.name!="")
		params=params +value.name+"="+ encodeURI(value.value)+"&";
	});
	 $.getJSON('/json/test!bianfengSign.e'+params,function(data){
		$("[name='sign']").val(data.data.sign);
     });
  }
  
  function getPay(){
  	var invoice = {
		"appId":$("[name='appId']").val(),
		"partnerId":$("[name='partnerId']").val(),
		"tradeId":$("[name='tradeId']").val(),
		"tradeName":$("[name='tradeName']").val(),
		"tradeDescription":$("[name='tradeDescription']").val(),
		"fee":$("[name='fee']").val(),
		"notifyUrl":$("[name='notifyUrl']").val(),
		"separable":$("[name='separable']").val(),
		"playerId":$("[name='playerId']").val(),
		"qn" : $("[name='qn']").val(),
		"extInfo": $("[name='extInfo']").val(),
		"sign": $("[name='sign']").val()
	}
	
	var value ="";
	for(var i in invoice){
		value += i+"="+encodeURIComponent(invoice[i])+"&";
	}
	
	///json/order.e?channelId=1000100020001001&invoice=${}
  	$.getJSON('/json/trade.e?'+value,function(data){
		var code = data.data.code;
		if(code!== "0"){
			alert(data.data.msg);
			return;
		}else{
			$("#payInfo").html(data.data.msg);
		}
		
		var url = "/json/order.e?channelId=1000100020001001&invoice="+data.data.invoice;
		
		//定时请求M9的支付
		//setTimeout(function(){
			$.getJSON(url,function(dataUrl){
				/*var dataUrl = {
				"data":{
					"code":"0",
					"invoice":"344e8de0fd164149b31b2d5c6ad9225b",
					"msg":"操作成功",
					"url":"amount=1.00&app_id=1211060012&currency=CNY&extra_param=1000100010001001&invoice=344e8de0fd164149b31b2d5c6ad9225b&item_name=10%E5%85%83%E5%AE%9D&lc=CN&notify_url=https%3A%2F%2Ftestservice.pay.fantingame.com%2Fservice%2FnotifyFromMO9.e&pay_to_email=mo9_billing%40staff.fantingame.com&payer_id=ABC&sign=FA7A03528E90C2D150544C78C77E12EA&version=2.1"
					}}*/
					
				if(dataUrl.data.code!== "0"){
					alert(dataUrl.data.msg);
					return;
				}else{
					$("#payInfo").html(dataUrl.data.msg);
				}
				var payUrl = "https://sandbox.mo9.com.cn/gateway/mobile.shtml?m=mobile&"+dataUrl.data.url;
				var button = "<input type='button' value='支付'  onclick='location.href=\""+payUrl+"\"'/>";
				$("#pay").html(button);
			});
			
		//},500)
		
     });
  }
  
  $(document).ready(function(){
  	$("[name='appId']").val("SANGGUOSHA");
	$("[name='partnerId']").val("1000100010001001");
	$("[name='tradeName']").val("10元宝");
	$("[name='tradeDescription']").val("三国杀的10元宝充值");
	$("[name='fee']").val("1.00");
	$("[name='notifyUrl']").val("https://testservice.pay.fantingame.com/json/test.e");
	$("[name='separable']").val("false");
	$("[name='playerId']").val("ABC");
	
  	$("#submit").click(function(){
		getPay();
	})
  })
 </script>
</head>

<body>

<table>
<tr><td>appId:</td><td><input type="text" name="appId" value="mo9_billing@staff.fantingame.com"  style="width: 400px;"/></td></tr>
<tr><td>partnerId:</td><td><input type="text" name="partnerId" value="1211060012"  style="width: 400px;"/></td></tr>
<tr><td>tradeId:</td><td><input type="text" name="tradeId" value="86929df"  style="width: 400px;"/></td></tr>
<tr><td>tradeName:</td><td><input type="text" name="tradeName" value="CN"  style="width: 400px;"/></td></tr>

<tr><td>tradeDescription:</td><td><input type="text" name="tradeDescription" value="10元宝"  style="width: 400px;"/></td></tr>
<tr><td>fee:</td><td><input type="text" name="fee" value="0.99"  style="width: 400px;"/></td></tr>
<tr><td>notifyUrl:</td><td><input type="text" name="notifyUrl" value="CNY"  style="width: 400px;"/></td></tr>
<tr><td>separable:</td><td><input type="text" name="separable" value="0.99"  style="width: 400px;"/></td></tr>
<tr><td>playerId:</td><td><input type="text" name="playerId" value="CNY"  style="width: 400px;"/></td></tr>

<tr><td>qn:</td><td><input type="text" name="qn" value="12345678123456"  style="width: 400px;"/></td></tr>
<tr><td>extInfo:</td><td><input type="text" name="extInfo" value="GAACLOBEPREJOOOH"  style="width: 400px;"/></td></tr>
<tr><td>sign:</td><td><input type="text" name="sign" value="=194D48903F21B3DA96913EF52B507154"  style="width: 400px;"/>
                      <input type="button" value="获取签名" onclick="getSign()"/></td></tr>
<tr><td colspan="2" ><input type="button" id="submit" value="提交"  /><span id="pay"></span><span id="payInfo"></span></td></tr>
</table>

</body>
</html>
