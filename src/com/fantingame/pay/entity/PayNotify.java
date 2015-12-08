package com.fantingame.pay.entity;

import java.util.Date;

public class PayNotify {
     private Long   id;
     private String invoice;
     private String tradeNo;
     private Long   channelId;
     private String payerId;
     private String paidFee;
     private String paidCurrency;
     private String reqFee;
     private String reqCurrency;
     private String tradeName;
     private String appId;
     private Integer status;
     private Date   createDatetime;
     
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
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
	public String getReqCurrency() {
		return reqCurrency;
	}
	public void setReqCurrency(String reqCurrency) {
		this.reqCurrency = reqCurrency;
	}
	public String getTradeName() {
		return tradeName;
	}
	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public Date getCreateDatetime() {
		return createDatetime;
	}
	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getInvoice() {
		return invoice;
	}
	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}
	public String getPayerId() {
		return payerId;
	}
	public void setPayerId(String payerId) {
		this.payerId = payerId;
	}
     
	
	
	
	
}
