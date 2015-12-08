package com.fantingame.pay.action.ecenter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.easou.common.api.MD5;
import com.easou.common.util.MD5Util;
import com.fantingame.pay.entity.PayChannel;
import com.fantingame.pay.entity.PayEb;
import com.fantingame.pay.entity.PayTrade;
import com.fantingame.pay.entity.TradeCode;
import com.fantingame.pay.exception.EasouPayException;
import com.fantingame.pay.utils.Constants;


/**
 * mol创建、支付交易步骤
 * 
 */
public class MolAction extends ECenterBaseAction {

	private static final long serialVersionUID = -5019914247728372377L;
	private static final Logger logger = Logger.getLogger(MolAction.class);

	private Map<String,String> data = new TreeMap<String, String>();
	private String returnUrl;//返回地址
	private String desc;//充值描述
    private String channelId;//1 钱包   3 点卡
    
    
    //生成订单地址
	public String generatorPaymentUrl(){
		try {
			payTrade = getTradeByInvoice(invoice); //获取订单
			if(payTrade == null) throw new EasouPayException(TradeCode.EASOU_CODE_140.code,TradeCode.EASOU_CODE_140.msgC);
			PayEb payEb = newPayEbOrder(invoice,Constants.CHANNEL_ID_MOL,payTrade.getReqFee(),null); //下订单
			PayChannel channel = getChannelById(Constants.CHANNEL_ID_MOL);
			if(returnUrl!=null&&returnUrl.equals("")){
				returnUrl="#";
			}
			String payUrl = generalPaymentUrl(desc,returnUrl,channelId,payTrade,payEb,channel);
			if(payUrl!=null){
				setStatus(SUCCESS);
			    data.put(Constants.PAY_ORDER_ID,payEb.getId()+"");
				data.put(Constants.FIELD_INVOICE,payEb.getInvoice());
				data.put("result", payUrl);
			}else{
				setStatus(FAIL);
				data.put(Constants.FIELD_CODE,TradeCode.EASOU_CODE_NEG_1.code);
				data.put(Constants.FIELD_MSG,TradeCode.EASOU_CODE_NEG_1.msgE);
			}
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

	
	public String getChannelId() {
		return channelId;
	}


	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}


	private String generalPaymentUrl(String desc,String returnUrl,String channelId,PayTrade payTrade,PayEb payEb,PayChannel channel){
				try {
					String uriAPI = channel.getOrderUrl();
					HttpPost httpPost = new HttpPost(uriAPI); // 建立HTTP
					int amount = BigDecimal.valueOf(Double.valueOf(payTrade.getReqFee())).multiply(new BigDecimal(100)).intValue();
					String applicationCode =  channel.getAccount();
					String secretKey = channel.getSecretKey();
					String currencyCode = channel.getCardCode();
					String version = channel.getVersion();
					String orderId = payEb.getId()+"";
					
					//点卡支付不需要传这2个参数
					String amountStr = amount + "";
					if(channelId.equals("3")){
						amountStr="";
						currencyCode="";
					}
					String signValue = MD5Util.md5(amountStr + applicationCode + channelId
							+ currencyCode + orderId + desc + orderId
							+returnUrl + version + secretKey);
					StringBuffer stringBuffer = new StringBuffer();
					List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
					parameters.add(new BasicNameValuePair("applicationCode",
							applicationCode));
					stringBuffer.append("applicationCode="+applicationCode);
					parameters.add(new BasicNameValuePair("referenceId", orderId));
					stringBuffer.append("referenceId="+orderId);
					parameters.add(new BasicNameValuePair("version", version));
					stringBuffer.append("version="+version);
					parameters.add(new BasicNameValuePair("channelId", channelId));
					stringBuffer.append("channelId="+channelId);
					parameters.add(new BasicNameValuePair("amount", amountStr));
					stringBuffer.append("amount="+amountStr);
					parameters.add(new BasicNameValuePair("currencyCode", currencyCode));
					stringBuffer.append("currencyCode="+currencyCode);
					parameters.add(new BasicNameValuePair("returnUrl", returnUrl));
					stringBuffer.append("returnUrl="+returnUrl);
					parameters.add(new BasicNameValuePair("description",desc));
					stringBuffer.append("description="+desc);
					parameters.add(new BasicNameValuePair("customerId", orderId));
					stringBuffer.append("customerId="+orderId);
					parameters.add(new BasicNameValuePair("signature", signValue));
					stringBuffer.append("signValue="+signValue);
					HttpEntity entity = new UrlEncodedFormEntity(parameters, "utf-8");
					httpPost.setEntity(entity);
					DefaultHttpClient client = new DefaultHttpClient();
					client.getParams()
							.setParameter(
									CoreConnectionPNames.CONNECTION_TIMEOUT,
									5000);// 连接时间
					logger.info("mol请求数据:"+stringBuffer.toString());
					HttpResponse response = client.execute(httpPost);
					if(response.getStatusLine().getStatusCode() == 200){
						String resstr = EntityUtils.toString(response.getEntity());
						JSONObject jObject = JSONObject.parseObject(resstr);
						logger.info("mol请求成功,response = "+ resstr);
						return jObject.getString("paymentUrl");
					}else{
						String errorDesc = EntityUtils.toString(response.getEntity());
						logger.info("mol请求失败！code="+response.getStatusLine().getStatusCode()+",errorDesc="+errorDesc);
					}
				} catch (Exception e) {
					logger.error("general paymentUrl error!",e);
				}
		return null;
	}
	
	
	public Map<String, String> getData() {
		return data;
	}
	public void setData(Map<String, String> data) {
		this.data = data;
	}


	public String getReturnUrl() {
		return returnUrl;
	}


	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}


	public String getDesc() {
		return desc;
	}


	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
