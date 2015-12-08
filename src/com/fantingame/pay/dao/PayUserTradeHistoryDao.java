package com.fantingame.pay.dao;

import java.util.List;

import com.fantingame.pay.action.ecenter.vo.UserTradeHis;
import com.fantingame.pay.entity.PayUserTradeHistory;

public interface PayUserTradeHistoryDao extends BaseDao<PayUserTradeHistory>{

	//查询用户的交易流水记录
	public List<PayUserTradeHistory> getTradeHistoryByUserId(PayUserTradeHistory payTradeHistory);
	
	public int getTradeHistoryByUserIdCount(Long userId);
	
	List<UserTradeHis> getTradeHisByUserId(PayUserTradeHistory payTradeHistory);
	
	/**
	 * 查询用户pc类型的交易条数
	 * @param userId
	 * @return
	 */
	public int getTradeHistoryPCByUserIdCount(PayUserTradeHistory payTradeHistory);
	
	/**
	 * /**
	 * 查询用户PC类型的交易记录
	 * @param payTradeHistory
	 * @return
	 */
	List<UserTradeHis> getTradeHisPCByUserId(PayUserTradeHistory payTradeHistory);

}
