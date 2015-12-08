package com.fantingame.pay.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fantingame.pay.entity.PayEb;


public interface PayEbDao extends BaseDao<PayEb> {
	public PayEb getEntityByInvoice(String invoice);
	public int deleteByInvoice(String invoice);
	public int updAfterPay(PayEb payEb);
	public PayEb getEntityBySubId(Integer subId);
	public List<PayEb> getEntityByCondition(PayEb payEb);
	public List<PayEb> getListBySuccessTime(Map<String,Object> param);
	public List<PayEb> getListByMyCardNum(String myCardNum);
}
