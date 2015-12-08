package com.fantingame.pay.dao;

import com.fantingame.pay.entity.PayUserBindHistory;


public interface PayUserBindHistoryDao extends BaseDao<PayUserBindHistory> {
	public PayUserBindHistory getEntityByMobile(String mobile);
}
