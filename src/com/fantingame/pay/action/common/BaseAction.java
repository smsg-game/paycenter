package com.fantingame.pay.action.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.StrutsRequestWrapper;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.easou.common.util.StringUtil;
import com.fantingame.pay.entity.PayChannel;
import com.fantingame.pay.entity.PayEb;
import com.fantingame.pay.entity.PayOrder;
import com.fantingame.pay.entity.PayPartner;
import com.fantingame.pay.entity.PayTrade;
import com.fantingame.pay.entity.PayUserAccount;
import com.fantingame.pay.entity.TradeCode;
import com.fantingame.pay.entity.User;
import com.fantingame.pay.exception.EasouPayException;
import com.fantingame.pay.manager.PayChannelManager;
import com.fantingame.pay.manager.PayEbManager;
import com.fantingame.pay.manager.PayOrderManager;
import com.fantingame.pay.manager.PayPartnerManager;
import com.fantingame.pay.manager.PayTradeManager;
import com.fantingame.pay.manager.PayUserAccountManager;
import com.fantingame.pay.session.WapServletRequestWrapper;
import com.fantingame.pay.session.WapSession;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.StringTools;
import com.fantingame.pay.utils.bill99.Bill99Url;
import com.fantingame.pay.utils.yeepay.YeePayUtil;
import com.opensymphony.xwork2.ActionSupport;

public class BaseAction extends ActionSupport {
	
//	private static Logger logger = Logger.getLogger(BaseAction.class);

	private static final long serialVersionUID = 5137243520818995377L;
	
	public final static String REDIRECT    = "redirect";
	public final static String DISPATCHER  = "dispatcher";
	public final static String TRADE       = "trade";
	public final static String FAIL        = "fail";
	public final static String UNKNOW      = "unknow";
	public final static String JSON        = "json";
	public final static String CHARGE      = "charge";
	public final static String PAY_RESULT  = "pay_result";
	
	public String invoice;
	public String status;
	public String msg;
	public String redirectUrl;
	
	//ali
	public String code;
	public String type;

	public PayTrade payTrade;
	public boolean isCharge = false;
	
	public WapSession getWapSession() {
		StrutsRequestWrapper strutsRequest = (StrutsRequestWrapper) ServletActionContext.getRequest();
		WapServletRequestWrapper wapRequest = (WapServletRequestWrapper) strutsRequest.getRequest();
		return wapRequest.getWapSession();
	}

	public HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	public HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	public Object getBean(String beanName) {
		return WebApplicationContextUtils.getRequiredWebApplicationContext(ServletActionContext.getServletContext()).getBean(beanName);
	}

	public boolean isHttps() {
		return "https".equalsIgnoreCase(getRequest().getScheme());
	}
	
	public boolean isEasouHallApp(){
		return "AppEasouHall".equals(getRequest().getHeader("App-Agent"));
		
	}

	public String getParam(String key) {
		return getRequest().getParameter(key);
	}


	public User getUser() {
		return (User) getWapSession().getAttribute("user");
	}
	
	
	//根据传入的金额生成pay_trade订单并入库;
	protected PayTrade newChargeTrade(String money,String backUrl,String type,String invoiceId) throws Exception {
		//如果金额有问题，返回
		if(money==null || money.isEmpty() || !StringTools.isNum(money)||Double.valueOf(money)<0.01 || Double.valueOf(money)>=100000000){
			throw new EasouPayException(TradeCode.EASOU_CODE_190.code,TradeCode.EASOU_CODE_190.msgC+" <"+money+">");
		}
		PayTrade trade=null;
		String appId="100";   //设置应用和渠道的默认值
		String qn="1000";
		PayTradeManager payTradeManager = (PayTradeManager)getBean("payTradeManager");
		//首先判断,能否用传递过来的订单号获取到游戏币订单
		if(!StringUtil.isEmpty(invoiceId))
		trade=payTradeManager.getEntityByInvoice(invoiceId);
		if(trade!=null){
			appId=trade.getAppId();
			qn=trade.getQn();
		}else{
		//然后从session中取	
			WapSession sess=getWapSession();
			if(sess!=null){
				Object appIdObj=sess.getAttribute("appId");
				Object qnObj=sess.getAttribute("qn");
				if(appIdObj!=null)
					appId=appIdObj.toString();
				if(qnObj!=null)
					qn=qnObj.toString();
			}
			}
		PayTrade payTrade = new PayTrade();
		payTrade.setCreateDatetime(new Date());
		payTrade.setPartnerId(Constants.PARTNER_ID_SELF);
		payTrade.setAppId(appId);
		payTrade.setSeparable(0);
		payTrade.setPayerId(getUser().getId().toString());
		payTrade.setEasouId(getUser().getId().toString());
		payTrade.setReqFee(money);
		payTrade.setCurrency("CNY");
		payTrade.setNotifyUrl("");//充值不需要通知
		payTrade.setNotifyStatus(0);
		payTrade.setStatus(Constants.EASOU_SERVER_STATUS_CREATE);
		payTrade.setQn(qn);
		payTrade.setCookieQn(getCookieByName(Constants.EASOU_COOKIE_QN_NAME));
		payTrade.setTradeDesc("用户X币充值");
		payTrade.setTradeName("用户X币充值");
		String invoice = UUID.randomUUID().toString().replace("-", "");
		payTrade.setTradeId(getUser().getId()+""+System.currentTimeMillis());//以时间作为订单ID
		payTrade.setInvoice(invoice);
		payTrade.setRedirectUrl(StringUtil.isEmpty(backUrl)?getBaseUrl()+"/"+type+"/charge.e":backUrl);//E币充值回到指定的backUrl，如果为空，回到充值页面
		payTradeManager.save(payTrade);
		return payTrade;
	}
	
	

	
	
	/*创建E币充值订单，修改订单金额*/
	protected PayEb newPayEbOrder(String invoice, Long channelId,String reqFee,String otherInfo) throws Exception{
		User user = (User) getWapSession().getAttribute("user");
		//获取梵町订单
		PayTrade payTrade = getTradeByInvoice(invoice);
		if(payTrade==null){//订单不存在
			throw new EasouPayException(TradeCode.EASOU_CODE_140.code,TradeCode.EASOU_CODE_140.msgC);
		}
		//获取支付渠道信息
		PayChannel channel = getChannelById(channelId);
		PayEbManager payEbManager = (PayEbManager)getBean("payEbManager");
		
		//支付的时候
		if(!isCharge){
		   if (Double.valueOf(reqFee).doubleValue() * channel.getRate().doubleValue() < Double.valueOf(payTrade.getReqFee()).doubleValue()) throw new Exception(TradeCode.EASOU_CODE_190.msgC);
		}
		//保存PayEb订单
		PayEb payEb = payEbManager.getEntityByInvoice(invoice);
		if(payEb==null){//系统不存在此订单
			payEb = new PayEb();
			payEb.setEasouId(user.getId().toString());
			payEb.setChannelId(channel.getId());
			payEb.setRate(channel.getRate());
			payEb.setReqFee(reqFee == null ? payTrade.getReqFee() : reqFee);  //传入金额，使用传入的金额，否则，直接使用下单时的金额
			payEb.setReqCurrency("CNY");
			payEb.setInvoice(payTrade.getInvoice());
			payEb.setStatus(Constants.EASOU_SERVER_STATUS_CREATE);
			payEb.setCreateDatetime(new Date());
			payEb.setOtherInfo(otherInfo);
			payEbManager.save(payEb);
			return payEb;
		}else if(payEb.getStatus()==Constants.EASOU_SERVER_STATUS_CREATE){//已存在此订单，但是没交易成功，则更新支付方式
			payEb.setReqFee(reqFee == null ? payTrade.getReqFee() : reqFee);  //传入金额，使用传入的金额，否则，直接使用下单时的金额
			payEb.setChannelId(channel.getId());
			payEb.setRate(channel.getRate());
			payEb.setOtherInfo(otherInfo);
			payEbManager.update(payEb);
			return payEb;
		}else if(payEb.getStatus()!=Constants.EASOU_SERVER_STATUS_CREATE){//订单已经被处理过,当作失效
			throw new EasouPayException(TradeCode.EASOU_CODE_180.code,TradeCode.EASOU_CODE_180.msgC);
		}else {
			return null;
		}
	}
	
	/*创建E币充值订单，修改订单金额*/
	protected PayEb newEbOrder(Long channelId,String reqFee,String otherInfo) throws Exception{
		User user = (User) getWapSession().getAttribute("user");
		//获取支付渠道信息
		PayChannel channel = getChannelById(channelId);
		PayEbManager payEbManager = (PayEbManager)getBean("payEbManager");
		//支付的时候
		if(!isCharge){
		   if (Double.valueOf(reqFee).doubleValue() * channel.getRate().doubleValue() < Double.valueOf(reqFee).doubleValue()) throw new Exception(TradeCode.EASOU_CODE_190.msgC);
		}
		Date currentDate = new Date();
		Calendar calendar = Calendar.getInstance();
		String today = calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH)+" 00:00:00";
		PayEb payEb = new PayEb();
		payEb.setEasouId(user.getId().toString());
		payEb.setChannelId(channel.getId());
		payEb.setRate(channel.getRate());
		payEb.setReqFee(reqFee == null ? payTrade.getReqFee() : reqFee);  //传入金额，使用传入的金额，否则，直接使用下单时的金额
		payEb.setReqCurrency("CNY");
		payEb.setInvoice(null);
		payEb.setStatus(Constants.EASOU_SERVER_STATUS_CREATE);
		payEb.setCreateDatetime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(today));
		payEb.setOtherInfo(otherInfo);
		List<PayEb> payEbs = payEbManager.getEntityByCondition(payEb);
		if(payEbs == null || payEbs.isEmpty()){
			payEb.setCreateDatetime(currentDate);
			payEbManager.save(payEb);
		}else{
			payEb = payEbs.get(0);
		}
		return payEb;
	}
	
	//找不到对应的渠道，抛异常
	protected PayChannel getChannelById(long channelId) throws Exception{
		PayChannelManager payChannelManager = (PayChannelManager)getBean("payChannelManager");
		return payChannelManager.getEntityById(channelId);
	}
	
	//不抛异常
	protected PayTrade getTradeByInvoice(String invoice) {
		PayTradeManager payTradeManager = (PayTradeManager)getBean("payTradeManager");
		return payTradeManager.getEntityByInvoice(invoice);
	}
	
	//不抛异常
	protected PayUserAccount getUserAccountById(Long userId) {
		PayUserAccountManager payUserAccountManager = (PayUserAccountManager)getBean("payUserAccountManager");
		return payUserAccountManager.getUserAcountByJuserId(userId);
	}
	
	//不抛异常
	protected PayOrder getOrderByInvoice(String invoice) {
		PayOrderManager payOrderManager = (PayOrderManager)getBean("payOrderManager");
		return payOrderManager.getEntityByInvoice(invoice);
	}
	
	//不抛异常
	protected PayEb getEbOrderByInvoice(String invoice) {
		PayEbManager payEbManager = (PayEbManager)getBean("payEbManager");
		return payEbManager.getEntityByInvoice(invoice);
	}
	
	//找不到对应的合作商，抛异常
	protected PayPartner getPartnerById(Long partnerId) throws Exception {
		PayPartnerManager partnerManager = (PayPartnerManager)getBean("payPartnerManager");
		return partnerManager.getEntityById(partnerId);
	}

	/*获取账户E币余额*/
	public Long getEb() {
		Long userId = 0L;
		User user = getUser();
		if(null ==user) return 0L;
		userId = user.getId();
		PayUserAccountManager mgr = (PayUserAccountManager) getBean("payUserAccountManager");
		if (userId != null) {
			PayUserAccount acount = mgr.getUserAcountByJuserId(userId);
			if(null != acount) return acount.getFee();
		}
		return 0L;
	}
	
	
	/**
	 * 使用卡类支付订单，从request里面取参数，参数名称必须按代码所约定；
	 * @param result 存储支付结果，存放两对值 result ， msg
	 * @param invoice 对应订单订单号
	 * @throws Exception
	 */
	protected void payByCard(BaseAction action,String invoice,String otherInfo) throws Exception {
		String channelType    = StringTools.getValue("channelType",getParam("channelType"),8,true,true);
		String cardAmt = StringTools.getValue("cardAmt",getParam("cardAmt"),8,true,false);
		String cardNumber  = StringTools.getValue("cardNumber",getParam("cardNumber"),32,true,false);
		String cardPwd     = StringTools.getValue("cardPwd",getParam("cardPwd"),32,true,false);
		StringTools.getValue("invoice",invoice,32,true,false);
		payByCard(action,channelType, cardAmt, cardNumber, cardPwd, invoice,otherInfo);
	}
	
	
	
	protected String getCookieByName(String name) {
		Cookie[] cookies = getRequest().getCookies();
		for(Cookie cookie : cookies){
			if(name.equals(cookie.getName())){
				return cookie.getValue();
			}
		}
		return null;
	}

	
	
	/**
	 * 使用卡类支付订单
	 * @param result	存储支付结果，存放两对值 result ， msg
	 * @param channelType 卡类型
	 * @param cardAmt	充值卡金额
	 * @param cardNumber	充值卡序列号
	 * @param cardPwd	充值卡密码
	 * @param invoice	对应订单订单号
	 * @throws Exception
	 */
	protected void payByCard(BaseAction action, String channelType, String cardAmt,String cardNumber, String cardPwd, String invoice,String otherInfo) throws Exception {
		PayChannelManager channelManager = (PayChannelManager)getBean("payChannelManager");
		PayChannel channel = channelManager.getChannelByType(channelType);
		PayEb payEb = newPayEbOrder(invoice , channel.getId(),cardAmt,otherInfo); 
		PayTrade payTrade = getTradeByInvoice(invoice);
		if(Constants.CHANNEL_ID_YEEPAY == channel.getParentId()){//易宝-手机卡支付
			YeePayUtil.getYeePayResult(action, payTrade, payEb, channel,  cardAmt, cardNumber, cardPwd);
		}else if(Constants.CHANNEL_ID_99BILL == channel.getParentId()){
			Bill99Url.bill99Resopnse(action, payTrade, payEb, channel, cardAmt, cardNumber, cardPwd);
		}
	}

	public String getFullUrl() {
		return  getRequest().getScheme() + "://"+getRequest().getServerName()+getRequest().getServletPath();
	}
	public String getBaseUrl() {
		return getRequest().getScheme() + "://"+getRequest().getServerName();
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public PayTrade getPayTrade() {
		return payTrade;
	}

	public void setPayTrade(PayTrade payTrade) {
		this.payTrade = payTrade;
	}

}
