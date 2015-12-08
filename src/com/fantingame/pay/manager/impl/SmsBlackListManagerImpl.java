package com.fantingame.pay.manager.impl;

import java.util.List;

import com.fantingame.pay.dao.SmsBlackListDao;
import com.fantingame.pay.entity.SmsBlackList;
import com.fantingame.pay.manager.SmsBlackListManager;

public class SmsBlackListManagerImpl implements SmsBlackListManager {
	private SmsBlackListDao smsBlackListDao;

	@Override
	public int count(SmsBlackList t) {
		return smsBlackListDao.count(t);
	}

	@Override
	public int delete(List<String> ids) {
		return smsBlackListDao.delete(ids);
	}

	@Override
	public List<SmsBlackList> getEntity(SmsBlackList t) {
		return smsBlackListDao.getEntity(t);
	}

	@Override
	public SmsBlackList getEntityById(Long id) throws Exception {
		return smsBlackListDao.getEntityById(id);
	}
	
	@Override
	public SmsBlackList getEntityByMobile(String mobile) throws Exception {
		return smsBlackListDao.getEntityByMobile(mobile);
	}
	

	@Override
	public int save(SmsBlackList t) {
		return smsBlackListDao.save(t);
	}

	@Override
	public int update(SmsBlackList t) {
		return smsBlackListDao.update(t);
	}

	public SmsBlackListDao getSmsBlackListDao() {
		return smsBlackListDao;
	}

	public void setSmsBlackListDao(SmsBlackListDao smsBlackListDao) {
		this.smsBlackListDao = smsBlackListDao;
	}

   

}
