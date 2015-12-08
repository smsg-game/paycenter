package com.fantingame.pay.manager;

import com.fantingame.pay.entity.SmsSended;


public interface SmsSendedManager extends BaseManager<SmsSended> {
	public SmsSended getEntityByLinkId(String linkId);
	public int updateMo(SmsSended smsSended);
	public int updateMr(SmsSended smsSended);
	public int countSendedToday(SmsSended smsSended);
	public int countSendedMonth(SmsSended smsSended);
	public int countMoneyToday(SmsSended smsSended);
	public int countMoneyMonth(SmsSended smsSended);
}