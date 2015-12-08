package com.fantingame.pay.manager.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.fantingame.pay.dao.PayUserBindHistoryDao;
import com.fantingame.pay.entity.PayUserBindHistory;
import com.fantingame.pay.manager.PayUserBindHistoryManager;


public class PayUserBindHistoryManagerImpl implements PayUserBindHistoryManager{
	private Logger logger = Logger.getLogger(PayUserBindHistoryManagerImpl.class);
    private PayUserBindHistoryDao payUserBindHistoryDao = null;

	@Override
	public int count(PayUserBindHistory t) {
		return payUserBindHistoryDao.count(t);
	}

	@Override
	public int delete(List<String> ids) {
		return payUserBindHistoryDao.delete(ids);
	}

	@Override
	public List<PayUserBindHistory> getEntity(PayUserBindHistory t) {
		return payUserBindHistoryDao.getEntity(t);
	}

	@Override
	public PayUserBindHistory getEntityById(Long id) {
		try{
		   return payUserBindHistoryDao.getEntityById(id);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}

	@Override
	public int save(PayUserBindHistory t) {
		return payUserBindHistoryDao.save(t);
	}

	@Override
	public int update(PayUserBindHistory t) {
		return payUserBindHistoryDao.update(t);
	}
	
	
	/*用手机号获取绑定历史记录*/
	public PayUserBindHistory getEntityByMobile(String mobile){
		return payUserBindHistoryDao.getEntityByMobile(mobile);
	}

	public PayUserBindHistoryDao getPayUserBindHistoryDao() {
		return payUserBindHistoryDao;
	}

	public void setPayUserBindHistoryDao(PayUserBindHistoryDao payUserBindHistoryDao) {
		this.payUserBindHistoryDao = payUserBindHistoryDao;
	}

	
	
}