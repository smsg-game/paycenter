package com.fantingame.pay.manager.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.fantingame.pay.dao.PayAppVersionDao;
import com.fantingame.pay.entity.PayAppVersion;
import com.fantingame.pay.manager.PayAppVersionManager;


public class PayAppVersionManagerImpl implements PayAppVersionManager{
    private Logger logger = Logger.getLogger(PayAppVersionManagerImpl.class);
	private PayAppVersionDao payAppVersionDao = null;

	@Override
	public PayAppVersion getLatestForcedVersion() {
		return payAppVersionDao.getLatestForcedVersion();
	}

	@Override
	public PayAppVersion getLatestVersion() {
		return payAppVersionDao.getLatestVersion();
	}

	@Override
	public int count(PayAppVersion t) {
		return payAppVersionDao.count(t);
	}

	@Override
	public int delete(List<String> ids) {
		return payAppVersionDao.delete(ids);
	}

	@Override
	public List<PayAppVersion> getEntity(PayAppVersion t) {
		return payAppVersionDao.getEntity(t);
	}

	@Override
	public PayAppVersion getEntityById(Long id) {
		try{
		   return payAppVersionDao.getEntityById(id);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}

	@Override
	public int save(PayAppVersion t) {
		return payAppVersionDao.save(t);
	}

	@Override
	public int update(PayAppVersion t) {
		return payAppVersionDao.update(t);
	}
	

	public PayAppVersionDao getPayAppVersionDao() {
		return payAppVersionDao;
	}
	public void setPayAppVersionDao(PayAppVersionDao payAppVersionDao) {
		this.payAppVersionDao = payAppVersionDao;
	}
	
}
