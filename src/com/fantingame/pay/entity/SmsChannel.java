package com.fantingame.pay.entity;

import java.util.Date;



public class SmsChannel {
     private Long id;
     private String spNo;
     private String spName;
     private String businessCode;
     private String businessName;
     private Integer price;
     private Integer maxSendPerDay;
     private Integer maxSendPerMonth;
     private Integer maxMoneyPerDay;
     private Integer maxMoneyPerMonth;
     private Integer versionAdapter;
     private String freeSmsContent;
     private String freeSmsTo;
     private boolean freeSmsExtendable;
     private String feeSmsContent;
     private String feeSmsTo;
     private boolean feeSmsExtendable;
     private Integer sort;
     private Integer status;
     private Date   createDatetime;
     private Date   updateDatetime;
     
    public SmsChannel(){
    	
    }
     
    public SmsChannel(Integer versionAdapter,Integer status){
    	this.versionAdapter = versionAdapter;
    	this.status = status;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSpNo() {
		return spNo;
	}

	public void setSpNo(String spNo) {
		this.spNo = spNo;
	}

	public String getSpName() {
		return spName;
	}

	public void setSpName(String spName) {
		this.spName = spName;
	}

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getMaxSendPerDay() {
		return maxSendPerDay;
	}

	public void setMaxSendPerDay(Integer maxSendPerDay) {
		this.maxSendPerDay = maxSendPerDay;
	}

	public Integer getMaxSendPerMonth() {
		return maxSendPerMonth;
	}

	public void setMaxSendPerMonth(Integer maxSendPerMonth) {
		this.maxSendPerMonth = maxSendPerMonth;
	}

	public Integer getMaxMoneyPerDay() {
		return maxMoneyPerDay;
	}

	public void setMaxMoneyPerDay(Integer maxMoneyPerDay) {
		this.maxMoneyPerDay = maxMoneyPerDay;
	}

	public Integer getMaxMoneyPerMonth() {
		return maxMoneyPerMonth;
	}

	public void setMaxMoneyPerMonth(Integer maxMoneyPerMonth) {
		this.maxMoneyPerMonth = maxMoneyPerMonth;
	}

	public Integer getVersionAdapter() {
		return versionAdapter;
	}

	public void setVersionAdapter(Integer versionAdapter) {
		this.versionAdapter = versionAdapter;
	}

	public String getFreeSmsContent() {
		return freeSmsContent;
	}

	public void setFreeSmsContent(String freeSmsContent) {
		this.freeSmsContent = freeSmsContent;
	}

	public String getFreeSmsTo() {
		return freeSmsTo;
	}

	public void setFreeSmsTo(String freeSmsTo) {
		this.freeSmsTo = freeSmsTo;
	}

	public boolean getFreeSmsExtendable() {
		return freeSmsExtendable;
	}

	public void setFreeSmsExtendable(boolean freeSmsExtendable) {
		this.freeSmsExtendable = freeSmsExtendable;
	}

	public String getFeeSmsContent() {
		return feeSmsContent;
	}

	public void setFeeSmsContent(String feeSmsContent) {
		this.feeSmsContent = feeSmsContent;
	}

	public String getFeeSmsTo() {
		return feeSmsTo;
	}

	public void setFeeSmsTo(String feeSmsTo) {
		this.feeSmsTo = feeSmsTo;
	}

	public boolean getFeeSmsExtendable() {
		return feeSmsExtendable;
	}

	public void setFeeSmsExtendable(boolean feeSmsExtendable) {
		this.feeSmsExtendable = feeSmsExtendable;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
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
     
	

}
