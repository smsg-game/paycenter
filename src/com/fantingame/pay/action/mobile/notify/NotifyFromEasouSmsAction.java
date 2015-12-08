package com.fantingame.pay.action.mobile.notify;

import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fantingame.pay.entity.PayEb;
import com.fantingame.pay.entity.PayTrade;
import com.fantingame.pay.entity.SmsChannel;
import com.fantingame.pay.entity.SmsSended;
import com.fantingame.pay.exception.EasouPayException;
import com.fantingame.pay.manager.PayEbManager;
import com.fantingame.pay.manager.PayTradeManager;
import com.fantingame.pay.manager.PayUserAccountManager;
import com.fantingame.pay.manager.PayUserBindHistoryManager;
import com.fantingame.pay.manager.SmsChannelManager;
import com.fantingame.pay.manager.SmsSendedManager;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.StringTools;

public class NotifyFromEasouSmsAction extends BaseReceiveNotifyAction {
	private static final long serialVersionUID = -4885276354103619075L;
	private static final Logger logger = Logger.getLogger(NotifyFromEasouSmsAction.class);
	SmsSendedManager  smsSendedManager                  = (SmsSendedManager)getBean("smsSendedManager");
	SmsChannelManager  smsChannelManager                = (SmsChannelManager)getBean("smsChannelManager");
	PayEbManager      payEbManager                      = (PayEbManager)getBean("payEbManager");
	PayTradeManager   payTradeManager                   = (PayTradeManager)getBean("payTradeManager");
	PayUserAccountManager   payUserAccountManager       = (PayUserAccountManager)getBean("payUserAccountManager");
	PayUserBindHistoryManager payUserBindHistoryManager = (PayUserBindHistoryManager) getBean("payUserBindHistoryManager");
	 
	public String execute() throws Exception {
		getRequest().setCharacterEncoding("UTF-8");
		PrintWriter out = getResponse().getWriter();
		try {
			
		   logger.info("queryString:"+getRequest().getQueryString());
			
		   //获取参数 
		   String id = StringTools.getValue("id",getParam("id"),20,true,true);
		   //短信通道网关标示
		   String gateway = StringTools.getValue("gateway",getParam("gateway"),20,true,true);
		   //运营商扣费标示
		   String linkid = getParam("linkid");
		   String sid = getParam("sid");
		   
		   if(!StringUtils.isEmpty(linkid)){//MO
			   //手机用户上行内容(URLENCODER的UTF-8编码)
			   String msg = StringTools.getValue("msg",getParam("msg"),256,true,false);
			   
			   msg = URLDecoder.decode(msg,"UTF-8");
			   //手机用户号码
			   String omobile = StringTools.getValue("omobile",getParam("omobile"),256,true,true);
			   //长号码
			   String dmobile = StringTools.getValue("dmobile",getParam("dmobile"),256,true,true);
			   //分配渠道的唯一标示
			   String cp = getParam("cp");
			   //分配给渠道的业务
			   String stype = getParam("stype");
			   //数据类型,预留
			   String mtype = getParam("mtype");
			   //指令类型，1：点播  2：订购  3：退订  4：彩信
			   String action = getParam("action");
			   //业务类型，1：点播业务  2：包月业务  4：彩信业务
			   String servicetype = getParam("servicetype");
			   
			   //查找对应订单，msg的后四位
			   String subId = msg.substring(msg.length()-4);
			   PayEb payEb = payEbManager.getEntityBySubId(Integer.valueOf(subId));
			   if(payEb==null){
				   throw new Exception("订单号不存在....[子订单号]:"+subId);
			   }
			   SmsSended smsSended = new SmsSended();
			   smsSended.setMoId(id);
			   smsSended.setGateway(gateway);
			   smsSended.setLinkId(linkid);
			   smsSended.setMsg(msg);
			   smsSended.setMobile(Long.valueOf(omobile));
			   smsSended.setDmobile(dmobile);
			   smsSended.setCp(cp);
			   smsSended.setStype(stype);
			   smsSended.setMtype(mtype);
			   smsSended.setAction(action);
			   smsSended.setServicetype(servicetype);
			   smsSended.setCreateDatetime(new Date());
			   
			   //根据长号跟cp号来确定通道ID
			   if("10669538".equals(dmobile) && "199".equals(cp)){
				   smsSended.setChannelId(1L);
			   }else{
				   smsSended.setChannelId(0L);
			   }
			   smsSended.setOrderId(payEb.getId());
			   int result = smsSendedManager.updateMo(smsSended);
			   if(result==0){//还没有MR记录
				   smsSendedManager.save(smsSended);
			   }else{
				   //更新payEB
				   updatePayStatus(linkid,omobile);
			   }
		   }else if(!StringUtils.isEmpty(sid)){//MR
			   String status = StringTools.getValue("status",getParam("status"),32,true,false);
			   String mobile = StringTools.getValue("mobile",getParam("mobile"),11,true,true);
			   SmsSended smsSended = new SmsSended();
			   smsSended.setStatus(status);
			   smsSended.setMobile(Long.valueOf(mobile));
			   smsSended.setLinkId(sid);
			   smsSended.setMrId(id);
			   smsSended.setCreateDatetime(new Date());
			   int result = smsSendedManager.updateMr(smsSended);
			   if(result==0){//还没有MO记录
				   smsSendedManager.save(smsSended);
			   }else{
				  //更新payEB
				  updatePayStatus(sid,mobile);
			   }
		   }else{
			   out.print("被忽悠了.....");
			   return null;
		   }
		   //回复D哥那边
		   out.print("0");
		}catch (SQLException e) {
			//有主键重复的错误是正常的，因为MO,MR会同时写入，但没关系
			//通道那边会重新发送
			logger.error(e.getMessage());
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	private void updatePayStatus(String linkId,String mobile) throws Exception{
		 //如果MO,MR数据还没整合
		 SmsSended smsSended   = smsSendedManager.getEntityByLinkId(linkId);
		 if(StringUtils.isEmpty(smsSended.getMoId()) || StringUtils.isEmpty(smsSended.getMrId()) || StringUtils.isEmpty(smsSended.getStatus())) return;
		 
		 
		 SmsChannel smsChannel = smsChannelManager.getEntityById(smsSended.getChannelId());
		 PayEb payEb = payEbManager.getEntityById(smsSended.getOrderId());
		 if(payEb==null) throw new EasouPayException("-1","支付订单号不存在");
		 PayTrade payTrade = payTradeManager.getEntityByInvoice(payEb.getInvoice());
		 if(payTrade==null) throw new EasouPayException("-1","交易订单号不存在");
		   
		 //状态为‘创建’，意思是没修改过状态
		 if(payTrade.getStatus()==Constants.EASOU_SERVER_STATUS_CREATE && payEb.getStatus()==Constants.EASOU_SERVER_STATUS_CREATE){
			 if("success".equals(smsSended.getStatus())){//支付成功
				    payTrade.setStatus(1);
				    payTrade.setPaidFee(StringTools.getNumFormat_2().format(Float.valueOf(smsChannel.getPrice()/100)));
				    payTrade.setSuccessDatetime(new Date());
				    payEb.setStatus(1);
				    payEb.setPaidCurrency("CNY");
				    payEb.setPaidFee(payTrade.getPaidFee());
				    payEb.setSuccessDatetime(payTrade.getSuccessDatetime());
				    //帐号绑定手机号
				    //if(bindHistory==null){//没送过
				    	//绑定帐号
				    	//RequestInfo ri = new RequestInfo();
				    	//ri.setSource("33");
				    	//发送到用户中心，绑定手机号
				    	//EucApiResult<JUser> apiResult = EucInternalApiCall.directlyBindMobileString(payUserAccount.getJuserId(),smsSended.getMobile()+"", ri);
				    	//if(CodeConstant.OK.equals(apiResult.getResultCode())){
				    	   //本地绑定手机号,并赠送100E币
				    	   //payUserAccountManager.updGiftForBindMobile(payUserAccount.getJuserId(),smsSended.getMobile()+"");
				    	//}
				    //}
			 }else{
				    payTrade.setStatus(-1);
				    payEb.setStatus(-1);
			 }
			 smsSended.setFee(smsChannel.getPrice());
			 smsSendedManager.update(smsSended);
			 //更新流水
			 payEbManager.updAfterRecNotify(payTrade, payEb);
		 }
	}

}
