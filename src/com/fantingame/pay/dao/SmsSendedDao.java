package com.fantingame.pay.dao;

import com.fantingame.pay.entity.SmsSended;


public interface SmsSendedDao extends BaseDao<SmsSended> {
	public SmsSended getEntityByLinkId(String linkId);
	public int updateMo(SmsSended smSended);
	public int updateMr(SmsSended smSended);
	public int countSendedToday(SmsSended smSended);
	public int countSendedMonth(SmsSended smSended);
	public int countMoneyToday(SmsSended smSended);
	public int countMoneyMonth(SmsSended smSended);
}
