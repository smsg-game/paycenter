package com.fantingame.pay.manager.impl;

import java.util.List;

import com.fantingame.pay.dao.SmsSendedDao;
import com.fantingame.pay.entity.SmsSended;
import com.fantingame.pay.manager.SmsSendedManager;

public class SmsSendedManagerImpl implements SmsSendedManager {
	private SmsSendedDao smsSendedDao;

	@Override
	public int count(SmsSended t) {
		return smsSendedDao.count(t);
	}

	@Override
	public int delete(List<String> ids) {
		return smsSendedDao.delete(ids);
	}

	@Override
	public List<SmsSended> getEntity(SmsSended t) {
		return smsSendedDao.getEntity(t);
	}

	@Override
	public SmsSended getEntityById(Long id) throws Exception {
		return smsSendedDao.getEntityById(id);
	}

	@Override
	public int save(SmsSended t) {
		return smsSendedDao.save(t);
	}

	@Override
	public int update(SmsSended t) {
		return smsSendedDao.update(t);
	}
	
	
	
	@Override
	public SmsSended getEntityByLinkId(String linkId) {
		return smsSendedDao.getEntityByLinkId(linkId);
	}

	@Override
	public int countMoneyMonth(SmsSended smSended) {
		return smsSendedDao.countMoneyMonth(smSended);
	}

	@Override
	public int countMoneyToday(SmsSended smSended) {
		return smsSendedDao.countMoneyToday(smSended);
	}

	@Override
	public int countSendedMonth(SmsSended smSended) {
		return smsSendedDao.countSendedMonth(smSended);
	}

	@Override
	public int countSendedToday(SmsSended smSended) {
		return smsSendedDao.countSendedToday(smSended);
	}

	@Override
	public int updateMo(SmsSended smSended) {
		return smsSendedDao.updateMo(smSended);
	}

	@Override
	public int updateMr(SmsSended smSended) {
		return smsSendedDao.updateMr(smSended);
	}

	public SmsSendedDao getSmsSendedDao() {
		return smsSendedDao;
	}

	public void setSmsSendedDao(SmsSendedDao smsSendedDao) {
		this.smsSendedDao = smsSendedDao;
	}

}
