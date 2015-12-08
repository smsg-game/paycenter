package com.fantingame.pay.job;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;


public class DelayItem implements Delayed {
	private int  retry     = 1;
	private long sendTime  = 0;
	private long delayTime = 0;
	private String invoice;//梵町支付订单流水号
	private String notifyUrl;//返回地址
	private String tradeId;//商家交易订单号
	private String tradeStatus;//交易状态
	private String paidFee;//已交易费用
	private String tradeName;//交易名称
	private String appId;//应用ID
	private String payerId;//付款者ID
	private String reqFee;//要求付款数目
	private Long   partnerId;
	private String notifyDatetime;
	
	public DelayItem(int retry){
		if(retry<=1){//马上发送，优先级最高
		    retry = 1;
		}else{
			this.delayTime = (retry-1)*10*1000;
		}
		this.sendTime  = (new Date()).getTime() + this.delayTime;
		this.retry = retry;
   }     
	
	@Override
	public long getDelay(TimeUnit unit) {
		return unit.convert(sendTime - System.currentTimeMillis(),TimeUnit.MILLISECONDS);
	}

	@Override
	public int compareTo(Delayed o) {
		  DelayItem that = (DelayItem) o;  
	      return sendTime > that.sendTime?1:-1;
	}

	public long getSendTime() {
		return sendTime;
	}

	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}

	public int getRetry() {
		return retry;
	}

	public void setRetry(int retry) {
		if(retry<=1){//马上发送，优先级最高
		    retry = 1;
		}else{
			this.delayTime = (retry-1)*10*1000;
		}
		this.sendTime  = (new Date()).getTime() + this.delayTime;
		this.retry = retry;
	}

	public long getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(long delayTime) {
		this.delayTime = delayTime;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getPayerId() {
		return payerId;
	}

	public void setPayerId(String payerId) {
		this.payerId = payerId;
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

	public Long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
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

	public String getNotifyDatetime() {
		return notifyDatetime;
	}

	public void setNotifyDatetime(String notifyDatetime) {
		this.notifyDatetime = notifyDatetime;
	}

}
