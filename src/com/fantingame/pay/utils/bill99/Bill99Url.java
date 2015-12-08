package com.fantingame.pay.utils.bill99;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.http.client.methods.HttpPost;

import com.fantingame.pay.action.common.BaseAction;
import com.fantingame.pay.entity.PayChannel;
import com.fantingame.pay.entity.PayEb;
import com.fantingame.pay.entity.PayTrade;
import com.fantingame.pay.entity.TradeCode;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.HttpUtil;
import com.fantingame.pay.utils.StringTools;

public class Bill99Url {

	public static void bill99Resopnse(BaseAction action,PayTrade payTrade,PayEb payEb,
			PayChannel channel,String cardAmt,String cardNumber,String cardPwd) throws Exception{
	
		Map<String, String> postParams = get99BillMapl(payTrade, payEb, channel, cardNumber, cardPwd,cardAmt);
		HttpPost httpPost = new HttpPost(channel.getOrderUrl());
 		String content = HttpUtil.getPostContent(Constants.HTTPCLIENT, httpPost, postParams, "utf-8");
 		String returncode = "";
 		if(content!=null){  //添加成功失败判断信息，并写入到data中；
 			int codePos = content.indexOf("returncode=");
 			if(codePos!=-1){
 				returncode = content.substring(codePos+11,content.length());
 			}
 		}
 		if("120".equals(returncode)){
 			action.setStatus("success");
 			action.setMsg(TradeCode.EASOU_ORDER_1.msgC);
		    return ;
 		}else{
 			action.setStatus("fail");
 			action.setMsg(Bill99Code.getMsg(returncode));
 		}
	}
	
	
	public static Map<String,String> get99BillMapl(PayTrade trade,PayEb payEb,PayChannel channel,String cardNo,String cardPwd,String cardAmt) throws Exception{
		Map<String,String> map = new LinkedHashMap<String, String>();
		map.put("inputCharset","1");
		map.put("bgUrl",channel.getNotifyUrl());
		map.put("version",channel.getVersion());
		map.put("language","1");
		map.put("signType","1");
        map.put("merchantAcctId",channel.getPartnerId());
		map.put("orderId",payEb.getId()+"");
		map.put("orderAmount",Float.valueOf(Float.valueOf(cardAmt)*100).intValue()+"");
		map.put("payType","52");
		map.put("cardNumber",cardNo);
		map.put("cardPwd",cardPwd);
		map.put("fullAmountFlag","1");
		map.put("orderTime",StringTools.getSecondFormat().format(payEb.getCreateDatetime()));
		map.put("ext1",channel.getId()+"");
		map.put("bossType",channel.getCardCode());
		map.put("key", channel.getSecretKey());
		StringBuffer signStr = new StringBuffer();
		for(Map.Entry<String,String> entity : map.entrySet()){
			signStr.append(entity.getKey()).append("=").append(entity.getValue()).append("&");
		}
		String stringForSign = signStr.substring(0, signStr.length()-1);
		String sign =  MD5Util.md5Hex(stringForSign.getBytes("gb2312")).toUpperCase();
		map.put("signMsg", sign);
		map.remove("key");//key只用于参与签名，但不能传递过去；
		return map;
	}
	
}
