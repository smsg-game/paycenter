package com.fantingame.pay.action.wap;

import java.util.Date;

import org.apache.log4j.Logger;

import com.fantingame.pay.action.common.BaseAction;
import com.fantingame.pay.entity.PayEb;
import com.fantingame.pay.entity.PayTrade;
import com.fantingame.pay.entity.TradeCode;
import com.fantingame.pay.entity.User;
import com.fantingame.pay.manager.PayEbManager;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.EbTools;

public class EbAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(EbAction.class);

	/*E币支付*/
	public String execute() {
		try {
			payTrade = getTradeByInvoice(invoice);
			PayEb payEb = newPayEbOrder(invoice, Constants.CHANNEL_ID_EB,payTrade.getReqFee(),null);
			if (null == payEb || payTrade == null) {
				setMsg(TradeCode.EASOU_CODE_140.msgC);
				return ERROR;
			}
			
			User user = (User) getWapSession().getAttribute("user");
			//将订单金额转化为E币数量
			Long reqEb = EbTools.rmbToEb(payEb.getReqFee());
			Long eb = getEb();
			//如果E币不足以支付订单，跳转到提示页
			if (eb == null || eb < reqEb) {
				return TRADE;
			}
			//能拿到用户信息
			if (null != user && eb > 0 && eb >= reqEb) {
				PayEbManager ebManager = (PayEbManager) getBean("payEbManager");
				//用E币支付
				payTrade.setPaidFee(payTrade.getReqFee());
				payTrade.setStatus(1);
				payTrade.setSuccessDatetime(new Date());
				payEb.setPaidCurrency("CNY");
				payEb.setPaidFee(payTrade.getPaidFee());
				payEb.setStatus(payTrade.getStatus());
				payEb.setSuccessDatetime(payTrade.getSuccessDatetime());	
				//一系列事务操作
				ebManager.updAfterRecNotify(payTrade,payEb);
				
				setStatus("success");
				return PAY_RESULT;
				
				//setRedirectUrl(payTrade.getRedirectUrl());
				//return REDIRECT;
			}
		} catch (Exception e) {
			setMsg(e.getMessage());
			logger.error(e.getMessage(),e);
			return ERROR;
		}
		return ERROR;
	}

}
