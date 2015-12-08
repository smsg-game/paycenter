package com.fantingame.pay.utils.mo9;

import java.util.TreeMap;

import com.fantingame.pay.entity.PayChannel;
import com.fantingame.pay.entity.PayEb;
import com.fantingame.pay.entity.PayTrade;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.StringTools;
import com.mokredit.payment.Md5Encrypt;

public class Mo9Util {
	
	public static String getReqUrl(PayTrade payTrade,PayEb payEb,PayChannel channel,String returnUrl) throws Exception{
		TreeMap<String,String> map = new TreeMap<String, String>();
		StringTools.setValue(map,"pay_to_email",channel.getAccount(),50,true,false);
		StringTools.setValue(map,"version",channel.getVersion(),10,true,false);
		StringTools.setValue(map,"return_url",returnUrl,250,false,false);
		StringTools.setValue(map,"notify_url",channel.getNotifyUrl(),250,true,false);
		StringTools.setValue(map,"invoice",""+payEb.getId(),32,true,false);
		map.put("lc","CN");
		map.put("payer_id",payEb.getEasouId());
		StringTools.setValue(map,"amount",payTrade.getReqFee(),10,true,true);
		StringTools.setValue(map,"currency",payTrade.getCurrency(),3,true,false);
		StringTools.setValue(map,"item_name",payTrade.getTradeName(),30,true,false);
		if(payTrade.getSeparable()==1){
			StringTools.setValue(map,"app_id",Constants.APPID_SEPARABLE,255,true,false);
		}else{
			StringTools.setValue(map,"app_id",Constants.APPID_NOT_SEPARABLE,255,true,false);
		}
		String sign = Md5Encrypt.sign(map, channel.getSecretKey());
		StringTools.setValue(map,"sign",sign,32,true,false);
		return channel.getOrderUrl() + StringTools.getEncodedUrl(map);
	}
	
}
