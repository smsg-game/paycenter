package com.fantingame.pay.dao;

import com.fantingame.pay.entity.SmsLocation;


public interface SmsLocationDao extends BaseDao<SmsLocation> {
	public SmsLocation getEntityByMobile(String mobile);
}
