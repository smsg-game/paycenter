package com.fantingame.pay.action.ecenter.vo;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import com.fantingame.pay.utils.EncryptUtil;




public class MyCardApiPayment {

	
	private static final Logger LOGGER = Logger
			.getLogger(MyCardApiPayment.class);

	private static final String PAY_SUCCESS = "1";

	/**
	 * 厂商编号，即 appId
	 */
	private String facId;

	/**
	 * 会员账号
	 */
	private String facMemId;

	/**
	 * CP 方订单号
	 */
	private String facTradeSeq;

	/**
	 * MyCard 订单号
	 */
	private String tradeSeq;

	/**
	 * 本次儲值之 MyCard 卡號
	 */
	private String cardId;

	/**
	 * 本次交易活動代號
	 */
	private String oProjNo;

	/**
	 * 
	 */
	private String cardKind;

	/**
	 * 卡片面額
	 */
	private int cardPoint;

	/**
	 * 回傳結果, 若為字串 1 則為成功,其他為失敗
	 */
	private String returnMsgNo;

	/**
	 * 錯誤代碼
	 */
	private String errorMsgNo;
	private String errorMsg;
	private String hash;

	/**
	 * 点卡的金额（单位是新台币），要根据 cardPoing 去 system_mycard_card_payment 中查出
	 */
	private BigDecimal finishAmount;

	/**
	 * 这笔订单可以获得的金币数量，要根据 cardPoing 去 system_mycard_card_payment 中查出
	 */
	private int goldNum;

	 
	
	
	public boolean isPaySucceed() {
		if (this.returnMsgNo.equalsIgnoreCase(PAY_SUCCESS)) {
			return true;
		}

		return false;
	}

	public boolean isPaymentDataVerified(String publicKey,String privateKey) {
		String strToComputeSign = this.getStrToComputeSign(publicKey,privateKey);
		String sign = EncryptUtil.getSHA256(strToComputeSign);
		if (sign.equalsIgnoreCase(hash.trim()) == true) {
			return true;
		}
		LOGGER.error("MyCard web-site 点卡充值签名错误 - 计算签名使用的字符串["
				+ strToComputeSign + "] 计算得到的签名[" + sign + "]");
		return false;
	}



	private String getStrToComputeSign(String publicKey,String privateKey) {
		StringBuffer buf = new StringBuffer();
		buf.append(publicKey);
		buf.append(this.facId);
		buf.append(this.facMemId);
		buf.append(this.facTradeSeq);
		buf.append(this.tradeSeq);
		buf.append(this.cardId);
		buf.append(this.oProjNo);
		buf.append(this.cardKind);
		buf.append(this.cardPoint);
		buf.append(this.returnMsgNo);
		buf.append(this.errorMsgNo);
		buf.append(this.errorMsg);
		buf.append(privateKey);
		return buf.toString();
	}

	public int getGameWaresId() {
		return this.cardPoint;
	}



	public String getExtInfo() {
//		String extInfo = "MyCard 点卡支付，点卡面额" + this.cardPoint;
		return cardId;
	}

	public String getFacId() {
		return facId;
	}

	public void setFacId(String facId) {
		this.facId = facId;
	}

	public String getFacMemId() {
		return facMemId;
	}

	public void setFacMemId(String facMemId) {
		this.facMemId = facMemId;
	}

	public String getFacTradeSeq() {
		return facTradeSeq;
	}

	public void setFacTradeSeq(String facTradeSeq) {
		this.facTradeSeq = facTradeSeq;
	}

	public String getTradeSeq() {
		return tradeSeq;
	}

	public void setTradeSeq(String tradeSeq) {
		this.tradeSeq = tradeSeq;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getOProjNo() {
		return oProjNo;
	}

	public void setOProjNo(String oProjNo) {
		this.oProjNo = oProjNo;
	}

	public String getCardKind() {
		return cardKind;
	}

	public void setCardKind(String cardKind) {
		this.cardKind = cardKind;
	}

	public int getCardPoint() {
		return cardPoint;
	}

	public void setCardPoint(int cardPoint) {
		this.cardPoint = cardPoint;
	}

	public String getReturnMsgNo() {
		return returnMsgNo;
	}

	public void setReturnMsgNo(String returnMsgNo) {
		this.returnMsgNo = returnMsgNo;
	}

	public String getErrorMsgNo() {
		return errorMsgNo;
	}

	public void setErrorMsgNo(String errorMsgNo) {
		this.errorMsgNo = errorMsgNo;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public BigDecimal getFinishAmount() {
		return finishAmount;
	}

	public void setFinishAmount(BigDecimal finishAmount) {
		this.finishAmount = finishAmount;
	}

	public int getGoldNum() {
		return goldNum;
	}

	public void setGoldNum(int goldNum) {
		this.goldNum = goldNum;
	}
}
