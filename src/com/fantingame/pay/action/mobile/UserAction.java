package com.fantingame.pay.action.mobile;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.easou.common.api.Md5SignUtil;
import com.easou.common.util.StringUtil;
import com.fantingame.pay.action.common.BaseAction;
import com.fantingame.pay.entity.PayEb;
import com.fantingame.pay.entity.PayPartner;
import com.fantingame.pay.entity.PayTrade;
import com.fantingame.pay.entity.PayUserAccount;
import com.fantingame.pay.entity.TradeCode;
import com.fantingame.pay.entity.User;
import com.fantingame.pay.exception.EasouPayException;
import com.fantingame.pay.job.DelayItem;
import com.fantingame.pay.manager.PayEbManager;
import com.fantingame.pay.manager.PayPartnerManager;
import com.fantingame.pay.manager.PayTradeManager;
import com.fantingame.pay.manager.PayUserAccountManager;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.EbTools;
import com.fantingame.pay.utils.PageUtil;
import com.fantingame.pay.utils.StringTools;

public class UserAction extends BaseAction {

	/**
	 * 注释内容
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(UserAction.class);
	private PageUtil dealflows;//交易流水记录
	private String ebNum = "0";
	
	public String userDetail() {
		int page = StringTools.strToInt(getParam("page"), 1);
		int size = StringTools.strToInt(getParam("size"), 3);
		User user = getUser();
		PayEbManager manager = (PayEbManager) getBean("payEbManager");
		dealflows = manager.getTradeHistoryByUserId(page, size, user.getId());
		return SUCCESS;
	}
	
	/*查询账户E币余额*/
	public String userEb(){
		try{
			String partnerId = getParam("partnerId");
			String userId = getParam("userId");
			String sign = getParam("sign");
			
		   if(StringUtil.isEmpty(partnerId) || StringUtil.isEmpty(userId)){
			   return SUCCESS;
		   }
		   PayPartnerManager payPartnerManager = (PayPartnerManager) getBean("payPartnerManager");
		   PayPartner payPartner = payPartnerManager.getEntityById(Long.valueOf(partnerId));
		   if(payPartner!=null){
		       //验签
			   Map<String,String> treeMap = new TreeMap<String, String>();
			   treeMap.put("partnerId", partnerId);
			   treeMap.put("userId", userId);
		       if(Md5SignUtil.doCheck(treeMap,sign,payPartner.getSecretKey())){
		    	   PayUserAccountManager payUserAccountManager = (PayUserAccountManager) getBean("payUserAccountManager");
				   PayUserAccount account = payUserAccountManager.getUserAcountByJuserId(Long.valueOf(getParam("userId")));
				   if(account!=null){
					   ebNum = account.getFee()+"";
				   }
		       }
		   }
		   return SUCCESS;
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return SUCCESS;
	}
	
	/*扣除账户E币，书城划扣接口
	 * status  1-成功
	 *         -1-交易失败
	 *         -2-验签失败
	 *         -3-订单金额有误
	 *         -4-余额额不足
	 *         -5-订单重复处理
	 *         -6-参数不符合规则，为空，或者长度超限制，或者类型不对
	 */
	public String tradeByEbForBookMall() throws Exception{
		PayEb payEb                = null;
		PayUserAccount userAccount = null;
		try{
			//获取参数
			String payerId  = StringTools.getValue(Constants.FIELD_PAYER_ID,getParam(Constants.FIELD_PAYER_ID),16,true,true);
			String tradeId = StringTools.getValue(Constants.FIELD_TRADE_ID,getParam(Constants.FIELD_TRADE_ID),64,true,false);
			String reqFee  = StringTools.getValue(Constants.FIELD_REQ_FEE,getParam(Constants.FIELD_REQ_FEE),10,true,true);
			String sign    = StringTools.getValue(Constants.FIELD_SIGN,getParam(Constants.FIELD_SIGN),32,true,false);
			//验签
			if(!Md5SignUtil.doCheck("payerId="+payerId+"&reqFee="+reqFee+"&tradeId="+tradeId,sign,"8C97398507CCD24342C835D7AB09077D")){//验签
				getResponse().getWriter().write("-2#0");
				return null;
			}
			//订单金额小于0.01的,大于1亿，报错
			if(Double.valueOf(reqFee)<0.01d || Double.valueOf(reqFee)>=100000000d){
				getResponse().getWriter().write("-3#0");
				return null;
			}
			
			// 查询用户是否存在
			PayUserAccountManager payUserAccountManager = (PayUserAccountManager)getBean("payUserAccountManager");
			userAccount = payUserAccountManager.getUserAcountByJuserId(Long.valueOf(payerId));
			// 账户不存在 则开户
			if (null == userAccount) {
				userAccount = new PayUserAccount();
				userAccount.setJuserId(Long.valueOf(payerId));
				userAccount.setUsername("es."+payerId);
				userAccount.setFee(0L);
				payUserAccountManager.save(userAccount);
			}
            
			//比对余额，如果余额不足，直接走人
			if(userAccount.getFee()<Double.valueOf(getParam(Constants.FIELD_REQ_FEE))*100){
				getResponse().getWriter().write("-4#"+userAccount.getFee());
				return null;
			}
			
			//生成订单
			PayTradeManager payTradeManager = (PayTradeManager)getBean("payTradeManager");
			PayEbManager payEbManager = (PayEbManager)getBean("payEbManager");
			
			//查一下订单是否存在
			PayTrade payTrade = payTradeManager.getByPartnerIdAndTradeId(new PayTrade(Constants.PARTNER_ID_BOOK_MALL,tradeId));
			
			if(payTrade==null){
				payTrade = new PayTrade();
				payTrade.setCreateDatetime(new Date());
				payTrade.setAppId("101");
				payTrade.setSeparable(0);
				payTrade.setPayerId(payerId);
				payTrade.setEasouId(payerId);
				payTrade.setReqFee(reqFee);
				payTrade.setCurrency("CNY");
				payTrade.setExtInfo("");
				payTrade.setNotifyUrl("http://shu.easou.com:8080/receiveYft");
				payTrade.setPartnerId(Constants.PARTNER_ID_BOOK_MALL);
				payTrade.setQn("1000");
				payTrade.setStatus(Constants.EASOU_SERVER_STATUS_CREATE);
				payTrade.setTradeDesc("梵町书城E币划扣");
				payTrade.setTradeId(tradeId);
				payTrade.setTradeName("梵町书城E币划扣");
				payTrade.setInvoice(UUID.randomUUID().toString().replace("-", ""));
				payTrade.setRedirectUrl("");
					
				payEb = new PayEb();
				payEb.setEasouId(payerId);
				payEb.setChannelId(Constants.CHANNEL_ID_EB);
				payEb.setRate(1.0d);
				payEb.setReqFee(payTrade.getReqFee());  //传入金额，使用传入的金额，否则，直接使用下单时的金额
				payEb.setReqCurrency("CNY");
				payEb.setInvoice(payTrade.getInvoice());
				payEb.setStatus(Constants.EASOU_SERVER_STATUS_CREATE);
				payEb.setCreateDatetime(new Date());
			}else if(payTrade.getStatus()==0){
				payEb = payEbManager.getEntityByInvoice(payTrade.getInvoice());
			}else{
				getResponse().getWriter().write("-5#0");
				return null;
			}
			
			//更新账户余额，插入pay_trade和pay_eb，notify
			int result = payEbManager.updEbForBookMall(payTrade, payEb);
			if(result==1){
			   getResponse().getWriter().write("1#"+(userAccount.getFee()-EbTools.rmbToEb(payEb.getPaidFee())));
			   return null;
			}
		}catch (EasouPayException e) {
			logger.error(e.getMessage(),e);
			getResponse().getWriter().write("-6#0");
			return null;
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		getResponse().getWriter().write("-1#0");
		return null;
	}
	

	public PageUtil getDealflows() {
		return dealflows;
	}

	public void setDealflows(PageUtil dealflows) {
		this.dealflows = dealflows;
	}


	public String getEbNum() {
		return ebNum;
	}


	public void setEbNum(String ebNum) {
		this.ebNum = ebNum;
	}
	
}
