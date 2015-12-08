package com.fantingame.pay.action.ecenter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
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
import com.fantingame.pay.utils.RsaSignUtil;
import com.fantingame.pay.utils.StringTools;
import com.fantingame.pay.utils.alipay.DirectTradeCreateRes;
import com.fantingame.pay.utils.alipay.ErrorCode;
import com.fantingame.pay.utils.alipay.ParameterUtil;
import com.fantingame.pay.utils.alipay.RSASignature;
import com.fantingame.pay.utils.alipay.ResponseResult;
import com.fantingame.pay.utils.alipay.StringUtil;
import com.fantingame.pay.utils.alipay.XMapUtil;

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
public class AlipayAction extends ECenterBaseAction {

	private static final long serialVersionUID = -5019914247728372377L;
	private static final Logger logger = Logger.getLogger(AlipayAction.class);
	private static final String SEC_ID = "0001";
	
	private Map<String,String> data = new TreeMap<String, String>();
	
	//使用支付宝支付订单
	public String execute(){
		try {
			payTrade = getTradeByInvoice(invoice); //获取订单
			if(payTrade == null) throw new EasouPayException(TradeCode.EASOU_CODE_140.code,TradeCode.EASOU_CODE_140.msgC);
			PayEb payEb = newPayEbOrder(invoice,Constants.CHANNEL_ID_ALIPAY_MODULE,payTrade.getReqFee(),null); //下订单
			PayChannel channel = null;
			if("android".equals(ty)) {
				channel = getChannelById(Constants.CHANNEL_ID_ALIPAY_MODULE_TER);	//获取支付商信息
			} else {
				channel = getChannelById(Constants.CHANNEL_ID_ALIPAY_MODULE);	//获取支付商信息
			}
			
			Map<String, String> params = prepareAliTradeParamMap(payTrade,payEb,channel);  //组装参数
			String sign = sign(params,SEC_ID,channel.getPrivateKey());//签名 
			params.put("sign", sign);

			ResponseResult resResult = new ResponseResult();   //发送请求获取结果
			String businessResult = "";
			resResult = send(params,channel.getOrderUrl(),SEC_ID,channel.getPrivateKey(),channel.getChannelPublicKey()); 
			if (resResult.isSuccess()) {
				businessResult = resResult.getBusinessResult();
			} else {
				setStatus(FAIL);
				data.put(Constants.FIELD_CODE,TradeCode.EASOU_CODE_NEG_1.code);
				data.put(Constants.FIELD_MSG,TradeCode.EASOU_CODE_NEG_1.msgE);
				return JSON;
			}
			XMapUtil.register(DirectTradeCreateRes.class);
			DirectTradeCreateRes directTradeCreateRes =  
				(DirectTradeCreateRes) XMapUtil.load(new ByteArrayInputStream(businessResult.getBytes("UTF-8")));
			String requestToken = directTradeCreateRes.getRequestToken();  // 从开放平台返回的内容中取出request_token
			
			Map<String, String> authParams = prepareAuthParamsMap(channel,requestToken);  //组装调用授权请求数据
			String authSign = sign(authParams,SEC_ID,channel.getPrivateKey());//签名
			authParams.put("sign", authSign);
			
			String redirectURL = getRedirectUrl(authParams,channel.getOrderUrl()); //获取最终跳转到支付宝的结果
			if(StringUtil.isNotBlank(redirectURL)) {
				setStatus(SUCCESS);
				if("android".equals(ty)) {
					String androidUrl = returnAlipayUrl(payTrade,payEb, channel);
					data.put(Constants.FIELD_URL,androidUrl);
					data.put(Constants.FIELD_INVOICE,payEb.getInvoice());
					return JSON;
				} else {
					data.put(Constants.FIELD_URL,redirectURL);
					data.put(Constants.FIELD_INVOICE,payEb.getInvoice());
					return JSON;
				}
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
	
	
	//支付宝充值,先生成订单，再走正常流程
	public String aliCharge(){
		try {
			isCharge = true;
			String money = getParam("money");
			String otherMoney = getParam("otherMoney");
			String invoiceId=getParam(Constants.FIELD_INVOICE_ID);
			if(otherMoney!=null && otherMoney.length()>0 && StringTools.isNum(otherMoney)){ //优先使用用户输入的自定义金额
				money = otherMoney;
			}
			PayTrade trade = newChargeTrade(money,null,"ecenter",invoiceId);
			if(trade!=null) invoice = trade.getInvoice();
			return this.execute();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return JSON;
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
	
	//准备创建交易并获取token
	private  Map<String, String> prepareAliTradeParamMap(PayTrade trade,PayEb payEb,PayChannel channel){
		Map<String, String> params = new HashMap<String, String>();
		// 商品名称
		String subject = trade.getTradeName();
		// 商品总价
        String totalFee = trade.getReqFee();
        // 外部交易号 对支付宝而言
        String outTradeNo = payEb.getId()+"";
        // 卖家帐号
		String sellerAccountName = channel.getAccount();
		// 接收支付宝发送的通知的url
		String notifyUrl = channel.getNotifyUrl();
		//未完成支付，用户点击链接返回商户url
		String returnUrl = channel.getReturnUrl()+"?invoice="+trade.getInvoice();
		String merchantUrl = channel.getMerchantUrl()+"?invoice="+trade.getInvoice();
		
		StringBuffer reqData = new StringBuffer();
		reqData.append("<direct_trade_create_req>");
		reqData.append("<subject>").append(subject).append("</subject>");
		reqData.append("<out_trade_no>").append(outTradeNo).append("</out_trade_no>");
		reqData.append("<total_fee>").append(totalFee).append("</total_fee>");
		reqData.append("<seller_account_name>").append(sellerAccountName).append("</seller_account_name>");
		reqData.append("<notify_url>").append(notifyUrl).append("</notify_url>");
		reqData.append("<call_back_url>").append(returnUrl).append("</call_back_url>");
		reqData.append("<merchant_url>").append(merchantUrl).append("</merchant_url>");
		reqData.append("</direct_trade_create_req>");
		
        params.put("req_data", reqData.toString());
        params.putAll(prepareCommonParamMap(channel));
		return params;
	}
	
	
	private  Map<String, String> prepareCommonParamMap(PayChannel channel){
		Map<String, String> params = new HashMap<String, String>();
		params.put("req_id", System.currentTimeMillis() + "");
        params.put("service", "alipay.wap.trade.create.direct");
        params.put("sec_id", SEC_ID);
        params.put("partner", channel.getPartnerId());
        String ic = isCharge ? "&ic=1" : "&ic=0";
        params.put("call_back_url", channel.getReturnUrl() + "?invoice=" + invoice + ic);
        params.put("format", "xml");
        params.put("v", "2.0");
        return params;
	}
	
	

	/**
	 * 准备alipay.wap.auth.authAndExecute服务的参数
	 * @param request
	 * @param requestToken
	 * @return
	 */
	private Map<String, String> prepareAuthParamsMap(PayChannel channel , String requestToken) {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.putAll(prepareCommonParamMap(channel));
		String reqData = "<auth_and_execute_req><request_token>" + requestToken + "</request_token></auth_and_execute_req>";
		requestParams.put("req_data", reqData);
		requestParams.put("service", "alipay.wap.auth.authAndExecute");
		return requestParams;
	}

	
	
	/**
	 * 对参数进行签名
	 * @param reqParams
	 * @return
	 */
	private String sign(Map<String, String> reqParams,String signAlgo,String key) throws Exception{
		String signData = ParameterUtil.getSignData(reqParams);
		String sign =RSASignature.sign(signData, key,"");
		return sign;
	}

	
	
	/**
	 * 调用alipay.wap.auth.authAndExecute服务的时候需要跳转到支付宝的页面，组装跳转url
	 * @param reqParams
	 * @throws Exception
	 */
	private String getRedirectUrl(Map<String, String> reqParams,String reqUrl)
			throws Exception {
		String redirectUrl = reqUrl + "?";
		redirectUrl = redirectUrl + ParameterUtil.mapToUrl(reqParams);
		return redirectUrl;
	}
	
	
	
	

	/**
	 * 调用支付宝开放平台的服务
	 * @param reqParams 请求参数
	 * @throws Exception
	 */
	private ResponseResult send(Map<String, String> reqParams,String reqUrl,String secId,String partnerRsaPrivateKey,String alipayPublicKey) throws Exception {
		String response = "";
		String invokeUrl = reqUrl  + "?";
		URL serverUrl = new URL(invokeUrl);
		HttpURLConnection conn = (HttpURLConnection) serverUrl.openConnection();

		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.connect();
		String params = ParameterUtil.mapToUrl(reqParams);
		//System.out.println("Request Token:"+URLDecoder.decode(params, "utf-8"));
		conn.getOutputStream().write(params.getBytes());

		InputStream is = conn.getInputStream();

		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		StringBuffer buffer = new StringBuffer();
		String line = "";
		while ((line = in.readLine()) != null) {
			buffer.append(line);
		}
		response = URLDecoder.decode(buffer.toString(), "utf-8");
		System.out.println("Response:"+response);
		conn.disconnect();
		return praseResult(response,secId,partnerRsaPrivateKey,alipayPublicKey);
	}
	
	
	

	/**
	 * 解析支付宝返回的结果
	 * @param response
	 * @throws Exception
	 */
	
	
	private ResponseResult praseResult(String response,String secId,String partnerRsaPrivateKey, String alipayPublicKey) throws Exception {
		// 调用成功
		HashMap<String, String> resMap = new HashMap<String, String>();
		String v = ParameterUtil.getParameter(response, "v");
		String service = ParameterUtil.getParameter(response, "service");
		String partner = ParameterUtil.getParameter(response, "partner");
		String sign = ParameterUtil.getParameter(response, "sign");
		String reqId = ParameterUtil.getParameter(response, "req_id");
		resMap.put("v", v);
		resMap.put("service", service);
		resMap.put("partner", partner);
		resMap.put("sec_id", secId);
		resMap.put("req_id", reqId);
		String businessResult = "";
		ResponseResult result = new ResponseResult();
		System.out.println("Token Result:"+response);
		if (response.contains("<err>")) {
			result.setSuccess(false);
			businessResult = ParameterUtil.getParameter(response, "res_error");

			// 转换错误信息
			XMapUtil.register(ErrorCode.class);
			ErrorCode errorCode = (ErrorCode) XMapUtil
					.load(new ByteArrayInputStream(businessResult
							.getBytes("UTF-8")));
			result.setErrorMessage(errorCode);

			resMap.put("res_error", ParameterUtil.getParameter(response,
					"res_error"));
		} else {
		    businessResult = ParameterUtil.getParameter(response, "res_data");
            result.setSuccess(true);
            //对返回的res_data数据先用商户私钥解密
            String resData= RSASignature.decrypt(businessResult, partnerRsaPrivateKey);
            result.setBusinessResult(resData);
            resMap.put("res_data", resData);
		}
		//获取待签名数据
		String verifyData = ParameterUtil.getSignData(resMap);
		System.out.println("verifyData:"+verifyData);
		//对待签名数据使用支付宝公钥验签名
		boolean verified = RSASignature.doCheck(verifyData, sign, alipayPublicKey,"");
		
		if (!verified) {
			throw new Exception("验证签名失败");
		}
		return result;
	}
	
	private String ty;

	public String getTy() {
		return ty;
	}


	public void setTy(String ty) {
		this.ty = ty;
	}

	public Map<String, String> getData() {
		return data;
	}
	public void setData(Map<String, String> data) {
		this.data = data;
	}
}
