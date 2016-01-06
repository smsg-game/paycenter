package com.fantingame.pay.action.mobile.notify;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alipay.util.AlipayNotify;
import com.fantingame.pay.entity.PayChannel;
import com.fantingame.pay.entity.TradeCode;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.RsaSignUtil;
import com.fantingame.pay.utils.StringTools;

public class NotifyFromNewMobileAlipayAction extends BaseReceiveNotifyAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(NotifyFromAlipayAction.class);
	
	
	public String execute() throws Exception{
		//设置编码
	    getResponse().setContentType("text/html;charset=UTF-8");
		PrintWriter out = getResponse().getWriter();
		try {

			Map<String, String> params = new HashMap<String, String>();
			Map requestParams = getRequest().getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter
					.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i]
							: valueStr + values[i] + ",";
				}
				// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				// valueStr = new String(valueStr.getBytes("ISO-8859-1"),
				// "gbk");
				params.put(name, valueStr);
				logger.info("参数["+name+"]=["+valueStr+"]");
			}

			// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
			// 商户订单号 
			String orderId = new String(getRequest().getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
			// 支付宝交易号 
			String tradeNo = new String(getRequest().getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
			//获取参数
//			String channelStatus = StringTools.getValue("trade_status",params.get("trade_status"),32,true,false);//交易状态 
//			String totalFee =  StringTools.getValue("total_fee",params.get("total_fee"),16,true,true);//交易金额
//			String orderId = StringTools.getValue("out_trade_no",params.get("out_trade_no"),64,true,false); //外部交易号(商户交易号)
//			String tradeNo = StringTools.getValue("trade_no",params.get("trade_no"),64,true,false); //支付宝交易号
			
			String totalFee = new String(getRequest().getParameter("total_fee").getBytes("ISO-8859-1"),"UTF-8");
			// 交易状态
			String channelStatus = new String(getRequest().getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
			
			logger.info("收到支付宝回调，参数为,orderId=["+orderId+"],tradeNo=["+tradeNo+"],totalFee=["+totalFee+"],status=["+channelStatus+"]");

			// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//

			if (AlipayNotify.verify(params)) {// 验证成功
				
				int easouStatus = Constants.EASOU_SERVER_STATUS_CREATE;
				if("TRADE_FINISHED".equals(channelStatus) || "TRADE_SUCCESS".equals(channelStatus)){//支付状态为成功
					easouStatus = Constants.EASOU_SERVER_STATUS_SUCCESS;
				}else if("WAIT_BUYER_PAY".equals(channelStatus)){//等待支付
					easouStatus = Constants.EASOU_SERVER_STATUS_WAITING;
				}else{
					easouStatus = Constants.EASOU_SERVER_STATUS_FAIL;
				}
				savePayNotify(orderId, tradeNo, Constants.CHANNEL_ID_ALIPAY, easouStatus, totalFee);
				//后续操作
				doWorksAfterReceiveNotify(Long.parseLong(orderId), easouStatus, channelStatus, null, totalFee, tradeNo);
				// ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
				out.println("success"); // 请不要修改或删除
				
				logger.info("回调成功成功");
				// ////////////////////////////////////////////////////////////////////////////////////////
			} else {// 验证失败
				out.println("fail");
				logger.info("回调失败验签名不成功！~~~");
			}
		} catch (Exception e) {
			out.println("fail");
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	public String execute2() throws Exception {
		//设置编码
	    getResponse().setContentType("text/html;charset=UTF-8");
		PrintWriter out = getResponse().getWriter();
		
		try {
			//获取Notify数据
			String notifyData = getParam("notify_data");  
			//获取密钥
			PayChannel channel = getChannelById(Constants.CHANNEL_ID_ALIPAY); 
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
			String channelStatus = StringTools.getValue("trade_status",params.get("trade_status"),32,true,false);//交易状态 
			String totalFee =  StringTools.getValue("total_fee",params.get("total_fee"),16,true,true);//交易金额
			String orderId = StringTools.getValue("out_trade_no",params.get("out_trade_no"),64,true,false); //外部交易号(商户交易号)
			String tradeNo = StringTools.getValue("trade_no",params.get("trade_no"),64,true,false); //支付宝交易号
			//String notifyRegTime = params.get("notify_reg_time");//通知时间
			
			int easouStatus = Constants.EASOU_SERVER_STATUS_CREATE;
			
			if("TRADE_FINISHED".equals(channelStatus) || "TRADE_SUCCESS".equals(channelStatus)){//支付状态为成功
				easouStatus = Constants.EASOU_SERVER_STATUS_SUCCESS;
			}else if("WAIT_BUYER_PAY".equals(channelStatus)){//等待支付
				easouStatus = Constants.EASOU_SERVER_STATUS_WAITING;
			}else{
				easouStatus = Constants.EASOU_SERVER_STATUS_FAIL;
			}
			//保存通知
			savePayNotify(orderId, tradeNo, Constants.CHANNEL_ID_ALIPAY, easouStatus, totalFee);
			//后续操作
			doWorksAfterReceiveNotify(Long.parseLong(orderId), easouStatus, channelStatus, null, totalFee, tradeNo);
			//回应支付宝
			out.print("success");
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		} 
		return null;
	}
}
