<%@ page language="java" import="java.util.*"  contentType="text/html; charset=UTF-8"%>
<%@ page isELIgnored="false"%>
<%@page import="java.net.URLEncoder"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
	String imgDomain = request.getContextPath();
	request.setAttribute("imgDomain", imgDomain);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
  <meta name="apple-mobile-web-app-capable" content="yes" />
  <meta name="apple-mobile-web-app-status-bar-style" content="black" />
  <title>e币支付 - 手机充值卡充值</title>
  <link rel="stylesheet" href="${imgDomain}/js/vendor/jquery-mobile/jquery.mobile-1.3.1.min.css" />
  <link rel="stylesheet" href="${imgDomain}/css/module.css" />
  <script src="${domain}/js/vendor/jquery/jquery-1.9.1.min.js"> </script> 
  <script src="${imgDomain}/js/vendor/jquery-mobile/jquery.mobile-1.3.1.min.js"> </script> 
  <script src="${imgDomain}/js/module.js"></script>

</head>
<body>
<div data-role="page" id="mcard-deposit">
  <div data-role="header" style="display:none">
    <h1>手机充值卡充值</h1>
  </div>
  <div data-role="content">
   <form action="" method="post">
      <section>
        <ul data-role="listview" data-divider-theme="b" data-inset="true">
          <li style="padding: 1.5em 15px;">
          	<span class="arror-down-to-right"></span>
            <div id="mcard-type-acceptor">
              <span>充值卡类型：移动</span>
            </div>
            <div data-role="popup" id="mcard-type-popup" data-overlay-theme="a">
              <fieldset data-role="controlgroup">
                <a href="" data-role="button" cardType="3">移动</a>
                <a href="" data-role="button" cardType="4">联通</a>
                <a href="" data-role="button" cardType="5">电信</a>
              </fieldset>
            </div>
            <input type="hidden" name="mcard-type" value="4">
          </li>
          <li style="padding: 1.5em 15px;">
          	<span class="arror-down-to-right"></span>
            <div id="mcard-amount-acceptor">
              <span>充值卡面额：<span class="font-striking">10</span>元</span>
            </div>
            <div data-role="popup" id="mcard-amount-popup" data-overlay-theme="a">
              <fieldset data-role="controlgroup"  id="fieldset-for-card-type-3">
                <a href="" data-role="button" money="10">10元</a>
                <a href="" data-role="button" money="20">20元</a>
                <a href="" data-role="button" money="30">30元</a>
                <a href="" data-role="button" money="50">50元</a>
                <a href="" data-role="button" money="100">100元</a>
                <a href="" data-role="button" money="200">200元</a>
                <a href="" data-role="button" money="300">300元</a>
                <a href="" data-role="button" money="500">500元</a>
              </fieldset>
              <fieldset data-role="controlgroup" id="fieldset-for-card-type-4" style="display:none">
                <a href="" data-role="button" money="20">20元</a>
                <a href="" data-role="button" money="30">30元</a>
                <a href="" data-role="button" money="50">50元</a>
                <a href="" data-role="button" money="100">100元</a>
                <a href="" data-role="button" money="300">300元</a>
                <a href="" data-role="button" money="500">500元</a>
              </fieldset>
              <fieldset data-role="controlgroup" id="fieldset-for-card-type-5" style="display:none">
                <a href="" data-role="button" money="50">50元</a>
                <a href="" data-role="button" money="100">100元</a>
              </fieldset>
            </div>
            <input type="hidden" name="mcard-amount" value="10">
          </li>
          <li class="inline-text-listview-li-padding">
            <div class="inline-text-input-grid-a ui-grid-a">
              <div class="ui-block-a">
                充值卡号：
              </div>
              <div class="ui-block-b">
                <input type="text" name="mcard-no" id="input"/>
              </div>
            </div>
          </li>
          <li class="inline-text-listview-li-padding">
            <div class="inline-text-input-grid-a ui-grid-a">
              <div class="ui-block-a">
                充值卡密码：
              </div>
              <div class="ui-block-b">
                <input type="text" name="mcard-pwd" id="password"/>
              </div>
            </div>
          </li>
          <li style="padding: 1.5em 15px;">
            <span>预计到账金额 <eb id="eb"><span class="font-striking">1000</span></eb> e币</span>
          </li>
        </ul>
        
      </section>
    </form>
    <textarea cols="30" rows="10" disabled>
充值说明：
1、移动充值卡支持全国卡和浙江、福建地方卡，福建卡只支持50元、100元；
2、选择充值卡面值时，请与您的卡实际面值保持一致，以免造成充值失败和充值卡失效。
    </textarea>
    
    <a class="pay" href="javascript:void(0)" id="ok" data-role="button">充值e币</a>
  </div>

  <script>
    $(function() {
		
    	var input = $("#input");
    	var password = $("#password");
    	var ok = $("#ok");
    	var eb = $("#eb");
    	
    	
      var $mcardType = $('input[name=mcard-type]');
      var $mcardTypePopup = $('#mcard-type-popup');
      var $mcardTypeAcceptor = $('#mcard-type-acceptor');
      
      var $mcardAmount = $('input[name=mcard-amount]');
      var $mcardAmountPopup = $('#mcard-amount-popup');
      var $mcardAmountAcceptor = $('#mcard-amount-acceptor');
      var $mcardAmountPopupFieldsets = $('#mcard-amount-popup > fieldset');
      
    //卡类型和金额
  	var c = $mcardType.val(),m = $mcardAmount.val();
    
      $mcardTypeAcceptor.click(function() {
        $mcardTypePopup.popup('open');
      });
      $mcardTypePopup.find('a').click(function() {
        var $this = $(this);
        //var type = parseInt($this.text());

        $mcardTypeAcceptor.text('充值卡类型：' + $this.text());
        $mcardTypePopup.popup('close');
        c = $this.attr("cardType");
        $mcardType.val(c);
        
        // Used for change different value for different card.
        $mcardAmountPopupFieldsets.css('display', 'none');
        $('#fieldset-for-card-type-' + c).css('display', 'block').find('a').first().click();
      });


      
      

      $mcardAmountAcceptor.click(function() {
        $mcardAmountPopup.popup('open');
      });
      $mcardAmountPopup.find('a').click(function() {
        var $this = $(this);
        
        $mcardAmountAcceptor.html('充值卡面额：<span class="font-striking">' + parseInt($this.text()) + '</span>元');
        $mcardAmountPopup.popup('close');
        m = $this.attr("money");
        $mcardAmount.val(m);
        eb.html('<span class="font-striking">' + m*100 + '</span>');
      });
      
      input.on("focus",function(){
  		if($(this).val() === "在此输入序列号"){
  			$(this).val("");
  		}
  	});
  	
  	function verifyCard(){
  	
  		var l = input.val().length;
  		//移动卡：序列号：输入位数大于等于10，小于等于17.密码：输入位数大于等于8，小于等于18；
  		if(c === "3"){
  		
  			if(l<10 || l>17){
  				showPrompt.init("请输入有效的序列号");
  				return false;
  			}
  		//联通卡：序列号15位阿拉伯数字，密码19位阿拉伯数字；
  		}else if(c === "4"){
  			if(l!== 15){
  				showPrompt.init("请输入有效的序列号");
  				return false;
  			}
  		//电信卡：卡号19位阿拉伯数字，密码18位阿拉伯数字；
  		}else if(c === "5"){
  			if(l!== 19){
  				showPrompt.init("请输入有效的序列号");
  				return false;
  			}
  		}
  		return true;
  	}
  	
  	
  	function passwordCard(){
  		//移动卡
  		var l = password.val().length;
  		
  		//移动卡：序列号：输入位数大于等于10，小于等于17.密码：输入位数大于等于8，小于等于18；
  		if(c === "3"){
  			if(l<8 || l>18){
  				showPrompt.init("请输入有效的密码");
  				return false;
  			}
  		//联通卡：序列号15位阿拉伯数字，密码19位阿拉伯数字；
  		}else if(c === "4"){
  			if(l!== 19){
  				showPrompt.init("请输入有效的密码");
  				return false;
  			}
  		//电信卡：卡号19位阿拉伯数字，密码18位阿拉伯数字；
  		}else if(c === "5"){
  			if(l!== 18){
  				showPrompt.init("请输入有效的密码");
  				return false;
  			}
  		}
  		return true;
  	}
  	
  	ok.on("click touchStart",function(){
  		var am = parseFloat($("#actualMoney").val());
  		if(verifyCard() && passwordCard()){
  			if(m<am){
  				showPrompt.init("抱歉,您选择的充值卡金额不够支付此订单，请重新选择!");
  			}else{
  				var commitUrl = "/module/card!cardCharge.e?channelType="+c+"&cardAmt="+m+"&cardNumber="+input.val()+"&cardPwd="+password.val();
  				var checkUrl = "/module/tradeResult.e"; //往这接口取支付确切结果
  				var successUrl= "/module/redirect!showResult.e"; //支付成功，走页面驱动
  				var failUrl= "/module/redirect!showResult.e"; //支付失败，走页面驱动
  				submitCardInfo2(commitUrl , checkUrl , successUrl ,failUrl );
  			}
  		}
  	})
    });
  </script>
</div>
</body>
</html>