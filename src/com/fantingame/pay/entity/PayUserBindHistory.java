package com.fantingame.pay.entity;

import java.util.Date;


/**
 * 用户绑定手机号历史记录表
 * */
public class PayUserBindHistory {
     private Long   id;
     private String userId;
     private String mobile;
     private Date   createDatetime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Date getCreateDatetime() {
		return createDatetime;
	}
	public void setCreateDatetime(Date createDatetime) {
		this.createDatetime = createDatetime;
	}
     
	
	
}
