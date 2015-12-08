<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8"%>
<%@ page isELIgnored="false"%>
<%@page import="java.net.URLEncoder"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String imgDomain = request.getContextPath();
	request.setAttribute("imgDomain", imgDomain);
	String url = request.getScheme() + "://" + request.getServerName()+ request.getContextPath(); 
    url = url +"/module/return!returnResult.e";
    String appAgent = request.getHeader("App-Agent");
	if("AndroidSDKEasouPay".equals(appAgent)) {
		request.setAttribute("isWeb", false);
	}else {
		request.setAttribute("isWeb", true);
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
  <meta name="apple-mobile-web-app-capable" content="yes" />
  <meta name="apple-mobile-web-app-status-bar-style" content="black" />
  <title>e币支付 - 短信充值</title>
  <link rel="stylesheet" href="${imgDomain}/js/vendor/jquery-mobile/jquery.mobile-1.3.1.min.css" />
  <link rel="stylesheet" href="${imgDomain}/css/module.css" />
  <script src="${domain}/js/vendor/jquery/jquery-1.9.1.min.js"> </script> 
  <script src="${imgDomain}/js/vendor/jquery-mobile/jquery.mobile-1.3.1.min.js"> </script> 
  <script src="${imgDomain}/js/module.js"></script>

</head>
<body>
<div data-role="page" id="sms-deposit">
  <div data-role="header" style="display:none">
    <h1>短信充值</h1>
  </div>
  <div data-role="content">
   <form action="" method="post">
      <section>
        <ul data-role="listview" data-divider-theme="b" data-inset="true">
          <li id="deposit-amout-input-grid" class="inline-text-listview-li-padding" style="display:none">  
            <div class="inline-text-input-grid-a ui-grid-a">
              <div class="ui-block-a">
                充值金额：
              </div>
              <div class="ui-block-b">
                <input type="text" placeholder="请输入您需要充值的金额" />
              </div>
            </div>
          </li>
          <li id="deposit-amount-acceptor-wrapper" style="padding: 1.5em 15px;">
          	<span class="arror-down-to-right"></span>
            <div id="deposit-amount-acceptor">
              <span>充值金额：<span class="font-striking">5</span>元</span>
            </div>
            <div data-role="popup" id="deposit-amount-popup" data-overlay-theme="a">
              <fieldset data-role="controlgroup">
              	<a href="" data-role="button" money="5">5元</a>
                <a href="" data-role="button" money="10">10元</a>
                <a href="" data-role="button" money="15">15元</a>
                <a href="" data-role="button" money="20">20元</a>
                <a href="" data-role="button" money="30">30元</a>
              </fieldset>
            </div>
            <input type="hidden" name="money" id="money" value="5">
          </li>
          <li class="inline-text-listview-li-padding">
            <div class="inline-text-input-grid-a ui-grid-a">
              <div class="ui-block-a">
                请输入手机号码：
              </div>
              <div class="ui-block-b">
                <input type="text" name="phone-number" id="phone-number" />
              </div>
            </div>
          </li>
          <li style="padding: 1.5em 15px;">
            <span>预计到账金额 <epay id="eb"><span class="font-striking">250</span></epay> e币</span>
          </li>
        </ul>
        
      </section>
    </form>
    <textarea name="" id="" cols="30" rows="10" disabled>
充值说明：
1.短信充值目前只支持移动、电信（新疆、宁夏、甘肃三省暂不支持）；
2.每天5元可以支付两次，其余一次；
3.每月支付总额不能超过70元；
    </textarea>
    
    <a class="pay" href="javascript:void(0)" id="ok" data-role="button">充值e币</a>
  </div>

  <script>
    $(function() {

      var $depositAmount = $('input[name=money]');
      var $depositAmountPopup = $('#deposit-amount-popup');
      var $depositAmountAcceptor = $('#deposit-amount-acceptor');
      var $depositAmountAcceptorWrapper = $('#deposit-amount-acceptor-wrapper');
      var $depositAmountInputGrid = $('#deposit-amout-input-grid');
      var $depositAmountInputGridInput = $depositAmountInputGrid.find('input');

      $depositAmountAcceptor.click(function() {
        $depositAmountPopup.popup('open');
      });

     $depositAmountPopup.find('a').click(function() {
        var $this = $(this);

        // The result will be NaN if 'other' option selected by users.
        var amount = parseInt($this.text());

        // If deposit amount is a valid number rather than NaN.
        if (amount) {
          $depositAmountAcceptor.html('充值金额：<span class="font-striking">' + amount + '</span>元');
        } else {
          $depositAmountAcceptorWrapper.hide();
          $depositAmountInputGrid.show();

          $depositAmountInputGridInput.focus();
          
	       	// Clean old value prepare to get new value from users, and 
	     		// set empty value for verify() method.
	        $depositAmount.val('');
        }
        //加入金额
        var money = $this.attr("money");
        $depositAmount.val(money);
        $("#eb").html('<span class="font-striking">' + money * 50 + '<span>');
        $depositAmountPopup.popup('close');
      });
      $depositAmountInputGridInput.keydown(function(e) {
        if (e.which == 13) {
          $depositAmountAcceptor.html('充值金额：<span class="font-striking">' + $(this).val() + "</span>元");

          $depositAmountInputGrid.hide();
          $depositAmountAcceptorWrapper.show();
          $("#eb").html('<span class="font-striking">' + $(this).val() * 50 + '</span>');
          $depositAmount.val($(this).val());
          e.preventDefault();
        }
      });
      
      //手机号对象	
      var phoneNum = $("#phone-number");
      var money = $("#money");
      phoneNum.on("focus",function(){
  		if(phoneNum.val()==="请输入您的手机号"){
  			phoneNum.val("");
  		}
  	  });
      	
     	 $("#ok").on("click touchStart",function(){
  	    	if(verify()){
  	    		var validateUrl = "/module/smsYhxf!vp.e?money=" + $depositAmount.val()+"&phoneNum="+phoneNum.val()+"&invoice="+$("#invoice").val();
  	  	    	$.getJSON(validateUrl, function(vdata) {
  	  	    		if(vdata.status == 'success') {
	  	  	    		var href= "/module/smsYhxf!smsCharge.e?money="+money.val()+"&phoneNum="+phoneNum.val();
	  		  	       	if('${isWeb}' == 'true') {
	  		   	    	   location.href = href + "&isWeb=1";
	  		  	       	} else {
	  			  	       	$.getJSON(href,function(data) {
	  				            var invoice = data.invoice;
	  						    if(data.status=='success'){
	  						       window.AndroidSmsPayForJavaScript.startSmsPay(data.msg,data.number,'<%=url%>?invoice='+invoice);
	  						    }else{
	  						       location.href = "/module/redirect!showResult.e?status="+data.status+"&msg="+encodeURIComponent(data.msg)+"&invoice="+data.invoice;
	  						    }
	  					   }); 
	  		  	       	}
  	  	    		} else {
  	  	    			showPrompt.init(vdata.msg);
  	  	    		}
  	  	    	});
  	    	} 
  		});
  	
	  	function verify(){
	  	    if(phoneNum.val()=="" || phoneNum.val()=="请输入您的手机号" || !new RegExp(/^(13|14|15|18)[0-9]{9}$/).test(phoneNum.val())){
	  	        showPrompt.init("请输入正确的手机号");
	  	        return false;
	  	    }
	  	  	if (!$depositAmount.val()) {
	  	    	showPrompt.init("请输入充值金额，并确认");
	  	      return false;
	  	    }
	  	    return true;
	  	}

      
    })
  </script>
</div>
</body>
</html>