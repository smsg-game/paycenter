package com.fantingame.pay.action.pc.nofity;

import java.io.PrintWriter;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import com.fantingame.pay.entity.PayChannel;
import com.fantingame.pay.entity.PayEb;
import com.fantingame.pay.manager.PayEbManager;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.EbTools;
import com.fantingame.pay.utils.LoggerUtil;
import com.fantingame.pay.utils.tenpay.RequestHandler;
import com.fantingame.pay.utils.tenpay.ResponseHandler;
import com.fantingame.pay.utils.tenpay.client.ClientResponseHandler;
import com.fantingame.pay.utils.tenpay.client.TenpayHttpClient;

/**
 * 财付通通知接口,主要处理支付宝发送过来的对订单支付结果
 * */

public class NotifyFromTenpayAction extends BaseReceiveNotifyAction {

	private static final long serialVersionUID = 5490792191825343426L;
	private static final Logger logger = Logger.getLogger(NotifyFromTenpayAction.class);
	
	private static final Lock lock = new ReentrantLock();

	public String execute() throws Exception {
		//设置编码
	    getResponse().setContentType("text/html;charset=UTF-8");
		PrintWriter out = getResponse().getWriter();
		try {
			PayChannel channel = getChannelById(Constants.CHANNEL_ID_TENPAY);
			//---------------------------------------------------------
			//财付通支付通知（后台通知）示例，商户按照此文档进行开发即可
			//---------------------------------------------------------
			//商户号
			String partner = channel.getPartnerId();
			//密钥
			String key = channel.getPrivateKey();
			//创建支付应答对象
			ResponseHandler resHandler = new ResponseHandler(getRequest(),getResponse());
			resHandler.setKey(key);
			//判断签名
			if(resHandler.isTenpaySign()) {
				//通知id
				String notify_id = resHandler.getParameter("notify_id");
				//创建请求对象
				RequestHandler queryReq = new RequestHandler(null, null);
				//通信对象
				TenpayHttpClient httpClient = new TenpayHttpClient();
				//应答对象
				ClientResponseHandler queryRes = new ClientResponseHandler();
				//通过通知ID查询，确保通知来至财付通
				queryReq.init();
				queryReq.setKey(key);
				queryReq.setGateUrl("https://gw.tenpay.com/gateway/verifynotifyid.xml");
				queryReq.setParameter("partner", partner);
				queryReq.setParameter("notify_id", notify_id);
				//通信对象
				httpClient.setTimeOut(5);
				//设置请求内容
				httpClient.setReqContent(queryReq.getRequestURL());
				//后台调用
				if(httpClient.call()) {
					//设置结果参数
					queryRes.setContent(httpClient.getResContent());
					queryRes.setKey(key);
					//获取返回参数
					String retcode = queryRes.getParameter("retcode");
					String trade_state = queryRes.getParameter("trade_state");
				
					String trade_mode = queryRes.getParameter("trade_mode");
					int easouStatus = Constants.EASOU_SERVER_STATUS_SUCCESS;
					long payId = Long.valueOf(queryRes.getParameter("out_trade_no"));
					PayEbManager payEbManager = (PayEbManager)getBean("payEbManager");
					PayEb payEb = null;
					//判断签名及结果
					if(queryRes.isTenpaySign()&& "0".equals(retcode) && "0".equals(trade_state) && "1".equals(trade_mode)) {
						lock.lock();
						try {
							payEb = payEbManager.getEntityById(payId);
							if(payEb == null){
								out.println("fail");
								return null;
							}
							String tradeNo = queryRes.getParameter("transaction_id");
							String totalFee = queryRes.getParameter("total_fee");
							LoggerUtil.error("status:"+payEb.getStatus());
							if(payEb.getStatus() == easouStatus){//已经处理过，不做处理
								resHandler.sendToCFT("Success");
								return null;
							}
							//保存通知
							savePayNotify(Long.toString(payId),tradeNo, Constants.CHANNEL_ID_TENPAY, easouStatus, EbTools.ebToRmb(totalFee));
							doWorksAfterReceiveNotifyByEb(payId, easouStatus, trade_state, null, EbTools.ebToRmb(totalFee), tradeNo);
							LoggerUtil.error("Success");
							resHandler.sendToCFT("Success");
							LoggerUtil.error("Success");
						} catch (Exception e) {
							LoggerUtil.error("tenpay error!"+e.getMessage());
							resHandler.sendToCFT("fail");
							return null;
						}finally{
							lock.unlock();
						}
					}else{
						LoggerUtil.error("tenpay error!retcode:"+retcode);
						payEb.setStatus(Constants.EASOU_SERVER_STATUS_FAIL);
						payEb.setUpdateDatetime(new Date());
						payEb.setReceiveStatus(retcode);
						payEbManager.update(payEb);
						resHandler.sendToCFT("fail");
					}
				
				}else {
					logger.info("后台调用通信失败");
					logger.info(httpClient.getResponseCode());
					logger.info(httpClient.getErrInfo());
					resHandler.sendToCFT("fail");
					//有可能因为网络原因，请求已经处理，但未收到应答。
				}
			}else{
				LoggerUtil.error("=============通知签名验证失败==========================");
				resHandler.sendToCFT("fail");
			}
		} catch (Exception e) {
			LoggerUtil.error(e.getMessage());
			out.println("fail");
		} 
		return null;
	}
	
	
	
}
