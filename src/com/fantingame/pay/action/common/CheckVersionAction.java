package com.fantingame.pay.action.common;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletRequest;

import org.apache.log4j.Logger;

import com.fantingame.pay.entity.PayAppVersion;
import com.fantingame.pay.entity.TradeBean;
import com.fantingame.pay.entity.TradeCode;
import com.fantingame.pay.exception.EasouPayException;
import com.fantingame.pay.manager.PayAppVersionManager;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.StringTools;


/**
 * 游戏商向梵町下单接口
 * 输入参数：versionName  版本名称  最长32位
 *         
 *      
 * 返回值：返回一个名为data的json对象,值有：code:0是正常状态码，其他都是非正常状态码
 *                                     msg:状态信息
 *                              needUpdate:是否需要更新  1-是，2-否
 *                               updateUrl:当不需要更新时，此栏为空
 *                             versionInfo:更新版本信息
 *                                  forced:是否强制更新，1-是，0-否
 * */
public class CheckVersionAction extends BaseAction{
	private static final long serialVersionUID = 7253695812868279076L;
	private Map<String,String> data = new TreeMap<String, String>();
	private Logger logger = Logger.getLogger(CheckVersionAction.class);
	
	
	@Override
	public String execute() throws Exception {
		try{
			//获取参数
			TradeBean trade = getParams(getRequest());
			
			PayAppVersionManager payAppVersionManager = (PayAppVersionManager)getBean("payAppVersionManager");
			PayAppVersion payAppVersion = payAppVersionManager.getLatestVersion();
			if(payAppVersion!=null && !trade.getVersionCode().equals(payAppVersion.getVersionCode())){
				PayAppVersion latestForcedVersion = payAppVersionManager.getLatestForcedVersion();
				data.put(Constants.FIELD_CODE, "0");
				data.put(Constants.FIELD_MSG, "需要更新");
				data.put(Constants.FIELD_NEED_UPDATE,"1");
				if(latestForcedVersion!=null && latestForcedVersion.getVersionCode()>trade.getVersionCode()){
					data.put(Constants.FIELD_FORCED,"1");
					data.put(Constants.FIELD_UPDATE_URL,payAppVersion.getFileUrl());
					data.put(Constants.FIELD_VERSION_INFO,payAppVersion.getVersionInfo());
					data.put(Constants.FIELD_VERSION_NAME, payAppVersion.getVersionName());
				}else{
					data.put(Constants.FIELD_FORCED,payAppVersion.getForced()+"");
					data.put(Constants.FIELD_UPDATE_URL,payAppVersion.getFileUrl());
					data.put(Constants.FIELD_VERSION_INFO,payAppVersion.getVersionInfo());
					data.put(Constants.FIELD_VERSION_NAME, payAppVersion.getVersionName());
				}
			}else{
				data.put(Constants.FIELD_CODE, "0");
				data.put(Constants.FIELD_MSG, "不需要更新");
				data.put(Constants.FIELD_NEED_UPDATE,"0");
				data.put(Constants.FIELD_UPDATE_URL,"");
				data.put(Constants.FIELD_VERSION_INFO,"");
				data.put(Constants.FIELD_FORCED,"");
			}
		}catch (EasouPayException e) {
			data.put(Constants.FIELD_CODE,e.getCode());
			data.put(Constants.FIELD_MSG,e.getMessage());
			logger.error(e.getMessage(),e);
		}catch (Exception e) {
			data.put(Constants.FIELD_CODE,TradeCode.EASOU_CODE_NEG_1.code);
			data.put(Constants.FIELD_MSG,TradeCode.EASOU_CODE_1.msgC);
			logger.error(e.getMessage(),e);
		}
		return SUCCESS;
	}
	
	private TradeBean getParams(ServletRequest req) throws Exception{
		TradeBean trade = new TradeBean();
		trade.setVersionCode(StringTools.toNum(StringTools.getValue("versionCode",getParam("versionCode"),16,false,true),0));
		return trade;
	}
	
	
	
	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}
	

	

}
