package com.fantingame.pay.entity;

import com.fantingame.pay.utils.TradeInfo;

public class TradeBean extends TradeInfo{
    private String  extInfo;
    private String  imei;
    private Integer versionCode;
    private String  channelId;
    private String  invoice;
    private String  code;
    private String  msg;
    
	public String getExtInfo() {
		return extInfo;
	}
	
	public void setExtInfo(String extInfo) {
		this.extInfo = extInfo;
	}

	public String getImei() {
		if(extInfo!=null){
			String arr[] = extInfo.split("\\|");
			for(String a : arr){
				if(a.contains("imei=")){
					return a.replaceAll("^imei=","");
				}
			}
			return "";
		}
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public Integer getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(Integer versionCode) {
		this.versionCode = versionCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}
   
}
