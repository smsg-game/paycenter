package com.fantingame.pay.manager.impl;

import java.util.List;

import com.fantingame.pay.dao.SmsAreaDao;
import com.fantingame.pay.entity.SmsArea;
import com.fantingame.pay.manager.SmsAreaManager;

public class SmsAreaManagerImpl implements SmsAreaManager {
	private SmsAreaDao smsAreaDao;

	@Override
	public int count(SmsArea t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(List<String> ids) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<SmsArea> getEntity(SmsArea t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SmsArea getEntityById(Long id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int save(SmsArea t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(SmsArea t) {
		// TODO Auto-generated method stub
		return 0;
	}

	public SmsAreaDao getSmsAreaDao() {
		return smsAreaDao;
	}

	public void setSmsAreaDao(SmsAreaDao smsAreaDao) {
		this.smsAreaDao = smsAreaDao;
	}

	

}
