package com.fantingame.pay.action.mobile;

import java.util.Date;

import org.apache.log4j.Logger;

import com.fantingame.pay.action.common.BaseAction;
import com.fantingame.pay.entity.PayEb;
import com.fantingame.pay.entity.PayTrade;
import com.fantingame.pay.entity.TradeCode;
import com.fantingame.pay.entity.User;
import com.fantingame.pay.exception.EasouPayException;
import com.fantingame.pay.manager.PayEbManager;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.EbTools;

public class EbAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(EbAction.class);

	public String execute() {
		try {
			payTrade    = getTradeByInvoice(invoice);
			PayEb payEb = newPayEbOrder(invoice, Constants.CHANNEL_ID_EB,payTrade.getReqFee(),null);
			if (null == payEb || payTrade == null) {
				setMsg(TradeCode.EASOU_CODE_140.msgC);
				return ERROR;
			}
			// 用户信息获取
			User user = (User) getWapSession().getAttribute("user");
			Long reqEb = EbTools.rmbToEb(payEb.getReqFee());
			// 获取E币
			Long eb = getEb();
			if (eb==null || eb < reqEb) {
				// eb不足够 反回到充值页面
				return "trade";
			}
			// eb足够
			if (user !=null && eb > 0 && eb >= reqEb) {

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
			}

		} catch (EasouPayException e) {
			setMsg(e.getMessage());
			logger.error("支付失败:" + e.getMessage());
		} catch (Exception e) {
			setMsg("未知错误!");
			logger.error(e.getMessage(), e);
		}
		return ERROR;
	}
}
