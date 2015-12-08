package com.fantingame.pay.action.mobile;

import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.fantingame.pay.action.common.BaseAction;
import com.fantingame.pay.entity.PayChannel;
import com.fantingame.pay.entity.PayEb;
import com.fantingame.pay.entity.PayTrade;
import com.fantingame.pay.entity.TradeCode;
import com.fantingame.pay.exception.EasouPayException;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.RsaSignUtil;
import com.fantingame.pay.utils.StringTools;

public class AlipayAction extends BaseAction {

	private static final long serialVersionUID = -5019914247728372377L;
	private Map<String,String> data = new TreeMap<String, String>();
	private static Logger logger = Logger.getLogger(AlipayAction.class);
	
	public String execute(){
		try{
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
		return JSON;
	}
	
	/*支付宝充值E币，先生成订单，再走正常流程*/
	public String aliCharge(){
		try {
			isCharge = true;
			String backUrl = (String)getRequest().getSession().getAttribute("charge_backurl");
			String money = getParam("money");
			String invoiceId=getParam(Constants.FIELD_INVOICE_ID);
			PayTrade trade = newChargeTrade(money,backUrl,"mobile",invoiceId);
			if(trade!=null) 
				invoice = trade.getInvoice();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return execute();
	}
	
	//返回一个含有sign的完整url,不是必要的参数如果为空则不参与签名，不参与签名
	private String returnAlipayUrl(PayTrade payTrade,PayEb payEb,PayChannel channel) throws Exception{
		//组装待签名数据
		StringBuffer sb = new StringBuffer("partner=" + "\"" + channel.getPartnerId() + "\"&");
		sb.append("seller=" + "\"" + channel.getAccount() + "\"&");
		sb.append("out_trade_no=" + "\"" + payEb.getId() + "\"&");
		sb.append("subject=" + "\"" + payTrade.getTradeName()+ "\"&");
		sb.append("body=" + "\"" + payTrade.getTradeDesc() + "\"&");
		sb.append("total_fee=" + "\""+ payTrade.getReqFee() + "\"&");
		sb.append("notify_url=" + "\""+channel.getNotifyUrl()+"\"");
		String sign = RsaSignUtil.sign(sb.toString(),channel.getPrivateKey());
		sb.append("&sign_type=\"RSA\"&sign=\""+URLEncoder.encode(sign,"UTF-8")+"\"");
		return sb.toString();
	}
	
	
	public Map<String, String> getData() {
		return data;
	}
	public void setData(Map<String, String> data) {
		this.data = data;
	}
}