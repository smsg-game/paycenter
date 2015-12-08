package com.fantingame.pay.action.mobile.confirm;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fantingame.pay.action.common.BaseAction;
import com.fantingame.pay.entity.PayChannel;
import com.fantingame.pay.entity.PayOrder;
import com.fantingame.pay.entity.TradeCode;
import com.fantingame.pay.exception.EasouPayException;
import com.fantingame.pay.manager.PayChannelManager;
import com.fantingame.pay.manager.PayOrderManager;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.RsaSignUtil;
import com.fantingame.pay.utils.StringTools;

/**
 * 客户端通知接口，用于处理客户端所反馈的订单状态信息
 * */
public class PayConfirmOfAlipayAction extends BaseAction {
	private static final long serialVersionUID = -1933669797143913506L;
	private static Logger logger = Logger.getLogger(PayConfirmOfAlipayAction.class);
	
	public String execute() throws Exception {
		try {
			logger.info("invoice:"+getParam("invoice") +"....data:"+getParam("data"));
			
			//获取参数
			String invoice = StringTools.getValue("invoice",getParam("invoice"),32,true,false);
			String data = StringTools.getValue("data",getParam("data"),2048,true,false);

			//data = "resultStatus={9000};memo={};result={partner=\"2088202274264810\"&seller=\"taobao@staff.easou.com\"&out_trade_no=\"def508ffada845b8bb9180949feb4067\"&subject=\"游戏币充值0.01元\"&body=\"1金币\"&total_fee=\"0.01\"&notify_url=\"http://testservice.pay.easou.com:8080/service/notifyFromAlipay.e\"&success=\"true\"&sign_type=\"RSA\"&sign=\"zLaSv6M2Cg1WIA2/cZ8rWcYJqV8pKr8krCBdSgnyDTsscC0c9jAxHp6jb1hTUhojcp/dCAN3zMqY25Ajq6tf4lOABsSPPkPPLvDP/jQZ/ynUz+wRUU650RHTbPebB7zfPlWL/sdOfGJsfTbz1IMWWIpQRBaM1vmSH+6k/NJAJLA=\"}";
			//data = "resultStatus={4000};memo={操作未成功。};result={}";
			
			Map<String,String> dataMap1 = extract(data,"\\}\\;","\\=\\{");
			String resultStatus = StringTools.getValue("resultStatus",dataMap1.get("resultStatus"),4,true,true);
			String memo = dataMap1.get("memo");
			memo = (memo==null || memo.isEmpty())?"":":"+memo;
			String result = dataMap1.get("result");
			
			//提取result
			result = result.substring(0,result.length()-2);
			Map<String,String> dataMap2 = extract(result,"\"&","\\=\"");
			
			//组装待签名数据
			StringBuffer sb = new StringBuffer("partner=" + "\"" + dataMap2.get("partner") + "\"&");
			sb.append("seller=" + "\"" + dataMap2.get("seller") + "\"&");
			sb.append("out_trade_no=" + "\"" + dataMap2.get("out_trade_no") + "\"&");
			sb.append("subject=" + "\"" + dataMap2.get("subject") + "\"&");
			sb.append("body=" + "\"" + dataMap2.get("body") + "\"&");
			sb.append("total_fee=" + "\"" + dataMap2.get("total_fee") + "\"&");
			sb.append("notify_url=" + "\"" + dataMap2.get("notify_url") + "\"&");
			sb.append("success=" + "\"" + dataMap2.get("success") + "\"");
	        
			String success = dataMap2.get("success");
			String sign = dataMap2.get("sign");
			
			//验签
	        PayChannelManager payChannelManager = (PayChannelManager)getBean("payChannelManager");
			PayChannel channel = payChannelManager.getEntityById(Constants.CHANNEL_ID_ALIPAY);
			if(!RsaSignUtil.doCheck(sb.toString(), sign, channel.getChannelPublicKey())){
				logger.error(TradeCode.EASOU_CODE_110.msgE);
				return null;
			}
			           
			PayOrderManager orderManager = (PayOrderManager)getBean("payOrderManager");
			PayOrder order = orderManager.getEntityByInvoice(invoice);
			if(order != null ){//订单存在
				if(order.getClientStatus()!=1){//订单未出理
					if(TradeCode.ALIPAY_CODE_9000.code.equals(resultStatus) && "true".equals(success)){
						order.setClientStatus(1);
					}else{
						order.setClientStatus(-1);
					}
					order.setClientMsg(resultStatus+memo);
					orderManager.update(order);
				}else{//订单已处理
					logger.error(TradeCode.EASOU_CODE_180.msgE+"...invoice:"+invoice);
				}
			}else{//订单不存在
				logger.error(TradeCode.EASOU_CODE_140.msgE+"...invoice:"+invoice);
			}
		} catch (EasouPayException e) {
			logger.error(e.getCode()+":"+e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} 
		return null;
	}
	
	
	public static Map<String,String> extract(String data,String sp1,String sp2){
		Map<String,String> map = new HashMap<String, String>();
		if(data!=null && !data.isEmpty()){
		   String arr1[] = data.split(sp1,-1);
		   for(String str : arr1){
			   String arr2[] = str.split(sp2,-1);
			   map.put(arr2[0],str.substring(arr2[0].length()+sp2.replace("\\", "").length()));
		   }
		}
		return map;
	}

}
