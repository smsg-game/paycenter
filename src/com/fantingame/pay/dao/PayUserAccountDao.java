package com.fantingame.pay.dao;

import com.fantingame.pay.entity.PayUserAccount;

public interface PayUserAccountDao  extends BaseDao<PayUserAccount>{
	
	//根据用户ID获取对象
	public PayUserAccount getUserAcountByJuserId(Long juserId);
	public int updEb(PayUserAccount payUserAccount);
}
