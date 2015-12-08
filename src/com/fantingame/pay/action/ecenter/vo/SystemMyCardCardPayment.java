package com.fantingame.pay.action.ecenter.vo;

import java.math.BigDecimal;

/**
 * MyCard 点卡充值套餐
 * @author chengevo
 *
 */
public class SystemMyCardCardPayment {
	
	private String waresId;
	private String cardName;
	private int goldNum;
	
	/**
	 * 返还的彩钻数量
	 */
	private int extraGoldNum;
	
	private int isDouble;
	
	/**
	 * 点卡的售价（单位是新台币）
	 */
	private BigDecimal twdAmount;

	public BigDecimal getTwdAmount() {
		return twdAmount;
	}

	public void setTwdAmount(BigDecimal twdAmount) {
		this.twdAmount = twdAmount;
	}

	public String getWaresId() {
		return waresId;
	}

	public void setWaresId(String waresId) {
		this.waresId = waresId;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public int getGoldNum() {
		return goldNum;
	}

	public void setGoldNum(int goldNum) {
		this.goldNum = goldNum;
	}

	public int getExtraGoldNum() {
		return extraGoldNum;
	}

	public void setExtraGoldNum(int extraGoldNum) {
		this.extraGoldNum = extraGoldNum;
	}

	public int getIsDouble() {
		return isDouble;
	}

	public void setIsDouble(int isDouble) {
		this.isDouble = isDouble;
	}


}
