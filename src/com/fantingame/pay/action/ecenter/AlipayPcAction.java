package com.fantingame.pay.action.ecenter;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.fantingame.pay.entity.PayChannel;
import com.fantingame.pay.entity.PayEb;
import com.fantingame.pay.entity.PayTrade;
import com.fantingame.pay.entity.TradeCode;
import com.fantingame.pay.exception.EasouPayException;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.alipay.pc.config.AlipayConfig;
import com.fantingame.pay.utils.alipay.pc.util.AlipaySubmit;

/**
 * 调用支付宝的开放平台创建、支付交易步骤
 * 
 * 1.将业务参数：外部交易号、商品名称、商品总价、卖家帐户、卖家帐户、notify_url这些东西按照xml 的格式放入<req_data></req_data>中
 * 2.将通用参数也放入请求参数中 
 * 3.对以上的参数进行签名，签名结果也放入请求参数中
 * 4.请求支付宝开放平台的alipay.wap.trade.create.direct服务
 * 5.从开放平台返回的内容中取出request_token（对返回的内容要先用私钥解密，再用支付宝的公钥验签名）
 * 6.使用拿到的request_token组装alipay.wap.auth.authAndExecute服务的跳转url
 * 7.根据组装出来的url跳转到支付宝的开放平台页面，交易创建和支付在支付宝的页面上完成
 */
public class AlipayPcAction extends ECenterBaseAction {

	private static final long serialVersionUID = -5019914247728372377L;
	private static final Logger logger = Logger.getLogger(AlipayPcAction.class);
	
	private Map<String,String> data = new TreeMap<String, String>();
	private static final String payment_type = "1";//支付类型

	//使用支付宝支付订单
	public String execute(){
		try {
			payTrade = getTradeByInvoice(invoice); //获取订单
			if(payTrade == null) throw new EasouPayException(TradeCode.EASOU_CODE_140.code,TradeCode.EASOU_CODE_140.msgC);
			PayEb payEb = newPayEbOrder(invoice,Constants.CHANNEL_ID_ALIPAY_PC,payTrade.getReqFee(),null); //下订单
			PayChannel channel = getChannelById(Constants.CHANNEL_ID_ALIPAY_PC);
			String sHtmlText = prepareAliTradeParamMap(payTrade,payEb,channel);  //组装参数
			setStatus(SUCCESS);
			data.put("text",sHtmlText);
			logger.info("pc充值跳转到支付宝，参数为："+sHtmlText);
			data.put(Constants.FIELD_INVOICE,payEb.getInvoice());
			return JSON;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			e.printStackTrace();
			setStatus(FAIL);
			data.put(Constants.FIELD_CODE,TradeCode.EASOU_CODE_NEG_1.code);
			data.put(Constants.FIELD_MSG,TradeCode.EASOU_CODE_NEG_1.msgE);
			return JSON;
		}
	}
	
	//准备创建交易并获取token
	private  String prepareAliTradeParamMap(PayTrade trade,PayEb payEb,PayChannel channel){
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "create_direct_pay_by_user");
        sParaTemp.put("partner",AlipayConfig.partner);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("payment_type", payment_type);
		sParaTemp.put("notify_url",channel.getNotifyUrl());
//		sParaTemp.put("return_url", channel.getReturnUrl());
		sParaTemp.put("seller_email",channel.getAccount());
		sParaTemp.put("out_trade_no",payEb.getId().toString());
		sParaTemp.put("subject","f币充值");
		sParaTemp.put("total_fee",payEb.getReqFee());
		sParaTemp.put("body","f币充值");
		sParaTemp.put("show_url", channel.getMerchantUrl());
		sParaTemp.put("anti_phishing_key","");
		sParaTemp.put("exter_invoke_ip","");
		//建立请求
		String sHtmlText = AlipaySubmit.buildRequest(sParaTemp,"get","确认");
		return sHtmlText;
	}

	public Map<String, String> getData() {
		return data;
	}
	public void setData(Map<String, String> data) {
		this.data = data;
	}
}
