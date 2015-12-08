package com.fantingame.pay.action.mobile.notify;

import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.apache.log4j.Logger;

import com.fantingame.pay.entity.PayChannel;
import com.fantingame.pay.entity.TradeCode;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.StringTools;
import com.fantingame.pay.utils.yeepay.YeePayDigestUtil;

public class NotifyFromYeePayAction extends BaseReceiveNotifyAction {

	private static final long serialVersionUID = -4885276354103619075L;
	private static final Logger logger = Logger.getLogger(NotifyFromYeePayAction.class);
	
	public String execute() throws Exception {
		
		PrintWriter out = getResponse().getWriter();
		try {
			//获取参数 
		   Map<String,String> params = checkParams(getRequest());
		   String orderId = params.get("p2_Order");
		   String r1_Code = params.get("r1_Code");
		   String p3_Amt  = params.get("p3_Amt");
		   String channelStatus = params.get("p8_cardStatus");
		   String hmac    = params.get("hmac");
		   String channelMsg = TradeCode.getMsgC(Constants.OWN_YEEPAY_STATUS,channelStatus);
		   
		   PayChannel channel = getChannelById(Constants.CHANNEL_ID_YEEPAY_PHONE_CARD);//获取密钥
		   //验签
		   StringBuffer sb = new StringBuffer();
		   for(Map.Entry<String,String> entity : params.entrySet()){
			   if(!"hmac".equals(entity.getKey())) sb.append(entity.getValue()==null?"":entity.getValue());
		   }
		   
		   String sign = YeePayDigestUtil.hmacSign(sb.toString(), channel.getSecretKey());
		   if(!sign.equals(hmac)){
			   out.print(TradeCode.EASOU_CODE_110.msgE);
			   logger.error("[orderId]:"+orderId+"..."+TradeCode.EASOU_CODE_110.msgE);
			   return null;
		   }
		   //验签通过
		   long ebOrderId = Long.parseLong(orderId);
		   int easouStatus = Constants.EASOU_SERVER_STATUS_SUCCESS;
		  
		   if(!"1".equals(r1_Code)){//支付不成功
			   easouStatus = Constants.EASOU_SERVER_STATUS_FAIL;
		   }
		   //保存通知
		   savePayNotify(orderId, null, Constants.CHANNEL_ID_YEEPAY_PHONE_CARD, easouStatus, p3_Amt);
		   //后续操作
		   doWorksAfterReceiveNotify(ebOrderId, easouStatus, channelStatus, channelMsg, p3_Amt, null);
		   //回复易爆那边
		   out.print("success");
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	
	
	private Map<String, String> checkParams(ServletRequest req) throws Exception{
		Map<String, String> map = new LinkedHashMap<String, String>();
		StringTools.setValue(map,"r0_Cmd",getParam("r0_Cmd"),20,true,false);
		StringTools.setValue(map,"r1_Code",getParam("r1_Code"),5,true,false);
		StringTools.setValue(map,"p1_MerId",getParam("p1_MerId"),16,true,false);
		StringTools.setValue(map,"p2_Order",getParam("p2_Order"),32,true,false);
		StringTools.setValue(map,"p3_Amt",getParam("p3_Amt"),16,true,true);
		StringTools.setValue(map,"p4_FrpId",getParam("p4_FrpId"),10,true,false);
		StringTools.setValue(map,"p5_CardNo",getParam("p5_CardNo"),300,true,false);
		StringTools.setValue(map,"p6_confirmAmount",getParam("p6_confirmAmount"),100,true,false);
		StringTools.setValue(map,"p7_realAmount",getParam("p7_realAmount"),100,true,false);
		StringTools.setValue(map,"p8_cardStatus",getParam("p8_cardStatus"),100,true,false);
		StringTools.setValue(map,"p9_MP",getParam("p9_MP"),100,false,false);
		StringTools.setValue(map,"pb_BalanceAmt",getParam("pb_BalanceAmt"),16,false,false);
		StringTools.setValue(map,"pc_BalanceAct",getParam("pc_BalanceAct"),32,false,false);
		StringTools.setValue(map,"hmac",getParam("hmac"),32,true,false);
		
		return map;
	}

}
