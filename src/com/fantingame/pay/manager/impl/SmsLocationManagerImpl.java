package com.fantingame.pay.manager.impl;

import java.util.List;

import com.fantingame.pay.dao.SmsLocationDao;
import com.fantingame.pay.entity.SmsLocation;
import com.fantingame.pay.manager.SmsLocationManager;

public class SmsLocationManagerImpl implements SmsLocationManager {
	private SmsLocationDao smsLocationDao;

	@Override
	public int count(SmsLocation t) {
		return smsLocationDao.count(t);
	}

	@Override
	public int delete(List<String> ids) {
		return smsLocationDao.delete(ids);
	}

	@Override
	public List<SmsLocation> getEntity(SmsLocation t) {
		return smsLocationDao.getEntity(t);
	}

	@Override
	public SmsLocation getEntityById(Long id) throws Exception {
		return smsLocationDao.getEntityById(id);
	}
	
	@Override
	public SmsLocation getEntityByMobile(String mobile) throws Exception {
		if(mobile==null || mobile.length()<7) return null;
		return smsLocationDao.getEntityByMobile(mobile.substring(0,7));
	}

	@Override
	public int save(SmsLocation t) {
		return smsLocationDao.save(t);
	}

	@Override
	public int update(SmsLocation t) {
		return smsLocationDao.update(t);
	}

	public SmsLocationDao getSmsLocationDao() {
		return smsLocationDao;
	}

	public void setSmsLocationDao(SmsLocationDao smsLocationDao) {
		this.smsLocationDao = smsLocationDao;
	}

   

}
