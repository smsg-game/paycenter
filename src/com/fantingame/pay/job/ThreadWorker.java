package com.fantingame.pay.job;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.DelayQueue;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.easou.common.api.Md5SignUtil;
import com.fantingame.pay.entity.PayNotifyPush;
import com.fantingame.pay.entity.PayPartner;
import com.fantingame.pay.entity.PayTrade;
import com.fantingame.pay.manager.PayNotifyPushManager;
import com.fantingame.pay.manager.PayPartnerManager;
import com.fantingame.pay.manager.PayTradeManager;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.HttpUtil;
import com.fantingame.pay.utils.RsaSignUtil;
import com.fantingame.pay.utils.StringTools;

public class ThreadWorker implements Runnable{
	private Logger logger = Logger.getLogger(ThreadWorker.class);
	private DelayQueue<DelayItem> dq = null;
	private HttpClient httpClient = null;
    private PayTradeManager payTradeManager = (PayTradeManager)Constants.CTX.getBean("payTradeManager");
    private PayNotifyPushManager payNotifyPushManager = (PayNotifyPushManager)Constants.CTX.getBean("payNotifyPushManager");
    private PayPartnerManager payPartnerManager = (PayPartnerManager)Constants.CTX.getBean("payPartnerManager");
	private int maxRetry = 10;
    
	public ThreadWorker(DelayQueue<DelayItem> dq,HttpClient httpClient){
		this.dq = dq;
		this.httpClient = httpClient;
	}
	
	@Override
	public void run(){
		    DelayItem di   = null;
		    String content = null;
		    PayNotifyPush notifyPush = null;
		    while(true){
				try{
					di = this.dq.take();
					logger.info("delayTime:"+di.getDelayTime()+"("+di.getRetry()+")...left:"+this.dq.size());
					PayPartner payPartner = payPartnerManager.getEntityById(di.getPartnerId());
					if(payPartner==null){
						throw new Exception("partner does not exist");
					}
					Map<String,String> formEntity = getFullUrl(di,payPartner);
					
					//发送Notify
					HttpPost httpPost = new HttpPost(di.getNotifyUrl());
					
					
					HttpResponse response = HttpUtil.doPost(httpClient,httpPost, formEntity,"UTF-8");
					content = EntityUtils.toString(response.getEntity(),"UTF-8");
					httpPost.abort();
					
					//push history
					notifyPush = new PayNotifyPush();
					notifyPush.setInvoice(di.getInvoice());
					notifyPush.setNotifyUrl(di.getNotifyUrl());
					notifyPush.setTimes(di.getRetry());
					notifyPush.setPaidFee(di.getPaidFee());
					notifyPush.setReqFee(di.getReqFee());
					notifyPush.setTradeId(di.getTradeId());
					
					//如果收到不是OK,则重新塞到队列里
					PayTrade payTrade = payTradeManager.getEntityByInvoice(di.getInvoice());
					PayTrade tmpPayTrade = null;
					if(payTrade!=null){//存在订单
						if(payTrade.getStatus() != 1) {
							logger.error("pay_trade-->status = "+ payTrade.getStatus()+",notify partner....invoice:"+di.getInvoice()+"...url:"+di.getNotifyUrl());
						}
						tmpPayTrade = new PayTrade();
						tmpPayTrade.setInvoice(di.getInvoice());
						if("OK".equals(content)){//确认OK
//							payTrade.setNotifyStatus(1);
							tmpPayTrade.setNotifyStatus(1);
							notifyPush.setStatus(1);
						}else{//有异常信息
//							payTrade.setNotifyStatus(-1);
							tmpPayTrade.setNotifyStatus(-1);
							notifyPush.setStatus(-1);
							logger.error("notify fail....invoice:"+di.getInvoice()+"...url:"+di.getNotifyUrl()+"...content:"+content);
							doRetry(di);
						}
						notifyPush.setMsg(content);
						notifyPush.setCreateDatetime(new Date());
//						payTradeManager.update(payTrade);
						payTradeManager.updNotifyStatus(tmpPayTrade);
						payNotifyPushManager.save(notifyPush);
					}else{//订单不存在
						logger.error("invoice does not exist:"+di.getInvoice());
					}
				}catch (Exception e) {
					logger.error(e.getMessage() +"...invoice:"+di.getInvoice()+"...url:"+di.getNotifyUrl(),e);
					doRetry(di);
					notifyPush.setStatus(-1);
					notifyPush.setMsg(e.getMessage());
					notifyPush.setCreateDatetime(new Date());
					payNotifyPushManager.save(notifyPush);
				}
		    }
	}

    private void doRetry(DelayItem item){
    	if(item.getRetry()<maxRetry){//重新塞到队列里
    		item.setRetry(item.getRetry()+1);
    		this.dq.add(item);
    	}else{//报警
    		
    	}
    }

    
    /*返回一个含有sign的完整url,不是必要的参数如果为空则不参与签名，不参与签名*/
	private Map<String,String> getFullUrl(DelayItem di,PayPartner payPartner) throws Exception{
		Map<String,String> map = new HashMap<String, String>();
		StringTools.setValue(map,"appId",di.getAppId(),32,true,false);
		StringTools.setValue(map,"paidFee",di.getPaidFee(),32,false,true);
		StringTools.setValue(map,"invoice",di.getInvoice(),32,true,false);
		StringTools.setValue(map,"payerId",di.getPayerId(),64,true,false);
		StringTools.setValue(map,"reqFee",di.getReqFee(),32,false,true);
		StringTools.setValue(map,"tradeId",di.getTradeId(),64,true,false);
		StringTools.setValue(map,"tradeStatus",di.getTradeStatus(),32,false,false);
		StringTools.setValue(map,"tradeName",di.getTradeName(),64,false,false);
		StringTools.setValue(map,"notifyDatetime",di.getNotifyDatetime(),19,true,false);
		//签名
		String sign = "";
		if("1".equals(payPartner.getEncryptType())){//RSA
			if(di.getPartnerId()!=null && Constants.PARTNER_ID_BIANFENG == di.getPartnerId()){
				map.put("playerId", map.get("payerId"));
				map.remove("payerId");
				sign = RsaSignUtil.sign(di.getNotifyUrl()+"?"+RsaSignUtil.preSignStr(map),payPartner.getPrivateKey());
			}else{
				sign = RsaSignUtil.sign(RsaSignUtil.preSignStr(map),payPartner.getPrivateKey());
			}
		}else if("2".equals(payPartner.getEncryptType())){//MD5
			sign = Md5SignUtil.sign(map, payPartner.getSecretKey());
		}
		StringTools.setValue(map,"sign",sign,172,true,false);
		return map;
	}

}
