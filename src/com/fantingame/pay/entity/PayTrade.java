package com.fantingame.pay.entity;

import java.util.Date;

import com.fantingame.pay.utils.EbTools;

/**
 * 接受游戏商的订单
 * */
public class PayTrade {
     private String invoice;//交易订单流水号(本系统)
     private Long   partnerId;//合作伙伴ID
     private String tradeId;//交易订单流水号(合作伙伴)
     private String tradeName;//交易名称
     private String tradeDesc;//交易描述
     private String reqFee;//请求支付金额
     private String paidFee;//已支付金额
     private String currency;//货币类型
     private String notifyUrl;//回调URL
     private String redirectUrl;//支付成功后将会回调到该地址；
     private String appId;//应用ID
     private Integer separable;//是否可拆分
     private String payerId;//游戏者ID
     private String easouId;//梵町用户ID
     private String qn;
     private String imei;
     private String extInfo;//扩展信息字段
     private Integer status;//状态
     private Integer notifyStatus;//通知状态，0未通知,默认，1已通知，2通知失败
     private Date createDatetime;
     private Date successDatetime;
     private Date updateDatetime;
     private Long reqEb;
     String cookieQn;
     
     
     public PayTrade(){
    	 
     }
     
     public PayTrade(Long partnerId,String tradeId){
    	 this.partnerId = partnerId;
    	 this.tradeId = tradeId;
     }
     
	public Long getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}
	public String getTradeId() {
		return tradeId;
	}
	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}
	public String getTradeName() {
		return tradeName;
	}
	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getNotifyUrl() {
		return notifyUrl;
	}
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getQn() {
		return qn;
	}
	public void setQn(String qn) {
		this.qn = qn;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public Integer getNotifyStatus() {
		return notifyStatus;
	}
	public void setNotifyStatus(Integer notifyStatus) {
		this.notifyStatus = notifyStatus;
	}
	public String getExtInfo() {
		return extInfo;
	}
	public void setExtInfo(String extInfo) {
		this.extInfo = extInfo;
	}
	public Integer getSeparable() {
		return separable;
	}
	public void setSeparable(Integer separable) {
		this.separable = separable;
	}
	public String getInvoice() {
		return invoice;
	}
	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}
	public String getReqFee() {
		return reqFee;
	}
	public void setReqFee(String reqFee) {
		this.reqFee = reqFee;
	}
	public String getPaidFee() {
		return paidFee;
	}
	public void setPaidFee(String paidFee) {
		this.paidFee = paidFee;
	}
	public Date getSuccessDatetime() {
		return successDatetime;
	}
	public void setSuccessDatetime(Date successDatetime) {
		this.successDatetime = successDatetime;
	}
	public String getRedirectUrl() {
		return redirectUrl;
	}
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
	public String getTradeDesc() {
		return tradeDesc;
	}
	public void setTradeDesc(String tradeDesc) {
		this.tradeDesc = tradeDesc;
	}
	public String getPayerId() {
		return payerId;
	}
	public void setPayerId(String payerId) {
		this.payerId = payerId;
	}
	public Long getReqEb() {
		if(reqEb==null){
			this.reqEb=EbTools.rmbToEb(this.reqFee);
		}
		return reqEb;
	}
	public void setReqEb(Long reqEb) {
		this.reqEb = reqEb;
	}
	public String getEasouId() {
		return easouId;
	}
	public void setEasouId(String easouId) {
		this.easouId = easouId;
	}

	public String getCookieQn() {
		return cookieQn;
	}

	public void setCookieQn(String cookieQn) {
		this.cookieQn = cookieQn;
	}
}
