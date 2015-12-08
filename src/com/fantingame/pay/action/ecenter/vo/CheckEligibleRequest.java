package com.fantingame.pay.action.ecenter.vo;


import org.apache.log4j.Logger;

import com.easou.common.json.JsonUtil;
import com.fantingame.pay.utils.EncryptUtil;


public class CheckEligibleRequest {

	private static final Logger LOGGER = Logger.getLogger(CheckEligibleRequest.class);

	
	/**
	 * 可以支付 
	 */
	public static final int PAY_ELIGIBLE = 1;
	
	/**
	 * 不可以支付
	 */
	public static final int PAY_NOT_ELIGIBLE = 0;
	

	/**
	 * 游戏中订单id
	 */
	private String CP_TxID;
	
	/**
	 * MyaCard 对传过来的数据计算的签名
	 */
	private String SecurityKey;
	private String Account;
	private int Amount;
	private String Realm_ID;
	private String Character_ID;
	
	public boolean isRequestDataCorrect(String secrityKey) {
		String strToComputeSign = getStrToComputeSign(secrityKey);
		String computedSign = EncryptUtil.getSHA1(strToComputeSign);
		
		if (computedSign.equalsIgnoreCase(SecurityKey) == true) {
			return true;
		}
		LOGGER.error("MyCard check isEligible 签名错误. 计算签名使用的字符串[" + strToComputeSign + "] 请求数据 " + JsonUtil.parserObjToJsonStr(this));
		return false;
	}
	
	public String getStrToComputeSign(String secrityKey) {
		StringBuffer buf = new StringBuffer();
		buf.append(secrityKey);
		buf.append(CP_TxID);
		buf.append(Account);
		buf.append(Amount);
		buf.append(Realm_ID);
		buf.append(Character_ID);
		
		return buf.toString();
	}
	
	public String getGameOrderId() {
		return CP_TxID;
	}

	public String getCP_TxID() {
		return CP_TxID;
	}

	public void setCP_TxID(String cP_TxID) {
		CP_TxID = cP_TxID;
	}

	public String getSecurityKey() {
		return SecurityKey;
	}

	public void setSecurityKey(String securityKey) {
		SecurityKey = securityKey;
	}

	public String getAccount() {
		return Account;
	}

	public void setAccount(String account) {
		Account = account;
	}

	public int getAmount() {
		return Amount;
	}

	public void setAmount(int amount) {
		Amount = amount;
	}

	public String getRealm_ID() {
		return Realm_ID;
	}

	public void setRealm_ID(String realm_ID) {
		Realm_ID = realm_ID;
	}

	public String getCharacter_ID() {
		return Character_ID;
	}

	public void setCharacter_ID(String character_ID) {
		Character_ID = character_ID;
	}
	
	
}
