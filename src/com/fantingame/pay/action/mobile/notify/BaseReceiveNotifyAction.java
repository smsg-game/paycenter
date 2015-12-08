package com.fantingame.pay.action.mobile.notify;

import java.util.Date;

import org.apache.log4j.Logger;

import com.fantingame.pay.action.common.BaseAction;
import com.fantingame.pay.entity.PayEb;
import com.fantingame.pay.entity.PayNotify;
import com.fantingame.pay.entity.PayTrade;
import com.fantingame.pay.entity.TradeCode;
import com.fantingame.pay.exception.EasouPayException;
import com.fantingame.pay.manager.PayEbManager;
import com.fantingame.pay.manager.PayNotifyManager;
import com.fantingame.pay.manager.PayTradeManager;
import com.fantingame.pay.utils.Constants;

public class BaseReceiveNotifyAction extends BaseAction {

	private static final long serialVersionUID = 8215443336486744016L;

	private static final Logger logger = Logger.getLogger(BaseReceiveNotifyAction.class);

	/**
	 * 保存接收到Notify的历史记录
	 * */
	public void savePayNotify(String ebOrderId, String channelTradeNo,long channelId, int easouStatus, String paidAmt) throws Exception{
		PayNotifyManager notifyManager = (PayNotifyManager) getBean("payNotifyManager");
		PayNotify notify = new PayNotify();
		notify.setInvoice(ebOrderId);
		notify.setTradeNo(channelTradeNo);
		notify.setChannelId(channelId);
		notify.setStatus(easouStatus);
		notify.setPaidFee(paidAmt);
		notify.setCreateDatetime(new Date());
		int success = notifyManager.save(notify);
		if(success!=1){
			throw new Exception("保存PayNotify实例出错，更新条数为:"+success);
		}
	}
	
	
	public void doWorksAfterReceiveNotify(long orderId, int easouStatus , String channelStatus,String channelMsg,String paidAmt ,String channelTradeNo) throws Exception{
		PayEbManager payEbManager = (PayEbManager)getBean("payEbManager");
		PayTradeManager tradeManager = (PayTradeManager) getBean("payTradeManager");
		PayEb payEb  = payEbManager.getEntityById(orderId);
		PayTrade payTrade  = tradeManager.getEntityByInvoice(payEb.getInvoice());
		
		//检测订单是否存在
		if(payEb ==null || payTrade == null){
			throw new EasouPayException(TradeCode.EASOU_CODE_140.code, TradeCode.EASOU_CODE_140.msgE);
		}
		
		//判断是否已经处理过
		if(Constants.EASOU_SERVER_STATUS_SUCCESS == payTrade.getStatus() && Constants.EASOU_SERVER_STATUS_SUCCESS == payEb.getStatus()){
			logger.info("[orderId]:"+payEb.getId()+"..."+TradeCode.EASOU_CODE_180.msgE);
			return ;
		}

		if(Constants.EASOU_SERVER_STATUS_SUCCESS == easouStatus){  //支付成功
			payTrade.setPaidFee(payTrade.getReqFee());  //需付金额
			payTrade.setSuccessDatetime(new Date());
			if(paidAmt.equals("-1")){
				paidAmt = payTrade.getReqFee();
			}
			payEb.setPaidFee(paidAmt);	//已付金额
			payEb.setPaidCurrency("CNY"); //货币类型
			payEb.setSuccessDatetime(new Date());
		}
		payTrade.setStatus(easouStatus);
		payEb.setTradeNo(channelTradeNo);
		payEb.setStatus(easouStatus);
		payEb.setReceiveStatus(channelStatus);
		payEb.setReceiveMsg(channelMsg);

		payEbManager.updAfterRecNotify(payTrade,payEb);
		
	}
}
