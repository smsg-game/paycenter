package com.fantingame.pay.action.mobile.notify;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;



import org.apache.log4j.Logger;

import com.easou.common.api.MD5;
import com.easou.common.json.JsonUtil;
import com.fantingame.pay.entity.PayChannel;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.mol.MolPaymentObj;



/**
 * 支付宝通知接口,主要处理支付宝发送过来的对订单支付结果
 * */

public class NotifyFromMolAction extends BaseReceiveNotifyAction {

	private static final long serialVersionUID = 5490792191825343426L;
	private static final Logger logger = Logger.getLogger(NotifyFromMolAction.class);
	//提供给mol官方查询订单接口
	public String molCallBack() throws Exception{
	    getResponse().setContentType("text/html;charset=UTF-8");
		PrintWriter out = getResponse().getWriter();
		MolPaymentObj data = new MolPaymentObj();
		HttpServletRequest req = this.getRequest();
		data.setAmount(req.getParameter("amount"));
		data.setApplicationCode(req.getParameter("applicationCode"));
		data.setChannelId(req.getParameter("channelId"));
		data.setCurrencyCode(req.getParameter("currencyCode"));
		data.setCustomerId(req.getParameter("customerId"));
		data.setPaymentId(req.getParameter("paymentId"));
		data.setPaymentStatusCode(req.getParameter("paymentStatusCode"));
		data.setReferenceId(req.getParameter("referenceId"));
		data.setSignature(req.getParameter("signature"));
		data.setVersion(req.getParameter("version"));
		try {
			data.setPaymentStatusDate(URLDecoder.decode(req.getParameter("paymentStatusDate"),"utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		LOG.info("accept payment info:"+JsonUtil.parserObjToJsonStr(req.getParameterMap()));
		LOG.info("mol payment info:"+JsonUtil.parserObjToJsonStr(data));
		
		PayChannel channel = getChannelById(Constants.CHANNEL_ID_MOL);
		
		if(checkPayCallbackSign(data,channel.getSecretKey())&&data.getPaymentStatusCode().equals("00")){ //校验 成功就加款
			//直接加钱
			savePayNotify(data.getCustomerId(), data.getPaymentId(), Constants.CHANNEL_ID_MOL, Constants.EASOU_SERVER_STATUS_SUCCESS, data.getAmountInYuan()+"");
			//后续操作
			doWorksAfterReceiveNotify(Long.parseLong(data.getCustomerId()), Constants.EASOU_SERVER_STATUS_SUCCESS, data.getPaymentStatusCode()+"", null, data.getAmountInYuan()+"",data.getPaymentId());
		}
		out.print("200");
		return null;
	}
	
	private boolean checkPayCallbackSign(MolPaymentObj cb,String appSecret) {
		String json = JsonUtil.parserObjToJsonStr(cb);
		Map<String, Object> map = JsonUtil.parserStrToObject(json, Map.class);
		try {
			String sign = makeSign(map,appSecret);
			return sign.equalsIgnoreCase(cb.getSignature());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	private String makeSign(Map<String, Object> paraMap,String appSecret) throws Exception{
		Set<String> keys = paraMap.keySet();
		TreeSet<String> treeSet = new TreeSet<String>();
		treeSet.addAll(keys);
		String signSrcStr = "";
		for(String key : treeSet){
			if(key.equals("signature")){
				continue;
			}
			Object value = paraMap.get(key);
			signSrcStr += (value == null ? "" : value);
		}
		return MD5.digest(signSrcStr+appSecret);
	}
	
}
