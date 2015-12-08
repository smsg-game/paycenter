package com.fantingame.pay.manager.impl;

import java.util.List;

import com.fantingame.pay.dao.PayMaskDao;
import com.fantingame.pay.entity.PayMask;
import com.fantingame.pay.manager.PayMaskManager;


public class PayMaskManagerImpl implements PayMaskManager{
	
	private PayMaskDao maskDao;
	
	
	public String getMaskChannleByCondiction(String maskCondition) {
		return maskDao.getMaskChannleByCondiction(maskCondition);
	}
	
	public int count(PayMask t) {
		return 0;
	}
	public int delete(List<String> ids) {
		return 0;
	}
	public List<PayMask> getEntity(PayMask t) {
		return null;
	}
	public PayMask getEntityById(Long id) throws Exception {
		return null;
	}
	public int save(PayMask t) {
		return 0;
	}
	public int update(PayMask t) {
		return 0;
	}

	public PayMaskDao getMaskDao() {
		return maskDao;
	}

	public void setMaskDao(PayMaskDao maskDao) {
		this.maskDao = maskDao;
	}


}
