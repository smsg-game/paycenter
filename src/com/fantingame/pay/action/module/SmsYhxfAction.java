package com.fantingame.pay.action.module;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.methods.HttpPost;
import org.apache.log4j.Logger;

import com.easou.common.api.Md5SignUtil;
import com.easou.common.util.StringUtil;
import com.fantingame.pay.action.common.BaseAction;
import com.fantingame.pay.entity.PayChannel;
import com.fantingame.pay.entity.PayEb;
import com.fantingame.pay.entity.PayTrade;
import com.fantingame.pay.entity.SmsLocation;
import com.fantingame.pay.entity.TradeCode;
import com.fantingame.pay.exception.EasouPayException;
import com.fantingame.pay.manager.SmsLocationManager;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.HttpUtil;
import com.fantingame.pay.utils.StringTools;

public class SmsYhxfAction extends BaseAction{
	private static final long serialVersionUID = -1936642025433531784L;
	private static Logger logger = Logger.getLogger(SmsYhxfAction.class);
	//in
	private String phoneNum;
	private String money;
	private String number;
	
	
	
	/**
	 * 盈华讯方-大额短信支付接口
	 */
	public String execute() throws Exception{
		try {
			if(StringTools.isPhoneNum(phoneNum) && StringTools.isNum(money)){
				//检测是否三个省份
				SmsLocationManager smsLocationManager = (SmsLocationManager)getBean("smsLocationManager");
				SmsLocation smsLocation = smsLocationManager.getEntityByMobile(phoneNum);
				payTrade = getTradeByInvoice(invoice);
				if(!StringUtil.isEmpty(smsLocation.getProvince()) && !"新疆".equals(smsLocation.getProvince()) && !"甘肃".equals(smsLocation.getProvince()) && !"宁夏".equals(smsLocation.getProvince())){
					//只有移动可以
					if(!StringUtil.isEmpty(smsLocation.getOperator()) && smsLocation.getOperator().contains("移动")){
						//查询PayTrade
						invoice = StringTools.getValue("invoice",invoice,32,true,false);
//						PayTrade payTrade = getTradeByInvoice(invoice);
						//检查订单是否存在
						if(payTrade==null) throw new EasouPayException(TradeCode.EASOU_CODE_140.code,TradeCode.EASOU_CODE_140.msgC);
						//检查订单是否已经被处理
						if(payTrade.getStatus()!=Constants.EASOU_SERVER_STATUS_CREATE) throw new EasouPayException(TradeCode.EASOU_CODE_180.code,TradeCode.EASOU_CODE_180.msgC);
						//检查短信金额是否能支付订单
						if(Double.valueOf(payTrade.getReqFee())>Double.valueOf(money)) throw new EasouPayException(TradeCode.EASOU_CODE_190.code,TradeCode.EASOU_CODE_190.msgC);
						PayEb payEb = newPayEbOrder(invoice, Constants.CHANNEL_ID_SMSYHXF,money,"手机号:"+phoneNum);
					    //看下缓存里有没有上次提交过的
					    String content = (String)getRequest().getSession().getAttribute("sms_yhxf_"+invoice);
					    if(StringUtil.isEmpty(content)){
						    PayChannel channel = getChannelById(Constants.CHANNEL_ID_SMSYHXF);	//获取支付商信息
						    Map<String,String> params = new HashMap<String, String>();
						    params.put("sp",channel.getPartnerId()+"");
						    params.put("od",payEb.getId()+"");
						    params.put("mz",payEb.getReqFee()+"");
						    params.put("spzdy",phoneNum);
						    params.put("mob",phoneNum);
						    params.put("uid",payEb.getEasouId());
						    params.put("spreq","http://service.pay.easou.com/");
						    params.put("spsuc","http://service.pay.easou.com/");
						    String signStr = params.get("sp")+ params.get("od") + channel.getSecretKey() + params.get("mz") + params.get("spreq") + params.get("spsuc") + params.get("mob"); 
						    params.put("md5",Md5SignUtil.sign(signStr,"").toUpperCase());
						    
						    HttpPost post = new HttpPost(channel.getOrderUrl());
						    content = HttpUtil.getPostContent(Constants.HTTPCLIENT, post, params, "UTF-8");
							if(StringUtil.isEmpty(content)){
							    content = HttpUtil.getPostContent(Constants.HTTPCLIENT, post, params, "UTF-8");
							}
							if(StringUtil.isEmpty(content)){
							    content = HttpUtil.getPostContent(Constants.HTTPCLIENT, post, params, "UTF-8");
							}
							if(!StringUtil.isEmpty(content)){
							    if(content.startsWith("yhxfsucc")){
							    	//存到session里
						    		getRequest().getSession().setAttribute("sms_yhxf_"+invoice,content);
						    		// success|内容|通道号
						    		fillResult("success",content.split("\\|")[1],content.split("\\|")[2]);
							    }else{
							    	// yhxffail|原因
							    	setMsg(content.split("\\|")[1]);
							    	logger.error("fail:"+content.split("\\|")[1]);
							    }
							 }else{
								 setStatus("fail");
								 setMsg("网络无响应");
							 }
					      }else{//缓存里有上次的结果
					    	  fillResult("success",content.split("\\|")[1],content.split("\\|")[2]);
						  }
					}else{
						setStatus("fail");
						setMsg("仅中国移动手机号支持短信支付");
					}
				}else{
					setStatus("fail");
					setMsg("该手机号地域不支持短信支付");
				}
			}else{
				setStatus("fail");
				setMsg("手机号或者金额有误");
			}
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
			setMsg(e.getMessage());
		}
		String isWeb = getRequest().getParameter("isWeb");
		if("1".equals(isWeb)) {
			if(status != null && "fail".equals(status)) {
				return ERROR;
			}
			return SUCCESS;
		}
		return JSON;
	}
	
	public String vp() {
		setStatus("success");
		if(!StringTools.isPhoneNum(phoneNum) || !StringTools.isNum(money)){
			fillResult("fail", "手机号或者金额有误", phoneNum);
			return JSON;
		}
		//检测是否三个省份
		SmsLocationManager smsLocationManager = (SmsLocationManager)getBean("smsLocationManager");
		SmsLocation smsLocation = null;
		try {
			smsLocation = smsLocationManager.getEntityByMobile(phoneNum);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		if(smsLocation == null) {
			fillResult("fail", "该手机号地域不支持短信支付", phoneNum);
		} else if(StringUtil.isEmpty(smsLocation.getProvince()) || "新疆".equals(smsLocation.getProvince()) 
				|| "甘肃".equals(smsLocation.getProvince()) || "宁夏".equals(smsLocation.getProvince())){
			fillResult("fail", "该手机号地域不支持短信支付", phoneNum);
		} else if(!StringUtil.isEmpty(smsLocation.getOperator()) && !smsLocation.getOperator().contains("移动")){
			fillResult("fail", "仅中国移动手机号支持短信支付", phoneNum);
		}
		return JSON;
	}
	
	/**
	 * 盈华讯方-大额短信充值接口
	 */
	public String smsCharge() throws Exception{
		//充值时，invoice是为空的，先生成订单获取invoice
		try {
			isCharge = true;
			String backUrl = (String)getRequest().getSession().getAttribute("charge_backurl");
			String money = getParam("money");
			String invoiceId=getParam(Constants.FIELD_INVOICE_ID);
			PayTrade trade = newChargeTrade(money,backUrl,"module",invoiceId);
			if(trade!=null) invoice = trade.getInvoice();
			//走支付流程
			return this.execute();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			setMsg(e.getMessage());
			setStatus("fail");
		}
		return JSON;
	}
	
	
	
	private void fillResult(String status,String msg,String number){
		this.status  = status;
		this.msg     = msg;
		this.number  = number;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
}
