package com.fantingame.pay.utils;


public class NotifyInfo {
	private String invoice;// 票号
	private String tradeId;// 游戏商的订单号
	private String tradeName; // 游戏商订单名称
	private String paidFee;// 已付费用
	private String tradeStatus;// 交易状态
	private String payerId;// 付款者
	private String appId;// 应用ID
	private String reqFee;// 请求付款费用
	private String notifyDatetime;//通知时间
	/**
	 * 信息签名
	 */
	private String sign;

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
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

	public String getPaidFee() {
		return paidFee;
	}

	public void setPaidFee(String paidFee) {
		this.paidFee = paidFee;
	}

	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public String getPayerId() {
		return payerId;
	}

	public void setPayerId(String payerId) {
		this.payerId = payerId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getReqFee() {
		return reqFee;
	}

	public void setReqFee(String reqFee) {
		this.reqFee = reqFee;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public String getNotifyDatetime() {
		return notifyDatetime;
	}

	public void setNotifyDatetime(String notifyDatetime) {
		this.notifyDatetime = notifyDatetime;
	}

}
