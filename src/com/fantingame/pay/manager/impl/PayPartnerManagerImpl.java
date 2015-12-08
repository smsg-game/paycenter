package com.fantingame.pay.manager.impl;

import java.util.List;

import com.fantingame.pay.dao.PayPartnerDao;
import com.fantingame.pay.entity.PayPartner;
import com.fantingame.pay.exception.EasouPayException;
import com.fantingame.pay.manager.PayPartnerManager;


public class PayPartnerManagerImpl implements PayPartnerManager{
	private PayPartnerDao payPartnerDao = null;

	@Override
	public int count(PayPartner t) {
		return payPartnerDao.count(t);
	}

	@Override
	public int delete(List<String> ids) {
		return payPartnerDao.delete(ids);
	}

	@Override
	public List<PayPartner> getEntity(PayPartner t) {
		return payPartnerDao.getEntity(t);
	}

	@Override
	//@Cacheable(value="payPartner",key="#id")
	public PayPartner getEntityById(Long id) throws Exception {
		PayPartner parther = null;
		parther =  payPartnerDao.getEntityById(id);
		if(parther == null){
			throw new EasouPayException(null, "不存在对应合作商！");
		}
		return parther;
	}
	
	@Override
	public PayPartner test(PayPartnerManager p,Long id){
		try {
			return getEntityById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int save(PayPartner t) {
		return payPartnerDao.save(t);
	}

	@Override
	public int update(PayPartner t) {
		return payPartnerDao.update(t);
	}

	public PayPartnerDao getPayPartnerDao() {
		return payPartnerDao;
	}

	public void setPayPartnerDao(PayPartnerDao payPartnerDao) {
		this.payPartnerDao = payPartnerDao;
	}


	
	
	
}
