package com.fantingame.pay.action.mobile.notify;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.easou.common.json.JsonUtil;
import com.fantingame.pay.action.ecenter.MyCardAction;
import com.fantingame.pay.action.ecenter.vo.MyCardApiPayment;
import com.fantingame.pay.action.ecenter.vo.SystemMyCardCardPayment;
import com.fantingame.pay.entity.PayChannel;
import com.fantingame.pay.entity.PayEb;
import com.fantingame.pay.manager.PayEbManager;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.RequestUtils;


/**
 * 支付宝通知接口,主要处理支付宝发送过来的对订单支付结果
 * */

public class NotifyFromMyCardAction extends BaseReceiveNotifyAction {

	private static final long serialVersionUID = 5490792191825343426L;
	private static final Logger logger = Logger.getLogger(NotifyFromMyCardAction.class);
	/**
	 * 提供给mycard官方查询订单接口（SDK）
	 * @return
	 * @throws Exception
	 */
	public String mycardSdkService() throws Exception{
		
	    getResponse().setContentType("text/html;charset=UTF-8");
		PrintWriter out = getResponse().getWriter();
		HttpServletRequest req = this.getRequest();
		String startDateStr = req.getParameter("StartDate");
		String endDateStr = req.getParameter("EndDate");
		String mycardNo = req.getParameter("MyCardID");
		
		Date startDate = null;
		Date endDate = null;
		
		if (!StringUtils.isBlank(startDateStr)) {
			startDate = stringToDate(startDateStr + " 00:00:00", "yyyy/MM/dd HH:mm:ss");
		}
		if (!StringUtils.isBlank(endDateStr)) {
			endDate = stringToDate(endDateStr + " 23:59:59", "yyyy/MM/dd HH:mm:ss");
		}
		
		logger.info("查询条件为:MyCardID=["+mycardNo+"],StartDate=["+startDateStr+"],EndDate=["+endDateStr+"]");
		
		PayEbManager payEbManager = (PayEbManager)getBean("payEbManager");
		
		List<PayEb> list = payEbManager.getMyCardLogService(startDate, endDate, mycardNo,"2");
		
		StringBuffer sb = new StringBuffer();
		if(list!=null&&list.size()>0){
			for(PayEb payEb:list){
				String otherInfo = payEb.getOtherInfo();
				String[] array = otherInfo.split(",");
				String PaymentType = array[0];
				
				sb.append(PaymentType);sb.append(",");
				
				String TradeSeq = payEb.getTradeNo();
				
				sb.append(TradeSeq);sb.append(",");
				
				String MyCardTradeNo = payEb.getReceiveMsg();
				
				sb.append(MyCardTradeNo);sb.append(",");
				
				String FacTradeSeq = payEb.getId()+"";
				
				sb.append(FacTradeSeq);sb.append(",");
				
				String CustomerId = array[1];
				
				sb.append(CustomerId);sb.append(",");
				
				String Amount = payEb.getReqFee();
				
				sb.append(Amount);sb.append(",");
				
				String Currency = "TWD";
				
				sb.append(Currency);sb.append(",");
				
				SimpleDateFormat oFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String TradeDateTime = oFormatter.format(payEb.getSuccessDatetime());
				
				sb.append(TradeDateTime);sb.append("<BR>");
				
			}
		}
		out.print(sb.toString());
		return null;
	}
	
	
	/**
	 * 提供给mycard官方查询订单接口（SDK）
	 * @return
	 * @throws Exception
	 */
	public String mycardApiService() throws Exception{
	    getResponse().setContentType("text/html;charset=UTF-8");
		PrintWriter out = getResponse().getWriter();
		HttpServletRequest req = this.getRequest();
		String startDateStr = req.getParameter("StartDate");
		String endDateStr = req.getParameter("EndDate");
		String mycardNo = req.getParameter("MyCardID");
		Date startDate = null;
		Date endDate = null;
		if (!StringUtils.isBlank(startDateStr)) {
			startDate = stringToDate(startDateStr + " 00:00:00", "yyyy/MM/dd HH:mm:ss");
		}
		if (!StringUtils.isBlank(endDateStr)) {
			endDate = stringToDate(endDateStr + " 23:59:59", "yyyy/MM/dd HH:mm:ss");
		}
		logger.info("查询条件为:MyCardID=["+mycardNo+"],StartDate=["+startDateStr+"],EndDate=["+endDateStr+"]");
		PayEbManager payEbManager = (PayEbManager)getBean("payEbManager");
		List<PayEb> list = payEbManager.getMyCardLogService(startDate, endDate, mycardNo,"1");
		StringBuffer sb = new StringBuffer();
		if(list!=null&&list.size()>0){
			for(PayEb payEb:list){
				String cardId = payEb.getReceiveMsg();
				sb.append(cardId);sb.append(",");
				String receiveNo = payEb.getReceiveStatus();
				String facMem = receiveNo.split(",")[0];
				sb.append(facMem);sb.append(",");
				sb.append(payEb.getTradeNo());sb.append(",");
				sb.append(payEb.getId()+"");sb.append(",");
				SimpleDateFormat oFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String TradeDateTime = oFormatter.format(payEb.getSuccessDatetime());
				sb.append(TradeDateTime);sb.append("<BR>");
			}
		}
		out.print(sb.toString());
		return null;
	}
	/**
	 * Convert string to Date
	 * 
	 * @return a java.util.Date object converted.
	 */
	public static java.util.Date stringToDate(String pstrValue, String pstrDateFormat) {
		if ((pstrValue == null) || (pstrValue.equals(""))) {
			return null;
		}
		java.util.Date dttDate = null;
		try {
			SimpleDateFormat oFormatter = new SimpleDateFormat(pstrDateFormat);
			dttDate = oFormatter.parse(pstrValue);
			oFormatter = null;
		} catch (Exception e) {
			return null;
		}

		return dttDate;
	}
	 /**
	  * mycard点卡支付 回调
	 * @throws Exception 
	  */
	 public void mycardAPIPayment() throws Exception{
		 String requestData = RequestUtils.getRequestData(this.getRequest());
		 logger.info("MyCard web-site 点卡支付回调请求数据:getQueryString= " + this.getRequest().getQueryString()+",requestData="+requestData);
//		 Map<String, String> map = RequestUtils.parseRequestString(requestData);
		 HttpServletRequest req = this.getRequest();
		 MyCardApiPayment payment = new MyCardApiPayment();
		 payment.setCardId(req.getParameter("CardId"));
		 payment.setCardKind(req.getParameter("CardKind"));
		 payment.setCardPoint(Integer.valueOf(req.getParameter("CardPoint")));
		 payment.setErrorMsg(req.getParameter("ErrorMsg"));
		 payment.setErrorMsgNo(req.getParameter("ErrorMsgNo"));
		 payment.setFacId(req.getParameter("facId"));
		 payment.setFacMemId(req.getParameter("facMemId"));
		 payment.setFacTradeSeq(req.getParameter("facTradeSeq"));
		 payment.setHash(req.getParameter("hash"));
		 payment.setOProjNo(req.getParameter("oProjNo"));
		 payment.setReturnMsgNo(req.getParameter("ReturnMsgNo"));
		 payment.setTradeSeq(req.getParameter("tradeSeq"));
		 apiPayDoPayment(payment);
	 }
	 
	private void apiPayDoPayment(MyCardApiPayment payment) throws Exception {
		    PayChannel channel = getChannelById(Constants.CHANNEL_ID_MY_CARD);
		    if(!payment.isPaymentDataVerified(channel.getPublicKey(), channel.getPrivateKey())){
		    	return;
		    }
			int cardPoint = payment.getCardPoint();
			//SystemMyCardCardPayment systemMyCardCardPayment = Constants.systemMyCardPayList.get(cardPoint);
			payment.setFinishAmount(BigDecimal.valueOf(cardPoint));
//			payment.setGoldNum(systemMyCardCardPayment.getGoldNum());
			//直接加钱
			savePayNotify(payment.getFacTradeSeq(), payment.getTradeSeq(), Constants.CHANNEL_ID_MY_CARD, Constants.EASOU_SERVER_STATUS_SUCCESS, String.valueOf(payment.getFinishAmount().doubleValue()));
			//后续操作
			doWorksAfterReceiveNotify(Long.parseLong(payment.getFacTradeSeq()), Constants.EASOU_SERVER_STATUS_SUCCESS, payment.getReturnMsgNo()+"", null, String.valueOf(payment.getFinishAmount().doubleValue()),  payment.getTradeSeq());
	}
	
	
//	//检查订单是否存在
//	public String isEligible()  throws Exception{
//	    getResponse().setContentType("text/html;charset=UTF-8");
//		PrintWriter out = getResponse().getWriter();
//		String requestData = this.getRequest().getParameter("DATA");
//
//		logger.info("MyCard isEligible - 请求数据 " + requestData);
//		
//		Map<String, Object> map= JsonUtil.parserStrToObject(requestData, Map.class);
//		CheckEligibleRequest checkPayUser = new CheckEligibleRequest();
//		
//		checkPayUser.setAccount((String) map.get("Account"));
//		checkPayUser.setAmount((Integer) map.get("Amount"));
//		checkPayUser.setCharacter_ID((String) map.get("Character_ID"));
//		checkPayUser.setCP_TxID((String) map.get("CP_TxID"));
//		checkPayUser.setRealm_ID((String) map.get("Realm_ID"));
//		checkPayUser.setSecurityKey((String) map.get("SecurityKey"));
//		
//		Map<String, Integer> returnValue = new HashMap<String, Integer>();
//		returnValue.put("ResultCode", CheckEligibleRequest.PAY_NOT_ELIGIBLE);
////		if (checkPayUser.isRequestDataCorrect() == true) {
////			String gameOrderId = checkPayUser.getGameOrderId();
////			PaymentOrder paymentOrder = paymentOrderDao.get(gameOrderId);
////			if (paymentOrder != null) {
////				returnValue.put("ResultCode", CheckEligibleRequest.PAY_ELIGIBLE);
////			} else {
////				logger.error("MyCard isEligible - 订单为空 gameOrderId[" + gameOrderId + "] MyCard 请求数据 " + requestData );
////			}
////		}
//		out.print("isEligible success");
//		return null;
//	}
	
	/**
	 * mycard SDK支付 补单
	 * @return
	 * @throws Exception
	 */
	public String mycardSDKPayment()  throws Exception{
	    getResponse().setContentType("text/html;charset=UTF-8");
		PrintWriter out = getResponse().getWriter();
		String data = this.getRequest().getParameter("DATA");
		logger.info("mycard支付回调，收到的参数列表为:"+data);
		Map<String,String> map = JsonUtil.parserStrToObject(data, Map.class);
		String ReturnCode = map.get("ReturnCode");
		String ReturnMsg = map.get("ReturnMsg");
		String FacServiceId = map.get("FacServiceId");
		String TotalNum = map.get("TotalNum");
		String FacTradeSeq = map.get("FacTradeSeq");
		
		PayChannel channel = getChannelById(Constants.CHANNEL_ID_MY_CARD_PACKAGE);
		
		if(ReturnCode.equals("1")){
			int num = Integer.valueOf(TotalNum);
			List<String> list = JsonUtil.parserStrToObject(FacTradeSeq, List.class);
			for(int i=0;i<num;i++){
				String facTradeSeqTemp = list.get(i);
				long orderId = Long.valueOf(facTradeSeqTemp);
				logger.info("执行加钱操作~~~~~~~~！！！！！！！！！！！！facTradeSeqTemp="+facTradeSeqTemp);
				PayEbManager payEbManager = (PayEbManager)getBean("payEbManager");
				PayEb payEb  = payEbManager.getEntityById(orderId);
				if(payEb==null){
					 logger.info("订单找不到,orderId="+facTradeSeqTemp);
					 continue;
				}
				String otherInfo = payEb.getOtherInfo();
				String[] array = otherInfo.split(",");
				if(array.length<3){
					 logger.info("订单找不到,orderId="+facTradeSeqTemp+",otherInfo中逗号隔开的变量小于3，找不到authCode,otherInfo = " + otherInfo);
					 continue;
				}
				String authCode = array[2];
				String str = MyCardAction.pay(authCode, channel);
				if(str!=null){
					 logger.info("订单补单结果,orderId="+facTradeSeqTemp+",result = "+str);
				}
			}
		}else{
			logger.info("不执行加钱操作~~~~~~~~！！！！！！！！！！！！");
		}
		out.print("200");
		return null;
	}
}
