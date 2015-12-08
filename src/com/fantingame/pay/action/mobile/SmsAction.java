package com.fantingame.pay.action.mobile;

import org.apache.log4j.Logger;

import com.fantingame.pay.action.common.BaseAction;
import com.fantingame.pay.entity.PayChannel;
import com.fantingame.pay.entity.PayTrade;
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
			setRedirectUrl(getBaseUrl()+"/mobile/redirect!showResult.e?invoice="+payTrade.getInvoice());
			String phoneNo = getUser().getMobile();
			getRequest().setAttribute("phoneNo", phoneNo);
			return "sms_yhxf";
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
			setMsg(e.getMessage());
			return ERROR;
		}
	}
	
	/*明确知道手机号的验证方法
	private String checkForMobile(String mobile,PayTrade payTrade) throws Exception{
		//黑名单
		SmsBlackListManager smsBlackListManager = (SmsBlackListManager)getBean("smsBlackListManager");
		SmsBlackList bl = smsBlackListManager.getEntityByMobile(mobile);
		if(bl!=null) return returnFail("黑名单限制");
		//查一下手机归属地
		SmsLocation sl = smsLocationManager.getEntityByMobile(mobile);
		if(sl==null) return returnFail("无法判断手机号归属地");
		
		//1-sdk,2-wap,3-web,2表示适应wap版的通道
		List<SmsChannel> channelList = smsChannelManager.getEntity(new SmsChannel(2,1));
		for(SmsChannel smsChannel : channelList){
			//wap版,要检查免费短信是否可可扩展
			if(!smsChannel.getFeeSmsExtendable()) continue;
			
			//检查请求支付金额是否与通道单价一致
			if(Float.valueOf(payTrade.getReqFee())*100!=smsChannel.getPrice()) continue;
			
			//查一下发送历史
			SmsSended smsSended = new SmsSended(smsChannel.getId(),Long.valueOf(mobile));
			int sendedToday = smsSendedManager.countSendedToday(smsSended);
			int moneyToday  = smsSendedManager.countMoneyToday(smsSended);
			int sendedMonth = smsSendedManager.countSendedMonth(smsSended);
			int moneyMonth  = smsSendedManager.countMoneyMonth(smsSended);
			if(sendedToday>=smsChannel.getMaxSendPerDay() || sendedMonth>=smsChannel.getMaxSendPerMonth() || moneyToday>=smsChannel.getMaxMoneyPerDay() || moneyMonth>=smsChannel.getMaxMoneyPerMonth()){
				return returnFail("超出发送限额");
			}
			//查找规则
			SmsRule smsRule = smsRuleManager.getEntityByCity(new SmsRule(smsChannel.getId(),sl.getProvince(),sl.getCity()));
			if(smsRule!=null){
				//判断是否在允许发送时间范围内
				String hour = StringTools.getHourFormat().format(new Date());
				if(!smsRule.getTime().contains(hour)) continue;
				//判断运营商
				if(sl.getOperator()!=null && !sl.getOperator().isEmpty()){//判断运营商
					if(sl.getOperator().contains("移动")){//移动
					    if(smsRule.getOptCmcc().equals("all") || smsRule.getOptCmcc().contains(sl.getType())){
					    	return returnSuccess(smsChannel,payTrade);
					    }
				   }else if(sl.getOperator().contains("联通")){//联通
					   if(smsRule.getOptUnicom().equals("all") || smsRule.getOptUnicom().contains(sl.getType())){
						   return returnSuccess(smsChannel,payTrade);
					    }
				   }else if(sl.getOperator().contains("电信")){//电信
					   if(smsRule.getOptTelecom().equals("all") || smsRule.getOptTelecom().contains(sl.getType())){
						   return returnSuccess(smsChannel,payTrade);
					    }
				   }
				}
			}
		}//end for
		return returnFail("没有合适的通道");
	}//end method
	*/
	/*只有IP号走的验证方法
	private String checkForIp(String ip,PayTrade payTrade) throws Exception{
		//第一次
		String content = resolveIpFromTaobao(ip);
		//第二次
		if(content==null || content.isEmpty()) content = resolveIpFromTaobao(ip);
		//第三次
		if(content==null || content.isEmpty()) content = resolveIpFromTaobao(ip);
		//判断是否为空
		if(content==null || content.isEmpty()) return returnFail("无法获取IP地址");
		
		JSONObject root = JSONObject.fromObject(content);
		if("1".equals(root.get("code"))) return returnFail("获取IP地址出错");
		
		JSONObject ipResult  = root.getJSONObject("data");
		SmsLocation sl       = new SmsLocation();
		sl.setProvince(ipResult.getString("region"));
		sl.setCity(ipResult.getString("city"));
		sl.setOperator(ipResult.getString("isp"));
		
		//检查是否获得IP的省份跟城市
		if(StringUtils.isEmpty(sl.getProvince()) || StringUtils.isEmpty(sl.getCity())) return returnFail("无法判断IP归属地");
		
		List<SmsChannel> channelList = smsChannelManager.getEntity(new SmsChannel(2,1));
		for(SmsChannel smsChannel : channelList){
			//wap版,要检查免费短信是否可可扩展
			if(!smsChannel.getFeeSmsExtendable()) continue;
			
			//检查请求支付金额是否与通道单价一致
			if(Float.valueOf(payTrade.getReqFee())*100!=smsChannel.getPrice()) continue;
			
			//查找规则
			SmsRule smsRule = smsRuleManager.getEntityByCity(new SmsRule(smsChannel.getId(),sl.getProvince(),sl.getCity()));
			if(smsRule!=null){
				//判断是否在允许发送时间范围内
				String hour = StringTools.getHourFormat().format(new Date());
				if(!smsRule.getTime().contains(hour)) continue;
				//判断运营商
				if(!StringUtils.isEmpty(sl.getOperator()) && sl.getOperator().contains("移动")){//移动
					if(!StringUtils.isEmpty(smsRule.getOptCmcc())) return returnSuccess(smsChannel,payTrade);
				}else if(!StringUtils.isEmpty(sl.getOperator()) && sl.getOperator().contains("联通")){//联通
					if(!StringUtils.isEmpty(smsRule.getOptUnicom())) return returnSuccess(smsChannel,payTrade);
				}else if(!StringUtils.isEmpty(sl.getOperator()) && sl.getOperator().contains("电信")){//电信
					if(!StringUtils.isEmpty(smsRule.getOptTelecom())) return returnSuccess(smsChannel,payTrade);
				}else{//三大运营商之外,默认当作移动
					if(!StringUtils.isEmpty(smsRule.getOptCmcc())) return returnSuccess(smsChannel,payTrade);
				}
			}
		}//end for
		return returnFail("没有合适的通道");
	}
	*/
	/*
	private String returnSuccess(SmsChannel smsChannel,PayTrade payTrade) throws Exception{
		PayEb payEb = newPayEbOrder(payTrade.getInvoice(),Constants.CHANNEL_ID_SMS);
		String subId = StringTools.getNumFormat_4().format(payEb.getId());
		subId = subId.substring(subId.length()-4);
		to  = smsChannel.getFeeSmsTo();
		msg = smsChannel.getFeeSmsContent()+subId;
		return SUCCESS;
	}
	
	private String returnFail(String errMsg){
		msg = errMsg;
        return FAIL;
	}
    
	private String resolveIpFromTaobao(String ip){
		String content = null;
		try{
		    HttpGet httpGet = new HttpGet("http://ip.taobao.com/service/getIpInfo.php?ip="+ip);
		    HttpResponse response = Constants.HTTPCLIENT.execute(httpGet);
			content = EntityUtils.toString(response.getEntity(),"UTF-8");
			httpGet.abort();
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return content;
	}
    */

}
