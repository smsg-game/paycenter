<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<title>易宝银行卡支付</title>
</head>

<body>
<form action="/json/test!yeePaySign.e">
<input type="hidden" name="p0_Cmd" value="Buy"/>
<input type="hidden" name="pr_NeedResponse" value="1"/>
<input type="hidden" name="hmac" value="SDFSFSF"/>
<input type="hidden" name="p1_MerId" value="10001126856"/>
<table>
<tr><td>订单号:</td><td><input type="text" name="p2_Order" value="1211060012"  style="width: 400px;"/></td></tr>
<tr><td>订单金额:</td><td><input type="text" name="p3_Amt" value="0.01"  style="width: 400px;"/></td></tr>
<tr><td>交易币种:</td><td><input type="text" name="p4_Cur" value="CNY"  style="width: 400px;"/></td></tr>
<tr><td>商品名称:</td><td><input type="text" name="p5_Pid" value="10元宝"  style="width: 400px;"/></td></tr>
<tr><td>回调地址:</td><td><input type="text" name="p8_Url" value="http://testservice1.pay.fantingame.com/test.e"  style="width: 400px;"/></td></tr>
<tr><td>支付方式:</td><td><input type="text" name="pd_FrpId" value="ICBC-NET-B2C"  style="width: 400px;"/></td></tr>
<tr><td colspan="2"><input type="submit" id="submit" value="提交" /></td></tr>
</table>
</form>
</body>
</html>
