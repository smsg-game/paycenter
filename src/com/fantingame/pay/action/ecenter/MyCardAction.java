package com.fantingame.pay.action.ecenter;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.easou.common.json.JsonUtil;
import com.fantingame.pay.action.ecenter.vo.MyCardServerToServerPayment;
import com.fantingame.pay.action.ecenter.vo.SystemMyCardCardPayment;
import com.fantingame.pay.action.mobile.notify.BaseReceiveNotifyAction;
import com.fantingame.pay.entity.PayChannel;
import com.fantingame.pay.entity.PayEb;
import com.fantingame.pay.entity.TradeCode;
import com.fantingame.pay.exception.EasouPayException;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.EncryptUtil;
import com.fantingame.pay.utils.UrlRequestUtils;
import com.fantingame.pay.utils.mycard.MyCardSdk;

/**
 * mycard创建、支付交易步骤
 * 
 * 1.获取authcode
 * 2.将通用参数也放入请求参数中 
 	private String facId;   = channel.account
	private String securityKey;  = channel.secretKey
	private String sendConfirmUrl; = channel.returnUrl
	private String authUrl;  = channel.orderUrl
	private String serverToServerPayUrl;  = channel.notifyUrl  //点卡支付 卡号 密码确认地址
	在获取授权码时，用户计算签名的 key。共有两个 key 用于计算签名，这个 key 由 MyCard 提供
	private String authKeyMyCard;  = channel.publicKey
             在获取授权码时，用户计算签名的 key。共有两个 key 用于计算签名，这个 key 由 CP 方提供
	private String authKeyCP; = channel.privateKey
 */
public class MyCardAction extends ECenterBaseAction {

	private static final long serialVersionUID = -5019914247728372377L;
	private static final Logger logger = Logger.getLogger(MyCardAction.class);
	//生成令牌地址
//	private static final String TEST_ORDER = "http://test.b2b.mycard520.com.tw/MyBillingPay/api/Auth";
//	private static final String PRODUCT_ORDER = "https://b2b.mycard520.com.tw/MyBillingPay/api/Auth";
	//验证订单地址
//	private static final String TEST_AVALID_TRADE = "http://test.b2b.mycard520.com.tw/MyBillingPay/api/TradeQuery";
//	private static final String AVALID_TRADE = "https://b2b.mycard520.com.tw/MyBillingPay/api/TradeQuery";

	private Map<String,String> data = new TreeMap<String, String>();
	/**
	 * SdK获取校验码
	 * @return
	 */
	public String sdkAuth(){
		try {
			payTrade = getTradeByInvoice(invoice); //获取订单
			if(payTrade == null) throw new EasouPayException(TradeCode.EASOU_CODE_140.code,TradeCode.EASOU_CODE_140.msgC);
			
			PayChannel channel = getChannelById(Constants.CHANNEL_ID_MY_CARD_PACKAGE);
			String ServerId = this.getRequest().getParameter("ServerId");
			String CustomerId = this.getRequest().getParameter("CustomerId");
			String PaymentType = this.getRequest().getParameter("PaymentType");
			String ItemCode = this.getRequest().getParameter("ItemCode");
			String ProductName = this.getRequest().getParameter("ProductName");
			String otherInfo = PaymentType+","+CustomerId;
			PayEb payEb = newPayEbOrder(invoice,Constants.CHANNEL_ID_MY_CARD_PACKAGE,payTrade.getReqFee(),otherInfo); //下订单
			if(payEb!=null) {
				//获取校验信息
				String result = MyCardSdk.instance().sdkAuth(payEb.getId()+"", ServerId, CustomerId, PaymentType, ItemCode, ProductName, payTrade.getReqFee()+"", channel);
				if(result!=null){
					Map<String,String> authResultMap = JsonUtil.parserStrToObject(result, Map.class);
					otherInfo = otherInfo+","+authResultMap.get("AuthCode");
					newPayEbOrder(invoice,Constants.CHANNEL_ID_MY_CARD_PACKAGE,payTrade.getReqFee(),otherInfo); //下订单
					setStatus(SUCCESS);
				    data.put(Constants.PAY_ORDER_ID,payEb.getId()+"");
					data.put(Constants.FIELD_INVOICE,payEb.getInvoice());
					data.put("result", result);
				}else{
					setStatus(FAIL);
					data.put(Constants.FIELD_CODE,TradeCode.EASOU_CODE_NEG_1.code);
					data.put(Constants.FIELD_MSG,TradeCode.EASOU_CODE_NEG_1.msgE);
				}
				return JSON;
			} else {
				throw new Exception("获取结果URL出错!");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			e.printStackTrace();
			setStatus(FAIL);
			data.put(Constants.FIELD_CODE,TradeCode.EASOU_CODE_NEG_1.code);
			data.put(Constants.FIELD_MSG,TradeCode.EASOU_CODE_NEG_1.msgE);
			return JSON;
		}
	}
	/**
	 * (SDK) 请求支付
	 * @return
	 */
	public String sdkRequestPay(){
		try {
			String authCode = this.getRequest().getParameter("AuthCode");
			PayChannel channel = getChannelById(Constants.CHANNEL_ID_MY_CARD_PACKAGE);
		    //查询结构
		    String jsonStr = pay(authCode,channel);
			 if(jsonStr!=null){
				setStatus(SUCCESS);
				data.put("result", jsonStr);
			}else{
				setStatus(FAIL);
				data.put(Constants.FIELD_CODE,TradeCode.EASOU_ORDER_NEG_1.code);
				data.put(Constants.FIELD_MSG,TradeCode.EASOU_ORDER_NEG_1.msgE);
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
	

    /**
     *(SDK) 执行支付  先查询订单结果  如果成功则申请减钱
     * @param authCode
     * @param channel
     * @return
     * @throws Exception 
     */
    public static String pay(String authCode,PayChannel channel) throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		map.put("AuthCode", authCode);
		String queryResult = UrlRequestUtils.execute(channel.getReturnUrl(), map, UrlRequestUtils.Mode.GET);
		Map<String,String> queryResultMap = JsonUtil.parserStrToObject(queryResult, Map.class);
		logger.info("mycard sdk支付，查询订单结果返回的参数为:"+queryResult+",authCode="+authCode);
		if(queryResultMap.get("ReturnCode").equals("1")&&queryResultMap.get("PayResult").equals("3")){
			String jsonStr = UrlRequestUtils.execute(channel.getNotifyUrl(), map, UrlRequestUtils.Mode.GET);
			Map<String,String> result = JsonUtil.parserStrToObject(jsonStr, Map.class);
			String ReturnMsg = result.get("ReturnMsg");
			String FacTradeSeq = result.get("FacTradeSeq");
			String TradeSeq = result.get("TradeSeq");
			String SerialId = result.get("SerialId");
			String message = queryResultMap.get("MyCardTradeNo");
			String Amount = queryResultMap.get("Amount");
			if(result.get("ReturnCode").equals("1")){//成功
				//直接加钱
				BaseReceiveNotifyAction base = new BaseReceiveNotifyAction();
				base.savePayNotify(FacTradeSeq, TradeSeq, Constants.CHANNEL_ID_MY_CARD_PACKAGE, Constants.EASOU_SERVER_STATUS_SUCCESS, Amount);
				//后续操作
				base.doWorksAfterReceiveNotify(Long.parseLong(FacTradeSeq), Constants.EASOU_SERVER_STATUS_SUCCESS, "1", message, Amount, TradeSeq);
			}
			return jsonStr;
		}
		return null;
    }
    
    /**
     * 使用mycard点卡支付获取校验码
     * @return
     */
	public String apiAuth(){
		try {
			payTrade = getTradeByInvoice(invoice); //获取订单
			if(payTrade == null) throw new EasouPayException(TradeCode.EASOU_CODE_140.code,TradeCode.EASOU_CODE_140.msgC);
			
			PayEb payEb = newPayEbOrder(invoice,Constants.CHANNEL_ID_MY_CARD,payTrade.getReqFee(),null); //下订单
			PayChannel channel = getChannelById(Constants.CHANNEL_ID_MY_CARD);
			if(payEb!=null) {
				//获取校验信息
				String result = MyCardSdk.instance().apiAuth(payEb.getId()+"", channel);
				if(result!=null){
					setStatus(SUCCESS);
				    data.put(Constants.PAY_ORDER_ID,payEb.getId()+"");
//					data.put(Constants.FIELD_INVOICE,payEb.getInvoice());
					data.put("result", result);
				}else{
					setStatus(FAIL);
					data.put(Constants.FIELD_CODE,TradeCode.EASOU_CODE_NEG_1.code);
					data.put(Constants.FIELD_MSG,TradeCode.EASOU_CODE_NEG_1.msgE);
				}
				return JSON;
			} else {
				throw new Exception("获取结果URL出错!");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			e.printStackTrace();
			setStatus(FAIL);
			data.put(Constants.FIELD_CODE,TradeCode.EASOU_CODE_NEG_1.code);
			data.put(Constants.FIELD_MSG,TradeCode.EASOU_CODE_NEG_1.msgE);
			return JSON;
		}
	}
	 /**
	  * MyCard 点卡支付 - 客户端拿到授权码后，将卡号和密码传给服务端，由服务端提交到 MyCard 进行支付
	  */
	private String authCode;
	private String facMemid;
	private String cardId;
	private String cardPwd;
	public String apiPay() throws Exception{
		 logger.info("MyCard server-to-server 点卡支付 - 客户端发送支付请求, 参数 authCode[" + authCode + "] facMemid[" + facMemid + "] cardId[" + cardId + "] cardPwd[" + cardPwd + "]");
		 PayChannel channel = getChannelById(Constants.CHANNEL_ID_MY_CARD);
		 String result = serverToServerPay(authCode, facMemid, cardId, cardPwd,channel);
		 logger.info("MyCard server-to-server 点卡支付 - 返回支付结果给客户端 " + result + "]");
		 return result;
	 }
	
	private String serverToServerPay(String authCode, String facMemid, String cardId, String cardPwd,PayChannel channel) throws Exception {
		try {
			
		
		StringBuffer sb = new StringBuffer();
		sb.append(channel.getPublicKey());
		sb.append(channel.getAccount());
		sb.append(authCode).append(facMemid).append(cardId).append(cardPwd);
		sb.append(channel.getPrivateKey());
		String strToComputeSign = sb.toString();
		String sign = EncryptUtil.getSHA256(strToComputeSign);
		logger.debug("MyCard 客户端发送支付请求，计算签名 - 签名使用的字符串[" + strToComputeSign + "] 签名[" + sign + "]");
		Map<String, String> param = new HashMap<String, String>();
		param.put("facId", channel.getAccount());
		param.put("authCode", authCode);
		param.put("facMemId", facMemid);
		param.put("cardId", cardId);
		param.put("cardPwd", cardPwd);
		param.put("hash", sign);
		String url = channel.getNotifyUrl();//MyCardSdk.instance().getServerToServerPayUrl();
		String jsonStr = UrlRequestUtils.execute(url, param, UrlRequestUtils.Mode.GET);
		logger.info("MyCard server-to-server 支付返回信息[" + jsonStr + "]");
		if(jsonStr!=null){
			MyCardServerToServerPayment payment = convertToMyCardServerToServerPayment(jsonStr);
			payment.setCardId(cardId);
			if (payment.isPaySucceed() == true) {
				int cardPoint = payment.getCardPoint();
	//			SystemMyCardCardPayment systemMyCardCardPayment = Constants.systemMyCardPayList.get(cardPoint);
	//			if (systemMyCardCardPayment != null) {
					payment.setFinishAmount(BigDecimal.valueOf(cardPoint));
	//				payment.setGoldNum(systemMyCardCardPayment.getGoldNum());
	//			}
				//直接加钱
				BaseReceiveNotifyAction base = new BaseReceiveNotifyAction();
				base.savePayNotify(payment.getFacTradeSeq(), payment.getSaveSeq(), Constants.CHANNEL_ID_MY_CARD, Constants.EASOU_SERVER_STATUS_SUCCESS, String.valueOf(payment.getFinishAmount().doubleValue()));
				//后续操作
				base.doWorksAfterReceiveNotify(Long.parseLong(payment.getFacTradeSeq()), Constants.EASOU_SERVER_STATUS_SUCCESS, facMemid+","+payment.getReturnMsgNo(), cardId, String.valueOf(payment.getFinishAmount().doubleValue()), payment.getSaveSeq());
				setStatus(SUCCESS);
				data.put("result", jsonStr);
			}else{
				setStatus(FAIL);
				data.put("result", jsonStr);
			}
		}else{
			setStatus(FAIL);
			data.put(Constants.FIELD_CODE,TradeCode.EASOU_CODE_NEG_1.code);
			data.put(Constants.FIELD_MSG,TradeCode.EASOU_CODE_NEG_1.msgE);
		}
		} catch (Exception e) {
			setStatus(FAIL);
			data.put(Constants.FIELD_CODE,TradeCode.EASOU_CODE_NEG_1.code);
			data.put(Constants.FIELD_MSG,TradeCode.EASOU_CODE_NEG_1.msgE);
		}
		return JSON;
	}
	private MyCardServerToServerPayment convertToMyCardServerToServerPayment(String jsonStr) {
		Map<String, Object> map = JsonUtil.parserStrToObject(jsonStr, Map.class);
		MyCardServerToServerPayment payment = new MyCardServerToServerPayment();
		payment.setCardKind((Integer) map.get("CardKind"));
		payment.setCardPoint((Integer) map.get("CardPoint"));
		payment.setFacTradeSeq((String) map.get("facTradeSeq"));
		payment.setOProjNo((String) map.get("oProjNo"));
		payment.setReturnMsg((String) map.get("ReturnMsg"));
		payment.setReturnMsgNo((Integer) map.get("ReturnMsgNo"));
		payment.setSaveSeq((String) map.get("SaveSeq"));
		return payment;
	}

	public Map<String, String> getData() {
		return data;
	}
	public void setData(Map<String, String> data) {
		this.data = data;
	}
}
