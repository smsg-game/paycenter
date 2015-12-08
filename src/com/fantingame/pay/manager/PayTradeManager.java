package com.fantingame.pay.manager;

import java.util.List;

import com.fantingame.pay.entity.PayTrade;



public interface PayTradeManager extends BaseManager<PayTrade>{
	public PayTrade getByPartnerIdAndTradeId(PayTrade t);
	public PayTrade getEntityByInvoice(String invoice);
	public List<PayTrade> getNotifyTask();
	/**
	 * 更新通知：主要是解决mybatis多线程更新同一条记录数据不准确的问题
	 * <br>备注：payTrade不能为null，并且需要invoice是真正存在表里，notifyStatus需要设置值
	 * @param payTrade
	 * @return 更新成功返回大于0，否则小于或等于0
	 * @author leo.liao
	 */
	public int updNotifyStatus(PayTrade payTrade);
}
