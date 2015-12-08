package com.fantingame.pay.utils.yeepay;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.methods.HttpPost;
import org.apache.log4j.Logger;

import com.fantingame.pay.action.common.BaseAction;
import com.fantingame.pay.action.mobile.CardAction;
import com.fantingame.pay.entity.PayChannel;
import com.fantingame.pay.entity.PayEb;
import com.fantingame.pay.entity.PayTrade;
import com.fantingame.pay.entity.TradeCode;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.HttpUtil;
import com.fantingame.pay.utils.StringTools;

public class YeePayUtil{
	
	private static Logger logger = Logger.getLogger(CardAction.class);

	public static  void getYeePayResult(BaseAction action,PayTrade payTrade,PayEb payEb,
			PayChannel channel,String cardAmt,String cardNumber,String cardPwd) throws Exception{

		Map<String,String> formEntity = phoneCardYeePayMap(payTrade,payEb,channel,cardAmt,cardNumber,cardPwd);
		
//		for(Map.Entry<String,String> entity : formEntity.entrySet()){
//			System.out.println(entity.getKey()+":"+entity.getValue());
//		}
		
		HttpPost httpPost = new HttpPost(channel.getOrderUrl());
		
		String content  = null;
		//第一次
		content = HttpUtil.getPostContent(Constants.HTTPCLIENT,httpPost,formEntity,"GBK");
		//第二次
		if(content==null){
			content = HttpUtil.getPostContent(Constants.HTTPCLIENT,httpPost,formEntity,"GBK");
		}
		//第三次
		if(content==null){
			content = HttpUtil.getPostContent(Constants.HTTPCLIENT,httpPost,formEntity,"GBK");
		}
		
		String r1_Code = null;
		if(content!=null){//获取到返回的正文
			String r0_Cmd = StringTools.getSpecVlaue(content, "r0_Cmd=", "\n");
			       r1_Code = StringTools.getSpecVlaue(content, "r1_Code=", "\n");
			String r6_Order = StringTools.getSpecVlaue(content, "r6_Order=", "\n");
			String rq_ReturnMsg = StringTools.getSpecVlaue(content, "rq_ReturnMsg=", "\n");
			String hmac = StringTools.getSpecVlaue(content, "hmac=", "\n");
			
			String signStr = r0_Cmd + r1_Code + r6_Order + rq_ReturnMsg;
			//验签
			if(!YeePayDigestUtil.hmacSign(signStr, channel.getSecretKey()).equals(hmac)){
				logger.error(TradeCode.YEEPAY_CODE_NEG_1.msgE+"...invoice:"+r6_Order);
				action.setStatus("fail");
				action.setMsg(TradeCode.YEEPAY_CODE_NEG_1.msgC);
			}
			//提交成功之后，开始轮训结果
			if("1".equals(r1_Code)){//成功
			   //循环查数据库
				action.setStatus("success");
				action.setMsg(TradeCode.EASOU_ORDER_1.msgC);
		    }else{//提交失败
		       action.setStatus("fail");
			   action.setMsg(TradeCode.getMsgC(Constants.OWN_YEEPAY_CODE,r1_Code));
		    }
			return ;
		}else{//返回正文为空
			logger.error(TradeCode.EASOU_ORDER_NEG_2.msgC+"...YeePay's resopnse content is null");
			action.setStatus("unknow");
			action.setMsg(TradeCode.EASOU_ORDER_NEG_2.msgC);
			return ;
		}
	}


	

	private static Map<String,String> phoneCardYeePayMap(PayTrade trade,PayEb payEb,PayChannel channel,
			String cardAmt,String cardNumber,String cardPwd) throws Exception{
		Map<String,String> map = new HashMap<String, String>();
		String[] signParams = new String[17];
		
		map.put("p0_Cmd","ChargeCardDirect");
		signParams[0] = map.get("p0_Cmd");
		
		map.put("p1_MerId",channel.getPartnerId());
		signParams[1] = map.get("p1_MerId");
		
		map.put("p2_Order",payEb.getId()+"");
		signParams[2] = map.get("p2_Order");
		
		map.put("p3_Amt",payEb.getReqFee());
		signParams[3] = map.get("p3_Amt");
		
		map.put("p4_verifyAmt","true");
		signParams[4] = map.get("p4_verifyAmt");
		
		map.put("p5_Pid",trade.getTradeName());
		signParams[5] = map.get("p5_Pid");
		
		map.put("p6_Pcat","");
		signParams[6] = map.get("p6_Pcat");
		
		map.put("p7_Pdesc",trade.getTradeDesc()==null?"":trade.getTradeDesc());
		signParams[7] = map.get("p7_Pdesc");
		
		map.put("p8_Url",channel.getNotifyUrl());
		signParams[8] = map.get("p8_Url");
		
		map.put("pa_MP","");
		signParams[9] = map.get("pa_MP");
		
		map.put("pa7_cardAmt",cardAmt);
		signParams[10] = map.get("pa7_cardAmt");
		
		map.put("pa8_cardNo",cardNumber);
		signParams[11] = map.get("pa8_cardNo");
		
		map.put("pa9_cardPwd",cardPwd);
		signParams[12] = map.get("pa9_cardPwd");
		
		map.put("pd_FrpId",channel.getCardCode());//SZX-神州行，TELECOM-电信，UNICOM-联通
		signParams[13] = map.get("pd_FrpId");
		
		map.put("pr_NeedResponse","1");
		signParams[14] = map.get("pr_NeedResponse");
		
		map.put("pz_userId","");
		signParams[15] = map.get("pz_userId");
		
		map.put("pz1_userRegTime","");
		signParams[16] = map.get("pz1_userRegTime");
		
		String hmac = YeePayDigestUtil.getHmac(signParams,channel.getSecretKey());
		map.put("hmac", hmac);
		
		return map;
	}
}
