package com.fantingame.pay.utils.mol;

import java.math.BigDecimal;


public class MolPaymentObj {
	private String applicationCode;
	private String referenceId;
	private String paymentId;
	private String version;
	private String amount;
	private String currencyCode;
	private String paymentStatusCode;
	private String paymentStatusDate;
	private String channelId;
	private String customerId;
	private String signature;
	public String getApplicationCode() {
		return applicationCode;
	}

	public void setApplicationCode(String applicationCode) {
		this.applicationCode = applicationCode;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getPaymentStatusCode() {
		return paymentStatusCode;
	}

	public void setPaymentStatusCode(String paymentStatusCode) {
		this.paymentStatusCode = paymentStatusCode;
	}

	public String getPaymentStatusDate() {
		return paymentStatusDate;
	}

	public void setPaymentStatusDate(String paymentStatusDate) {
		this.paymentStatusDate = paymentStatusDate;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	public BigDecimal getAmountInYuan() {
		return BigDecimal.valueOf(Double.valueOf(this.getAmount())).divide(new BigDecimal(100));
	}
}
