package com.fantingame.pay.manager;

import java.util.List;

import com.fantingame.pay.action.ecenter.vo.UserTradeHis;
import com.fantingame.pay.entity.PayUserAccount;
import com.fantingame.pay.entity.PayUserTradeHistory;

public interface PayUserAccountManager  extends BaseManager<PayUserAccount>{
    //根据用户ID获取对象
	public PayUserAccount getUserAcountByJuserId(Long juserId);
	//更新账户EB数量，并记流水
	public int  updEb(Long userId,String tradeName,String orderId,String channelName,Long num);
	//更新账户EB数量以及手机号，并记流水
	/*public int  updGiftForBindMobile(Long userId,String mobile);*/
	//查询用户的交易流水记录
	public List<PayUserTradeHistory> getTradeHistoryByUserId(PayUserTradeHistory dealFlow);
	//按用户ID统计交易流水记录条数
	public int getTradeHistoryByUserIdCount(Long userId);
	//按用户ID统计PC交易流水记录条数
	public int getTradeHistoryPCByUserIdCount(PayUserTradeHistory dealFlow);
	//查询用户PC的交易流水记录
	public List<UserTradeHis> getTradeHistoryPCByUserId(PayUserTradeHistory dealFlow);
	//开户
	public int save(PayUserAccount account);
	//更新
	public int update(PayUserAccount account);
	
	public List<UserTradeHis> getTradeHisByUserId(PayUserTradeHistory payUserTradeHistory);

}