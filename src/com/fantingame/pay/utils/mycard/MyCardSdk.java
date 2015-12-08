package com.fantingame.pay.utils.mycard;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fantingame.pay.entity.PayChannel;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.EncryptUtil;
import com.fantingame.pay.utils.UrlRequestUtils;


public class MyCardSdk {

	private static Logger LOGGER = Logger.getLogger(MyCardSdk.class); 
	
	private static MyCardSdk ins;
	

	
	public static MyCardSdk instance() {
		synchronized (MyCardSdk.class) { 
			if (ins == null) {
				ins = new MyCardSdk();
			}
		}
		
		return ins;
	}
	
	private MyCardSdk() {
	}
	public String apiAuth(String gameOrderId,PayChannel payChannel) {
		Map<String, String> map = new HashMap<String, String>();
		
		String strToComputeSign = payChannel.getPublicKey() + payChannel.getAccount() + gameOrderId + payChannel.getPrivateKey();
		String sign = EncryptUtil.getSHA256(strToComputeSign);
		
		LOGGER.debug("MyCard 获取授权码 - 计算签名使用的字符串[" + strToComputeSign + "] 计算得到的签名[" + sign + "]");
		
		map.put("facId", payChannel.getAccount());
		map.put("facTradeSeq", gameOrderId);
		map.put("hash", sign);
		
		String jsonStr = UrlRequestUtils.execute(payChannel.getOrderUrl(), map, UrlRequestUtils.Mode.GET);
		
		LOGGER.info("MyCard 获取授权码返回结果[" + jsonStr + "]");
		
		return jsonStr;
	}
	
	
	public String sdkAuth(String gameOrderId,String serverId,String CustomerId,String PaymentType,String ItemCode,String ProductName,String Amount,PayChannel payChannel) {
		Map<String, String> map = new HashMap<String, String>();
		
		String strToComputeSign = payChannel.getPublicKey() + payChannel.getAccount() + gameOrderId + payChannel.getPrivateKey();
		String sign = EncryptUtil.getSHA256(strToComputeSign);
		
		LOGGER.debug("MyCard 获取授权码 - 计算签名使用的字符串[" + strToComputeSign + "] 计算得到的签名[" + sign + "]");
		
		String SandBoxMode = Constants.IS_TEST_ENV+"";
		String Currency = "TWD";
		String TradeType = "1";
		map.put("FacServiceId", payChannel.getAccount());//廠商服務代碼  Service Code 由 MyCard 編列  Given by MyCard
		map.put("facTradeSeq", gameOrderId);
		map.put("TradeType", TradeType);
		map.put("ServerId", serverId);
		map.put("CustomerId", CustomerId);
		map.put("PaymentType", PaymentType);
		map.put("ItemCode", ItemCode);
		map.put("ProductName", ProductName);
		map.put("Amount", Amount);
		map.put("Currency", Currency);
		map.put("SandBoxMode", SandBoxMode);
		
		String preHashValue = payChannel.getAccount() + gameOrderId  +  TradeType  + serverId + CustomerId + PaymentType + 
				ItemCode + ProductName + Amount + Currency + SandBoxMode +  payChannel.getPrivateKey();
		
		map.put("Hash", preHashValue);
		
		String jsonStr = UrlRequestUtils.execute(payChannel.getOrderUrl(), map, UrlRequestUtils.Mode.GET);
		
		LOGGER.info("MyCard 获取授权码返回结果[" + jsonStr + "]");
		
		return jsonStr;
	}

}
