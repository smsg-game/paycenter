package com.fantingame.pay.action.mobile.notify;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.easou.common.api.Md5SignUtil;
import com.fantingame.pay.entity.PayChannel;
import com.fantingame.pay.manager.PayEbManager;
import com.fantingame.pay.manager.PayTradeManager;
import com.fantingame.pay.manager.PayUserAccountManager;
import com.fantingame.pay.manager.PayUserBindHistoryManager;
import com.fantingame.pay.manager.SmsChannelManager;
import com.fantingame.pay.manager.SmsSendedManager;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.StringTools;

/***
 * 
 * @author Brian
 * 盈华讯方大额短信支付回调
 */
public class NotifyFromYhxfSmsAction extends BaseReceiveNotifyAction {
	private static final long serialVersionUID = -4885276354103619075L;
	private static final Logger logger = Logger.getLogger(NotifyFromYhxfSmsAction.class);
	SmsSendedManager  smsSendedManager                  = (SmsSendedManager)getBean("smsSendedManager");
	SmsChannelManager  smsChannelManager                = (SmsChannelManager)getBean("smsChannelManager");
	PayEbManager      payEbManager                      = (PayEbManager)getBean("payEbManager");
	PayTradeManager   payTradeManager                   = (PayTradeManager)getBean("payTradeManager");
	PayUserAccountManager   payUserAccountManager       = (PayUserAccountManager)getBean("payUserAccountManager");
	PayUserBindHistoryManager payUserBindHistoryManager = (PayUserBindHistoryManager) getBean("payUserBindHistoryManager");
	 
	public String execute() throws Exception {
		getRequest().setCharacterEncoding("UTF-8");
		PrintWriter out = getResponse().getWriter();
		try{
			//获取参数
			Map<String,String> params = verifyParams();
			
			
			logger.info("IP:"+getRequest().getRemoteAddr());
			/*校验服务器IP
			if(!"116.204.96.88".equals(getRequest().getRemoteAddr()) && !"211.162.68.88".equals(getRequest().getRemoteAddr())){
				  logger.info("IP:"+getRequest().getRemoteAddr());
				  out.write("failydzf");
				  return null;
			}
			*/
			
			PayChannel channel = getChannelById(Constants.CHANNEL_ID_SMSYHXF);
			//验签
			String signStr = params.get("oid") + params.get("sporder") + params.get("spid") + params.get("mz") + channel.getSecretKey();
			if(Md5SignUtil.doCheck(signStr, params.get("md5"),"")){
				String orderId = params.get("sporder"); //EB订单id
				String oid = params.get("oid");	//交易渠道id	
				String mz = params.get("mz");  //已付金额
				String zdy = params.get("zdy"); //渠道消息
				
				//保存通知
				savePayNotify(orderId, oid, Constants.CHANNEL_ID_SMSYHXF, 1, mz);
				//后续操作
				doWorksAfterReceiveNotify(Long.parseLong(orderId), 1,"1", zdy, mz, oid);
				//回应
				out.print("okydzf");
			}else{
				out.write("failydzf");
			}
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	
	private Map<String,String> verifyParams() throws Exception{ 
		Map<String,String> map= new HashMap<String, String>();
		StringTools.setValue(map,"spid",getParam("spid"),5,true,true);
		StringTools.setValue(map,"md5",getParam("md5"),32,true,false); 
		StringTools.setValue(map,"oid",getParam("oid"),64,true,false);
		StringTools.setValue(map,"sporder",getParam("sporder"),32,true,true);
		StringTools.setValue(map,"mz",getParam("mz"),8,true,true);
		StringTools.setValue(map,"zdy",getParam("zdy"),255,true,false);
		StringTools.setValue(map,"spuid",getParam("spuid"),16,true,true);
		return map;
	}

}
