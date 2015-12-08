package com.fantingame.pay.action.ecenter;

import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.fantingame.pay.action.common.BaseAction;
import com.fantingame.pay.entity.PayEb;
import com.fantingame.pay.entity.PayTrade;
import com.fantingame.pay.entity.SmsBlackList;
import com.fantingame.pay.entity.SmsChannel;
import com.fantingame.pay.entity.SmsLocation;
import com.fantingame.pay.entity.SmsRule;
import com.fantingame.pay.entity.SmsSended;
import com.fantingame.pay.entity.TradeCode;
import com.fantingame.pay.entity.User;
import com.fantingame.pay.exception.EasouPayException;
import com.fantingame.pay.manager.SmsBlackListManager;
import com.fantingame.pay.manager.SmsChannelManager;
import com.fantingame.pay.manager.SmsLocationManager;
import com.fantingame.pay.manager.SmsRuleManager;
import com.fantingame.pay.manager.SmsSendedManager;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.StringTools;

public class SmsEasouAction extends BaseAction{
	private static final long serialVersionUID = -1936642025433531784L;
	private static Logger logger = Logger.getLogger(SmsEasouAction.class);
	private String to         = null;
	private String msg        = null;
	
	
	private SmsLocationManager  smsLocationManager  = (SmsLocationManager)getBean("smsLocationManager");
	private SmsChannelManager   smsChannelManager   = (SmsChannelManager)getBean("smsChannelManager");
	private SmsRuleManager      smsRuleManager      = (SmsRuleManager)getBean("smsRuleManager");
	private SmsSendedManager    smsSendedManager    = (SmsSendedManager)getBean("smsSendedManager");
	
	public String execute() throws Exception{
		try {
			User user = (User)getWapSession().getAttribute("user");
			//查询PayTrade
			invoice = StringTools.getValue("invoice",getParam("invoice"),32,true,false);
			PayTrade payTrade = getTradeByInvoice(invoice);
			//检查订单是否存在
			if(payTrade==null) throw new EasouPayException(TradeCode.EASOU_CODE_140.code,TradeCode.EASOU_CODE_140.msgC);
			//检查订单是否已经被处理
			if(payTrade.getStatus()!=Constants.EASOU_SERVER_STATUS_CREATE) throw new EasouPayException(TradeCode.EASOU_CODE_180.code,TradeCode.EASOU_CODE_180.msgC);

			if(user.getMobile()!=null && !user.getMobile().isEmpty()){//明确知道手机号
				return checkForMobile(user.getMobile(),payTrade);
			}else{//获取不到手机，只能用IP来做判断依据
				String ip = getRequest().getRemoteAddr();
				return checkForIp(ip,payTrade);
			}
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
			setMsg(e.getMessage());
			return ERROR;
		}
	}
	
	/*明确知道手机号的验证方法*/
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
	
	/*只有IP号走的验证方法*/
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
	
	
	private String returnSuccess(SmsChannel smsChannel,PayTrade payTrade) throws Exception{
		PayEb payEb = newPayEbOrder(payTrade.getInvoice(),Constants.CHANNEL_ID_SMS,payTrade.getReqFee(),null);
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

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
