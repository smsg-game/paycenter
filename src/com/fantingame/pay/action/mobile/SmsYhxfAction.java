package com.fantingame.pay.action.mobile;

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
	protected String phoneNum  = null;
	protected String money     = null;
	protected String number    = null;
	
	public String execute() throws Exception{
		try {
			
			if(StringTools.isPhoneNum(phoneNum) && StringTools.isNum(money)){
				//检测是否三个省份
				SmsLocationManager smsLocationManager = (SmsLocationManager)getBean("smsLocationManager");
				SmsLocation smsLocation = smsLocationManager.getEntityByMobile(phoneNum);
				if(!StringUtil.isEmpty(smsLocation.getProvince()) && !"新疆".equals(smsLocation.getProvince()) && !"甘肃".equals(smsLocation.getProvince()) && !"宁夏".equals(smsLocation.getProvince())){
					//只有移动可以
					if(!StringUtil.isEmpty(smsLocation.getOperator()) && smsLocation.getOperator().contains("移动")){
						//查询PayTrade
						invoice = StringTools.getValue("invoice",invoice,32,true,false);
						PayTrade payTrade = getTradeByInvoice(invoice);
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
						    		fillResult("success",content.split("\\|")[1],content.split("\\|")[2]);
						    	}else{
						    		fillResult("fail",content.split("\\|")[1],"");
						    		logger.error("fail:"+content.split("\\|")[1]);
						    	}
						    }else{//content拿不到值
						    	fillResult("fail","网络无响应","");
						    }
					    }else{//缓存里有上次的结果
					    	fillResult("success",content.split("\\|")[1],content.split("\\|")[2]);
					    }
					}else{
						fillResult("fail","仅中国移动手机号支持短信支付","");
					}
				}else{
					fillResult("fail","该手机号地域不支持短信支付","");
				}
			}else{
				fillResult("fail","手机号或者金额有误","");
			}
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
			fillResult("fail",e.getMessage(),"");
		}
		return JSON;
	}
	
	
	//smsCharge
	public String smsCharge(){
	   try {
		    isCharge = true;
		    String backUrl = (String)getRequest().getSession().getAttribute("charge_backurl");
			String invoiceId=getParam(Constants.FIELD_INVOICE_ID);
			PayTrade trade = newChargeTrade(money,backUrl,"mobile",invoiceId);
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
		this.msg = msg;
		this.number = number;
	}
	
	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
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


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getMsg() {
		return msg;
	}


	public void setMsg(String msg) {
		this.msg = msg;
	}


	public String getNumber() {
		return number;
	}


	public void setNumber(String number) {
		this.number = number;
	}
	
}
