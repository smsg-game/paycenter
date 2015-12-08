package com.fantingame.pay.action.ecenter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.fantingame.pay.entity.PayChannel;
import com.fantingame.pay.entity.PayEb;
import com.fantingame.pay.entity.PayTrade;
import com.fantingame.pay.entity.TradeCode;
import com.fantingame.pay.exception.EasouPayException;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.StringTools;
import com.fantingame.pay.utils.XmlUtil;
import com.fantingame.pay.utils.unionpay.CertificateCoder;
import com.fantingame.pay.utils.unionpay.HttpsUtils1;
import com.fantingame.pay.utils.unionpay.PluginAlgorithm;

/**
 * 调用易联的开放平台创建订单
 * 
 * @author elvis
 * 
 */
public class UnionPayAction extends ECenterBaseAction {

	private static final long serialVersionUID = -5019914247728372377L;
	private static final Logger logger = Logger.getLogger(UnionPayAction.class);

	private Map<String, String> data = new TreeMap<String, String>();
	private Map<String, String> params=new TreeMap<String, String>();

	// 使用易联银联手机支付订单
	public String execute() {
		try {
			payTrade = getTradeByInvoice(invoice); // 获取订单
			if (payTrade == null)
				throw new EasouPayException(TradeCode.EASOU_CODE_140.code,
						TradeCode.EASOU_CODE_140.msgC);
			long channelId=0L;
			//判断为测试还是正式平台
			if(Constants.IS_TEST_ENV)
				channelId=Constants.CHANNEL_ID_UNIONPAY_TEST;
			else
				channelId=Constants.CHANNEL_ID_UNIONPAY;
			//创建EB订单
			PayEb payEb = newPayEbOrder(invoice, channelId,
					payTrade.getReqFee(), null); // 下订单
			PayChannel channel = null;
			channel = getChannelById(channelId); // 获取支付商信息

			String paramsXml = prepareUnionPayParamXml(payTrade, payEb, channel); // 组装参数

			// 发送请求获取结果
			String response = send(paramsXml, channel.getOrderUrl());

			// 解析返回结果
			String result = null;
			if ((response != null) && (response.length() > 0)) {
				result = URLDecoder.decode(response, "UTF-8");
				if (isSuccess(result)) {
					setStatus(SUCCESS);
					setMsg("下单成功");

				} else {
					setStatus(FAIL);
					setMsg("下单失败");
				}
				
			} else {
				setStatus(FAIL);
				setMsg("下单失败");
				data.put(Constants.FIELD_CODE, TradeCode.EASOU_CODE_NEG_1.code);
				data.put(Constants.FIELD_MSG, TradeCode.EASOU_CODE_NEG_1.msgE);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			setStatus(FAIL);
			setMsg("下单失败");
			data.put(Constants.FIELD_CODE, TradeCode.EASOU_CODE_NEG_1.code);
			data.put(Constants.FIELD_MSG, TradeCode.EASOU_CODE_NEG_1.msgE);
		}
		return JSON;
	}

	/**
	 * 生成EB充值订单,EB充值下单入口
	 * @return
	 */
		public String uniCharge(){
			try {
				isCharge = true;
				String money = getParam("money");
				String invoiceId=getParam(Constants.FIELD_INVOICE_ID);
				PayTrade trade = newChargeTrade(money,null,"ecenter",invoiceId);
				if(trade!=null) invoice = trade.getInvoice();
				return this.execute();
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
				setStatus(FAIL);
				setMsg("下单失败,传入金额有误");
				data.put(Constants.FIELD_CODE, TradeCode.EASOU_CODE_NEG_1.code);
				data.put(Constants.FIELD_MSG, TradeCode.EASOU_CODE_NEG_1.msgE);
			}
			return JSON;
		}
		
	/**
	 * 判断返回结果
	 * 
	 * @param resultXml
	 * @return
	 */
	private boolean isSuccess(String resultXml) {
		if ("".equals(resultXml) || null == resultXml) {
			data.put(Constants.FIELD_CODE, TradeCode.EASOU_CODE_NEG_1.code);
			data.put(Constants.FIELD_MSG, TradeCode.EASOU_CODE_NEG_1.msgE);
			return false;
		} else {
			XmlUtil util = new XmlUtil(resultXml);
			String respCode = util.getSimpleXmlValue("respCode"); // 响应码
			String respDesc = util.getSimpleXmlValue("respDesc"); // 响应描述
			String merchantOrderId = util.getSimpleXmlValue("merchantOrderId"); // 商户订单号
			String merchantOrderTime = util
					.getSimpleXmlValue("merchantOrderTime"); // 订单时间
			String merchantOrderAmt = util
					.getSimpleXmlValue("merchantOrderAmt"); // 订单金额

			data.put(Constants.FIELD_CODE, respCode);
			data.put(Constants.FIELD_MSG, respDesc);
			if (Constants.UNION_PAY_ORDER_SUC.equals(respCode)) {
				data.put(Constants.FIELD_ORDER_ID, merchantOrderId);
				data.put(Constants.FIELD_ORDER_TIME, merchantOrderTime);
				data.put(Constants.FIELD_ORDER_AMT, merchantOrderAmt);
				if(Constants.IS_TEST_ENV){
					data.put(Constants.FIELD_CONNECT_TYPE, "00");
				}else{
					data.put(Constants.FIELD_CONNECT_TYPE, "01");
				}
				data.putAll(params);
				return true;
			} else
				return false;

		}
	}

	/**
	 * 组装请求报文
	 * 
	 * @param trade
	 * @param payEb
	 * @param channel
	 * @return
	 */
	private String prepareUnionPayParamXml(PayTrade trade, PayEb payEb,
			PayChannel channel) {
		// 商户名
		String merchantName = Constants.FIELD_BUSS_NAME;
		// 商户代码
		String merchantId = channel.getAccount();
		// 订单号
		String merchantOrderId =payEb.getId()+""; 	//payTrade.getTradeId();
		// 订单金额
		String merchantOrderAmt = moneyFormat(trade.getReqFee());
		// 订单描述
		String merchantOrderDesc = trade.getTradeDesc();
		// 通知地址
		String backEndUrl = channel.getNotifyUrl();

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		Date now = new Date();
		// 下单时间
		String merchantOrderTime = format.format(now);
		// 交易超时时间 (5分钟)
		String transTimeout = format.format(new Date(now.getTime() + 300000));

		
		// 组装签名参数
		StringBuilder builder = new StringBuilder();
		builder.append("merchantName").append("=").append(merchantName);
		builder.append("&");
		builder.append("merchantId").append("=").append(merchantId);
		builder.append("&");
		builder.append("merchantOrderId").append("=").append(merchantOrderId);
		builder.append("&");
		builder.append("merchantOrderTime").append("=")
				.append(merchantOrderTime);
		builder.append("&");
		builder.append("merchantOrderAmt").append("=").append(merchantOrderAmt);
		builder.append("&");
		builder.append("merchantOrderDesc").append("=")
				.append(merchantOrderDesc);
		builder.append("&");
		builder.append("transTimeout").append("=").append(transTimeout);

		String signStr = builder.toString();
		
		params.put("merchantName", merchantName);
		params.put("merchantId", merchantId);
		params.put("merchantOrderDesc", merchantOrderDesc);
		params.put("transTimeout", transTimeout);
		params.put("sign", sign(signStr));
		params.put("merchantPublicCert", getPublicKey());

		
		StringBuilder reqData = new StringBuilder();
		reqData.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		reqData.append("<pomp application=\"SubmitOrder.Req\" version=\"1.2.0\">");
		reqData.append("<merchantName>").append(merchantName)
				.append("</merchantName>"); // 商户名称
		reqData.append("<merchantId>").append(merchantId)
				.append("</merchantId>"); // 商户代码
		reqData.append("<merchantOrderId>").append(merchantOrderId)
				.append("</merchantOrderId>"); // 商户订单号
		reqData.append("<merchantOrderTime>").append(merchantOrderTime)
				.append("</merchantOrderTime>"); // 商户订单时间
		reqData.append("<merchantOrderAmt>").append(merchantOrderAmt)
				.append("</merchantOrderAmt>"); // 商户订单金额
		reqData.append("<merchantOrderDesc>").append(merchantOrderDesc)
				.append("</merchantOrderDesc>"); // 商户订单描述
		reqData.append("<transTimeout>").append(transTimeout)
				.append("</transTimeout>"); // 交易超时时间
		reqData.append("<backEndUrl>").append("02"+backEndUrl)
				.append("</backEndUrl>"); // 商户通知URL
		reqData.append("<sign>").append(sign(signStr)).append("</sign>"); // 商户签名
		reqData.append("<merchantPublicCert>").append(getPublicKey())
				.append("</merchantPublicCert>"); // 商户公钥证书
		reqData.append("</pomp>");
		return reqData.toString();
	}

	/**
	 * 钱数值格式化为12位,不足在前面补0
	 * 
	 */
	
	private String moneyFormat(String money){
		String monStr=String.format("%12s", money);
		monStr=monStr.replace(' ', '0');
		return monStr;
	}
	/**
	 * 用私钥给组装参数签名
	 * 
	 * @param signStr
	 * @return
	 */
	private String sign(String signStr) {
		InputStream keyStoreStream = null;
		String sign = "";
		try {
			HttpServletRequest req=getRequest();
			String realPath=req.getRealPath("/")+"WEB-INF/classes";
			// 私钥地址
			String pfxPath="";
			if(Constants.IS_TEST_ENV)   //测试环境
				pfxPath = realPath+Constants.PATH_OF_CERTIFICATE
					+ Constants.NAME_OF_TEST_PFX;
			else      //生产环境
				pfxPath = realPath+Constants.PATH_OF_CERTIFICATE
			+ Constants.NAME_OF_PRO_PFX;
			keyStoreStream = new FileInputStream(new File(pfxPath));
			//获取签名     
			if(Constants.IS_TEST_ENV)   //测试环境
				sign = PluginAlgorithm.signBase64(signStr.getBytes("UTF-8"),
					keyStoreStream, "11175751");
			else     //生产环境
				sign = PluginAlgorithm.signBase64(signStr.getBytes("UTF-8"),
					keyStoreStream, "19111667");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		} finally {
			if (keyStoreStream != null)
				try {
					keyStoreStream.close();
				} catch (IOException e) {
					e.printStackTrace();
					logger.error(e.getMessage(), e);
				}
		}
		return sign;
	}

	/**
	 * 获取公钥
	 * 
	 * @return
	 */
	private String getPublicKey() {
		HttpServletRequest req=getRequest();
		String realPath=req.getRealPath("/")+"WEB-INF/classes";
		String cerPath="";
		//测试环境
		if(Constants.IS_TEST_ENV)
		  cerPath = realPath+Constants.PATH_OF_CERTIFICATE
				+ Constants.NAME_OF_TEST_CER;
		//生产环境
		else
		 cerPath = realPath+Constants.PATH_OF_CERTIFICATE
		+ Constants.NAME_OF_PRO_CER;
		InputStream in = null;
		String publicKey = "";
		try {
			in = new FileInputStream(new File(cerPath));
			publicKey = CertificateCoder.getCertificate(in);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
					logger.error(e.getMessage(), e);
				}
		}

		return publicKey;
	}

	/**
	 * 调用易联手机支付 下单
	 * 
	 * @param paramsXml
	 *            参数组装的xml
	 * @param reqUrl
	 *            请求的url
	 * @return
	 * @throws Exception
	 */
	private String send(String paramsXml, String reqUrl) throws Exception {
		String response = null;
		String requestData = URLEncoder.encode(paramsXml, "UTF-8");
		response = HttpsUtils1.sendHttps(requestData, reqUrl);
		return response;
	}


	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

}
