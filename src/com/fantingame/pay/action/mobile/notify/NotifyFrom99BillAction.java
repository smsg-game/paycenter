package com.fantingame.pay.action.mobile.notify;

import java.io.PrintWriter;

import org.apache.log4j.Logger;

import com.fantingame.pay.entity.PayChannel;
import com.fantingame.pay.exception.EasouPayException;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.StringTools;
import com.fantingame.pay.utils.bill99.Bill99Result;

public class NotifyFrom99BillAction extends BaseReceiveNotifyAction {

	private static final long serialVersionUID = -4885276354103619075L;
	private static final Logger logger = Logger.getLogger(NotifyFrom99BillAction.class);
	
	public String execute() throws Exception {
		
		PrintWriter out = getResponse().getWriter();// 输出流
		try {
			
			String channelId     = getParam("ext1");
			PayChannel channel   = getChannelById(Long.valueOf(channelId));  
			String channelStatus = Bill99Result.getActualResult (getRequest(), channel.getSecretKey());  //验签 + 返回支付结果  验签不通过，会抛出异常
			String orderId       = getParam("orderId");

		    int easouStatus = "10".equals(channelStatus)? Constants.EASOU_SERVER_STATUS_SUCCESS:Constants.EASOU_SERVER_STATUS_FAIL;
		    
		    String paidFee = StringTools.getNumFormat_2().format(Integer.valueOf(getParam("payAmount"))/100);
		    //保存通知
			savePayNotify(orderId, null, channel.getId(), easouStatus, paidFee);
			//后续操作
			doWorksAfterReceiveNotify(Long.parseLong(orderId), easouStatus, channelStatus, null, paidFee, null);
			//回应快钱
			out.print("<result>1</result><redirecturl>http://www.mysite.com/</redirecturl>");
		}catch (EasouPayException e) {
			logger.error(e.getCode()+":"+e.getMessage());
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

}
