package com.fantingame.pay.entity;

import com.easou.common.api.JUser;

@SuppressWarnings("serial")
public class User extends JUser{
	
	private Long eb = 0L;  //金币数目
	private String userToken = null;
	
	public User(JUser jUser,String userToken){
		if (jUser!=null) {
			super.setId(jUser.getId());
			super.setName(jUser.getName());
			super.setNickName(jUser.getNickName());
			super.setMobile(jUser.getMobile());
			super.setStatus(jUser.getStatus());
			super.setSex(jUser.getSex());
			super.setOccuId(jUser.getOccuId());
			super.setBirthday(jUser.getBirthday());
			super.setCity(jUser.getCity());
//			super.setIsActive(jUser.getIsActive());
			super.setRandomPasswd(jUser.getRandomPasswd());
			//ext
			this.setUserToken(userToken);
			
		}
	}

	public Long getEb() {
		return eb;
	}
	public void setEb(Long eb) {
		this.eb = eb;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
}
