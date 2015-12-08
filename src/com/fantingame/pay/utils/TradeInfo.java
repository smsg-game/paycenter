package com.fantingame.pay.utils;

import java.util.HashMap;
import java.util.Map;

import com.easou.common.api.Md5SignUtil;


/**
 * 此类与EasouPayLib中的同名类保持同步
 * 
 * @author looming
 * 
 */
public class TradeInfo {
	/**
	 * 商家ID，非空、16 位数字
	 */
	private String partnerId;
	/**
	 * 商家自己产生的订单编号，回调和跳转时原样返回，此号不能有重复，非空、最长64 位字母、数字和下划线组成
	 */
	private String tradeId;
	/**
	 * 商家定义的商品名称，最长64个字符（英文字母和中文都只算一个字符）
	 */
	private String tradeName;
	/**
	 * 商家定义的商品描述，非空、最长1024 个字符（英文字母和中文都只算一个字符）
	 */
	private String tradeDesc;
	/**
	 * 本次支付的费用，人民币为单位，非空、大于0的数字，最多精确到小数点后两位
	 */
	private String reqFee;
	/**
	 * 商品是否可拆分，"false"为不可拆，"true"为可拆
	 * </br>若可拆分，则当用户账户余额小于商品金额时，可按账户余额等比例折算出实际能购的商品</br>
	 * 例如：购100游戏币，总价10元。用户账户余额为5元。若separable为"true",则<b>交易成功</b>，交易实际金额为5元。若
	 * separable为"false"，则<b>交易失败</b>
	 */
	private String separable;

	/**
	 * 支付结束后，会回调此url通知商家本次支付的结果，非空、255 位，需要符合 url 编码规则
	 */
	private String notifyUrl;

	/**
	 * 应用Id，同一游戏商的不同应用，此id需不一样
	 */
	private String appId;
	/**
	 * 玩家唯一标识，appId相同时，需保证该id唯一
	 */
	private String payerId;
	/**
	 * 渠道号，同一应用的不同的安装包，此名需不一样
	 */
	private String qn;

	/**
	 * 订单信息签名，签名方法参考约定算法
	 */
	private String sign;
	
	/**
	 * 支付完成后，浏览器回跳地址,sdk版可以不填
	 */
	private String redirectUrl;

	public TradeInfo() {
		
	}
	
	public String getStrForSign(){
		Map<String,String> map = new HashMap<String, String>();
		map.put("partnerId", getPartnerId());
		map.put("tradeId", getTradeId());
		map.put("tradeName", getTradeName());
		map.put("tradeDesc", getTradeDesc());
		map.put("reqFee", getReqFee());
		map.put("separable", getSeparable());
		map.put("notifyUrl", getNotifyUrl());
		
		map.put("appId", getAppId());
		map.put("payerId", getPayerId());
		map.put("qn", getQn());
		map.put("redirectUrl", getRedirectUrl());
		
		return Md5SignUtil.getStringForSign(map);
	}
	

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
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

	public String getSeparable() {
		return separable;
	}

	public void setSeparable(String separable) {
		this.separable = separable;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
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

	public String getPayerId() {
		return payerId;
	}

	public void setPayerId(String payerId) {
		this.payerId = payerId;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
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

	public String getReqFee() {
		return reqFee;
	}

	public void setReqFee(String reqFee) {
		this.reqFee = reqFee;
	}
}
