package com.fantingame.pay.dao;

import com.fantingame.pay.entity.PayOrder;


public interface PayOrderDao extends BaseDao<PayOrder> {
	public PayOrder getEntityByInvoice(String invoice);
	
	public int updateWhenPayed(PayOrder order);
}
