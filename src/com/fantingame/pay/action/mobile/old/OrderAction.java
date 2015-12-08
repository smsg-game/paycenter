package com.fantingame.pay.action.mobile.old;

import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletRequest;

import org.apache.log4j.Logger;

import com.fantingame.pay.action.common.BaseAction;
import com.fantingame.pay.entity.PayChannel;
import com.fantingame.pay.entity.PayEb;
import com.fantingame.pay.entity.PayOrder;
import com.fantingame.pay.entity.PayTrade;
import com.fantingame.pay.entity.TradeBean;
import com.fantingame.pay.entity.TradeCode;
import com.fantingame.pay.exception.EasouPayException;
import com.fantingame.pay.manager.PayChannelManager;
import com.fantingame.pay.manager.PayOrderManager;
import com.fantingame.pay.manager.PayTradeManager;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.RsaSignUtil;
import com.fantingame.pay.utils.StringTools;
import com.mokredit.payment.Md5Encrypt;


/**
 * 梵町向支付渠道下单接口
 * */
public class OrderAction extends BaseAction{
	private static final long serialVersionUID = 48545533070067807L;
	private Map<String,String> data = new TreeMap<String, String>();
	private static Logger logger = Logger.getLogger(OrderAction.class);
	
	@Override
	public String execute() throws Exception {
		try{
			
			System.err.println("OrderAction中=============>"+getRequest().getSession().getId());
			//判断是否https请求
			if(!isHttps()){
				data.put(Constants.FIELD_CODE,TradeCode.EASOU_CODE_170.code);
				data.put(Constants.FIELD_MSG,TradeCode.EASOU_CODE_170.msgE);
				logger.error(TradeCode.EASOU_CODE_170.msgE);
				return SUCCESS;
			}
			
			//获取参数
			TradeBean trade = checkParams(getRequest());
			
			
			PayTradeManager payTradeManager = (PayTradeManager)getBean("payTradeManager");
			PayOrderManager payOrderManager = (PayOrderManager)getBean("payOrderManager");
			
			//根据梵町订单信息
			PayTrade payTrade = payTradeManager.getEntityByInvoice(trade.getInvoice());
			if(payTrade==null){//梵町订单不存在
				data.put(Constants.FIELD_CODE,TradeCode.EASOU_CODE_140.code);
				data.put(Constants.FIELD_MSG,TradeCode.EASOU_CODE_140.msgE);
				logger.error(TradeCode.EASOU_CODE_140.msgE+"...invoice:"+trade.getInvoice());
			    return SUCCESS;
			}
			
			if(!payTrade.getPartnerId().equals(Constants.PARTNER_ID_BIANFENG)){   // 兼容性代码，对于边锋的，保留原有功能，其他合作商，转到新的支付宝接口去！
				return newEbOrder();
			}
			
			
			//获取支付渠道信息
			PayChannelManager payChannelManager = (PayChannelManager)getBean("payChannelManager");
			PayChannel channel = payChannelManager.getEntityById(Long.valueOf(trade.getChannelId()));
			if(channel==null){
				data.put(Constants.FIELD_CODE,TradeCode.EASOU_CODE_130.code);
				data.put(Constants.FIELD_MSG,TradeCode.EASOU_CODE_130.msgE);
				logger.error(TradeCode.EASOU_CODE_130.msgE+"...invoice:"+trade.getInvoice());
			    return SUCCESS;
			}
			
			//保存PayOrder订单
			PayOrder payOrder = payOrderManager.getEntityByInvoice(trade.getInvoice());
			if(payOrder==null){//系统不存在此订单,可以新增
				payOrder = new PayOrder();
				payOrder.setChannelId(Long.valueOf(trade.getChannelId()));
				payOrder.setAppId(payTrade.getSeparable()==1?Constants.APPID_SEPARABLE:Constants.APPID_NOT_SEPARABLE);
				payOrder.setClientStatus(0);
				payOrder.setServerStatus(0);
				payOrder.setReqFee(payTrade.getReqFee());
				payOrder.setInvoice(payTrade.getInvoice());
				payOrder.setCreateDatetime(new Date());
				payOrderManager.save(payOrder);
				data.put(Constants.FIELD_CODE,TradeCode.EASOU_ORDER_0.code);
				data.put(Constants.FIELD_MSG,TradeCode.EASOU_ORDER_0.msgE);
			}else if(payOrder.getServerStatus()==0){//已存在此订单，但是没交易成功，则更新支付方式
				payOrder.setChannelId(Long.valueOf(trade.getChannelId()));
				payOrderManager.update(payOrder);
				data.put(Constants.FIELD_CODE,TradeCode.EASOU_ORDER_0.code);
				data.put(Constants.FIELD_MSG,TradeCode.EASOU_ORDER_0.msgE);
			}else if(payOrder.getServerStatus()!=0){//订单已经被处理过,当作失效
				data.put(Constants.FIELD_CODE,TradeCode.EASOU_CODE_180.code);
				data.put(Constants.FIELD_MSG,TradeCode.EASOU_CODE_180.msgE);
				logger.error(TradeCode.EASOU_CODE_180.msgE+"...invoice:"+trade.getInvoice());
				return SUCCESS;
			}

			//签名并获取完整的URL
			if(Constants.CHANNEL_ID_MO9==channel.getId()){//MO9
				data.put(Constants.FIELD_URL,returnMo9Url(payTrade,payOrder,channel));
				data.put(Constants.FIELD_INVOICE,payOrder.getInvoice());
				logger.info(data.get(Constants.FIELD_URL));
			}else if(Constants.CHANNEL_ID_ALIPAY==channel.getId()){//支付宝
				data.put(Constants.FIELD_URL,returnAlipayUrl(payTrade,payOrder,channel));
				data.put(Constants.FIELD_INVOICE,payOrder.getInvoice());
				logger.info(data.get(Constants.FIELD_URL));
			}
		}catch (EasouPayException e) {
			data.put(Constants.FIELD_CODE,e.getCode());
			data.put(Constants.FIELD_MSG,e.getMessage());
			logger.error(e.getCode()+":"+e.getMessage());
		}catch (Exception e) {
			data.put(Constants.FIELD_CODE,TradeCode.EASOU_CODE_NEG_1.code);
			data.put(Constants.FIELD_MSG,TradeCode.EASOU_CODE_NEG_1.msgE);
			logger.error(e.getMessage(),e);
		}
		return SUCCESS;
	}
	
	
	//新版
	private String newEbOrder(){
		logger.error("=============>进入新支付宝接口===============");
		try{
			String invoice = getParam("invoice");
			PayTrade payTrade = getTradeByInvoice(invoice);
			//获取支付渠道信息
			PayEb payEb = newPayEbOrder(invoice, Constants.CHANNEL_ID_ALIPAY,payTrade.getReqFee(),null);
			PayChannel channel = getChannelById(Constants.CHANNEL_ID_ALIPAY);
			data.put(Constants.FIELD_URL,returnAlipayUrl(payTrade,payEb,channel));
			data.put(Constants.FIELD_INVOICE,payEb.getInvoice());
		}catch (EasouPayException e) {
			data.put(Constants.FIELD_CODE,e.getCode());
			data.put(Constants.FIELD_MSG,e.getMessage());
			logger.error(e.getMessage(),e);
		}catch (Exception e) {
			data.put(Constants.FIELD_CODE,TradeCode.EASOU_CODE_NEG_1.code);
			data.put(Constants.FIELD_MSG,TradeCode.EASOU_CODE_NEG_1.msgE);
			logger.error(e.getMessage(),e);
		}
		return SUCCESS;
	}
	/*返回一个含有sign的完整url,不是必要的参数如果为空则不参与签名，不参与签名*/
	private String returnAlipayUrl(PayTrade payTrade,PayEb payEb,PayChannel channel) throws Exception{
		//组装待签名数据
		StringBuffer sb = new StringBuffer("partner=" + "\"" + channel.getPartnerId() + "\"&");
		sb.append("seller=" + "\"" + channel.getAccount() + "\"&");
		sb.append("out_trade_no=" + "\"" + payTrade.getInvoice() + "\"&");
		sb.append("subject=" + "\"" + payTrade.getTradeName()+ "\"&");
		sb.append("body=" + "\"" + payTrade.getTradeDesc() + "\"&");
		sb.append("total_fee=" + "\""+ payTrade.getReqFee() + "\"&");
		sb.append("notify_url=" + "\""+this.getBaseUrl()+"/notifyFromAlipay.e\"");
		String sign = RsaSignUtil.sign(sb.toString(),channel.getPrivateKey());
		sb.append("&sign_type=\"RSA\"&sign=\""+URLEncoder.encode(sign,"UTF-8")+"\"");
		return sb.toString();
	}
	
	
	
	
	
	/*返回一个含有sign的完整url,不是必要的参数如果为空则不参与签名，不参与签名*/
	private String returnMo9Url(PayTrade payTrade,PayOrder payOrder,PayChannel channel) throws Exception{
		TreeMap<String,String> map = new TreeMap<String, String>();
		StringTools.setValue(map,"pay_to_email",channel.getAccount(),50,true,false);
		StringTools.setValue(map,"version",channel.getVersion(),10,true,false);
		StringTools.setValue(map,"return_url",channel.getReturnUrl(),200,false,false);
		StringTools.setValue(map,"notify_url",this.getBaseUrl()+"/notifyFromMo9.e",200,true,false);
		StringTools.setValue(map,"invoice",payOrder.getInvoice(),32,true,false);
		//SignTools.setValue(map,"extra_param",payTrade.getPartnerId()+"",255,false,false);
		map.put("lc","CN");
		map.put("payer_id",payTrade.getAppId()+"_"+payTrade.getPayerId());
		StringTools.setValue(map,"amount",payTrade.getReqFee(),10,true,true);
		StringTools.setValue(map,"currency",payTrade.getCurrency(),3,true,false);
		StringTools.setValue(map,"item_name",payTrade.getTradeName(),30,true,false);
		//判断是否可拆分交易
		if(payTrade.getSeparable()==1){
			StringTools.setValue(map,"app_id",Constants.APPID_SEPARABLE,255,true,false);
		}else{
			StringTools.setValue(map,"app_id",Constants.APPID_NOT_SEPARABLE,255,true,false);
		}
		
		//签名
		String sign = Md5Encrypt.sign(map, channel.getSecretKey());
		StringTools.setValue(map,"sign",sign,32,true,false);
		//获取一个编码过的URL
		return channel.getOrderUrl() + StringTools.getEncodedUrl(map);
	}
	
	
	/*返回一个含有sign的完整url,不是必要的参数如果为空则不参与签名，不参与签名*/
	private String returnAlipayUrl(PayTrade payTrade,PayOrder payOrder,PayChannel channel) throws Exception{
		//组装待签名数据
		StringBuffer sb = new StringBuffer("partner=" + "\"" + channel.getPartnerId() + "\"&");
		sb.append("seller=" + "\"" + channel.getAccount() + "\"&");
		sb.append("out_trade_no=" + "\"" + payTrade.getInvoice() + "\"&");
		sb.append("subject=" + "\"" + payTrade.getTradeName()+ "\"&");
		sb.append("body=" + "\"" + payTrade.getTradeDesc() + "\"&");
		sb.append("total_fee=" + "\""+ payTrade.getReqFee() + "\"&");
		sb.append("notify_url=" + "\""+this.getBaseUrl()+"/notifyFromAlipay.e\"");
		String sign = RsaSignUtil.sign(sb.toString(),channel.getPrivateKey());
		sb.append("&sign_type=\"RSA\"&sign=\""+URLEncoder.encode(sign,"UTF-8")+"\"");
		return sb.toString();
	}
	
	
	
	private TradeBean checkParams(ServletRequest req) throws Exception{ 
		TradeBean trade= new TradeBean();
		trade.setInvoice(StringTools.getValue("invoice",getParam("invoice"),32,true,false)); 
		trade.setChannelId(StringTools.getValue("channelId",getParam("channelId"),16,true,false));
		return trade;
	}


	public Map<String, String> getData() {
		return data;
	}
	public void setData(Map<String, String> data) {
		this.data = data;
	}

}
