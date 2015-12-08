package com.fantingame.pay.action.mobile.notify;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.easou.common.util.StringUtil;
import com.fantingame.pay.entity.PayChannel;
import com.fantingame.pay.entity.TradeCode;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.RsaSignUtil;
import com.fantingame.pay.utils.StringTools;
import com.fantingame.pay.utils.XmlUtil;
import com.fantingame.pay.utils.unionpay.Base64;
import com.fantingame.pay.utils.unionpay.MD5;

/**
 * 易联通知接口,主要处理易联发送过来的对订单支付结果
 * */

public class NotifyFromUnionPayAction extends BaseReceiveNotifyAction {

	private static final long serialVersionUID = 5490792191825343426L;
	private static final Logger logger = Logger.getLogger(NotifyFromUnionPayAction.class);
	private static long channelId=0L;
	public String execute() throws Exception {
		//设置编码
	    getResponse().setContentType("text/html;charset=UTF-8");
		PrintWriter out = getResponse().getWriter();
		//判断为测试还是正式平台
		if(Constants.IS_TEST_ENV)
			channelId=Constants.CHANNEL_ID_UNIONPAY_TEST;
		else
			channelId=Constants.CHANNEL_ID_UNIONPAY;
		try {			
			//检查参数
			HashMap<String, String> params = getParamsMap();
			//验证Mac
			if(!checkMac(params)){
				out.print(TradeCode.EASOU_CODE_100.msgE);
				logger.error(TradeCode.EASOU_CODE_100.msgE + "... The mac is illegal...");
				return null;
			}
			
			if (params==null || params.isEmpty()) {    //如果没有有效数据
				out.print(TradeCode.EASOU_CODE_100.msgE);
				logger.error(TradeCode.EASOU_CODE_100.msgE + "... UnionPay notifyData is null...");
				return null;
			}
			//获取参数
			String channelStatus = StringTools.getValue("orderState",params.get("orderState"),4,true,false);//交易状态 
			String totalFee =  StringTools.getValue("amount",String.valueOf(Double.parseDouble(params.get("amount"))),16,true,true);//交易金额
			String orderId = StringTools.getValue("orderNo",params.get("orderNo"),64,true,false); //外部交易号(商户交易号)
			String tradeNo = orderId; //易联交易订单号
			String channelMsg= StringTools.getValue("description", params.get("description"), 64, true, false); //状态描述
			//解析参数,获取EB订单号
			orderId=orderId.split("\\|")[0];
			orderId=orderId.substring(2,orderId.length());
			int easouStatus = Constants.EASOU_SERVER_STATUS_CREATE;
			
			if(Constants.UNION_PAY_PAY_SUC.equals(channelStatus)){//支付状态为成功
				easouStatus = Constants.EASOU_SERVER_STATUS_SUCCESS;
			}else{
				easouStatus = Constants.EASOU_SERVER_STATUS_FAIL;
			}
			//保存通知
			savePayNotify(orderId, tradeNo, channelId, easouStatus, totalFee);
			//后续操作
			doWorksAfterReceiveNotify(Long.parseLong(orderId), easouStatus, channelStatus, channelMsg, totalFee, tradeNo);
			//回应易联
			out.print("0000");
			return null;
		} catch (Exception e) {
			out.print("fail");
			logger.error(e.getMessage(),e);
		} 
		return null;
	}

	/**
	 * 验证异步回调参数mac
	 * @param params
	 * @return
	 * @throws Exception
	 */
	private boolean checkMac(Map<String,String> params) throws Exception {
		String macBlock=params.get("procCode");
		if(!StringUtil.isEmpty(params.get("accountNum")))
			macBlock+=" "+params.get("accountNum").trim();
		if(!StringUtil.isEmpty(params.get("processCode")))
			macBlock+=" "+params.get("processCode");
		if(!StringUtil.isEmpty(params.get("amount")))
			macBlock+=" "+params.get("amount");
		if(!StringUtil.isEmpty(params.get("transDatetime")))
			macBlock+=" "+params.get("transDatetime");
		if(!StringUtil.isEmpty(params.get("acqSsn")))
			macBlock+=" "+params.get("acqSsn");
		if(!StringUtil.isEmpty(params.get("upsNo")))
			macBlock+=" "+params.get("upsNo");
		if(!StringUtil.isEmpty(params.get("tsNo")))
			macBlock+=" "+params.get("tsNo");
		if(!StringUtil.isEmpty(params.get("reference")))
			macBlock+=" "+params.get("reference").trim().toUpperCase();
		if(!StringUtil.isEmpty(params.get("respCode")))
			macBlock+=" "+params.get("respCode").toUpperCase();
		
		if(!StringUtil.isEmpty(params.get("terminalNo")))
			macBlock+=" "+params.get("terminalNo").trim();
		if(!StringUtil.isEmpty(params.get("merchantNo")))
			macBlock+=" "+params.get("merchantNo").trim().toUpperCase();
		if(!StringUtil.isEmpty(params.get("orderNo")))
			macBlock+=" "+params.get("orderNo").trim().toUpperCase();
		if(!StringUtil.isEmpty(params.get("orderState")))
			macBlock+=" "+params.get("orderState").trim();
		String mac=params.get("mac");
		
		PayChannel payChannel=getChannelById(channelId);
		String secretKey=payChannel.getSecretKey();
		String myMac=new MD5().getMD5Str(macBlock+" "+secretKey);
		if(mac.equals(myMac)){
			logger.info("支付成功  respCode="+params.get("respCode").toUpperCase()+",orderState="+params.get("orderState").trim()+",tsNo="+params.get("tsNo"));
			return true;
		}
		else{ 
			return false;
		}
	}

	/**
	 * 获取异步回调参数
	 * @return
	 */
	private HashMap<String, String> getParamsMap() {
		HashMap<String,String> params=new HashMap<String, String>();
		params.put("procCode", getParam("ProcCode")); 	//报文类型
		params.put("accountNum", getParam("AccountNum")); //用户帐号
		params.put("processCode", getParam("ProcessCode")); //处理码
		params.put("amount", getParam("Amount")); //交易金额
		params.put("curCode", getParam("CurCode"));  //交易币种
		params.put("transDatetime", getParam("TransDatetime")); //传输日期和时间
		params.put("acqSsn", getParam("AcqSsn"));  //系统跟踪号
		params.put("lTime", getParam("Ltime"));		//本地交易时间
		params.put("lDate", getParam("Ldate"));		//本地交易日期
		params.put("reference", getParam("Reference")); //交易参考
		params.put("returnAddress", getParam("ReturnAddress")); //返回地址
		params.put("respCode", getParam("RespCode"));  //响应码
		params.put("remark", getParam("Remark")); //备注
		params.put("terminalNo", getParam("TerminalNo")); //终端号
		params.put("merchantNo", getParam("MerchantNo")); //商户号
		params.put("orderNo", getParam("OrderNo"));  //订单编号
		params.put("orderState", getParam("OrderState"));  //订单状态
		params.put("description", getParam("Description")); //订单描述
		params.put("orderType", getParam("OrderType"));  //订单类型
		params.put("transData", getParam("TransData"));  //业务交换数据
		params.put("upsNo", getParam("UpsNo"));  //银联流水号
		params.put("tsNo", getParam("TsNo"));   //终端流水号
		params.put("mac", getParam("Mac"));  //校验码
		return params;
	}
	
	
	
}
