package com.fantingame.pay.action.pc.nofity;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import com.fantingame.pay.entity.PayEb;
import com.fantingame.pay.entity.TradeCode;
import com.fantingame.pay.manager.PayEbManager;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.LoggerUtil;
import com.fantingame.pay.utils.alipay.pc.util.AlipayNotify;

/**
 * 支付宝通知接口,主要处理支付宝发送过来的对订单支付结果
 * */

public class NotifyFromAlipayAction extends BaseReceiveNotifyAction {

	private static final long serialVersionUID = 5490792191825343426L;
	private static final Logger logger = Logger.getLogger(NotifyFromAlipayAction.class);
	private static final Lock lock = new ReentrantLock();

	public String execute() throws Exception {
		//设置编码
	    getResponse().setContentType("text/html;charset=UTF-8");
		PrintWriter out = getResponse().getWriter();
		try {
			//获取支付宝POST过来反馈信息
			Map<String,String> params = new HashMap<String,String>();
			Map requestParams = getRequest().getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i]
							: valueStr + values[i] + ",";
				}
				//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
				params.put(name, valueStr);
			}
			String channelStatus = params.get("trade_status");//交易状态 
			String totalFee =  params.get("total_fee");//交易金额
			String orderId = params.get("out_trade_no"); //外部交易号(商户交易号)
			String tradeNo = params.get("trade_no"); //支付宝交易号
			int easouStatus = Constants.EASOU_SERVER_STATUS_SUCCESS;
			if(AlipayNotify.verify(params)){//验证成功
				PayEbManager payEbManager = (PayEbManager)getBean("payEbManager");
				PayEb payEb = payEbManager.getEntityById(Long.valueOf(orderId));
				if(payEb == null){
					out.println("fail");
					return null;
				}
				if(channelStatus.equals("TRADE_FINISHED")|| channelStatus.equals("TRADE_SUCCESS")){
						lock.lock();
						try {
							 if(payEb.getStatus() == easouStatus){//已经处理过，不做处理
									out.println("success");
									return null;
								}
							//保存通知
							savePayNotify(orderId, tradeNo,Constants.CHANNEL_ID_ALIPAY_PC, easouStatus, totalFee);
//							doWorksAfterReceiveNotifyByEb(Long.parseLong(orderId), easouStatus, channelStatus, null, totalFee, tradeNo);
							doWorksAfterReceiveNotify(Long.parseLong(orderId), easouStatus, channelStatus, null, totalFee, tradeNo);
						}catch (Exception e) {
							LoggerUtil.error("tenpay error!"+e.getMessage(),e);
							out.println("fail");
							return null;
						}finally{
							lock.unlock();
						}
				}else{
					LoggerUtil.info("alipay error!channelStatus:"+channelStatus);
					if("WAIT_BUYER_PAY".equals(channelStatus)){//等待支付
						easouStatus = Constants.EASOU_SERVER_STATUS_WAITING;
					}else{
						easouStatus = Constants.EASOU_SERVER_STATUS_FAIL;
					}
					payEb.setStatus(easouStatus);
					payEb.setUpdateDatetime(new Date());
					payEb.setReceiveStatus(channelStatus);
					payEbManager.update(payEb);
				}
				//回应支付宝
				out.println("success");
			}else{//验证失败
				LoggerUtil.error("签名验证失败");
				out.println("fail");
				logger.error(TradeCode.EASOU_CODE_110.msgE + "...notifyData:"+params);
			}
		} catch (Exception e) {
			LoggerUtil.error(e.getMessage(),e);
			out.println("fail");
		} 
		return null;
	}
	
	
	
}
