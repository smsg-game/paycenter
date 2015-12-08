package com.fantingame.pay.action.mobile.notify;

import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletRequest;

import org.apache.log4j.Logger;

import com.fantingame.pay.entity.PayChannel;
import com.fantingame.pay.entity.TradeCode;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.StringTools;
import com.mokredit.payment.Md5Encrypt;

/**
 * Mo9通知接口,主要处理mo9发送过来的对订单支付结果
 * */

public class NotifyFromMo9Action extends BaseReceiveNotifyAction {

	private static final long serialVersionUID = -7501954146542499359L;
	private static Logger logger = Logger.getLogger(NotifyFromMo9Action.class);
	
	public String execute() throws Exception {
		
		PrintWriter out = getResponse().getWriter();
		try {
			//获取参数
			Map<String, String> params = checkParams(getRequest());
			
			String sign = params.get("sign");
			String invoice = params.get("invoice");
			String channelStatus = params.get("trade_status");
			String trade_no = params.get("trade_no");
			
			PayChannel channel = getChannelById(Constants.CHANNEL_ID_MO9);//获取密钥

			//验签
			if(sign==null || "".equals(sign) || !sign.equals(Md5Encrypt.sign(params, channel.getSecretKey()))){ //签名为空/验证不通过
				out.print(TradeCode.EASOU_CODE_110.msgE);
				logger.error(TradeCode.EASOU_CODE_110.msgE+"...invoice:"+invoice);
				return null;
			}
			
			int easouStatus = Constants.EASOU_SERVER_STATUS_SUCCESS;
			  
			if(!Constants.MO9_TRADE_SUCCESS.equals(channelStatus)){// 交易状态为成功   ============  这里得判断mo9的可切分  支付部分状态；
				easouStatus = Constants.EASOU_SERVER_STATUS_FAIL;
			}
			
		  //保存通知
		   savePayNotify(invoice, trade_no, Constants.CHANNEL_ID_MO9, easouStatus, params.get("amount"));
		   //后续操作
		   doWorksAfterReceiveNotify( Long.parseLong(invoice), easouStatus, channelStatus, null, params.get("amount"), null);
			
			out.print("OK");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} 
		return null;
	}


	private Map<String, String> checkParams(ServletRequest req) throws Exception{
		Map<String, String> map = new TreeMap<String, String>();
		StringTools.setValue(map,"pay_to_email",getParam("pay_to_email"),50,true,false);
		StringTools.setValue(map,"payer_id",getParam("payer_id"),64,true,false);
		StringTools.setValue(map,"trade_no",getParam("trade_no"),32,false,false);
		StringTools.setValue(map,"trade_status",getParam("trade_status"),32,true,false);
		StringTools.setValue(map,"amount",getParam("amount"),16,true,true);
		StringTools.setValue(map,"currency",getParam("currency"),16,true,false);
		StringTools.setValue(map,"req_amount",getParam("req_amount"),16,true,false);
		StringTools.setValue(map,"req_currency",getParam("req_currency"),16,true,false);
		StringTools.setValue(map,"item_name",getParam("item_name"),30,true,false);
		StringTools.setValue(map,"lc",getParam("lc"),16,true,false);
		StringTools.setValue(map,"extra_param",getParam("extra_param"),255,false,false);
		StringTools.setValue(map,"app_id",getParam("app_id"),32,true,false);
		StringTools.setValue(map,"invoice",getParam("invoice"),32,true,false);
		StringTools.setValue(map,"sign",getParam("sign"),32,true,false);
		return map;
	}

}
