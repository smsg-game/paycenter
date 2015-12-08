package com.fantingame.pay.action.pc;

import org.apache.log4j.Logger;

import com.fantingame.pay.action.common.BaseAction;
import com.fantingame.pay.entity.PayChannel;
import com.fantingame.pay.entity.PayEb;
import com.fantingame.pay.entity.PayTrade;
import com.fantingame.pay.entity.TradeCode;
import com.fantingame.pay.exception.EasouPayException;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.StringTools;
import com.fantingame.pay.utils.mo9.Mo9Util;

//摩9下单接口
public class Mo9Action extends BaseAction{
	
	private static final long serialVersionUID = 48545533070067807L;
	private static Logger logger = Logger.getLogger(Mo9Action.class);
	
	/*用mo9充值E币充值成功后去到下单首页
	public String charge(){
		try {
			String money = getParam("money");
			PayTrade trade = newChargeTrade(money,null);
			PayEb payEb = newPayEbOrder(trade.getInvoice(), Constants.CHANNEL_ID_MO9);
			PayChannel channel = getChannelById(Constants.CHANNEL_ID_MO9);
			String returnUrl = getBaseUrl()+"/mobile/trade!toIndex.e";  //组装返回URL
			setRedirectUrl( Mo9Util.getReqUrl(trade,payEb,channel,returnUrl));
			return REDIRECT;
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
			return ERROR;
		}
	}
	*/
	
	//mobile版下单接口，与wap版只是returnUrl不同   //支付成功后去到支付结果页
	public String execute() throws Exception {
		try{
			PayTrade payTrade = getTradeByInvoice(invoice);
			PayEb payEb = newPayEbOrder(invoice, Constants.CHANNEL_ID_MO9,payTrade.getReqFee(),null);
			//判断为空，则后退
			if (null == payEb || payTrade == null) {
				setMsg(TradeCode.EASOU_CODE_140.msgC);
				return ERROR;
			}
			PayChannel channel = getChannelById(Constants.CHANNEL_ID_MO9);
			String returnUrl = getBaseUrl()+"/mobile/redirect.e";  //组装返回URL
			setRedirectUrl( Mo9Util.getReqUrl(payTrade,payEb,channel,returnUrl));
			return REDIRECT;
		}catch (EasouPayException e) {
			setMsg(TradeCode.EASOU_CODE_200.msgC);
			logger.error(e.getMessage(), e);
			return ERROR;
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			setMsg(e.getMessage());
			return ERROR;
		}
	}
	
	/*用MO9充值E币，跳转到Mo9正常支付流程*/
	public String mo9Charge(){
		try {
			isCharge = true;
			String backUrl = (String)getRequest().getSession().getAttribute("charge_backurl");
			String money = getParam("money");
			String otherMoney = getParam("otherMoney");
			String invoiceId=getParam(Constants.FIELD_INVOICE_ID);
			if(otherMoney!=null && otherMoney.length()>0 && StringTools.isNum(otherMoney)){ //优先使用用户输入的自定义金额
				money = otherMoney;
			}
			PayTrade trade = newChargeTrade(money,backUrl,"mobile",invoiceId);
			if(trade!=null) invoice = trade.getInvoice();
			return this.execute();
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
			return ERROR;
		}
	}

}
