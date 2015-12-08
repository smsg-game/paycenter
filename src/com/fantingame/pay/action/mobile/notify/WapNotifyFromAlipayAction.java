package com.fantingame.pay.action.mobile.notify;

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

import com.fantingame.pay.entity.PayChannel;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.LoggerUtil;
import com.fantingame.pay.utils.StringTools;
import com.fantingame.pay.utils.alipay.RSASignature;

/**
 * 支付宝通知接口,主要处理支付宝发送过来的对订单支付结果
 * */

public class WapNotifyFromAlipayAction extends BaseReceiveNotifyAction {

	private static final long serialVersionUID = 2108720011835866711L;
	private static final Logger logger = Logger.getLogger(WapNotifyFromAlipayAction.class);

	
	public String execute() throws Exception {
		doPost(getRequest(), getResponse());
		return null;
	}


    @SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	LoggerUtil.info("=====AlipayWapNotify========>"+request.getParameter("notify_data"));
    	PayChannel channel = getChannelById(Constants.CHANNEL_ID_ALIPAY_WAP);
        //获得通知参数
        Map map = request.getParameterMap();
        PrintWriter out = response.getWriter();
        //获得通知签名
        String sign = (String) ((Object[]) map.get("sign"))[0];
        //获得待验签名的数据
        HashMap<String, String> notifyDataMap = new HashMap<String, String>();
        String verifyData = getVerifyData(map,channel,notifyDataMap);
        boolean verified = false;
        //使用支付宝公钥验签名
        try {
            verified=RSASignature.doCheck(verifyData, sign, channel.getChannelPublicKey(),"");
        } catch (Exception e) {
        	LoggerUtil.error("alipay verify sign error" + e.getMessage());
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
			savePayNotify(invoice, tradeNo, Constants.CHANNEL_ID_ALIPAY, easouStatus, totalFee);
			//后续操作
			doWorksAfterReceiveNotify(Long.parseLong(invoice), easouStatus, channelStatus, null, totalFee, tradeNo);
			//回应支付宝
			if(easouStatus == Constants.EASOU_SERVER_STATUS_SUCCESS) {
				//回应支付宝
				logger.info("wap notify alipay update pay_trade and pay_eb success,invoice=" +invoice);
				out.print("success");
			} else {
				out.print("fail");
			}
        } else {
        	LoggerUtil.error("alipay verify sign fail");
        	logger.error("接收支付宝系统通知验证签名失败，请检查！");
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
        String service = (String) ((Object[]) map.get("service"))[0];
        String v = (String) ((Object[]) map.get("v"))[0];
        String sec_id = (String) ((Object[]) map.get("sec_id"))[0];
        String notify_data = (String) ((Object[]) map.get("notify_data"))[0];
        try {
            //对返回的notify_data数据用商户私钥解密
            notify_data=RSASignature.decrypt(notify_data, channel.getPrivateKey());
        } catch (Exception e) {
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
			e.printStackTrace();
		}
	}
	
}
