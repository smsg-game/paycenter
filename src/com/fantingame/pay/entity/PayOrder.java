package com.fantingame.pay.entity;

import java.util.Date;

/**
 * 接受游戏商的订单
 * */
public class PayOrder {
     private String invoice;//支付订单流水号(本系统)
     private String tradeNo;//属于游戏商的订单号
     private Long   channelId;//支付渠道
     private String appId;//appId
     private String paidFee;//交易金额
     private String paidCurrency;//货币类型
     private String reqFee;//要求交易金额，有可能与amount不一致，以amount为准
     private Integer serverStatus;//服务端notify的状态栏
     private Integer clientStatus;//客户端notify的状态栏
     private String  clientMsg;
     private String receiveStatus;
     private String receiveMsg;
     private Date createDatetime;
     private Date updateDatetime;

	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	public Integer getServerStatus() {
		return serverStatus;
	}
	public void setServerStatus(Integer serverStatus) {
		this.serverStatus = serverStatus;
	}
	public Integer getClientStatus() {
		return clientStatus;
	}
	public void setClientStatus(Integer clientStatus) {
		this.clientStatus = clientStatus;
	}
	public Date getCreateDatetime() {
		return createDatetime;
	}
	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}
	public Date getUpdateDatetime() {
		return updateDatetime;
	}
	public void setUpdateDatetime(Date updateDatetime) {
		this.updateDatetime = updateDatetime;
	}
	public Long getChannelId() {
		return channelId;
	}
	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}
	public String getPaidFee() {
		return paidFee;
	}
	public void setPaidFee(String paidFee) {
		this.paidFee = paidFee;
	}
	public String getPaidCurrency() {
		return paidCurrency;
	}
	public void setPaidCurrency(String paidCurrency) {
		this.paidCurrency = paidCurrency;
	}
	public String getReqFee() {
		return reqFee;
	}
	public void setReqFee(String reqFee) {
		this.reqFee = reqFee;
	}
	public String getClientMsg() {
		return clientMsg;
	}
	public void setClientMsg(String clientMsg) {
		this.clientMsg = clientMsg;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getInvoice() {
		return invoice;
	}
	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}
	public String getReceiveStatus() {
		return receiveStatus;
	}
	public void setReceiveStatus(String receiveStatus) {
		this.receiveStatus = receiveStatus;
	}
	public String getReceiveMsg() {
		return receiveMsg;
	}
	public void setReceiveMsg(String receiveMsg) {
		this.receiveMsg = receiveMsg;
	}
  
	
}
