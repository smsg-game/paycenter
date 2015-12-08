package com.fantingame.pay.manager.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.fantingame.pay.dao.PayNotifyDao;
import com.fantingame.pay.entity.PayNotify;
import com.fantingame.pay.manager.PayNotifyManager;


public class PayNotifyManagerImpl implements PayNotifyManager{
	private Logger logger = Logger.getLogger(PayNotifyManagerImpl.class);
    private PayNotifyDao payNotifyDao = null;

    @Override
	public int count(PayNotify t) {
		return payNotifyDao.count(t);
	}

	@Override
	public int delete(List<String> ids) {
		return payNotifyDao.delete(ids);
	}

	@Override
	public List<PayNotify> getEntity(PayNotify t) {
		return payNotifyDao.getEntity(t);
	}

	@Override
	public PayNotify getEntityById(Long id) {
		try{
		   return payNotifyDao.getEntityById(id);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}

	@Override
	public int save(PayNotify t) {
		return payNotifyDao.save(t);
	}

	@Override
	public int update(PayNotify t) {
		return payNotifyDao.update(t);
	}

	public PayNotifyDao getPayNotifyDao() {
		return payNotifyDao;
	}

	public void setPayNotifyDao(PayNotifyDao payNotifyDao) {
		this.payNotifyDao = payNotifyDao;
	}
 
}
