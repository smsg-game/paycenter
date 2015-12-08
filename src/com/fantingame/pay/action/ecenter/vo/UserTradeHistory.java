package com.fantingame.pay.action.ecenter.vo;



public class UserTradeHistory {
	
	private String invoice;
	
	private String tradeName;
	
	private String paidFee;
	
	private String createDatetime;
	
	private int type;
	
	private String channelName;
	
	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public UserTradeHistory() {
		
	}

	public UserTradeHistory(String invoice, String tradeName, String paidFee, String createDatetime, int type,String channelName) {
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

	public String getCreateDatetime() {
		return createDatetime;
	}

	public void setCreateDatetime(String createDatetime) {
		this.createDatetime = createDatetime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
