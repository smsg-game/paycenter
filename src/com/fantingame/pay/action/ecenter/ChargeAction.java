package com.fantingame.pay.action.ecenter;

import org.apache.log4j.Logger;

import com.fantingame.pay.action.ecenter.vo.UserPayTrade;
import com.fantingame.pay.entity.PayTrade;
import com.fantingame.pay.entity.TradeCode;
import com.fantingame.pay.manager.PayTradeManager;

public class ChargeAction extends CardAction {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(ChargeAction.class);
	private String backUrl;//充值完成后回跳的地址
	private UserPayTrade data;
	
	//第一步，进来则导向到选择支付渠道页面
	public String execute() {
		String invoice = (String)getWapSession().getAttribute("tradeInvoice");
		logger.info("charge success redirect url=ecenter/charge.e,invoice=" + invoice);
		PayTradeManager payTradeManager = (PayTradeManager)getBean("payTradeManager");
		payTrade  = payTradeManager.getEntityByInvoice(invoice);
		if(payTrade != null) {
			UserPayTrade userPayTrade = makeData(payTrade);
			setStatus(SUCCESS);
			setData(userPayTrade);
		} else {
			setStatus(FAIL);
		}
		return JSON;
	}
	
	private UserPayTrade makeData(PayTrade payTrade) {
		if(payTrade == null) {
			return null;
		}
		UserPayTrade userPayTrade = new UserPayTrade();
		userPayTrade.setEasouId(payTrade.getEasouId());
		userPayTrade.setUsername(getUser().getName());
		userPayTrade.setPayerId(payTrade.getPayerId());
		userPayTrade.setEb(getEb());
		userPayTrade.setInvoice(payTrade.getInvoice());
		userPayTrade.setTradeId(payTrade.getTradeId());
		userPayTrade.setTradeName(payTrade.getTradeName());
		userPayTrade.setTradeDesc(payTrade.getTradeDesc());
		userPayTrade.setReqFee(payTrade.getReqFee());
		userPayTrade.setPaidFee(payTrade.getPaidFee());
		userPayTrade.setReqEb(payTrade.getReqEb());
		return userPayTrade;
	}
	
	//选择充值金额，直接导向到输入金额页面
	public String selectAmount() {
		String phoneNo = getUser().getMobile();
		getRequest().setAttribute("phoneNo", phoneNo);
		return getParam("type");
	}

	/*回到支付订单首页*/
	public String  toIndex() throws Exception{
		logger.info("charge success url=ecenter/charge!toIndex.e,invoice=" + invoice);
		PayTradeManager payTradeManager = (PayTradeManager)getBean("payTradeManager");
		payTrade  = payTradeManager.getEntityByInvoice(invoice);
		if(payTrade != null) {
			UserPayTrade userPayTrade = makeData(payTrade);
			setStatus(SUCCESS);
			setData(userPayTrade);
		} else {
			setMsg(TradeCode.EASOU_CODE_140.msgE);
			setStatus(FAIL);
		}
		return JSON;
	}
	public String getBackUrl() {
		return backUrl;
	}

	public UserPayTrade getData() {
		return data;
	}

	public void setData(UserPayTrade data) {
		this.data = data;
	}

	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}
}
