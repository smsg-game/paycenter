package com.fantingame.pay.manager.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.aspectj.apache.bcel.classfile.Constant;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.easou.common.util.StringUtil;
import com.fantingame.pay.action.ecenter.vo.UserTradeHis;
import com.fantingame.pay.action.ecenter.vo.UserTradeHistory;
import com.fantingame.pay.dao.PayChannelDao;
import com.fantingame.pay.dao.PayEbDao;
import com.fantingame.pay.dao.PayTradeDao;
import com.fantingame.pay.entity.PayChannel;
import com.fantingame.pay.entity.PayEb;
import com.fantingame.pay.entity.PayTrade;
import com.fantingame.pay.entity.PayUserAccount;
import com.fantingame.pay.entity.PayUserTradeHistory;
import com.fantingame.pay.entity.TradeCode;
import com.fantingame.pay.entity.User;
import com.fantingame.pay.job.DelayItem;
import com.fantingame.pay.manager.PayEbManager;
import com.fantingame.pay.manager.PayTradeManager;
import com.fantingame.pay.manager.PayUserAccountManager;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.EbTools;
import com.fantingame.pay.utils.PageData;
import com.fantingame.pay.utils.PageUtil;
import com.fantingame.pay.utils.StringTools;

public class PayEbManagerImpl implements PayEbManager {

	private static Logger logger = Logger.getLogger(PayEbManagerImpl.class);
	
	private PayUserAccountManager payUserAccountManager;

	private PayTradeDao payTradeDao;

	private PayEbDao payEbDao;

	private PayChannelDao payChannelDao;


	@Override
	public PayEb getEntityByInvoice(String invoice) {
		return payEbDao.getEntityByInvoice(invoice);
	}

	@Override
	public int count(PayEb t) {
		return payEbDao.count(t);
	}

	@Override
	public int delete(List<String> ids) {
		return payEbDao.delete(ids);
	}

	@Override
	public List<PayEb> getEntity(PayEb t) {
		return payEbDao.getEntity(t);
	}

	@Override
	public PayEb getEntityById(Long id) throws Exception {
		return payEbDao.getEntityById(id);
	}

	@Override
	public int save(PayEb t) {
		return payEbDao.save(t);
	}

	@Override
	public int update(PayEb t) {
		return payEbDao.update(t);
	}

	public void updPcAfterRecNotify(PayTrade payTrade,PayEb payEb){
		if(payEb.getStatus()==Constants.EASOU_SERVER_STATUS_SUCCESS){
			Long userId = Long.parseLong(payEb.getEasouId());
			// 查询用户是否存在
			PayUserAccount userAccount = payUserAccountManager.getUserAcountByJuserId(userId);
			// 账户不存在 则开户
			if (null == userAccount) {
				userAccount = new PayUserAccount();
				userAccount.setJuserId(userId);
				userAccount.setUsername("ft."+userId);
				userAccount.setFee(0L);
				payUserAccountManager.save(userAccount);
			}
			PayChannel payChannel = payChannelDao.getEntityById(payEb.getChannelId());
			long reqEb = EbTools.rmbToEb(payEb.getPaidFee());
			//加上一个折算率
			reqEb = Math.round(reqEb*payChannel.getRate());
			payUserAccountManager.updEb(Long.valueOf(payEb.getEasouId()),payTrade.getTradeName(),payTrade.getInvoice(), payChannel.getName(),reqEb);
		}
	}

	public int updAfterRecNotify(PayTrade payTrade,PayEb payEb) throws Exception{
		int payResult = 0;
		// 查询渠道号
		Long channelId = payEb.getChannelId();
		if(payEb.getStatus()==1){//成功了
			Long userId = Long.parseLong(payEb.getEasouId());
			// 查询用户是否存在
			PayUserAccount userAccount = payUserAccountManager.getUserAcountByJuserId(userId);
			// 账户不存在 则开户
			if (null == userAccount) {
				userAccount = new PayUserAccount();
				userAccount.setJuserId(userId);
				userAccount.setUsername("ft."+userId);
				userAccount.setFee(0L);
				payUserAccountManager.save(userAccount);
			}
			
			PayChannel payChannel = payChannelDao.getEntityById(channelId);
			
			//EB支付除外的动作
			if(payEb.getChannelId()!=Constants.CHANNEL_ID_EB){//充值
				long reqEb = EbTools.rmbToEb(payEb.getPaidFee());
				//加上一个折算率
				reqEb = Math.round(reqEb*payChannel.getRate());
				payUserAccountManager.updEb(Long.valueOf(payEb.getEasouId()),payTrade.getTradeName(),payTrade.getInvoice(), payChannel.getName(),reqEb);
			}
			//TODO 此处注销  同意都需要使用f币支付  不再具有自动扣款功能了！
			//EB充值除外的动作
//			if(payTrade.getPartnerId()!=Constants.PARTNER_ID_SELF){
//				long reqEb = EbTools.rmbToEb(payTrade.getReqFee());
//				//1-成功，0-失败
//				payResult = payUserAccountManager.updEb(Long.valueOf(payEb.getEasouId()), payTrade.getTradeName(), payTrade.getInvoice(), payChannel.getName(), -reqEb);
//			}
		}
		//保存
		payTradeDao.update(payTrade);
		payEbDao.updAfterPay(payEb);
		
		//notify动作,payResult代表支付成功
		if(payResult==1 && !StringUtil.isEmpty(payTrade.getNotifyUrl())){
			DelayItem di = new DelayItem(1);
			di.setPaidFee(payTrade.getPaidFee());
			di.setNotifyUrl(payTrade.getNotifyUrl());
			di.setTradeId(payTrade.getTradeId());
			di.setTradeStatus("TRADE_SUCCESS");
			di.setInvoice(payTrade.getInvoice());
			di.setPartnerId(payTrade.getPartnerId());
			di.setAppId(payTrade.getAppId());
			di.setReqFee(payTrade.getReqFee());
			di.setPayerId(payTrade.getPayerId());
			di.setTradeName(payTrade.getTradeName());
			di.setNotifyDatetime(StringTools.getSecondFormat_().format(payTrade.getSuccessDatetime()));
			Constants.DQ.add(di);
		}
		return 1;
	}
	
	public int updAfterRecNotify2(PayTrade payTrade,PayEb payEb) throws Exception{
		int payResult = 0;
		// 查询渠道号
		Long channelId = payEb.getChannelId();
		if(payEb.getStatus()==1){//成功了
			Long userId = Long.parseLong(payEb.getEasouId());
			// 查询用户是否存在
			PayUserAccount userAccount = payUserAccountManager.getUserAcountByJuserId(userId);
			// 账户不存在 则开户
			if (null == userAccount) {
				userAccount = new PayUserAccount();
				userAccount.setJuserId(userId);
				userAccount.setUsername("ft."+userId);
				userAccount.setFee(0L);
				payUserAccountManager.save(userAccount);
			}
			
			PayChannel payChannel = payChannelDao.getEntityById(channelId);
			
			//EB支付除外的动作
			if(payEb.getChannelId()!=Constants.CHANNEL_ID_EB){//充值
				long reqEb = EbTools.rmbToEb(payEb.getPaidFee());
				//加上一个折算率
				reqEb = Math.round(reqEb*payChannel.getRate());
				payUserAccountManager.updEb(Long.valueOf(payEb.getEasouId()),payTrade.getTradeName(),payTrade.getInvoice(), payChannel.getName(),reqEb);
			}
			//EB充值除外的动作
			if(payTrade.getPartnerId()!=Constants.PARTNER_ID_SELF){
				long reqEb = EbTools.rmbToEb(payTrade.getReqFee());
				//1-成功，0-失败
				payResult = payUserAccountManager.updEb(Long.valueOf(payEb.getEasouId()), payTrade.getTradeName(), payTrade.getInvoice(), payChannel.getName(), -reqEb);
			}
		}
		//保存
		payTradeDao.update(payTrade);
		payEbDao.updAfterPay(payEb);
		
		//notify动作,payResult代表支付成功
		if(payResult==1 && !StringUtil.isEmpty(payTrade.getNotifyUrl())){
			DelayItem di = new DelayItem(1);
			di.setPaidFee(payTrade.getPaidFee());
			di.setNotifyUrl(payTrade.getNotifyUrl());
			di.setTradeId(payTrade.getTradeId());
			di.setTradeStatus("TRADE_SUCCESS");
			di.setInvoice(payTrade.getInvoice());
			di.setPartnerId(payTrade.getPartnerId());
			di.setAppId(payTrade.getAppId());
			di.setReqFee(payTrade.getReqFee());
			di.setPayerId(payTrade.getPayerId());
			di.setTradeName(payTrade.getTradeName());
			di.setNotifyDatetime(StringTools.getSecondFormat_().format(payTrade.getSuccessDatetime()));
			Constants.DQ.add(di);
		}
		return 1;
	}
	/**
	 * 划扣EB
	*/
	public int updEbForBookMall(PayTrade payTrade,PayEb payEb) throws Exception {
		//订单是否已经被处理
		int result = 0;
		if(payTrade.getStatus()==0){
			long reqEb = EbTools.rmbToEb(payTrade.getReqFee());
			result = payUserAccountManager.updEb(Long.valueOf(payEb.getEasouId()),payTrade.getTradeName(),payTrade.getInvoice(),"梵町E币支付",-reqEb);
			if(result==1){
				payTrade.setPaidFee(payTrade.getReqFee());
				payTrade.setSuccessDatetime(new Date());
				payEb.setPaidFee(payTrade.getPaidFee());
				payEb.setPaidCurrency("CNY");
				payEb.setSuccessDatetime(new Date());
				payTrade.setStatus(1);
				payEb.setStatus(1);
			}else{
				payTrade.setStatus(-1);
				payEb.setStatus(-1);
		    }
				
			if(payEb.getId()!=null){
				payTradeDao.update(payTrade);
				payEbDao.update(payEb);
			}else{
				payTradeDao.save(payTrade);
				payEbDao.save(payEb);
			}
				
			//notify动作,payResult代表支付成功
			if(payTrade.getStatus()==1 && !StringUtil.isEmpty(payTrade.getNotifyUrl())){
				DelayItem di = new DelayItem(1);
				di.setPaidFee(payEb.getPaidFee());
				di.setNotifyUrl(payTrade.getNotifyUrl());
				di.setTradeId(payTrade.getTradeId());
				di.setTradeStatus("TRADE_SUCCESS");
				di.setInvoice(payTrade.getInvoice());
				di.setPartnerId(payTrade.getPartnerId());
				di.setAppId(payTrade.getAppId());
				di.setReqFee(payTrade.getReqFee());
				di.setPayerId(payTrade.getPayerId());
				di.setTradeName(payTrade.getTradeName());
				di.setNotifyDatetime(StringTools.getSecondFormat_().format(payTrade.getSuccessDatetime()));
				Constants.DQ.add(di);
			}
		}
		return result;
	}
	// 查看用户的流水记录
	public PageUtil getTradeHistoryByUserId(int page, int rowsPerPage, Long userId) {

		// 统计记录条数
		int recordCount = payUserAccountManager.getTradeHistoryByUserIdCount(userId);

		// 判断页数是否能被整除
		int i_t = recordCount % rowsPerPage;
		// 总的页数
		int countPage = i_t == 0 ? recordCount / rowsPerPage : recordCount
				/ rowsPerPage + 1;

		countPage = countPage > 0 ? countPage : 1;

		int startIndex = 0;

		if (page <= 0) {
			page = 1; // page最小为1
		}
		if (page > countPage) {
			page = countPage;
		}
		//流水记录
		PayUserTradeHistory puth = new PayUserTradeHistory();

		puth.setUserID(userId);
		startIndex = (page - 1) * rowsPerPage;
		puth.setStartIndex(startIndex);
		puth.setEndIndex(rowsPerPage);

		List<PayUserTradeHistory> list = payUserAccountManager.getTradeHistoryByUserId(puth);

		PageUtil pu = new PageUtil();

		pu.setDataContext(list);
		pu.setThisPage(page);
		pu.setCountRow(recordCount);
		pu.setEveryPageRow(rowsPerPage);

		return pu;
	}
	
	// 查看用户PC的流水记录
	public PageData<UserTradeHistory> getTradeHisPCByUserId(int page, int pageSize, Long userId,String startTime,String endTime) {

		//流水记录
		PayUserTradeHistory puth = new PayUserTradeHistory();
		puth.setUserID(userId);
		if(StringUtils.isNotBlank(startTime)){
			startTime = startTime+" 00:00:00";
			puth.setStartTime(startTime);
		}
		if(StringUtils.isNotBlank(endTime)){
			endTime = endTime+" 23:59:59";
			puth.setEndTime(endTime);
		}
		// 统计记录条数
		int recordCount = payUserAccountManager.getTradeHistoryPCByUserIdCount(puth);

		// 判断页数是否能被整除
		int i_t = recordCount % pageSize;
		// 总的页数
		int countPage = i_t == 0 ? recordCount / pageSize : recordCount
				/ pageSize + 1;

		countPage = countPage > 0 ? countPage : 1;

		int startIndex = 0;

		if (page <= 0) {
			page = 1; // page最小为1
		}
		if (page > countPage) {
			page = countPage;
		}
		startIndex = (page - 1) * pageSize;
		puth.setStartIndex(startIndex);
		puth.setEndIndex(pageSize);
		List<UserTradeHis> list = payUserAccountManager.getTradeHistoryPCByUserId(puth);
		List<UserTradeHistory> list2 = new ArrayList<UserTradeHistory>();
		if(list != null && !list.isEmpty()) {
			for(UserTradeHis tradeHis : list) {
				String timestamp = StringTools.Date2String(tradeHis.getCreateDatetime());
				String channelName=tradeHis.getChannelName().contains("财付通")?"财付通":"支付宝";
				UserTradeHistory userTradeHistory = new UserTradeHistory(tradeHis.getInvoice(), tradeHis.getTradeName(),EbTools.ebToRmb(tradeHis.getPaidFee()), timestamp, tradeHis.getType(),channelName);
				list2.add(userTradeHistory);
			}
		}
		PageData<UserTradeHistory> pageData = new PageData<UserTradeHistory>();

		pageData.setList(list2);
		pageData.setThisPage(page);
		pageData.setCountRow(recordCount);
		pageData.setEveryPageRow(pageSize);

		return pageData;
	}
	
	// 查看用户的流水记录
		public PageData<UserTradeHistory> getTradeHisByUserId(int page, int pageSize, Long userId) {

			// 统计记录条数
			int recordCount = payUserAccountManager.getTradeHistoryByUserIdCount(userId);

			// 判断页数是否能被整除
			int i_t = recordCount % pageSize;
			// 总的页数
			int countPage = i_t == 0 ? recordCount / pageSize : recordCount
					/ pageSize + 1;

			countPage = countPage > 0 ? countPage : 1;

			int startIndex = 0;

			if (page <= 0) {
				page = 1; // page最小为1
			}
			if (page > countPage) {
				page = countPage;
			}
			//流水记录
			PayUserTradeHistory puth = new PayUserTradeHistory();

			puth.setUserID(userId);
			startIndex = (page - 1) * pageSize;
			puth.setStartIndex(startIndex);
			puth.setEndIndex(pageSize);

			List<UserTradeHis> list = payUserAccountManager.getTradeHisByUserId(puth);
			
			List<UserTradeHistory> list2 = new ArrayList<UserTradeHistory>();
			if(list != null && !list.isEmpty()) {
				for(UserTradeHis tradeHis : list) {
					String timestamp = StringTools.Date2String(tradeHis.getCreateDatetime());
					UserTradeHistory userTradeHistory = new UserTradeHistory(tradeHis.getInvoice(), tradeHis.getTradeName(), tradeHis.getPaidFee(), timestamp, tradeHis.getType(),tradeHis.getChannelName());
					list2.add(userTradeHistory);
				}
			}
			PageData<UserTradeHistory> pageData = new PageData<UserTradeHistory>();

			pageData.setList(list2);
			pageData.setThisPage(page);
			pageData.setCountRow(recordCount);
			pageData.setEveryPageRow(pageSize);

			return pageData;
		}
	
	
	public PayEb getEntityBySubId(Integer subId){
		return payEbDao.getEntityBySubId(subId);
	}
	

	public PayTradeDao getPayTradeDao() {
		return payTradeDao;
	}

	public void setPayTradeDao(PayTradeDao payTradeDao) {
		this.payTradeDao = payTradeDao;
	}

	public PayEbDao getPayEbDao() {
		return payEbDao;
	}

	public void setPayEbDao(PayEbDao payEbDao) {
		this.payEbDao = payEbDao;
	}

	public PayUserAccountManager getPayUserAccountManager() {
		return payUserAccountManager;
	}

	public void setPayUserAccountManager(PayUserAccountManager payUserAccountManager) {
		this.payUserAccountManager = payUserAccountManager;
	}

	public PayChannelDao getPayChannelDao() {
		return payChannelDao;
	}

	public void setPayChannelDao(PayChannelDao payChannelDao) {
		this.payChannelDao = payChannelDao;
	}

	@Override
	public List<PayEb> getEntityByCondition(PayEb payEb) {
		return payEbDao.getEntityByCondition(payEb);
	}
	/**
	 * 创建trade和Eb订单
	 * @author qinhua
	 * @param payChannel
	 * @param reqFee
	 * @param otherInfo
	 * @return
	 * @throws Exception
	 */
	public PayEb addEbOrder(PayChannel channel, User user,String reqFee, String otherInfo) throws Exception {
		
		//获取支付渠道信息
		//PayChannel channel = getChannelById(channelId);
		//PayEbManager payEbManager = (PayEbManager)getBean("payEbManager");
		if (Double.valueOf(reqFee).doubleValue() * channel.getRate().doubleValue() < Double.valueOf(reqFee).doubleValue()) throw new Exception(TradeCode.EASOU_CODE_190.msgC);
		SimpleDateFormat format=new SimpleDateFormat("yyyyMMddhhmm");
		//创建pay_trade订单
		PayTrade payTrade = new PayTrade() ;
		payTrade.setAppId("100");
		payTrade.setSeparable(0);
		payTrade.setPayerId(user.getId().toString());
		payTrade.setEasouId(user.getId().toString());
		payTrade.setReqFee(reqFee);
		payTrade.setCurrency("CNY");
		payTrade.setPartnerId(Constants.PARTNER_ID_SELF);
		String tradeId=user.getId()+format.format(new Date())+reqFee.replace(".", "");
//		tradeId=channel.getId().equals(Constants.CHANNEL_ID_ALIPAY_PC)?tradeId+1:tradeId+2;
		payTrade.setTradeId(tradeId);
		payTrade.setQn("1000");
		payTrade.setStatus(Constants.EASOU_SERVER_STATUS_CREATE);
		payTrade.setTradeName("e币充值");
		
		payTrade.setNotifyUrl("");
		payTrade.setRedirectUrl("");
		PayEb payEb = null;
		
			payEb = new PayEb();
			payTrade.setCreateDatetime(new Date());
			payTrade.setInvoice(UUID.randomUUID().toString().replace("-", ""));
			
			
			//创建EB订单
			Date currentDate = new Date();
			Calendar calendar = Calendar.getInstance();
			String today = calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH)+" 00:00:00";
			
			payEb.setEasouId(user.getId().toString());
			payEb.setChannelId(channel.getId());
			payEb.setRate(channel.getRate());
			payEb.setReqFee(reqFee);  //传入金额，使用传入的金额，否则，直接使用下单时的金额
			payEb.setReqCurrency("CNY");
			payEb.setInvoice(payTrade.getInvoice());
			payEb.setStatus(Constants.EASOU_SERVER_STATUS_CREATE);
			payEb.setCreateDatetime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(today));
			payEb.setOtherInfo(otherInfo);
			
			
			List<PayEb> payEbTmp=payEbDao.getEntityByCondition(payEb);
			if(payEbTmp.size()==0){
				payEb.setCreateDatetime(currentDate);
				payTradeDao.save(payTrade);
				payEbDao.save(payEb);
				
				return payEb;
				
			}else{
				return payEbTmp.get(0);
			}
			
		
	}
    /**
     * 
     * @param startTime
     * @param endTime
     * @param myCardNum
     * @param type   1  api  2 sdk
     * @return
     */
	@Override
	public List<PayEb> getMyCardLogService(Date startTime, Date endTime,
			String myCardNum,String type) {
		if(!StringUtils.isBlank(myCardNum)){
			return payEbDao.getListByMyCardNum(myCardNum);
		}
		if(startTime!=null&&endTime!=null){
			Map<String,Object> param = new HashMap<String,Object>();
			param.put("startTime", startTime);
			param.put("endTime", endTime);
			
			if(type.equals("1")){//api
				param.put("c1", Constants.CHANNEL_ID_MY_CARD);
				param.put("c2", Constants.CHANNEL_ID_MY_CARD_TEST);
			}else{
				param.put("c1", Constants.CHANNEL_ID_MY_CARD_PACKAGE);
				param.put("c2", Constants.CHANNEL_ID_MY_CARD_PACKAGE_TEST);
			}
			return payEbDao.getListBySuccessTime(param);
		}
		return null;
	}
	

}
