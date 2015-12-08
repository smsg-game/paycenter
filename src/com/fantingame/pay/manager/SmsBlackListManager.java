package com.fantingame.pay.manager;

import com.fantingame.pay.entity.SmsBlackList;


public interface SmsBlackListManager extends BaseManager<SmsBlackList> {
	public SmsBlackList getEntityByMobile(String mobile) throws Exception;
}