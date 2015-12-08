package com.fantingame.pay.manager;

import com.fantingame.pay.entity.PayOrder;

public interface PayOrderManager extends BaseManager<PayOrder>{
	public PayOrder getEntityByInvoice(String invoice);
	
}
