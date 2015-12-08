package com.fantingame.pay.entity;

import java.util.Date;

/**
 * 接受游戏商的订单
 * */
public class PayEb {
	 private Long   id;
     private String invoice;//支付订单流水号(本系统)
     private String tradeNo;//属于游戏商的订单号
     private Long   channelId;//支付渠道
     private String easouId;//easouId
     private String paidFee;//交易金额
     private String paidCurrency;//货币类型
     private String reqFee;//要求交易金额，有可能与amount不一致，以amount为准
     private String reqCurrency;//货币类型
     private Double rate;//折算率
     private Integer status;//服务端notify的状态栏
     private String receiveStatus;//接收到的notify状态码
     private String receiveMsg;//接收到的notify信息
     private Date createDatetime;
     private Date successDatetime;//成功时间，失败留空即可
     private Date updateDatetime;
     private String otherInfo;

	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getReqCurrency() {
		return reqCurrency;
	}
	public void setReqCurrency(String reqCurrency) {
		this.reqCurrency = reqCurrency;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getSuccessDatetime() {
		return successDatetime;
	}
	public void setSuccessDatetime(Date successDatetime) {
		this.successDatetime = successDatetime;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public String getEasouId() {
		return easouId;
	}
	public void setEasouId(String easouId) {
		this.easouId = easouId;
	}
	public String getOtherInfo() {
		return otherInfo;
	}
	public void setOtherInfo(String otherInfo) {
		this.otherInfo = otherInfo;
	}
	
}
