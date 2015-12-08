package com.fantingame.pay.action.mobile.old;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.fantingame.pay.action.common.BaseAction;
import com.fantingame.pay.entity.PayPartner;
import com.fantingame.pay.entity.PayTrade;
import com.fantingame.pay.entity.TradeCode;
import com.fantingame.pay.exception.EasouPayException;
import com.fantingame.pay.manager.PayPartnerManager;
import com.fantingame.pay.manager.PayTradeManager;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.RsaSignUtil;
import com.fantingame.pay.utils.StringTools;


/**
 * 游戏商向梵町下单接口
 * 接收参数：
 *         必填：appId,partnerId,tradeId,tradeName,tradeDescription,
 *         fee,notifyUrl,sign,separable,payerId,qn
 *         可选：extInfo
 *      
 * 返回值：返回一个名为result的json对象，属性code为0即为成功，其他均为失败
 *       
 * */
public class TradeAction extends BaseAction{
	private static final long serialVersionUID = 4874488652388728333L;
	private Map<String,String> data = new TreeMap<String, String>();
	private static Logger logger = Logger.getLogger(TradeAction.class);
	
	@Override
	public String execute() throws Exception {
		try{
			//判断是否https请求
			if(!isHttps()){
				data.put(Constants.FIELD_CODE,TradeCode.EASOU_CODE_170.code);
				data.put(Constants.FIELD_MSG,TradeCode.EASOU_CODE_170.msgE);
				logger.error(TradeCode.EASOU_CODE_170.msgE);
				return SUCCESS;
			}

			//获取参数
			Map<String,String> map = verifyParams();
			
			PayPartnerManager partnerManager = (PayPartnerManager)getBean("payPartnerManager");
			PayPartner payPartner = partnerManager.getEntityById(Long.valueOf(map.get("partnerId")));
			if(payPartner==null){//检验游戏商身份
				data.put(Constants.FIELD_CODE,TradeCode.EASOU_CODE_120.code);
				data.put(Constants.FIELD_MSG,TradeCode.EASOU_CODE_120.msgE);
				logger.error(TradeCode.EASOU_CODE_120.msgE);
			}else if(!RsaSignUtil.doCheck(this.getFullUrl()+"?"+RsaSignUtil.preSignStr(map),map.get("sign"), payPartner.getPartnerPublicKey())){//验签
				data.put(Constants.FIELD_CODE,TradeCode.EASOU_CODE_110.code);
				data.put(Constants.FIELD_MSG,TradeCode.EASOU_CODE_110.msgE);
				logger.error(TradeCode.EASOU_CODE_110.msgE);
			}else{//操作成功
				PayTrade of = new PayTrade() ;
				of.setCreateDatetime(new Date());
				of.setAppId(map.get(Constants.FIELD_APP_ID));
				of.setSeparable("true".equals(map.get(Constants.FIELD_SEPARABLE))?1:0);
				of.setPayerId(map.get("playerId"));
				of.setReqFee(map.get("fee"));
				of.setCurrency("CNY");
				of.setImei("");
				of.setExtInfo(map.get(Constants.FIELD_EXT_INFO));
				of.setNotifyUrl(map.get(Constants.FIELD_NOTIFY_URL));
				of.setPartnerId(Long.valueOf(map.get(Constants.FIELD_PARTNER_ID)));
				of.setQn(map.get(Constants.FIELD_QN));
				of.setCookieQn(getCookieByName(Constants.EASOU_COOKIE_QN_NAME));
				of.setStatus(0);
				of.setTradeDesc(map.get("tradeDescription"));
				of.setTradeId(map.get(Constants.FIELD_TRADE_ID));
				of.setTradeName(map.get(Constants.FIELD_TRADE_NAME));
				of.setInvoice(UUID.randomUUID().toString().replace("-", ""));
				
				PayTradeManager payTradeManager = (PayTradeManager)getBean("payTradeManager");
				PayTrade tmpTrade = payTradeManager.getByPartnerIdAndTradeId(of);
				if(tmpTrade==null){//正确
					data.put(Constants.FIELD_INVOICE,of.getInvoice());
					data.put(Constants.FIELD_CODE,TradeCode.EASOU_ORDER_0.code);
					data.put(Constants.FIELD_MSG,TradeCode.EASOU_ORDER_0.msgE);
					payTradeManager.save(of);
				}else{//订单重复
					data.put(Constants.FIELD_CODE,TradeCode.EASOU_CODE_150.code);
					data.put(Constants.FIELD_MSG,TradeCode.EASOU_CODE_150.msgE);
					logger.error(TradeCode.EASOU_CODE_150.msgE+"...inovice"+of.getInvoice());
				}
			}
		}catch (EasouPayException e) {
			data.put(Constants.FIELD_CODE,e.getCode());
			data.put(Constants.FIELD_MSG,e.getMessage());
			logger.error(e.getCause()+":"+e.getMessage());
		}catch (Exception e) {
			data.put(Constants.FIELD_CODE,TradeCode.EASOU_CODE_NEG_1.code);
			data.put(Constants.FIELD_MSG,TradeCode.EASOU_CODE_NEG_1.msgE);
			logger.error(e.getMessage(),e);
		}

		return SUCCESS;
	}
	
	
	/**
	 * 把所有参数都放到TradeInfo里
	 * */
	private Map<String,String> verifyParams() throws Exception{ 
		Map<String,String> map = new HashMap<String, String>();
		StringTools.setValue(map,Constants.FIELD_APP_ID,getParam(Constants.FIELD_APP_ID),32,true,false);
		StringTools.setValue(map,Constants.FIELD_PARTNER_ID,getParam(Constants.FIELD_PARTNER_ID),16,true,true);
		StringTools.setValue(map,Constants.FIELD_TRADE_ID,getParam(Constants.FIELD_TRADE_ID),64,true,false);
		StringTools.setValue(map,Constants.FIELD_TRADE_NAME,getParam(Constants.FIELD_TRADE_NAME),64,true,false);
		StringTools.setValue(map,Constants.FIELD_NOTIFY_URL,getParam(Constants.FIELD_NOTIFY_URL),255,true,false);
		StringTools.setValue(map,Constants.FIELD_SIGN,getParam(Constants.FIELD_SIGN),172,true,false);
		StringTools.setValue(map,Constants.FIELD_SEPARABLE,getParam(Constants.FIELD_SEPARABLE),16,true,false);
		StringTools.setValue(map,Constants.FIELD_QN,getParam(Constants.FIELD_QN),16,true,false);
		StringTools.setValue(map,Constants.FIELD_EXT_INFO,getParam(Constants.FIELD_EXT_INFO),255,false,false);
		//特殊字段
		StringTools.setValue(map,"fee",getParam("fee"),16,true,true);
        StringTools.setValue(map,"tradeDescription",getParam("tradeDescription"),1024,true,false);
        StringTools.setValue(map,"playerId",getParam("playerId"),32,true,false);	
		
		
		return map;
	}
		
	
	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}
	
}
