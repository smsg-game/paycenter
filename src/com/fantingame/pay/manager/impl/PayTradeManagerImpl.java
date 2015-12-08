package com.fantingame.pay.manager.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.fantingame.pay.dao.PayTradeDao;
import com.fantingame.pay.entity.PayTrade;
import com.fantingame.pay.manager.PayTradeManager;


public class PayTradeManagerImpl implements PayTradeManager{
	private Logger logger = Logger.getLogger(PayTradeManagerImpl.class);
    private PayTradeDao payTradeDao = null;

    
    
    @Override
	public List<PayTrade> getNotifyTask() {
		return payTradeDao.getNotifyTask();
	}

	@Override
    public PayTrade getByPartnerIdAndTradeId(PayTrade t){
    	return payTradeDao.getByPartnerIdAndTradeId(t);
    }
    
    @Override
	public int count(PayTrade t) {
		return payTradeDao.count(t);
	}

	@Override
	public int delete(List<String> ids) {
		return payTradeDao.delete(ids);
	}

	@Override
	public List<PayTrade> getEntity(PayTrade t) {
		return payTradeDao.getEntity(t);
	}

	@Override
	public PayTrade getEntityById(Long id) {
		try{
		   return payTradeDao.getEntityById(id);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	@Override
	public PayTrade getEntityByInvoice(String invoice){
		try{
			return payTradeDao.getEntityByInvoice(invoice);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}

	@Override
	public int save(PayTrade t) {
		return payTradeDao.save(t);
	}

	@Override
	public int update(PayTrade t) {
		return payTradeDao.update(t);
	}

	public PayTradeDao getPayTradeDao() {
		return payTradeDao;
	}

	public void setPayTradeDao(PayTradeDao payTradeDao) {
		this.payTradeDao = payTradeDao;
	}

	@Override
	public int updNotifyStatus(PayTrade payTrade) {
		return payTradeDao.updNotifyStatus(payTrade);
	}



	
	
	
}
