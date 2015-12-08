package com.fantingame.pay.manager.impl;

import java.util.List;

import com.fantingame.pay.dao.SmsRuleDao;
import com.fantingame.pay.entity.SmsRule;
import com.fantingame.pay.manager.SmsRuleManager;

public class SmsRuleManagerImpl implements SmsRuleManager {
	private SmsRuleDao smsRuleDao;

	@Override
	public int count(SmsRule t) {
		return smsRuleDao.count(t);
	}

	@Override
	public int delete(List<String> ids) {
		return smsRuleDao.delete(ids);
	}

	@Override
	public List<SmsRule> getEntity(SmsRule t) {
		return smsRuleDao.getEntity(t);
	}

	@Override
	public SmsRule getEntityById(Long id) throws Exception {
		return smsRuleDao.getEntityById(id);
	}

	@Override
	public int save(SmsRule t) {
		return smsRuleDao.save(t);
	}

	@Override
	public int update(SmsRule t) {
		return smsRuleDao.update(t);
	}

	@Override
	public SmsRule getEntityByCity(SmsRule smsRule) {
		return smsRuleDao.getEntityByCity(smsRule);
	}

	public SmsRuleDao getSmsRuleDao() {
		return smsRuleDao;
	}

	public void setSmsRuleDao(SmsRuleDao smsRuleDao) {
		this.smsRuleDao = smsRuleDao;
	}

   

}
