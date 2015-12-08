package com.fantingame.pay.manager.impl;

import java.util.List;

import com.fantingame.pay.dao.PayChannelDao;
import com.fantingame.pay.entity.PayChannel;
import com.fantingame.pay.entity.TradeCode;
import com.fantingame.pay.exception.EasouPayException;
import com.fantingame.pay.manager.PayChannelManager;

public class PayChannelManagerImpl implements PayChannelManager{
	
	private PayChannelDao payChannelDao = null;


	public PayChannel getChannelByType(String type) throws Exception{
		PayChannel channel = payChannelDao.getChannelByType(type);
		if(channel == null){
			throw new EasouPayException(TradeCode.EASOU_CODE_130.code, TradeCode.EASOU_CODE_130.msgC);
		}else{
			return channel;
		}
	}

	
	@Override
	public int count(PayChannel t) {
		return payChannelDao.count(t);
	}

	@Override
	public int delete(List<String> ids) {
		return payChannelDao.delete(ids);
	}

	@Override
	public List<PayChannel> getEntity(PayChannel t) {
		return payChannelDao.getEntity(t);
	}

	@Override
	//@Cacheable(value="payPartner",key="#id")
	public PayChannel getEntityById(Long id) throws Exception{
		PayChannel channel = payChannelDao.getEntityById(id);
		if(channel == null){
			throw new EasouPayException(null , "不存在对应的支付渠道!");
		}else {
			return channel;
		}
	}

	@Override
	public int save(PayChannel t) {
		return payChannelDao.save(t);
	}

	@Override
	public int update(PayChannel t) {
		return payChannelDao.update(t);
	}

	public PayChannelDao getPayChannelDao() {
		return payChannelDao;
	}

	public void setPayChannelDao(PayChannelDao payChannelDao) {
		this.payChannelDao = payChannelDao;
	}
	
}
