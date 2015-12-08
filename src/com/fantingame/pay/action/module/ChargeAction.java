package com.fantingame.pay.action.module;

import org.apache.log4j.Logger;

import com.fantingame.pay.manager.PayTradeManager;

public class ChargeAction extends CardAction {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(ChargeAction.class);
	private String backUrl;//充值完成后回跳的地址
	
	//第一步，进来则导向到选择支付渠道页面
	public String execute() {
		//充值回跳地址
		getRequest().getSession().setAttribute("charge_backurl",backUrl);
		payTrade = getTradeByInvoice(invoice); //获取订单
		return SUCCESS;
	}
	
	//选择充值金额，直接导向到输入金额页面
	public String selectAmount() {
		String phoneNo = getUser().getMobile();
		getRequest().setAttribute("phoneNo", phoneNo);
		return getParam("type");
	}

	/*回到支付订单首页*/
	public String  toIndex() throws Exception{
		String invoice = (String)getWapSession().getAttribute("tradeInvoice");
		PayTradeManager payTradeManager = (PayTradeManager)getBean("payTradeManager");
		payTrade  = payTradeManager.getEntityByInvoice(invoice);
		return SUCCESS;
	}
	public String getBackUrl() {
		return backUrl;
	}

	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}
}
