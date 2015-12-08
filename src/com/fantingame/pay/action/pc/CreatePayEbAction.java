package com.fantingame.pay.action.pc;

import org.apache.log4j.Logger;

import com.easou.common.util.StringUtil;
import com.fantingame.pay.action.common.BaseAction;
import com.fantingame.pay.entity.PayEb;
import com.fantingame.pay.entity.TradeCode;
import com.fantingame.pay.exception.EasouPayException;
import com.fantingame.pay.utils.Constants;

/**
 * 创建eb订单
 */
public class CreatePayEbAction extends BaseAction {

	private static final long serialVersionUID = -5019914247728372377L;
	private static final Logger logger = Logger.getLogger(CreatePayEbAction.class);
	
	private PayEb payEb;
	
	//使用支付宝支付订单
	public String execute(){
		try {
			String reqFee = getRequest().getParameter("reqFee");
			if(StringUtil.isEmpty(reqFee) && !StringUtil.isNumber(reqFee)){
				return ERROR;
			}
			payEb = newEbOrder(null,reqFee,null); //下订单
		} catch (Exception e) {
			logger.error("to pc alipay error!",e);
			return ERROR;
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
