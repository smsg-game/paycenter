package com.fantingame.pay.action.wap;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.easou.common.api.Md5SignUtil;
import com.fantingame.pay.action.common.BaseAction;
import com.fantingame.pay.entity.PayPartner;
import com.fantingame.pay.entity.PayTrade;
import com.fantingame.pay.entity.TradeCode;
import com.fantingame.pay.exception.EasouPayException;
import com.fantingame.pay.manager.PayPartnerManager;
import com.fantingame.pay.manager.PayTradeManager;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.RsaSignUtil;
import com.fantingame.pay.utils.StringTools;

/**
 * 对外下单接口: 必须做验签
 * 游戏商用其私钥对请求信息做签名；传至此接口进行下订单操作！
 * 接口接收到参数后，用游戏商公钥进行验签；验签通过，后续作下订单操作！
 */
public class TradeAction extends BaseAction{
	
	private static final long serialVersionUID = 4874488652388728333L;
	private static Logger logger = Logger.getLogger(TradeAction.class);
	
	public String execute() throws Exception {
		try{
			
			//记录来源地址
			getRequest().getSession().setAttribute("charge_backurl",getParam(Constants.FIELD_REDIRECT_URL));

			Map<String,String> signMap = verifyParams(); //取参
			String sign = StringTools.getValue(Constants.FIELD_SIGN,getParam(Constants.FIELD_SIGN),172,true,false);
			String extInfo = StringTools.getValue(Constants.FIELD_EXT_INFO,getParam(Constants.FIELD_EXT_INFO),255,false,false);		
			
			//订单金额小于0.01的,大于1亿，报错
			if(Double.valueOf(signMap.get(Constants.FIELD_REQ_FEE))<0.01 || Double.valueOf(signMap.get(Constants.FIELD_REQ_FEE))>=100000000){
				setMsg(TradeCode.EASOU_CODE_190.msgC);
				return ERROR;
			}	
			
			if(!doCheck(signMap,sign)){//验签
				throw new EasouPayException(TradeCode.EASOU_CODE_110.code,TradeCode.EASOU_CODE_110.msgC);
			}
			payTrade  =  createPayTrade(signMap,extInfo);//下单
			//跳转到支付页
			if (payTrade!=null) {
				getWapSession().setAttribute("tradeInvoice", payTrade.getInvoice()); //放入第一个订单号，充值完后回到下单页面
				getWapSession().setAttribute("appId", payTrade.getAppId());  //获取应用ID
				getWapSession().setAttribute("qn", payTrade.getQn());  //获取渠道号
				//boolean smsEnable = false;
				/*
				float price = Float.valueOf(payTrade.getReqFee());
				User user = (User)getWapSession().getAttribute("user");
				if(!StringUtils.isEmpty(user.getMobile())){
					smsEnable = checkForMobile(user.getMobile(),price);
				}else{
					String ip = getRequest().getRemoteAddr();
					smsEnable = checkForIp(ip,price);
				}
				
				if(smsEnable){
					paySort = 1;
				}else if(price>6){
					paySort = 3;
				}
				*/

				return SUCCESS;
			}
		}catch (Exception e) {
			setMsg(e.getMessage());
			logger.error(e.getMessage()+"  PartnerId["+getParam(Constants.FIELD_PARTNER_ID)+"] AppId["+getParam(Constants.FIELD_APP_ID)+"]",e);
		}
		return ERROR;
	}
	
	
	/*按照数据库的密钥类型来决定用哪种方式验签*/
	private boolean doCheck(Map<String,String> map,String sign) throws Exception{
		PayPartnerManager partnerManager = (PayPartnerManager)getBean("payPartnerManager");
		PayPartner payPartner = partnerManager.getEntityById(Long.valueOf(map.get("partnerId")));
		if("1".equals(payPartner.getEncryptType())){//RSA
			return RsaSignUtil.doCheck(map,sign,payPartner.getPartnerPublicKey());	
		}else if("2".equals(payPartner.getEncryptType())){//MD5
			return Md5SignUtil.doCheck(map,sign, payPartner.getSecretKey());
		}else{
			return false;
		}
	}
	
	
	private Map<String,String> verifyParams() throws Exception{ 
		Map<String,String> map= new HashMap<String, String>();
		StringTools.setValue(map,Constants.FIELD_APP_ID,getParam(Constants.FIELD_APP_ID),32,true,false);
		StringTools.setValue(map,Constants.FIELD_PARTNER_ID,getParam(Constants.FIELD_PARTNER_ID),16,true,true); 
		StringTools.setValue(map,Constants.FIELD_TRADE_ID,getParam(Constants.FIELD_TRADE_ID),64,true,false);
		StringTools.setValue(map,Constants.FIELD_TRADE_NAME,getParam(Constants.FIELD_TRADE_NAME),64,true,false);
		StringTools.setValue(map,Constants.FIELD_NOTIFY_URL,getParam(Constants.FIELD_NOTIFY_URL),255,true,false);
		StringTools.setValue(map,Constants.FIELD_SEPARABLE,getParam(Constants.FIELD_SEPARABLE),16,true,false);
		StringTools.setValue(map,Constants.FIELD_QN,getParam(Constants.FIELD_QN),24,true,false);
		StringTools.setValue(map,Constants.FIELD_REDIRECT_URL,getParam(Constants.FIELD_REDIRECT_URL),255,false,false); //接受redirectUrl参数
		StringTools.setValue(map,Constants.FIELD_REQ_FEE,getParam(Constants.FIELD_REQ_FEE),16,true,true);
        StringTools.setValue(map,Constants.FIELD_TRADE_DESC,getParam(Constants.FIELD_TRADE_DESC),1024,true,false);
        StringTools.setValue(map,Constants.FIELD_PAYER_ID,getParam(Constants.FIELD_PAYER_ID),32,true,false);
		return map;
	}
	
	/*根据获得的tradeBean来创建一个订单，注意要检测订单是否重复*/
	private PayTrade createPayTrade(Map<String,String> map,String extInfo) throws Exception{
		PayTrade payTrade = new PayTrade() ;
		payTrade.setCreateDatetime(new Date());
		payTrade.setAppId(map.get(Constants.FIELD_APP_ID));
		payTrade.setSeparable("true".equals(map.get(Constants.FIELD_SEPARABLE))?1:0);
		payTrade.setPayerId(map.get(Constants.FIELD_PAYER_ID));
		payTrade.setEasouId(getUser().getId().toString());
		payTrade.setReqFee(map.get(Constants.FIELD_REQ_FEE));
		payTrade.setCurrency("CNY");
		payTrade.setExtInfo(extInfo);
		payTrade.setNotifyUrl(map.get(Constants.FIELD_NOTIFY_URL));
		payTrade.setPartnerId(Long.valueOf(map.get(Constants.FIELD_PARTNER_ID)));
		payTrade.setQn(map.get(Constants.FIELD_QN));
		payTrade.setCookieQn(getCookieByName(Constants.EASOU_COOKIE_QN_NAME));
		payTrade.setStatus(Constants.EASOU_SERVER_STATUS_CREATE);
		payTrade.setTradeDesc(map.get(Constants.FIELD_TRADE_DESC));
		payTrade.setTradeId(map.get(Constants.FIELD_TRADE_ID));
		payTrade.setTradeName(map.get(Constants.FIELD_TRADE_NAME));
		payTrade.setInvoice(UUID.randomUUID().toString().replace("-", ""));
		payTrade.setRedirectUrl(map.get(Constants.FIELD_REDIRECT_URL));
		PayTradeManager payTradeManager = (PayTradeManager)getBean("payTradeManager");
		PayTrade tmpTrade = payTradeManager.getByPartnerIdAndTradeId(payTrade);
		if(tmpTrade==null){//不重复,保存新订单
			payTradeManager.save(payTrade);
			return payTrade;
		}else if(tmpTrade.getStatus()==Constants.EASOU_SERVER_STATUS_CREATE){//重复了，但是订单没处理过，直接返回
			return tmpTrade;
		}else{//订单重复，且订单已经被处理过
			throw new Exception(TradeCode.EASOU_CODE_180.msgC);
		}
	}
	
	/*回到支付订单首页*/
	public String  toIndex() throws Exception{
		String invoice = (String)getWapSession().getAttribute("tradeInvoice");
		PayTradeManager payTradeManager = (PayTradeManager)getBean("payTradeManager");
		payTrade  = payTradeManager.getEntityByInvoice(invoice);
		return SUCCESS;
	}
	
	
	
	
	
	/*明确知道手机号的验证方法
	private boolean checkForMobile(String mobile,Float price) throws Exception{
		//黑名单
		SmsBlackListManager smsBlackListManager = (SmsBlackListManager)getBean("smsBlackListManager");
		SmsLocationManager smsLocationManager  = (SmsLocationManager)getBean("smsLocationManager");
		SmsChannelManager smsChannelManager  = (SmsChannelManager)getBean("smsChannelManager");
		SmsSendedManager smsSendedManager  = (SmsSendedManager)getBean("smsSendedManager");
		SmsRuleManager smsRuleManager  = (SmsRuleManager)getBean("smsRuleManager");
		
		SmsBlackList bl = smsBlackListManager.getEntityByMobile(mobile);
		if(bl!=null) return false;
		//查一下手机归属地
		SmsLocation sl = smsLocationManager.getEntityByMobile(mobile);
		if(sl==null) return false;
		
		List<SmsChannel> channelList = smsChannelManager.getEntity(new SmsChannel(2,1));
		for(SmsChannel smsChannel : channelList){
			//检查请求支付金额是否与通道单价一致
			if(price*100!=smsChannel.getPrice()) continue;
			
			//查一下发送历史
			SmsSended smsSended = new SmsSended(smsChannel.getId(),Long.valueOf(mobile));
			int sendedToday = smsSendedManager.countSendedToday(smsSended);
			int moneyToday  = smsSendedManager.countMoneyToday(smsSended);
			int sendedMonth = smsSendedManager.countSendedMonth(smsSended);
			int moneyMonth  = smsSendedManager.countMoneyMonth(smsSended);
			if(sendedToday>=smsChannel.getMaxSendPerDay() || sendedMonth>=smsChannel.getMaxSendPerMonth() || moneyToday>=smsChannel.getMaxMoneyPerDay() || moneyMonth>=smsChannel.getMaxMoneyPerMonth()){
				return false;
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
					    	return true;
					    }
				   }else if(sl.getOperator().contains("联通")){//联通
					   if(smsRule.getOptUnicom().equals("all") || smsRule.getOptUnicom().contains(sl.getType())){
						   return true;
					    }
				   }else if(sl.getOperator().contains("电信")){//电信
					   if(smsRule.getOptTelecom().equals("all") || smsRule.getOptTelecom().contains(sl.getType())){
						   return true;
					    }
				   }
				}
			}
		}//end for
		return false;
	}//end method
	*/
	/*只有IP号走的验证方法
	private boolean checkForIp(String ip,Float price){
		SmsChannelManager smsChannelManager  = (SmsChannelManager)getBean("smsChannelManager");
		SmsRuleManager smsRuleManager  = (SmsRuleManager)getBean("smsRuleManager");
		
		//第一次
		String content = resolveIpFromTaobao(ip);
		//第二次
		if(content==null || content.isEmpty()) content = resolveIpFromTaobao(ip);
		//第三次
		if(content==null || content.isEmpty()) content = resolveIpFromTaobao(ip);
		//判断是否为空
		if(content==null || content.isEmpty()) return false;
		
		JSONObject root = JSONObject.fromObject(content);
		if("1".equals(root.get("code"))) return false;
		
		JSONObject ipResult  = root.getJSONObject("data");
		SmsLocation sl       = new SmsLocation();
		sl.setProvince(ipResult.getString("region"));
		sl.setCity(ipResult.getString("city"));
		sl.setOperator(ipResult.getString("isp"));
		
		//检查是否获得IP的省份跟城市
		if(StringUtils.isEmpty(sl.getProvince()) || StringUtils.isEmpty(sl.getCity())) return false;
		
		List<SmsChannel> channelList = smsChannelManager.getEntity(new SmsChannel(2,1));
		for(SmsChannel smsChannel : channelList){
			//检查请求支付金额是否与通道单价一致
			if(price*100!=smsChannel.getPrice()) continue;
			
			//查找规则
			SmsRule smsRule = smsRuleManager.getEntityByCity(new SmsRule(smsChannel.getId(),sl.getProvince(),sl.getCity()));
			if(smsRule!=null){
				//判断是否在允许发送时间范围内
				String hour = StringTools.getHourFormat().format(new Date());
				if(!smsRule.getTime().contains(hour)) continue;
				//判断运营商
				if(!StringUtils.isEmpty(sl.getOperator()) && sl.getOperator().contains("移动")){//移动
					if(!StringUtils.isEmpty(smsRule.getOptCmcc())) return true;
				}else if(!StringUtils.isEmpty(sl.getOperator()) && sl.getOperator().contains("联通")){//联通
					if(!StringUtils.isEmpty(smsRule.getOptUnicom())) return true;
				}else if(!StringUtils.isEmpty(sl.getOperator()) && sl.getOperator().contains("电信")){//电信
					if(!StringUtils.isEmpty(smsRule.getOptTelecom())) return true;
				}else{//三大运营商之外,默认当作移动
					if(!StringUtils.isEmpty(smsRule.getOptCmcc())) return true;
				}
			}
		}//end for
		return false;
	}
	*/
	/*
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
