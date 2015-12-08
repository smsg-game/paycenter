package com.fantingame.pay.entity;

/**
 * easou用户账户信息
 * 
 * @version 1.0, 2012-11-27
 */
public class PayUserAccount {
	
	private Long id;

	private Long fee;

	private Long juserId;

	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getFee() {
		return fee;
	}

	public void setFee(Long fee) {
		this.fee = fee;
	}

	public Long getJuserId() {
		return juserId;
	}

	public void setJuserId(Long juserId) {
		this.juserId = juserId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
