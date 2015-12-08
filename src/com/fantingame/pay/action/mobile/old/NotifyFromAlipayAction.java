package com.fantingame.pay.action.mobile.old;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.fantingame.pay.action.common.BaseAction;
import com.fantingame.pay.entity.PayChannel;
import com.fantingame.pay.entity.PayNotify;
import com.fantingame.pay.entity.PayOrder;
import com.fantingame.pay.entity.PayTrade;
import com.fantingame.pay.entity.TradeCode;
import com.fantingame.pay.exception.EasouPayException;
import com.fantingame.pay.job.DelayItem;
import com.fantingame.pay.manager.PayChannelManager;
import com.fantingame.pay.manager.PayNotifyManager;
import com.fantingame.pay.manager.PayOrderManager;
import com.fantingame.pay.manager.PayTradeManager;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.RsaSignUtil;
import com.fantingame.pay.utils.StringTools;

/**
 * 支付宝通知接口,主要处理支付宝发送过来的对订单支付结果
 * */

public class NotifyFromAlipayAction extends BaseAction {

	private static final long serialVersionUID = 5490792191825343426L;
	private static final Logger logger = Logger.getLogger(NotifyFromAlipayAction.class);

	public String execute() throws Exception {
		//设置编码
	    getResponse().setContentType("text/html;charset=UTF-8");
		PrintWriter out = getResponse().getWriter();
		
		PayChannelManager payChannelManager = (PayChannelManager) getBean("payChannelManager");
		PayNotifyManager payNotifyManager = (PayNotifyManager) getBean("payNotifyManager");
		PayTradeManager tradeManager = (PayTradeManager) getBean("payTradeManager");
		PayOrderManager orderManager = (PayOrderManager)getBean("payOrderManager");
		
		try {
			//获取Notify数据
			String notifyData = getParam("notify_data");  
			//获取密钥
			PayChannel channel = payChannelManager.getEntityById(Constants.CHANNEL_ID_ALIPAY); 
			//验签
			if(!RsaSignUtil.doCheck("notify_data="+notifyData, getParam("sign") , channel.getChannelPublicKey())){ //如果验签不通过
				out.print(TradeCode.EASOU_CODE_110.msgE);
				logger.error(TradeCode.EASOU_CODE_110.msgE + "...notifyData:"+notifyData);
				return null;
			}
			//检查参数
			HashMap<String, String> params = StringTools.parserSimpleXmlStr(notifyData);
			if (params==null || params.isEmpty()) {    //如果没有有效数据
				out.print(TradeCode.EASOU_CODE_100.msgE);
				logger.error(TradeCode.EASOU_CODE_100.msgE + "...notifyData:"+notifyData);
				return null;
			}
			//获取参数
			String tradeStatus = StringTools.getValue("trade_status",params.get("trade_status"),32,true,false);//交易状态 
			String totalFee =  StringTools.getValue("total_fee",params.get("total_fee"),16,true,true);//交易金额
			String invoice = StringTools.getValue("out_trade_no",params.get("out_trade_no"),64,true,false); //外部交易号(商户交易号)
			String tradeNo = StringTools.getValue("trade_no",params.get("trade_no"),64,true,false); //支付宝交易号
			String subject = params.get("subject");//商品名称
			//String notifyRegTime = params.get("notify_reg_time");//通知时间
			
			//获取相关订单实体
			PayTrade trade  = tradeManager.getEntityByInvoice(invoice);    
			PayOrder order = orderManager.getEntityByInvoice(invoice);
			if(trade == null || order == null ){  //如果获取不到
				out.print(TradeCode.EASOU_CODE_140.msgE);
				logger.error(TradeCode.EASOU_CODE_140.msgE + "...invoice:" + invoice);
				return null;
			}
			
			//检查是否被处理过
			if(trade.getStatus()==1 && order.getServerStatus()==1){// 如果已经被处理过
				out.print("success");
				logger.error(TradeCode.EASOU_CODE_180.msgE+"...invoice:" + invoice);
				return null;
			}
			
			//保存Notify日志
			PayNotify  payNotify = new PayNotify();
			payNotify.setInvoice(invoice);
			payNotify.setTradeNo(tradeNo);
			payNotify.setTradeName(subject);
			payNotify.setStatus(tradeStatusToInt(tradeStatus));//转换状态
			payNotify.setReqFee(trade.getReqFee());
			payNotify.setPaidFee(totalFee);
			payNotify.setReqCurrency(trade.getCurrency());
			payNotify.setPaidCurrency(trade.getCurrency());
			payNotify.setAppId(trade.getAppId());
			payNotify.setPayerId(trade.getPayerId());
			payNotify.setChannelId(order.getChannelId());
			payNotify.setCreateDatetime(new Date());
			payNotifyManager.save(payNotify);
			
			if("TRADE_FINISHED".equals(tradeStatus) || "TRADE_SUCCESS".equals(tradeStatus)){//支付状态为成功
				//二、更新 pay_trade 表
				trade.setStatus(1);// 支付状态
				trade.setPaidFee(totalFee);//已支付金额
				trade.setSuccessDatetime(new Date());
				tradeManager.update(trade);
				//三、更新 pay_order 表
				order.setTradeNo(tradeNo);
				order.setPaidFee(totalFee);
				order.setPaidCurrency("CNY");
				order.setServerStatus(trade.getStatus());// 支付状态
				order.setReceiveMsg(tradeStatus);
				orderManager.update(order);
				//往DelayQueue里添加任务
				notifyGameParnter(trade);
				logger.info(Constants.EASOU_MSG_TRADE_SUCCESS + "...trade_no:" + tradeNo +" invoice:"+invoice);
			}else if("WAIT_BUYER_PAY".equals(tradeStatus)){//等待支付
				logger.info(Constants.EASOU_MSG_TRADE_WAITTING + "...trade_no:" + tradeNo +" invoice:"+invoice);
			}else{//支付失败
				trade.setStatus(-1);
				order.setServerStatus(-1);
				order.setReceiveMsg(tradeStatus);
				tradeManager.update(trade);
				orderManager.update(order);
				logger.info("trade_no:" + tradeNo +" invoice:"+invoice+"...trade_status"+tradeStatus);
			}
			out.print("success");
		}catch (EasouPayException e) {
			logger.error(e.getCode()+":"+e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		} 
		return null;
	}
	
	private void notifyGameParnter(PayTrade trade) {
		if(Constants.PARTNER_ID_SELF == trade.getPartnerId())  //E币充值，不鸟
			return ; 
	    if(trade.getNotifyUrl()==null || trade.getNotifyUrl().isEmpty())  // notifyUrl 为空，不鸟
	    	return; 
		DelayItem di = new DelayItem(1);
		di.setPaidFee(trade.getPaidFee());
		di.setNotifyUrl(trade.getNotifyUrl());
		di.setTradeId(trade.getTradeId());
		di.setTradeStatus("TRADE_SUCCESS");
		di.setInvoice(trade.getInvoice());
		di.setPartnerId(trade.getPartnerId());
		di.setAppId(trade.getAppId());
		di.setReqFee(trade.getReqFee());
		di.setPayerId(trade.getPayerId());
		di.setTradeName(trade.getTradeName());
		di.setNotifyDatetime(StringTools.getSecondFormat_().format(trade.getSuccessDatetime()));
		Constants.DQ.add(di);
    }
	
	private int tradeStatusToInt(String tradeStatus){
		if("TRADE_FINISHED".equals(tradeStatus) || "TRADE_SUCCESS".equals(tradeStatus)){
			return 1;
		}else if("WAIT_BUYER_PAY".equals(tradeStatus)){
			return 3;
		}else {
			return 0;
		}
	}

	
}
