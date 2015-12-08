package com.fantingame.pay.action.module;

import org.apache.log4j.Logger;

import com.fantingame.pay.action.common.BaseAction;
import com.fantingame.pay.entity.PayChannel;
import com.fantingame.pay.entity.TradeCode;
import com.fantingame.pay.exception.EasouPayException;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.StringTools;

public class SmsAction extends BaseAction{
	private static final long serialVersionUID = -1936642025433531784L;
	private static Logger logger = Logger.getLogger(SmsAction.class);
	
	public String execute() throws Exception{
		try {
			
			//查询PayTrade
			String invoice = StringTools.getValue("invoice",getParam("invoice"),32,true,false);
			payTrade = getTradeByInvoice(invoice);
			//检查订单是否存在
			if(payTrade==null) throw new EasouPayException(TradeCode.EASOU_CODE_140.code,TradeCode.EASOU_CODE_140.msgC);
			//检查订单是否已经被处理
			if(payTrade.getStatus()!=Constants.EASOU_SERVER_STATUS_CREATE) throw new EasouPayException(TradeCode.EASOU_CODE_180.code,TradeCode.EASOU_CODE_180.msgC);


			//获取通道列表，按权重
		    //for()	
		    //每日发送，每月发送限额
			//匹配sms_rule
			PayChannel payChannel = getChannelById(Constants.CHANNEL_ID_SMSYHXF);
			//把折算率传送到前端
			getRequest().setAttribute("rate",payChannel.getRate());
			setRedirectUrl(getBaseUrl()+"/module/redirect!showResult.e?invoice="+payTrade.getInvoice());
			String phoneNo = getUser().getMobile();
			getRequest().setAttribute("phoneNo", phoneNo);
			if(isEasouHallApp()){
				return "sms_yhxf_app";
			}else{
				return "sms_yhxf";
			}
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
			setMsg(e.getMessage());
			return ERROR;
		}
	}
	
}
