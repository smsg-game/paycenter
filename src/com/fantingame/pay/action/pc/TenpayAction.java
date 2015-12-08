package com.fantingame.pay.action.pc;

import java.net.URLEncoder;

import org.apache.log4j.Logger;

import com.easou.common.util.StringUtil;
import com.fantingame.pay.action.common.BaseAction;
import com.fantingame.pay.entity.PayChannel;
import com.fantingame.pay.entity.PayEb;
import com.fantingame.pay.entity.TradeCode;
import com.fantingame.pay.entity.User;
import com.fantingame.pay.exception.EasouPayException;
import com.fantingame.pay.manager.PayEbManager;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.EbTools;
import com.fantingame.pay.utils.LoggerUtil;
import com.fantingame.pay.utils.tenpay.RequestHandler;
import com.fantingame.pay.utils.tenpay.util.TenpayUtil;

/**
 * 财付通支付
 * @author easou
 *
 */
public class TenpayAction extends BaseAction {

	private static final long serialVersionUID = -5019914247728372377L;
	private static final Logger logger = Logger.getLogger(TenpayAction.class);
	
	//使用财付通支付订单
	public String execute(){
		try {
			String url = getRequest().getParameter("returnUrl");
			getRequest().getSession().setAttribute("returnUrl",url);
			String reqFee = getRequest().getParameter("reqFee");
			if(StringUtil.isEmpty(reqFee) && !StringUtil.isNumber(reqFee)){
				return ERROR;
			}
			PayChannel channel = getChannelById(Constants.CHANNEL_ID_TENPAY);	//获取支付商信息
			User user = (User) getWapSession().getAttribute("user");
			PayEbManager payEbManager = (PayEbManager)getBean("payEbManager");
			PayEb payEb = payEbManager.addEbOrder(channel, user, reqFee, "财付通PC版充值");
			if(payEb == null){
				throw new EasouPayException(TradeCode.EASOU_CODE_140.code,TradeCode.EASOU_CODE_140.msgC);
			}
			
			//---------------------------------------------------------
			//财付通网关支付请求示例，商户按照此文档进行开发即可
			//---------------------------------------------------------
			/* 商户号，上线时务必将测试商户号替换为正式商户号 */
			String partner = channel.getPartnerId();
			//密钥
			String key = channel.getPrivateKey();

			//交易完成后跳转的URL
			String return_url = channel.getReturnUrl();
			//接收财付通通知的URL
			String notify_url = channel.getNotifyUrl();
			logger.info("notify_url:"+notify_url);
			String currTime = TenpayUtil.getCurrTime();
			//---------------生成订单号 开始------------------------;
			//订单号，此处用时间加随机数生成，商户根据自己情况调整，只要保持全局唯一就行
			String out_trade_no = payEb.getId().toString();
			//---------------生成订单号 结束------------------------

			//创建支付请求对象
			RequestHandler reqHandler = new RequestHandler(getRequest(),getResponse());
			reqHandler.init();
			//设置密钥
			reqHandler.setKey(key);
			reqHandler.setGateUrl(channel.getOrderUrl());

			//-----------------------------
			//设置支付参数
			//-----------------------------
			reqHandler.setParameter("partner", partner);		        //商户号
			reqHandler.setParameter("out_trade_no", out_trade_no);		//商家订单号
			reqHandler.setParameter("total_fee",String.valueOf(EbTools.yuanToFen(payEb.getReqFee())));		//商品金额,以分为单位
			reqHandler.setParameter("return_url", return_url);		    //交易完成后跳转的URL
			reqHandler.setParameter("notify_url", notify_url);		    //接收财付通通知的URL
			reqHandler.setParameter("body",URLEncoder.encode("e币充值","GBK"));	                    //商品描述
			reqHandler.setParameter("bank_type", "DEFAULT");		    //银行类型
			reqHandler.setParameter("spbill_create_ip",getRequest().getRemoteAddr());   //用户的公网ip
			reqHandler.setParameter("fee_type", "1");

			//系统可选参数
			reqHandler.setParameter("sign_type", "MD5");
			reqHandler.setParameter("service_version", "1.0");
			reqHandler.setParameter("input_charset", "GBK");
			reqHandler.setParameter("sign_key_index", "1");

			//业务可选参数
			reqHandler.setParameter("attach", "");
			reqHandler.setParameter("product_fee", String.valueOf(EbTools.yuanToFen(payEb.getReqFee())));
			reqHandler.setParameter("transport_fee", "0");
			reqHandler.setParameter("time_start", currTime);
			reqHandler.setParameter("time_expire", "");
			
			reqHandler.setParameter("buyer_id", "");
			reqHandler.setParameter("goods_tag", "");
			//reqHandler.setParameter("agentid", "");
			//reqHandler.setParameter("agent_type", "");

			//请求的url
			String requestUrl = reqHandler.getRequestURL();
			//获取debug信息,建议把请求和debug信息写入日志，方便定位问题
//			String debuginfo = reqHandler.getDebugInfo();
//
//			System.out.println("requestUrl:" + requestUrl);
//			System.out.println("debuginfo:" + debuginfo);
			setRedirectUrl(requestUrl);
		} catch (Exception e) {
			logger.error("to pc alipay error!",e);
		}
		return REDIRECT;
	}
}
