package com.fantingame.pay.action.mobile;

import org.apache.log4j.Logger;

import com.fantingame.pay.entity.PayTrade;

public class ChargeAction extends CardAction {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(ChargeAction.class);
	
	private String backUrl;//充值完成后回跳的地址
	
	//默认，进来则导向到选择支付渠道页面
	public String execute() {
		//假如是不是App访问，则跳到App的请求地址去
		if(isEasouHallApp()) return "charge_wap";
		
		//充值回跳地址
		getRequest().getSession().setAttribute("charge_backurl",backUrl);
		return SUCCESS;
	}

	//选择充值金额，直接导向到输入金额页面
	public String selectAmount() {
		String phoneNo = getUser().getMobile();
		getRequest().setAttribute("phoneNo", phoneNo);
		return getParam("type");
	}

	public String getBackUrl() {
		return backUrl;
	}

	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}
	
}
