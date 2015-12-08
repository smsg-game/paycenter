package com.fantingame.pay.manager.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.fantingame.pay.dao.PayNotifyPushDao;
import com.fantingame.pay.entity.PayNotifyPush;
import com.fantingame.pay.manager.PayNotifyPushManager;


public class PayNotifyPushManagerImpl implements PayNotifyPushManager{
	private Logger logger = Logger.getLogger(PayNotifyPushManagerImpl.class);
    private PayNotifyPushDao payNotifyPushDao = null;

    @Override
	public int count(PayNotifyPush t) {
		return payNotifyPushDao.count(t);
	}

	@Override
	public int delete(List<String> ids) {
		return payNotifyPushDao.delete(ids);
	}

	@Override
	public List<PayNotifyPush> getEntity(PayNotifyPush t) {
		return payNotifyPushDao.getEntity(t);
	}

	@Override
	public PayNotifyPush getEntityById(Long id) {
		try{
		   return payNotifyPushDao.getEntityById(id);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}

	@Override
	public int save(PayNotifyPush t) {
		return payNotifyPushDao.save(t);
	}

	@Override
	public int update(PayNotifyPush t) {
		return payNotifyPushDao.update(t);
	}

	public PayNotifyPushDao getPayNotifyPushDao() {
		return payNotifyPushDao;
	}

	public void setPayNotifyPushDao(PayNotifyPushDao payNotifyPushDao) {
		this.payNotifyPushDao = payNotifyPushDao;
	}

	


	
	
	

     
}
