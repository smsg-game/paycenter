package com.fantingame.pay.action.ecenter.vo;

import java.math.BigDecimal;

import org.apache.log4j.Logger;


/**
 * 
 * @author chengevo
 *
 */
public class MyCardServerToServerPayment{
	
	private static final Logger LOGGER = Logger.getLogger(MyCardServerToServerPayment.class);
	
	private static final int PAY_SUCCESS = 1;
	
	private int CardKind;
	
	private String cardId;
	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}


	/**
	 * 卡片面额
	 */
	private int CardPoint;
	
	/**
	 * MyCard 订单号
	 */
	private String SaveSeq;
	
	/**
	 * CP 方订单号
	 */
	private String facTradeSeq;
	private String oProjNo;
	
	/**
	 * 返回状态码
	 */
	private int ReturnMsgNo;
	
	/**
	 * 返回消息
	 */
	private String ReturnMsg;
	
	private int goldNum;
	
	private BigDecimal finishAmount;

	
	public boolean isPaySucceed() {
		if (this.ReturnMsgNo == PAY_SUCCESS) {
			return true;
		}
		
		return false;
	}

	public int getCardKind() {
		return CardKind;
	}

	public void setCardKind(int cardKind) {
		CardKind = cardKind;
	}

	public int getCardPoint() {
		return CardPoint;
	}

	public void setCardPoint(int cardPoint) {
		CardPoint = cardPoint;
	}

	public String getSaveSeq() {
		return SaveSeq;
	}

	public void setSaveSeq(String saveSeq) {
		SaveSeq = saveSeq;
	}

	public String getFacTradeSeq() {
		return facTradeSeq;
	}

	public void setFacTradeSeq(String facTradeSeq) {
		this.facTradeSeq = facTradeSeq;
	}

	public String getOProjNo() {
		return oProjNo;
	}

	public void setOProjNo(String oProjNo) {
		this.oProjNo = oProjNo;
	}

	public int getReturnMsgNo() {
		return ReturnMsgNo;
	}

	public void setReturnMsgNo(int returnMsgNo) {
		ReturnMsgNo = returnMsgNo;
	}

	public String getReturnMsg() {
		return ReturnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		ReturnMsg = returnMsg;
	}

	public int getGoldNum() {
		return goldNum;
	}

	public void setGoldNum(int goldNum) {
		this.goldNum = goldNum;
	}

	public BigDecimal getFinishAmount() {
		return finishAmount;
	}

	public void setFinishAmount(BigDecimal finishAmount) {
		this.finishAmount = finishAmount;
	}
}
