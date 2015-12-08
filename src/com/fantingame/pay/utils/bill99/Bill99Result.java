package com.fantingame.pay.utils.bill99;

import javax.servlet.ServletRequest;

import com.fantingame.pay.exception.EasouPayException;

public class Bill99Result {
	
	public static String getActualResult(ServletRequest request , String md5Key) throws Exception {
		//获取充值卡网关账户号
		String merchantAcctId=(String)request.getParameter("merchantAcctId").trim();
		//获取网关版本.固定值  本代码版本号固定为v2.0
		String version=(String)request.getParameter("version").trim();
		//获取语言种类.固定选择值。 /1代表中文；2代表英文
		String language=(String)request.getParameter("language").trim();
		//获取支付方式 41 代表快钱账户支付；42 代表快钱默认支付方式，目前为卡密支付和快钱账户支付；52 代表卡密支付
		String payType=(String)request.getParameter("payType").trim();
		//充值卡序号 使用充值卡支付时返回
		String cardNumber=(String)request.getParameter("cardNumber").trim();
		//获取充值卡密码 使用充值卡支付时返回
		String cardPwd=(String)request.getParameter("cardPwd").trim();
		//获取商户订单号
		String orderId=(String)request.getParameter("orderId").trim();
		//获取原始订单金额 比方2 ，代表0.02元
		String orderAmount=(String)request.getParameter("orderAmount").trim();
		//获取快钱交易号 获取该交易在快钱的交易号
		String dealId=(String)request.getParameter("dealId").trim();
		//获取商户提交订单时的时间  如：20080101010101
		String orderTime=(String)request.getParameter("orderTime").trim();
		//获取扩展字段1 与商户提交订单时的扩展字段1保持一致
		String ext1=(String)request.getParameter("ext1").trim();
		//获取扩展字段2 与商户提交订单时的扩展字段2保持一致
		String ext2=(String)request.getParameter("ext2").trim();
		//获取实际支付金额 比方 2 ，代表0.02元
		String payAmount=(String)request.getParameter("payAmount").trim();
		//获取快钱处理时间
		String billOrderTime=(String)request.getParameter("billOrderTime").trim();
		//获取处理结果 10代表支付成功； 11代表支付失败
		String payResult=(String)request.getParameter("payResult").trim();
		//获取签名类型 1代表MD5签名 当前版本固定为1
		String signType=(String)request.getParameter("signType").trim();
		//充值卡类型 与提交订单时充值卡类型保持一致
		String bossType=(String)request.getParameter("bossType").trim();
		//支付卡类型
		//固定选择值：0、1、3、4、9，用户实际支付的卡类型
		//0代表神州行充值卡
		//1代表联通充值卡
		//3代表电信充值卡
		//4代表骏网一卡通
		//9代表已开通任一卡类型
		//例如商户提交bossType类型为9，用户实际支付卡类型为0，则receiveBossType返回为0
		String receiveBossType=(String)request.getParameter("receiveBossType").trim();
		//实际收款账号
		//用户实际支付的卡类型对应的收款账号
		//必须对应receiveBossType卡类型收款账号
		String receiverAcctId =(String)request.getParameter("receiverAcctId").trim();
		//获取加密签名串
		String signMsg=(String)request.getParameter("signMsg").trim();
		
		StringBuffer  merchantSignMsgVal=new StringBuffer();
		appendParam(merchantSignMsgVal,"merchantAcctId",merchantAcctId);
		appendParam(merchantSignMsgVal,"version",version);
		appendParam(merchantSignMsgVal,"language",language);
		appendParam(merchantSignMsgVal,"payType",payType);
		appendParam(merchantSignMsgVal,"cardNumber",cardNumber);
		appendParam(merchantSignMsgVal,"cardPwd",cardPwd);
		appendParam(merchantSignMsgVal,"orderId",orderId);
		appendParam(merchantSignMsgVal,"orderAmount",orderAmount);
		appendParam(merchantSignMsgVal,"dealId",dealId);
		appendParam(merchantSignMsgVal,"orderTime",orderTime);
		appendParam(merchantSignMsgVal,"ext1",ext1);
		appendParam(merchantSignMsgVal,"ext2",ext2);
		appendParam(merchantSignMsgVal,"payAmount",payAmount);
	    appendParam(merchantSignMsgVal,"billOrderTime",billOrderTime);
		appendParam(merchantSignMsgVal,"payResult",payResult);
		appendParam(merchantSignMsgVal,"signType",signType);
		appendParam(merchantSignMsgVal,"bossType",bossType);
		appendParam(merchantSignMsgVal,"receiveBossType",receiveBossType);
		appendParam(merchantSignMsgVal,"receiverAcctId",receiverAcctId);
		appendParam(merchantSignMsgVal,"key",md5Key);
		String merchantSignMsg=MD5Util.md5Hex(merchantSignMsgVal.toString().getBytes("gb2312")).toUpperCase();

		if(!signMsg.toUpperCase().equals(merchantSignMsg.toUpperCase())){
			throw new EasouPayException(null, "验签错误!");
		}
		return payResult;
	}
	
	

	public static void appendParam(StringBuffer returnStr, String paramId, String paramValue) {
		if (!returnStr.toString().equals("")) {
			if (!paramValue.equals("")) {
				returnStr.append("&").append(paramId).append("=").append(paramValue);
			}
		} else {
			if (!paramValue.equals("")) {
				returnStr.append(paramId).append("=").append(paramValue);
			}
		}
	}

}
