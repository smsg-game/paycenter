<%@ page language="java" import="java.util.*"  contentType="text/html; charset=UTF-8"%>
<%@ page isELIgnored="false"%>
<%@page import="java.net.URLEncoder"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@page import="com.fantingame.pay.utils.Constants"%>
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
  <title>e币支付 - mo9充值</title>
  <link rel="stylesheet" href="${imgDomain}/js/vendor/jquery-mobile/jquery.mobile-1.3.1.min.css" />
  <link rel="stylesheet" href="${imgDomain}/css/module.css" />
  <script src="${domain}/js/vendor/jquery/jquery-1.9.1.min.js"> </script> 
  <script src="${imgDomain}/js/vendor/jquery-mobile/jquery.mobile-1.3.1.min.js"> </script> 
  <script src="${imgDomain}/js/module.js"></script>

</head>
<body>
<div data-role="page" id="mo9-deposit">
  <div data-role="header" style="display:none">
    <h1>mo9充值</h1>
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
              	<a href="" data-role="button">10元</a>
                <a href="" data-role="button">30元</a>
                <a href="" data-role="button">50元</a>
                <a href="" data-role="button">100元</a>
                <a href="" data-role="button">300元</a>
                <a href="" data-role="button">500元</a>
                <a href="" data-role="button">1000元</a>
                <a href="" data-role="button">3000元</a>
                <a href="" data-role="button">5000元</a>
                <a href="" data-role="button">手动输入</a>
              </fieldset>
            </div>
            <input type="hidden" name="deposit-amount" value="5">
          </li>
          <li style="padding: 1.5em 15px;">
            <span>预计到账金额 <eb id="eb"><span class="font-striking">500</span></eb> e币</span>
          </li>
        </ul>
        
      </section>
    </form>

    <a class="pay" href="javascript:void(0)" id="ok" data-role="button">充值e币</a>
  </div>

  <script>
    $(function() {

      var $depositAmount = $('input[name=deposit-amount]');
      var $depositAmountPopup = $('#deposit-amount-popup');
      var $depositAmountAcceptor = $('#deposit-amount-acceptor');
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
          $depositAmount.val(amount);
          $("#eb").html('<span class="font-striking">' + amount * 100 + '</span>');
        } else {
          $depositAmountAcceptorWrapper.hide();
          $depositAmountInputGrid.show();

          $depositAmountInputGridInput.focus();
        }
		
        $depositAmountPopup.popup('close');
      });
      $depositAmountInputGridInput.keydown(function(e) {
        if (e.which == 13) {
          $depositAmountAcceptor.html('充值金额：<span class="font-striking">' + $(this).val() + '</span>元');

          $depositAmountInputGrid.hide();
          $depositAmountAcceptorWrapper.show();
			$("#eb").html('<span class="font-striking">' + $(this).val * 100 + '</span>');
          e.preventDefault();
        }
      });

      $("#ok").on("click touchStart",function(){
  		if("<%=request.getParameter("channelId")%>"=="<%=Constants.CHANNEL_ID_ALIPAY_WAP%>"){//阿里网页支付
  			location.href= "/wap/ali!aliCharge.e?money="+$depositAmount.val();
  		}else{
  			location.href= "/wap/mo9!mo9Charge.e?money="+$depositAmount.val();
  		}
  	});
    });
  </script>
</div>
</body>
</html>