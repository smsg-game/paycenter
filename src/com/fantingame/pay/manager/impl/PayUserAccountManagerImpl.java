package com.fantingame.pay.manager.impl;

import java.util.List;

import com.fantingame.pay.action.ecenter.vo.UserTradeHis;
import com.fantingame.pay.dao.PayUserAccountDao;
import com.fantingame.pay.dao.PayUserBindHistoryDao;
import com.fantingame.pay.dao.PayUserTradeHistoryDao;
import com.fantingame.pay.entity.PayUserAccount;
import com.fantingame.pay.entity.PayUserTradeHistory;
import com.fantingame.pay.manager.PayUserAccountManager;

public class PayUserAccountManagerImpl implements PayUserAccountManager {

	private PayUserAccountDao payUserAccountDao;
	private PayUserTradeHistoryDao payUserTradeHistoryDao;
	private PayUserBindHistoryDao payUserBindHistoryDao;

	@Override
	public int save(PayUserAccount t) {
		return payUserAccountDao.save(t);
	}

	@Override
	public int update(PayUserAccount t) {
		return payUserAccountDao.update(t);
	}

	@Override
	public int count(PayUserAccount t) {
		return payUserAccountDao.count(t);
	}

	@Override
	public int delete(List<String> ids) {
		return payUserAccountDao.delete(ids);
	}

	@Override
	public List<PayUserAccount> getEntity(PayUserAccount t) {
		return payUserAccountDao.getEntity(t);
	}

	@Override
	public PayUserAccount getEntityById(Long id) throws Exception {
		return payUserAccountDao.getEntityById(id);
	}

	
	
	//根据用户ID获取对象
	public PayUserAccount getUserAcountByJuserId(Long juserId){
		return payUserAccountDao.getUserAcountByJuserId(juserId);
	}
	
	//更新账户EB数量
	public int  updEb(Long userId,String tradeName,String invoice,String channelName,Long num){
		PayUserTradeHistory df = new PayUserTradeHistory();
		df.setUserID(userId);
		df.setTradeName(tradeName);
		df.setInvoice(invoice);
		df.setUserName("es."+userId);
		df.setChannelName(channelName);
		df.setPaidFee(num+"");
		df.setType(num>0?1:-1);
		
		PayUserAccount pua = payUserAccountDao.getUserAcountByJuserId(userId);
		Long fee = pua.getFee()==null?num:pua.getFee()+num;
		//如果E币不够支付,返回0
		if(fee<0) return 0;
		pua.setFee(num);
		
		//更新账户信息
		int result = payUserAccountDao.updEb(pua);
		if(result==1){
			//保存用户流水
			payUserTradeHistoryDao.save(df);
		}
		return result;
	}
	
	/*更新账户EB数量和手机号，送100E币
	public int  updGiftForBindMobile(Long userId,String mobile){
		PayUserTradeHistory df = new PayUserTradeHistory();
		df.setUserID(userId);
		df.setTradeName("用户帐号绑定手机号赠送100E币");
		df.setInvoice("");
		df.setUserName("es."+userId);
		df.setChannelName("");
		df.setPaidFee("100");
		df.setType(1);
		
		PayUserAccount pua = payUserAccountDao.getUserAcountByJuserId(userId);
		Long fee = pua.getFee()==null?100L:pua.getFee()+100L;
		pua.setFee(fee);
		
	    PayUserBindHistory payUserBindHistory = new PayUserBindHistory();
	    payUserBindHistory.setMobile(mobile);
	    payUserBindHistory.setUserId(userId+"");
		
		//保存用户流水
		payUserTradeHistoryDao.save(df);
		//保存绑定记录
		payUserBindHistoryDao.save(payUserBindHistory);
		//更新账户信息
		return payUserAccountDao.update(pua);
	}
	*/
	
	//查询用户的交易流水记录
	public List<PayUserTradeHistory> getTradeHistoryByUserId(PayUserTradeHistory payUserTradeHistory){
		return payUserTradeHistoryDao.getTradeHistoryByUserId(payUserTradeHistory);
	}
	
	public List<UserTradeHis> getTradeHisByUserId(PayUserTradeHistory payUserTradeHistory){
		return payUserTradeHistoryDao.getTradeHisByUserId(payUserTradeHistory);
	}
	
	//按用户ID统计交易流水记录条数
	public int getTradeHistoryByUserIdCount(Long userId){
		return payUserTradeHistoryDao.getTradeHistoryByUserIdCount(userId);
	}

	public PayUserAccountDao getPayUserAccountDao() {
		return payUserAccountDao;
	}

	public void setPayUserAccountDao(PayUserAccountDao payUserAccountDao) {
		this.payUserAccountDao = payUserAccountDao;
	}

	public PayUserTradeHistoryDao getPayUserTradeHistoryDao() {
		return payUserTradeHistoryDao;
	}

	public void setPayUserTradeHistoryDao(
			PayUserTradeHistoryDao payUserTradeHistoryDao) {
		this.payUserTradeHistoryDao = payUserTradeHistoryDao;
	}

	public PayUserBindHistoryDao getPayUserBindHistoryDao() {
		return payUserBindHistoryDao;
	}

	public void setPayUserBindHistoryDao(PayUserBindHistoryDao payUserBindHistoryDao) {
		this.payUserBindHistoryDao = payUserBindHistoryDao;
	}

	@Override
	public int getTradeHistoryPCByUserIdCount(PayUserTradeHistory dealFlow) {
		// TODO Auto-generated method stub
		return payUserTradeHistoryDao.getTradeHistoryPCByUserIdCount(dealFlow);
	}

	@Override
	public List<UserTradeHis> getTradeHistoryPCByUserId(
			PayUserTradeHistory dealFlow) {
		// TODO Auto-generated method stub
		return payUserTradeHistoryDao.getTradeHisPCByUserId(dealFlow);
	}



}
