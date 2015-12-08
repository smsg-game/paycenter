<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="com.fantingame.pay.utils.RsaSignUtil"%>
<%@page import="com.fantingame.pay.entity.PayPartner"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.apache.struts2.ServletActionContext"%>
<%@page import="com.fantingame.pay.manager.PayPartnerManager"%>
<%@page import="com.easou.common.api.Md5SignUtil"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.fantingame.pay.utils.Constants"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+path;

String submit = request.getParameter("submit");

String appId="101";
String partnerId="1000100010001010";
String tradeId=""+System.currentTimeMillis();
String tradeName="100梵町书城币";
String tradeDesc="100梵町书城币只卖1分钱";
String reqFee="0.01";
String notifyUrl= "";
if(Constants.isTest){
  notifyUrl = "http://testservice1.pay.fantingame.com/service/test.e";
}else{
  notifyUrl = "http://service.pay.fantingame.com/service/test.e";
}
String separable="false";
String payerId="123456";
String qn="1000";
String extInfo="123456";
String redirectUrl=basePath+"/pub/xx.html";
if(submit!=null){
	appId = request.getParameter("appId");
	partnerId = request.getParameter("partnerId");
	tradeId = request.getParameter("tradeId");
	tradeName = request.getParameter("tradeName");
	tradeDesc = request.getParameter("tradeDesc");
	reqFee = request.getParameter("reqFee");
	notifyUrl = request.getParameter("notifyUrl");
	separable = request.getParameter("separable");
	payerId = request.getParameter("payerId");
	qn = request.getParameter("qn");
	extInfo = request.getParameter("extInfo");
	redirectUrl = request.getParameter("redirectUrl");
	
	Map<String,String> map = new HashMap<String,String>();
	map.put("appId",appId);
	map.put("partnerId",partnerId);
	map.put("tradeId",tradeId);
	map.put("tradeName",tradeName);
	map.put("tradeDesc",tradeDesc);
	map.put("reqFee",reqFee);
	map.put("notifyUrl",notifyUrl);
    map.put("separable",separable);
    map.put("payerId",payerId);
    map.put("qn",qn);
	//map.put("extInfo",extInfo);
	if(redirectUrl != null && !"".equals(redirectUrl)) {
		map.put("redirectUrl",redirectUrl);
	}
	
    
    PayPartnerManager payPartnerManager = (PayPartnerManager)WebApplicationContextUtils.getRequiredWebApplicationContext(ServletActionContext.getServletContext()).getBean("payPartnerManager");
    PayPartner payPartner = payPartnerManager.getEntityById(Long.valueOf(partnerId));
    
    
    String visitPath = basePath + "/mobile/trade.e?";
	if(submit.startsWith("wap触屏版")){
		visitPath = basePath + "/wap/trade.e?";
	}
	if(submit.startsWith("wap彩版")){
		visitPath = basePath + "/wapc/trade.e?";
	}
	if(submit.startsWith("module版")){
		visitPath = basePath + "/module/trade.e?";
	}
	if(submit.startsWith("ecenter版")){
		visitPath = basePath + "/ecenter/trade.e?";
	}
	if(submit.startsWith("pc版")){
		visitPath = basePath + "/pc/trade.e?";
	}

	String sign = "";
	if("1".equals(payPartner.getEncryptType())){
	  sign = RsaSignUtil.sign(map, payPartner.getPrivateKey());
	}else if("2".equals(payPartner.getEncryptType())){
		System.out.println(map.toString());
	  sign = Md5SignUtil.sign(map, payPartner.getSecretKey());
	}
	String visitUrl = visitPath + "appId="+URLEncoder.encode(appId,"UTF-8")+
		"&partnerId="+URLEncoder.encode(partnerId,"UTF-8")+
		"&tradeId="+URLEncoder.encode(tradeId,"UTF-8")+
		"&tradeName="+URLEncoder.encode(tradeName,"UTF-8")+
		"&tradeDesc="+URLEncoder.encode(tradeDesc,"UTF-8")+
		"&reqFee="+URLEncoder.encode(reqFee,"UTF-8")+
		"&notifyUrl="+URLEncoder.encode(notifyUrl,"UTF-8")+
		"&redirectUrl="+URLEncoder.encode(redirectUrl,"UTF-8")+
		"&sign="+URLEncoder.encode(sign,"UTF-8")+
		"&separable="+URLEncoder.encode(separable,"UTF-8")+
		"&payerId="+URLEncoder.encode(payerId,"UTF-8")+
		"&qn="+URLEncoder.encode(qn,"UTF-8")+
		"&extInfo="+URLEncoder.encode(extInfo,"UTF-8");

	if(submit.startsWith("module版")){
		String payTradeUrl = basePath + "/module/payTrade.e?" + "EASOUTGC=TGT-50824-FhetD7gFbBXXmbs6q1OJQMazf0TMOAos6nTocjksyl6XUlL7xh-sso" + "&redirectUrl=" + URLEncoder.encode(visitUrl,"UTF-8");
		System.out.println(payTradeUrl);
		response.sendRedirect(payTradeUrl);
	}else {
		response.sendRedirect(visitUrl);
	}
	
}


%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>梵付通 - 模拟游戏商下单</title>
    <style>
    	body{
    		text-align: center;
    		padding:20px 0;
    		margin:0;
    		font-family: "arial";
    	}
    	a{text-decoration: none;}
    	#fuck span{
    		display:inline-block;
    		width:150px;
    	}
    	#fuck input{
	    	height: 25px;
			width: 250px;
		}
		#fuck div{
	    	margin:10px;
		}
    </style>
  </head>

  <body>
  <a href="/ua.jsp">获取UA页面</a><br/>
  	<div id="fuck">
		<form action="index.jsp" method="post">
		  	<div><span>appId : </span>	<input type="text"  name="appId" value="<%=appId%>"/><br/></div>
		  	<div><span>partnerId :</span><input type="text" name="partnerId" value="<%=partnerId%>"/><br/></div>
		  	<div><span>tradeId : </span><input type="text" name="tradeId" value="<%=tradeId%>"/><br/></div>
		  	<div><span>tradeName :</span><input type="text" name="tradeName" value="<%=tradeName%>"/><br/></div>
		  	<div><span>tradeDesc :</span><input type="text" name="tradeDesc" value="<%=tradeDesc%>"/><br/></div>
		  	<div><span>reqFee : </span><input type="text" name="reqFee" value="<%=reqFee%>"/><br/></div>
		  	<div><span>notifyUrl : </span>	<input type="text" name="notifyUrl" value="<%=notifyUrl%>"/><br/></div>
		  	<div><span>redirectUrl :</span><input type="text" name="redirectUrl" value="<%=redirectUrl%>"/><br/></div>
		  	<div><span>separable : </span><input type="text" name="separable" value="<%=separable%>"/><br/></div>
		  	<div><span>payerId : </span>	<input type="text" name="payerId" value="<%=payerId%>"/><br/></div>
		  	<div><span>qn :</span><input type="text" name="qn" value="<%=qn%>"/><br/></div>
		  	<div><span>extInfo :</span><input type="text" name="extInfo" value="<%=extInfo%>"/><br/></div>
		  	<input type="submit" name="submit" value="mobile模拟提交"/>		
		  	<input type="submit" name="submit" value="wap触屏版模拟提交">
		  	<input type="submit" name="submit" value="module版模拟提交">
		  	<input type="submit" name="submit" value="ecenter版模拟提交">
		  	<input type="submit" name="submit" value="pc版模拟提交"/>
	  	</form>
  	</div>
  </body>
</html>
