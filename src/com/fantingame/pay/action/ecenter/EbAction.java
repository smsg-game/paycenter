package com.fantingame.pay.action.ecenter;

import java.util.Date;

import org.apache.log4j.Logger;

import com.fantingame.pay.action.ecenter.vo.UserPayTrade;
import com.fantingame.pay.entity.PayEb;
import com.fantingame.pay.entity.PayTrade;
import com.fantingame.pay.entity.TradeCode;
import com.fantingame.pay.entity.User;
import com.fantingame.pay.manager.PayEbManager;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.EbTools;

public class EbAction extends ECenterBaseAction {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(EbAction.class);
	private UserPayTrade data;
	
	/*E币支付*/
	public String execute() {
		try {
			payTrade = getTradeByInvoice(invoice);
			PayEb payEb = newPayEbOrder(invoice, Constants.CHANNEL_ID_EB,payTrade.getReqFee(),null);
			if (null == payEb || payTrade == null) {
				setMsg(TradeCode.EASOU_CODE_140.msgC);
				setStatus(FAIL);
				return JSON;
			}
			
			User user = (User) getWapSession().getAttribute("user");
			//将订单金额转化为E币数量
			Long reqEb = EbTools.rmbToEb(payEb.getReqFee());
			Long eb = getEb();
			//如果E币不足以支付订单，跳转到提示页
			if (eb == null || eb < reqEb) {
				setStatus("eb_not_enough");
				setMsg("f币余额不足");
				UserPayTrade userPayTrade = makeData(payTrade);
				setData(userPayTrade);
				return JSON;
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
				ebManager.updAfterRecNotify2(payTrade,payEb);
				
				//e币购买成功
				setStatus(SUCCESS);
				UserPayTrade userPayTrade = makeData(payTrade);
				setData(userPayTrade);
				return JSON;
			}
		} catch (Exception e) {
			setStatus(FAIL);
			setMsg(e.getMessage());
			logger.error(e.getMessage(),e);
			return JSON;
		}
		return JSON;
	}
	
	public UserPayTrade getData() {
		return data;
	}

	public void setData(UserPayTrade data) {
		this.data = data;
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

}
