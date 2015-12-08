package com.fantingame.pay.manager.impl;

import java.util.List;

import com.fantingame.pay.dao.SmsChannelDao;
import com.fantingame.pay.entity.SmsChannel;
import com.fantingame.pay.manager.SmsChannelManager;

public class SmsChannelManagerImpl implements SmsChannelManager {
	private SmsChannelDao smsChannelDao;

	@Override
	public int count(SmsChannel t) {
		return smsChannelDao.count(t);
	}

	@Override
	public int delete(List<String> ids) {
		return smsChannelDao.delete(ids);
	}

	@Override
	public List<SmsChannel> getEntity(SmsChannel t) {
		return smsChannelDao.getEntity(t);
	}

	@Override
	public SmsChannel getEntityById(Long id) throws Exception {
		return smsChannelDao.getEntityById(id);
	}

	@Override
	public int save(SmsChannel t) {
		return smsChannelDao.save(t);
	}

	@Override
	public int update(SmsChannel t) {
		return smsChannelDao.update(t);
	}

	public SmsChannelDao getSmsChannelDao() {
		return smsChannelDao;
	}

	public void setSmsChannelDao(SmsChannelDao smsChannelDao) {
		this.smsChannelDao = smsChannelDao;
	}

   

}
