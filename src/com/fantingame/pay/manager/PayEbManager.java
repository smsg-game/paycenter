package com.fantingame.pay.manager;

import java.util.Date;
import java.util.List;

import com.fantingame.pay.action.ecenter.vo.UserTradeHistory;
import com.fantingame.pay.entity.PayChannel;
import com.fantingame.pay.entity.PayEb;
import com.fantingame.pay.entity.PayTrade;
import com.fantingame.pay.entity.User;
import com.fantingame.pay.utils.PageData;
import com.fantingame.pay.utils.PageUtil;

public interface PayEbManager extends BaseManager<PayEb>{
	
	/**
	 * 创建E币充值订单
	 */
	public PayEb getEntityByInvoice(String invoice);
	
	/**
	 * 收到notify后，更新订单状态
	 */
	public int updAfterRecNotify(PayTrade payTrade,PayEb payEb) throws Exception;
	
	public int updAfterRecNotify2(PayTrade payTrade,PayEb payEb) throws Exception;
	
	public int updEbForBookMall(PayTrade payTrade,PayEb payEb) throws Exception;

	/**
	 * 查询流水
	 */
	public PageUtil getTradeHistoryByUserId(int page, int rowsPerPage, Long userId);
	/**
	 * 用订单号的后4位来查询订单（一天之内）
	 * */
	public PayEb getEntityBySubId(Integer subId);
	
	public PageData<UserTradeHistory> getTradeHisByUserId(int page, int pageSize, Long userId);
	
	/**
	 * 查询用户pc版本充值记录
	 * @param page
	 * @param pageSize
	 * @param userId
	 * @return
	 */
	public PageData<UserTradeHistory> getTradeHisPCByUserId(int page, int pageSize, Long userId,String startTime,String endTime);

	/**
	 * 查询当天内有没有创建相同的订单
	 * @param payEb
	 * @return
	 */
	public List<PayEb> getEntityByCondition(PayEb payEb);
	/**
	 * 创建Eb订单
	 * @author qinhua
	 * @param payChannel
	 * @param reqFee
	 * @param otherInfo
	 * @return
	 * @throws Exception
	 */
	public PayEb addEbOrder(PayChannel payChannel,User user,String reqFee,String otherInfo) throws Exception;
	/**
	 * 获取mycard订单列表(服务器运营商)
	 * @param startTime
	 * @param endTime
	 * @param myCardNum
	 * @return
	 */
	public List<PayEb> getMyCardLogService(Date startTime,Date endTime,String myCardNum,String type);
}