package com.fantingame.pay.action.ecenter.vo;

import java.sql.Timestamp;


public class UserTradeHis {
	
	private String invoice;
	
	private String tradeName;
	
	private String paidFee;
	
	private Timestamp createDatetime;
	
	private int type;
	
	private String channelName;

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public UserTradeHis() {
		
	}
	
	public UserTradeHis(String invoice, String tradeName, String paidFee, Timestamp createDatetime, int type,String channelName) {
		this.invoice = invoice;
		this.tradeName = tradeName;
		this.paidFee = paidFee;
		this.createDatetime = createDatetime;
		this.type = type;
		this.channelName=channelName;
	}
	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
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

	public Timestamp getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(Timestamp createDatetime) {
		this.createDatetime = createDatetime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	
}
