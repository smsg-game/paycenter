package com.fantingame.pay.action.module.notify;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.fantingame.pay.action.mobile.notify.BaseReceiveNotifyAction;
import com.fantingame.pay.entity.PayChannel;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.LoggerUtil;
import com.fantingame.pay.utils.StringTools;
import com.fantingame.pay.utils.alipay.RSASignature;

/**
 * 支付宝通知接口,主要处理支付宝发送过来的对订单支付结果
 * */

public class ModuleNotifyFromAlipayAction extends BaseReceiveNotifyAction {

	private static final long serialVersionUID = 8172832161371357880L;
	private static final Logger logger = Logger.getLogger(ModuleNotifyFromAlipayAction.class);

	
	public String execute() throws Exception {
		doPost(getRequest(), getResponse());
		return null;
	}


	@SuppressWarnings("rawtypes")
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	LoggerUtil.info("module =====AlipayWapNotify========>"+request.getParameter("notify_data"));
    	PayChannel channel = getChannelById(Constants.CHANNEL_ID_ALIPAY_MODULE);
        //获得通知参数
        Map map = request.getParameterMap();
        logger.info("module map=" + map);
        PrintWriter out = response.getWriter();
        //获得通知签名
        String sign = (String) ((Object[]) map.get("sign"))[0];
        logger.info("module sign=" + sign);
        //获得待验签名的数据
        HashMap<String, String> notifyDataMap = new HashMap<String, String>();
        String verifyData = getVerifyData(map,channel,notifyDataMap);
        logger.info("module verifyData=" + verifyData);
        boolean verified = false;
        //使用支付宝公钥验签名
        try {
            verified=RSASignature.doCheck(verifyData, sign, channel.getChannelPublicKey(),"");
        } catch (Exception e) {
        	LoggerUtil.error("module alipay verify sign error" + e.getMessage());
        	logger.error(e.getMessage(),e);
        }
        //验证签名通过
        if (verified) {
        	//获取参数
			String channelStatus = StringTools.getValue("trade_status",notifyDataMap.get("trade_status"),32,true,false);//交易状态 
			String totalFee =  StringTools.getValue("total_fee",notifyDataMap.get("total_fee"),16,true,true);//交易金额
			String invoice = StringTools.getValue("out_trade_no",notifyDataMap.get("out_trade_no"),64,true,false); //外部交易号(商户交易号)
			String tradeNo = StringTools.getValue("trade_no",notifyDataMap.get("trade_no"),64,true,false); //支付宝交易号
			
			int easouStatus = Constants.EASOU_SERVER_STATUS_CREATE;
			
			if("TRADE_FINISHED".equals(channelStatus) || "TRADE_SUCCESS".equals(channelStatus)){//支付状态为成功
				easouStatus = Constants.EASOU_SERVER_STATUS_SUCCESS;
			}else if("WAIT_BUYER_PAY".equals(channelStatus)){//等待支付
				easouStatus = Constants.EASOU_SERVER_STATUS_WAITING;
			}else{
				easouStatus = Constants.EASOU_SERVER_STATUS_FAIL;
			}
			//保存通知
			savePayNotify(invoice, tradeNo, Constants.CHANNEL_ID_ALIPAY_MODULE, easouStatus, totalFee);
			//后续操作
			doWorksAfterReceiveNotify(Long.parseLong(invoice), easouStatus, channelStatus, null, totalFee, tradeNo);
			if(easouStatus == Constants.EASOU_SERVER_STATUS_SUCCESS) {
				//回应支付宝
				logger.info("module notify alipay update pay_trade and pay_eb success,invoice=" +invoice);
				out.print("success");
			} else {
				out.print("fail");
			}
        } else {
        	LoggerUtil.error("module alipay verify sign fail");
        	logger.error("module 接收支付宝系统通知验证签名失败，请检查！");
            out.print("fail");
        }
    }
    
    

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        doPost(request, response);
    }

    
    
    /**
     * 获得验签名的数据
     * @param map
     * @param notifyDataMap 
     * @return
     * @throws Exception 
     */
    @SuppressWarnings("unchecked")
	private String getVerifyData(Map map,PayChannel channel, HashMap<String, String> notifyDataMap ) {
    	logger.info("module enter verifyData...");
        String service = (String) ((Object[]) map.get("service"))[0];
        String v = (String) ((Object[]) map.get("v"))[0];
        String sec_id = (String) ((Object[]) map.get("sec_id"))[0];
        String notify_data = (String) ((Object[]) map.get("notify_data"))[0];
        try {
            //对返回的notify_data数据用商户私钥解密
        	logger.info("module enter verifyData decrypt...");
            notify_data=RSASignature.decrypt(notify_data, channel.getPrivateKey());
            logger.info("module enter verifyData decrypt end...");
        } catch (Exception e) {
        	logger.info("module alipay decrypt data error:"+e.getMessage());
        	logger.error(e.getMessage(), e);
        }
       parserXml(notifyDataMap,notify_data);
        return "service=" + service + "&v=" + v + "&sec_id=" + sec_id + "&notify_data="+ notify_data;
    }
    
    
    
    @SuppressWarnings("unchecked")
	public static void parserXml(HashMap<String, String> notifyDataMap,String content) {
		try {
			Document document = DocumentHelper.parseText(content);
			Element notify = document.getRootElement();
			for (Iterator i = notify.elementIterator(); i.hasNext();) {
				Element node = (Element) i.next();
				notifyDataMap.put(node.getName(), node.getText());
			}
		} catch (Exception e) {
			logger.info("module alipay dparserXml data error,"+e.getMessage());
			e.printStackTrace();
		}
	}
	
}
