package com.fantingame.pay.manager;

import com.fantingame.pay.entity.SmsRule;


public interface SmsRuleManager extends BaseManager<SmsRule> {
	public SmsRule getEntityByCity(SmsRule smsRule);
}