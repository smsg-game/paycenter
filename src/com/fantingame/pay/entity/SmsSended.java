package com.fantingame.pay.entity;

import java.util.Date;



public class SmsSended {
     private Long id;
     private Long channelId;
     private String linkId;
     private String moId;
     private String mrId;
     private Long   mobile;
     private String dmobile;
     private String cp;
     private String msg;
     private String stype;
     private String mtype;
     private String gateway;
     private String action;
     private String servicetype;
     private Long orderId;
     private Integer fee;
     private String status;
     private Date createDatetime;
     private Date updateDatetime;
     
     public SmsSended(){
    	 
     }
     
     public SmsSended(Long channelId,Long mobile){
    	 this.channelId = channelId;
    	 this.mobile    = mobile;
     }
     
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLinkId() {
		return linkId;
	}
	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}
	public String getMoId() {
		return moId;
	}
	public void setMoId(String moId) {
		this.moId = moId;
	}
	public String getMrId() {
		return mrId;
	}
	public void setMrId(String mrId) {
		this.mrId = mrId;
	}
	public Long getMobile() {
		return mobile;
	}
	public void setMobile(Long mobile) {
		this.mobile = mobile;
	}
	public String getDmobile() {
		return dmobile;
	}
	public void setDmobile(String dmobile) {
		this.dmobile = dmobile;
	}
	public String getStype() {
		return stype;
	}
	public void setStype(String stype) {
		this.stype = stype;
	}
	public String getMtype() {
		return mtype;
	}
	public void setMtype(String mtype) {
		this.mtype = mtype;
	}
	public String getGateway() {
		return gateway;
	}
	public void setGateway(String gateway) {
		this.gateway = gateway;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getServicetype() {
		return servicetype;
	}
	public void setServicetype(String servicetype) {
		this.servicetype = servicetype;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Integer getFee() {
		return fee;
	}
	public void setFee(Integer fee) {
		this.fee = fee;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreateDatetime() {
		return createDatetime;
	}
	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}
	public Date getUpdateDatetime() {
		return updateDatetime;
	}
	public void setUpdateDatetime(Date updateDatetime) {
		this.updateDatetime = updateDatetime;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public String getCp() {
		return cp;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}
	
 
}
