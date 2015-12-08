package com.fantingame.pay.entity;



public class SmsRule {
     private Long id;
     private Long channelId;
     private String province;
     private String city;
     private String time;
     private String optCmcc;
     private String optUnicom;
     private String optTelecom;
     private String status;
	
     
     public SmsRule(){
    	 
     }
     
     public SmsRule(Long channelId,String province,String city){
    	 this.channelId = channelId;
    	 this.province  = province;
    	 this.city      = city;
     }
     
     
     public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getChannelId() {
		return channelId;
	}
	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getOptCmcc() {
		return optCmcc;
	}
	public void setOptCmcc(String optCmcc) {
		this.optCmcc = optCmcc;
	}
	public String getOptUnicom() {
		return optUnicom;
	}
	public void setOptUnicom(String optUnicom) {
		this.optUnicom = optUnicom;
	}
	public String getOptTelecom() {
		return optTelecom;
	}
	public void setOptTelecom(String optTelecom) {
		this.optTelecom = optTelecom;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
     
}
