package com.fantingame.pay.dao;

import com.fantingame.pay.entity.SmsBlackList;


public interface SmsBlackListDao extends BaseDao<SmsBlackList> {
	public SmsBlackList getEntityByMobile(String mobile);
}
