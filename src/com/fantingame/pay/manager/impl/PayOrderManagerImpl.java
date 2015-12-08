package com.fantingame.pay.manager.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.fantingame.pay.dao.PayOrderDao;
import com.fantingame.pay.entity.PayOrder;
import com.fantingame.pay.manager.PayOrderManager;


public class PayOrderManagerImpl implements PayOrderManager{
	private Logger logger = Logger.getLogger(PayOrderManagerImpl.class);
    private PayOrderDao payOrderDao = null;
    
    @Override
	public int count(PayOrder t) {
		return payOrderDao.count(t);
	}

	@Override
	public int delete(List<String> ids) {
		return payOrderDao.delete(ids);
	}

	@Override
	public List<PayOrder> getEntity(PayOrder t) {
		return payOrderDao.getEntity(t);
	}

	@Override
	public PayOrder getEntityById(Long id) {
		try{
		   return payOrderDao.getEntityById(id);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	@Override
	public PayOrder getEntityByInvoice(String invoice){
		try{
			   return payOrderDao.getEntityByInvoice(invoice);
		}catch (Exception e) {
				logger.error(e.getMessage(),e);
		}
		return null;
	}

	@Override
	public int save(PayOrder t) {
		return payOrderDao.save(t);
	}

	@Override
	public int update(PayOrder t) {
		return payOrderDao.update(t);
	}

	public PayOrderDao getPayOrderDao() {
		return payOrderDao;
	}

	public void setPayOrderDao(PayOrderDao payOrderDao) {
		this.payOrderDao = payOrderDao;
	}

	
}
