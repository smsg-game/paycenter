package com.fantingame.pay.action.module.notify;

import java.io.PrintWriter;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.fantingame.pay.action.mobile.notify.BaseReceiveNotifyAction;
import com.fantingame.pay.entity.PayChannel;
import com.fantingame.pay.entity.TradeCode;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.RsaSignUtil;
import com.fantingame.pay.utils.StringTools;

/**
 * 支付宝通知接口,主要处理支付宝发送过来的对订单支付结果
 * */

public class ModuleTerNotifyFromAlipayAction extends BaseReceiveNotifyAction {

	private static final long serialVersionUID = 5490792191825343426L;
	private static final Logger logger = Logger.getLogger(ModuleTerNotifyFromAlipayAction.class);

	public String execute() throws Exception {
		//设置编码
	    getResponse().setContentType("text/html;charset=UTF-8");
		PrintWriter out = getResponse().getWriter();
		
		try {
			//获取Notify数据
			String notifyData = getParam("notify_data");  
			//获取密钥
			PayChannel channel = getChannelById(Constants.CHANNEL_ID_ALIPAY_MODULE_TER); 
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
			savePayNotify(orderId, tradeNo, Constants.CHANNEL_ID_ALIPAY_MODULE_TER, easouStatus, totalFee);
			//后续操作
			doWorksAfterReceiveNotify(Long.parseLong(orderId), easouStatus, channelStatus, null, totalFee, tradeNo);
			if(easouStatus == Constants.EASOU_SERVER_STATUS_SUCCESS) {
				//回应支付宝
				out.print("success");
			} else {
				out.print("fail");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		} 
		return null;
	}
	
	
	
}
