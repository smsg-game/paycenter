package com.fantingame.pay.action.mobile.old;

import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletRequest;

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
import com.fantingame.pay.utils.StringTools;
import com.mokredit.payment.Md5Encrypt;

/**
 * Mo9通知接口,主要处理mo9发送过来的对订单支付结果
 * */

public class NotifyFromMo9Action extends BaseAction {

	private static final long serialVersionUID = -7501954146542499359L;
	private static Logger logger = Logger.getLogger(NotifyFromMo9Action.class);
	
	
	public String execute() throws Exception {
		//设置编码
		PrintWriter out = getResponse().getWriter();// 输出流
		try {
			
			logger.info("notify from mo9>>>[invoice]:"+getParam("invoice")+"...[trade_status]:"+getParam("trade_status"));
			
			//获取参数
			Map<String, String> params = checkParams(getRequest());
			
			PayChannelManager payChannelManager = (PayChannelManager) getBean("payChannelManager");
			PayNotifyManager notifyManager = (PayNotifyManager) getBean("payNotifyManager");
			PayTradeManager tradeManager = (PayTradeManager) getBean("payTradeManager");
			PayOrderManager orderManager = (PayOrderManager)getBean("payOrderManager");
			
			String sign = params.get("sign");
			String invoice = params.get("invoice");
			String status = params.get("trade_status");
			String trade_no = params.get("trade_no");
			PayChannel channel = payChannelManager.getEntityById(Constants.CHANNEL_ID_MO9);//获取密钥
			PayTrade trade  = tradeManager.getEntityByInvoice(invoice); 
			PayOrder order = orderManager.getEntityByInvoice(invoice);

			//验签
			if(sign==null || "".equals(sign) || !sign.equals(Md5Encrypt.sign(params, channel.getSecretKey()))){ //签名为空/验证不通过
				out.print(TradeCode.EASOU_CODE_110.msgE);
				logger.error(TradeCode.EASOU_CODE_110.msgE+"...invoice:"+invoice);
				return null;
			}
			
			//检查订单是否存在
			if(trade ==null || order == null){  // 不存在对应的订单
				out.print(TradeCode.EASOU_CODE_140.msgE);
				logger.error(TradeCode.EASOU_CODE_140.msgE + "...invoice:"+invoice);
				return null;
			}
			
			//检查订单是否已经被处理过
			if(trade.getStatus()==1 && order.getServerStatus()==1){ // 对应订单已经被处理过
				out.print("OK"); 
				logger.error(TradeCode.EASOU_CODE_180.msgE + "...invoice:"+invoice);
				return null;
			}
			
			// 一、保存数据到pay_notify表
			PayNotify notify = new PayNotify();
			notify.setInvoice(invoice);
			notify.setTradeNo(params.get("trade_no"));
			notify.setTradeName(params.get("item_name"));
			notify.setPayerId(params.get("payer_id"));
			notify.setStatus(StringTools.strToDouble(params.get("req_amount"))>StringTools.strToDouble(params.get("amount"))?2:1);
			notify.setReqFee(params.get("req_amount"));
			notify.setReqCurrency(params.get("req_currency"));
			notify.setPaidFee(params.get("amount"));
			notify.setPaidCurrency(params.get("currency"));
			notify.setAppId(params.get("app_id"));
			notify.setChannelId(channel.getId());
			notify.setCreateDatetime(new Date());
			notifyManager.save(notify); 
			
			//检查交易是否成功
			if(Constants.MO9_TRADE_SUCCESS.equals(status)){// 交易状态为成功
				//二、更新 pay_trade 表
				trade.setStatus(notify.getStatus());// 支付状态
				trade.setPaidFee(params.get("amount"));//已支付金额
				trade.setSuccessDatetime(new Date());
				tradeManager.update(trade);
				//三、更新 pay_order 表
				order.setTradeNo(params.get("trade_no"));
				order.setPaidFee(params.get("amount"));
				order.setPaidCurrency(params.get("currency"));
				order.setServerStatus(trade.getStatus());// 支付状态
				order.setReceiveMsg(status);
				orderManager.update(order);
				//往DelayQueue里添加任务(完全支付成功或者部分支付并且订单可切分)
				if(order.getServerStatus()==1 || (order.getServerStatus()==2 && trade.getSeparable()==1)){
					notifyGameParnter(trade);
				}
				logger.info(Constants.EASOU_MSG_TRADE_SUCCESS + "...trade_no:" + trade_no +" invoice:"+invoice);
			}else{//支付失败
				trade.setStatus(-1);
				order.setServerStatus(-1);
				order.setReceiveMsg(status);
				tradeManager.update(trade);
				orderManager.update(order);
				logger.info(Constants.EASOU_MSG_TRADE_FAIL + "...trade_no:" + trade_no +" trade_status:"+status);
			}
			out.print("OK");
		}catch (EasouPayException e) {
			logger.error(e.getCode()+":"+e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
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


	private Map<String, String> checkParams(ServletRequest req) throws Exception{
		Map<String, String> map = new TreeMap<String, String>();
		StringTools.setValue(map,"pay_to_email",getParam("pay_to_email"),50,true,false);
		StringTools.setValue(map,"payer_id",getParam("payer_id"),64,true,false);
		StringTools.setValue(map,"trade_no",getParam("trade_no"),32,false,false);
		StringTools.setValue(map,"trade_status",getParam("trade_status"),32,true,false);
		StringTools.setValue(map,"amount",getParam("amount"),16,true,true);
		StringTools.setValue(map,"currency",getParam("currency"),16,true,false);
		StringTools.setValue(map,"req_amount",getParam("req_amount"),16,true,false);
		StringTools.setValue(map,"req_currency",getParam("req_currency"),16,true,false);
		StringTools.setValue(map,"item_name",getParam("item_name"),30,true,false);
		StringTools.setValue(map,"lc",getParam("lc"),16,true,false);
		StringTools.setValue(map,"extra_param",getParam("extra_param"),255,false,false);
		StringTools.setValue(map,"app_id",getParam("app_id"),32,true,false);
		StringTools.setValue(map,"invoice",getParam("invoice"),32,true,false);
		StringTools.setValue(map,"sign",getParam("sign"),32,true,false);
		return map;
	}

}
