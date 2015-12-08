package com.fantingame.pay.action.ecenter.vo;

public class UserPayTrade {

	private String invoice;//交易订单流水号(本系统)
	private String tradeId;//交易名称
    private String tradeName;//交易名称
    private String tradeDesc;//交易描述
    private String reqFee;//请求支付金额
    private String paidFee;//已支付金额
    private Long reqEb;
    private String payerId;//游戏者ID
    private String easouId;//梵町用户ID
    private String username;//梵町用户名
    private Long eb;//用户e币余额
    
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
	public String getTradeDesc() {
		return tradeDesc;
	}
	public void setTradeDesc(String tradeDesc) {
		this.tradeDesc = tradeDesc;
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
	public Long getReqEb() {
		return reqEb;
	}
	public void setReqEb(Long reqEb) {
		this.reqEb = reqEb;
	}
	public String getPayerId() {
		return payerId;
	}
	public void setPayerId(String payerId) {
		this.payerId = payerId;
	}
	public String getEasouId() {
		return easouId;
	}
	public void setEasouId(String easouId) {
		this.easouId = easouId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Long getEb() {
		return eb;
	}
	public void setEb(Long eb) {
		this.eb = eb;
	}
    
}
