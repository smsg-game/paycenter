<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8"%>
<%@ page isELIgnored="false"%>
<%@page import="java.net.URLEncoder"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
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
  <title>梵付通收银台 - e币支付 - 游戏点卡充值</title>
  <link rel="stylesheet" href="${imgDomain}/js/vendor/jquery-mobile/jquery.mobile-1.3.1.min.css" />
  <link rel="stylesheet" href="${imgDomain}/css/module.css" />
  <script src="${domain}/js/vendor/jquery/jquery-1.9.1.min.js"> </script> 
  <script src="${imgDomain}/js/vendor/jquery-mobile/jquery.mobile-1.3.1.min.js"> </script> 
  <script src="${imgDomain}/js/module.js"></script>

</head>
<body>
<div data-role="page" id="gcard-deposit">
  <div data-role="header" style="display:none" data-theme="b">
    <h1>游戏点卡充值</h1>
  </div>
  <div data-role="content">
   <form action="" method="post">
   		<input type="hidden" id="actualMoney" value="${payTrade.reqFee}"/>
		<input type="hidden" id="invoice" value="${payTrade.invoice}"/>
      <section>
        <ul data-role="listview" data-divider-theme="b" data-inset="true">
          <li style="padding: 1.5em 15px;">
          	<span class="arror-down-to-right"></span>
            <div id="gcard-type-acceptor">
              <span>点卡类型：盛大一卡通</span>
            </div>
            <div data-role="popup" id="gcard-type-popup" data-overlay-theme="a">
              <fieldset data-role="controlgroup">
                <a href="" data-role="button" gcardType="6">盛大一卡通</a>
                <a href="" data-role="button" gcardType="8">征途卡</a>
                <a href="" data-role="button" gcardType="7">骏网一卡通</a>
                <a href="" data-role="button" gcardType="12">完美一卡通</a>
              </fieldset>
            </div>
            <input type="hidden" name="gcard-type" value="6">
          </li>
          <li style="padding: 1.5em 15px;">
          	<span class="arror-down-to-right"></span>
            <div id="gcard-amount-acceptor">
              <span>点卡面额：<span class="font-striking">10</span>元</span>
            </div>
            <div data-role="popup" id="gcard-amount-popup" data-overlay-theme="a">
              <fieldset data-role="controlgroup" id="fieldset-for-gcard-type-6">
	              <a href="" data-role="button" gcardAmount="10">10元</a>
	              <a href="" data-role="button" gcardAmount="15">15元</a>
	              <a href="" data-role="button" gcardAmount="25">25元</a>
	              <a href="" data-role="button" gcardAmount="30">30元</a>
	              <a href="" data-role="button" gcardAmount="35">35元</a>
	              <a href="" data-role="button" gcardAmount="50">50元</a>
	              <a href="" data-role="button" gcardAmount="100">100元</a>
	              <a href="" data-role="button" gcardAmount="1000">1000元</a>
	            </fieldset>
	            <fieldset data-role="controlgroup" id="fieldset-for-gcard-type-8" style="display:none">
	               <a href="" data-role="button" gcardAmount="5">5元</a>
	               <a href="" data-role="button" gcardAmount="10">10元</a>
	               <a href="" data-role="button" gcardAmount="20">20元</a>
	               <a href="" data-role="button" gcardAmount="30">30元</a>
	               <a href="" data-role="button" gcardAmount="50">50元</a>
	               <a href="" data-role="button" gcardAmount="100">100元</a>
	               <a href="" data-role="button" gcardAmount="250">250元</a>
	               <a href="" data-role="button" gcardAmount="300">300元</a>
	               <a href="" data-role="button" gcardAmount="500">500元</a>
	             </fieldset>
	             <fieldset data-role="controlgroup" id="fieldset-for-gcard-type-7" style="display:none">
	               <a href="" data-role="button" gcardAmount="10">10元</a>
	               <a href="" data-role="button" gcardAmount="20">20元</a>
	               <a href="" data-role="button" gcardAmount="30">30元</a>
	               <a href="" data-role="button" gcardAmount="50">50元</a>
	               <a href="" data-role="button" gcardAmount="100">100元</a>
	               <a href="" data-role="button" gcardAmount="200">200元</a>
	               <a href="" data-role="button" gcardAmount="300">300元</a>
	               <a href="" data-role="button" gcardAmount="500">500元</a>
	             </fieldset>
	             <fieldset data-role="controlgroup" id="fieldset-for-gcard-type-12" style="display:none">
	               <a href="" data-role="button" gcardAmount="15">15元</a>
	               <a href="" data-role="button" gcardAmount="30">30元</a>
	               <a href="" data-role="button" gcardAmount="50">50元</a>
	               <a href="" data-role="button" gcardAmount="100">100元</a>
	             </fieldset>
            </div>
            <input type="hidden" name="gcard-amount" value="10">
          </li>   
          <li class="inline-text-listview-li-padding">
            <div class="inline-text-input-grid-a ui-grid-a">
              <div class="ui-block-a">
                点卡号：
              </div>
              <div class="ui-block-b">
                <input type="text" id="gcard-no" name="gcard-no"/>
              </div>
            </div>
          </li>
          <li class="inline-text-listview-li-padding">
            <div class="inline-text-input-grid-a ui-grid-a">
              <div class="ui-block-a">
                点卡密码：
              </div>
              <div class="ui-block-b">
                <input type="text" id="gcard-pwd" name="gcard-pwd" />
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
1、全国官方完美游戏充值卡可用；
2、选择充值卡面值时，请与您的卡实际面值保持一致，以免造成充值失败和充值卡失效。
    </textarea>

    <a class="pay" id="ok" href="javascript:void(0)" data-role="button">充值e币</a>
  </div>

  <script>
  $(function() {

  	var cardNo = $("#gcard-no");
  	var cardPwd = $("#gcard-pwd");
  	var totalEb = $("#eb");
  	
    var $mcardType = $('input[name=gcard-type]');
    var $mcardTypePopup = $('#gcard-type-popup');
    var $mcardTypeAcceptor = $('#gcard-type-acceptor');
    
    var ok = $("#ok");
    
    var $mcardAmount = $('input[name=gcard-amount]');
    var $mcardAmountPopup = $('#gcard-amount-popup');
    var $mcardAmountAcceptor = $('#gcard-amount-acceptor');
    var $mcardAmountPopupFieldsets = $('#gcard-amount-popup > fieldset');
    
    var m = $mcardAmount.val();

    //卡类型和卡金额
    var c = $mcardType.val();
    
    $mcardTypeAcceptor.click(function() {
      $mcardTypePopup.popup('open');
    });
    $mcardTypePopup.find('a').click(function() {
      var $this = $(this);

      $mcardTypeAcceptor.text('点卡类型：' + $this.text());
      c = $this.attr("gcardType");
      $mcardType.val(c);
      
      // Used for change different value for different card.
      $mcardAmountPopupFieldsets.css("display", "none");
      $('#fieldset-for-gcard-type-' + c).css("display", "block").find('a').first().click();
      
      $mcardTypePopup.popup('close');
    });

    $mcardAmountAcceptor.click(function() {
      $mcardAmountPopup.popup('open');
    });
    $mcardAmountPopup.find('a').click(function() {
      var $this = $(this);

      $mcardAmountAcceptor.html('点卡面额：<span class="font-striking">' + parseInt($this.text()) + '</span>元');
      m = $this.attr("gcardAmount");
      $mcardAmount.val(m);
      totalEb.html('<span class="font-striking">' + m*100 + '</span>元');
      $mcardAmountPopup.popup('close');
    });
    
    	//卡号事件
    	cardNo.on("focus",function(){
			if($(this).val() === "在此输入序列号"){
				$(this).val("");
			}
		});
	
	 	function verifyCard(){
	 		
			var l = cardNo.val().length;
			//盛大卡：卡号15位或16位的数字/字母，密码8位或9位的阿拉伯数字；
			if(c === "6"){
				if(l!==15 && l!==16){
					showPrompt.init("请输入有效的卡号");
					return false;
				}
				
			//征途卡：卡号16位阿拉伯数字，密码8位阿拉伯数字；
			}else if(c === "8"){
				if(l!== 16){
					showPrompt.init("请输入有效的卡号");
					return false;
				}
			//骏网卡：卡号、密码都是16位的阿拉伯数字；
			}else if(c === "7"){
				if(l!== 16){
					showPrompt.init("请输入有效的卡号");
					return false;
				}
			
			//完美卡：卡号10位、密码15位的阿拉伯数字；
			}else if(c === "12"){
				if(l!== 10){
					showPrompt.init("请输入有效的卡号");
					return false;
				}
			}
			
			return true;
		}
    
	 	function passwordCard(){
			var l = cardPwd.val().length;
			//盛大卡：卡号15位或16位的数字/字母，密码8位或9位的阿拉伯数字；
			if(c === "6"){
				if(l!==8 && l!==9){
					showPrompt.init("请输入有效的密码");
					return false;
				}
				
			//征途卡：卡号16位阿拉伯数字，密码8位阿拉伯数字；
			}else if(c === "8"){
				if(l!== 8){
					showPrompt.init("请输入有效的密码");
					return false;
				}
			//骏网卡：卡号、密码都是16位的阿拉伯数字；
			}else if(c === "7"){
				if(l!== 16){
					showPrompt.init("请输入有效的密码");
					return false;
				}
			
			//完美卡：卡号10位、密码15位的阿拉伯数字；
			}else if(c === "12"){
				if(l!== 15){
					showPrompt.init("请输入有效的密码");
					return false;
				}
			}
			return true;
		}
    
    ok.on("click touchStart",function(){
		var am = parseFloat($("#actualMoney").val());
		
		if(verifyCard() && passwordCard()){
			//小于实际金额
			if(m<am){
				showPrompt.init("抱歉,您选择的充值卡金额不够支付此订单，请重新选择!");
			}else{
				var commitUrl = "/module/card.e?invoice="+$("#invoice").val()+"&channelType="+c+"&cardAmt="+m+"&cardNumber="+cardNo.val()+"&cardPwd="+cardPwd.val();
				var checkUrl = "/module/tradeResult.e"; //往这接口取支付确切结果
				var successUrl= "/module/redirect!showResult.e"; //支付成功，走页面驱动
				var failUrl= "/module/redirect!showResult.e"; //支付失败，走页面驱动
				submitCardInfo2(commitUrl , checkUrl , successUrl ,failUrl );
			}
		}
	});
	
  });
  </script>
</div>
</body>
</html>