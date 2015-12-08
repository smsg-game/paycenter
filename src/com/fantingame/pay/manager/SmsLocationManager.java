package com.fantingame.pay.manager;

import com.fantingame.pay.entity.SmsLocation;


public interface SmsLocationManager extends BaseManager<SmsLocation> {
	public SmsLocation getEntityByMobile(String mobile) throws Exception;
}