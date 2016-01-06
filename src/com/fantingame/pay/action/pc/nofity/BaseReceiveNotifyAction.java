package com.fantingame.pay.action.pc.nofity;

import java.util.Date;

import org.apache.log4j.Logger;

import com.fantingame.pay.action.common.BaseAction;
import com.fantingame.pay.dao.PayChannelDao;
import com.fantingame.pay.entity.PayChannel;
import com.fantingame.pay.entity.PayEb;
import com.fantingame.pay.entity.PayNotify;
import com.fantingame.pay.entity.PayTrade;
import com.fantingame.pay.entity.PayUserAccount;
import com.fantingame.pay.entity.TradeCode;
import com.fantingame.pay.exception.EasouPayException;
import com.fantingame.pay.manager.PayEbManager;
import com.fantingame.pay.manager.PayNotifyManager;
import com.fantingame.pay.manager.PayTradeManager;
import com.fantingame.pay.manager.PayUserAccountManager;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.EbTools;

public class BaseReceiveNotifyAction extends BaseAction {

	private static final long serialVersionUID = 8215443336486744016L;

	private static final Logger logger = Logger.getLogger(BaseReceiveNotifyAction.class);

	/**
	 * 保存接收到Notify的历史记录
	 * */
	protected void savePayNotify(String ebOrderId, String channelTradeNo,long channelId, int easouStatus, String paidAmt) throws Exception{
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
	
	protected void doWorksAfterReceiveNotifyByEb(long orderId, int easouStatus , String channelStatus,String channelMsg,String paidAmt ,String channelTradeNo) throws Exception{
		PayEbManager payEbManager = (PayEbManager)getBean("payEbManager");
		PayEb payEb  = payEbManager.getEntityById(orderId);
		//检测订单是否存在
		if(payEb ==null){
			throw new EasouPayException(TradeCode.EASOU_CODE_140.code, TradeCode.EASOU_CODE_140.msgE);
		}
		payEb.setTradeNo(channelTradeNo);
		//判断是否已经处理过
		if(Constants.EASOU_SERVER_STATUS_SUCCESS == payEb.getStatus()){
			logger.info("[orderId]:"+payEb.getId()+"..."+TradeCode.EASOU_CODE_180.msgE);
			return ;
		}
		Long userId = Long.parseLong(payEb.getEasouId());
		PayUserAccountManager payUserAccountManager = (PayUserAccountManager)getBean("payUserAccountManager");
		// 查询用户是否存在
		PayUserAccount userAccount = payUserAccountManager.getUserAcountByJuserId(userId);
		// 账户不存在 则开户
		if (null == userAccount) {
			userAccount = new PayUserAccount();
			userAccount.setJuserId(userId);
			userAccount.setUsername("ft."+userId);
			userAccount.setFee(0L);
			payUserAccountManager.save(userAccount);
		}
		PayChannelDao payChannelDao = (PayChannelDao)getBean("payChannelDao");;
		PayChannel payChannel = payChannelDao.getEntityById(payEb.getChannelId());
		//EB支付除外的动作
		if(payEb.getChannelId()!=Constants.CHANNEL_ID_EB){//充值
			long reqEb = EbTools.rmbToEb(paidAmt);
			//加上一个折算率
			reqEb = Math.round(reqEb*payChannel.getRate());
			payUserAccountManager.updEb(Long.valueOf(payEb.getEasouId()),"X币充值",payEb.getInvoice(), payChannel.getName(),reqEb);
		}
		payEb.setPaidFee(paidAmt);
		payEb.setPaidCurrency("CNY");
		payEb.setSuccessDatetime(new Date());
		payEb.setTradeNo(channelTradeNo);
		payEb.setStatus(easouStatus);
		payEb.setReceiveStatus(channelStatus);
		payEb.setReceiveMsg(channelMsg);
		payEbManager.update(payEb);
		PayTradeManager tradeManager = (PayTradeManager) getBean("payTradeManager");
		PayTrade payTrade  = tradeManager.getEntityByInvoice(payEb.getInvoice());
		payTrade.setPaidFee(paidAmt);
		payTrade.setStatus(easouStatus);
		payTrade.setNotifyStatus(easouStatus);
		payTrade.setSuccessDatetime(new Date());
		tradeManager.update(payTrade);
	}
	
	
	protected void doWorksAfterReceiveNotify(long orderId, int easouStatus , String channelStatus,String channelMsg,String paidAmt ,String channelTradeNo) throws Exception{
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
			payTrade.setPaidFee(payTrade.getReqFee());
			payTrade.setSuccessDatetime(new Date());
			payEb.setPaidFee(paidAmt);
			payEb.setPaidCurrency("CNY");
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
