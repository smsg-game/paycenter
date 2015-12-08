package com.fantingame.pay.utils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;

import com.easou.cas.client.EucApiAuthCall;
import com.easou.common.api.EucService;
import com.easou.common.api.JBean;
import com.easou.common.api.JBody;
import com.easou.common.api.RequestInfo;
import com.easou.common.constant.LoginType;
import com.easou.common.para.InAuthParametric;
import com.easou.common.util.CookieUtil;

public class LoginUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(LoginUtil.class);

	public static  Long doLogin(String tgc){
		try {
			if(StringUtils.isBlank(tgc)){
				return null;
			}
			EucService eucService = EucService.getInstance();
			// 判断是否是自动登录
			RequestInfo info = new RequestInfo();
			info.setSource("33");
			JBody jBody = new JBody();
			jBody.put(EucApiAuthCall.LOGIN_TYPE, LoginType.TGC);
			jBody.put(CookieUtil.COOKIE_TGC, tgc);
			JBean jBean = eucService.getResult(EucApiAuthCall.loginApiUri, jBody,new InAuthParametric(), info);
			if(jBean != null && jBean.getBody() != null){
				return jBean.getBody().getLong("easouId");
			}
		} catch (BeansException e) {
			logger.error("login error",e);
		} catch (Exception e) {
			logger.error("login error", e);
		}
		return null;
	} 
   
}
