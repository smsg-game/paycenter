package com.fantingame.pay.action.pc;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.fantingame.pay.action.common.BaseAction;
import com.fantingame.pay.entity.PayEb;
import com.fantingame.pay.manager.PayEbManager;
import com.fantingame.pay.utils.Constants;

//接口不可靠，仅用于驱动前端页面，故不加签名；文档中需注明；
public class RedirectAction extends BaseAction {

	private static Logger logger = Logger.getLogger(RedirectAction.class);
	private static final long serialVersionUID = 2414766626391918361L;

	private PayEb payEb;
	
	public String execute() {
		String payOrderId = getRequest().getParameter("out_trade_no");
		try {
			if(StringUtils.isNotBlank(payOrderId)){
				PayEbManager payEbManager = (PayEbManager)getBean("payEbManager");
				payEb = payEbManager.getEntityById(Long.valueOf(payOrderId));
				if(payEb == null){
					return ERROR;
				}
				String channel = "2";
				if(payEb.getChannelId() == Constants.CHANNEL_ID_ALIPAY_PC){
					channel = "1";
				}
				StringBuilder sbBuilder = new StringBuilder();
				String returnUrl = (String)getRequest().getSession().getAttribute("returnUrl");
				sbBuilder.append(returnUrl+"?").append("orderId=").append(payEb.getId()+"&")
											.append("username="+getUser().getName()+"&")
											.append("reqFee="+payEb.getReqFee()+"&")
											.append("channelId="+channel+"&")
											.append("createTime="+payEb.getCreateDatetime().getTime()/1000+"&");
				int i = 0;
				while(payEb.getStatus() != Constants.EASOU_SERVER_STATUS_SUCCESS
						&& i <= 10){
					//腾讯的休息2秒
					Thread.sleep(1000);
					payEb = payEbManager.getEntityById(Long.valueOf(payOrderId));
					i++;
				}
				if(payEb.getStatus() == Constants.EASOU_SERVER_STATUS_SUCCESS){
					sbBuilder.append("result=success");
				}else{
					sbBuilder.append("result=false");
				}
				sbBuilder.append("&allEb="+getUserAccountById(Long.valueOf(payEb.getEasouId())).getFee());
				this.redirectUrl = sbBuilder.toString();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return REDIRECT;
	}

	/*
	 * 梵町SDK--支付结果
	 * */
	public String showResult(){
		String payOrderId = getRequest().getParameter("out_trade_no");
		try {
			if(StringUtils.isNotBlank(payOrderId)){
				PayEbManager payEbManager = (PayEbManager)getBean("payEbManager");
				PayEb payEb = payEbManager.getEntityById(Long.valueOf(payOrderId));
				if(payEb != null){
					setInvoice(payEb.getInvoice());
					payTrade = getTradeByInvoice(invoice);
					if(payTrade != null && payTrade.getStatus() == Constants.EASOU_SERVER_STATUS_FAIL) {
						return FAIL;
					} else if(payTrade != null && payTrade.getStatus() != Constants.EASOU_SERVER_STATUS_SUCCESS) {
						return FAIL;
					} else if(payTrade != null && payTrade.getStatus() == Constants.EASOU_SERVER_STATUS_SUCCESS) {
						return SUCCESS;
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return SUCCESS;
	}

	public PayEb getPayEb() {
		return payEb;
	}

	public void setPayEb(PayEb payEb) {
		this.payEb = payEb;
	}
	
}
