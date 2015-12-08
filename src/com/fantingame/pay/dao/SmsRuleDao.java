package com.fantingame.pay.dao;

import com.fantingame.pay.entity.SmsRule;


public interface SmsRuleDao extends BaseDao<SmsRule> {
	public SmsRule getEntityByCity(SmsRule smsRule);
}
