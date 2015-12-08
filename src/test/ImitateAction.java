package test;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletRequest;

import org.apache.log4j.Logger;

import com.easou.common.api.Md5SignUtil;
import com.fantingame.pay.action.common.BaseAction;
import com.fantingame.pay.entity.PayPartner;
import com.fantingame.pay.entity.PayTrade;
import com.fantingame.pay.exception.EasouPayException;
import com.fantingame.pay.manager.PayPartnerManager;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.LoggerUtil;
import com.fantingame.pay.utils.RsaSignUtil;
import com.fantingame.pay.utils.StringTools;

public class ImitateAction extends BaseAction{
	
	private static final long serialVersionUID = 5994478896685683780L;
	private static final Logger logger = Logger.getLogger(ImitateAction.class);
	private Map<String,String> data = new TreeMap<String, String>();
	
	//模拟游戏商接受我们服务端发送的支付成功/失败信息
	public String execute() throws Exception {
	    //getResponse().setContentType("text/html;charset=UTF-8");
		getResponse().setCharacterEncoding("UTF-8");
		PrintWriter out = getResponse().getWriter();
		Map<String,String> map = null;
		try {
			//获取参数
			map = checkParams(getRequest());
			String sign = getParam("sign");
			
			if(doCheck(map,sign)){
				logger.info("(ImitateAction 模拟游戏商) --- > 接收到通知:invoice="+map.get("invoice")+"...map:"+map.toString());
				if("TRADE_SUCCESS".equals(map.get("tradeStatus"))){
					out.write("OK");
				}else{
					out.write("FUCK YOU ；tradeStatus ≠ TRADE_SUCCESS");
				}
			}else{
				logger.error("(ImitateAction 模拟游戏商) --- > 接收到通知:invoice="+map.get("invoice")+" 验签不通过");
				out.write("ILLEGAL_SIGN");
			}
		} catch(EasouPayException e) {
			out.write(e.getCode()+":"+e.getMessage());
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage()+"invoice:"+map.get("invoice"),e);
			out.print(e.getMessage());
		}
		return null;
	}
	
	private boolean doCheck(Map<String,String> map,String sign) throws Exception{
		PayTrade payTrade = getTradeByInvoice(map.get("invoice"));
		PayPartnerManager partnerManager = (PayPartnerManager)getBean("payPartnerManager");
		PayPartner payPartner = partnerManager.getEntityById(payTrade.getPartnerId());
		if("1".equals(payPartner.getEncryptType())){//RSA
			return RsaSignUtil.doCheck(map,sign,payPartner.getPartnerPublicKey());	
		}else if("2".equals(payPartner.getEncryptType())){//MD5
			if(payTrade.getPartnerId()==Constants.PARTNER_ID_BOOK_MALL) map.put("channelId",getParam("channelId"));
			return Md5SignUtil.doCheck(map,sign,payPartner.getSecretKey());
		}else{
			return false;
		}
	}

	private Map<String,String> checkParams(ServletRequest req) throws Exception{
		Map<String,String> map = new HashMap<String, String>();
		StringTools.setValue(map,"invoice",getParam("invoice"),32,true,false);
		StringTools.setValue(map,"tradeId",getParam("tradeId"),64,true,false);
		StringTools.setValue(map,"paidFee",getParam("paidFee"),32,true,true);
		StringTools.setValue(map,"tradeStatus",getParam("tradeStatus"),32,true,false);
		StringTools.setValue(map,"payerId",getParam("payerId"),32,true,false);
		StringTools.setValue(map,"appId",getParam("appId"),32,true,false);
		StringTools.setValue(map,"reqFee",getParam("reqFee"),32,true,true);
		StringTools.setValue(map,"tradeName",getParam("tradeName"),64,true,false);
		StringTools.setValue(map,"notifyDatetime",getParam("notifyDatetime"),19,true,false);
		return map;
	}

	public Map<String, String> getData() {
		return data;
	}


	public void setData(Map<String, String> data) {
		this.data = data;
	}
}
