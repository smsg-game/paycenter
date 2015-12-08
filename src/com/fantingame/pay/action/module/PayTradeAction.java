package com.fantingame.pay.action.module;

import javax.servlet.http.Cookie;

import com.easou.common.util.CookieUtil;
import com.fantingame.pay.action.common.BaseAction;

public class PayTradeAction extends BaseAction {

	private static final long serialVersionUID = 1196584399284841021L;

	@Override
	public String execute() throws Exception {
		String tgc = getRequest().getParameter(CookieUtil.COOKIE_TGC);
		String u = getRequest().getParameter(CookieUtil.COOKIE_U);
		if(tgc != null && !"".equals(tgc.trim())) {
			Cookie tgcCookie = new Cookie(CookieUtil.COOKIE_TGC, tgc);
			tgcCookie.setDomain(CookieUtil.DEFAULT_DOMAIN);
			tgcCookie.setMaxAge(60*60*24*182);
			tgcCookie.setPath("/");
			getResponse().addCookie(tgcCookie);
		}
		if(u != null && !"".equals(u.trim())) {
			Cookie uCookie = new Cookie("U", u);
			uCookie.setDomain(CookieUtil.DEFAULT_DOMAIN);
			uCookie.setMaxAge(60*60*24*183);
			uCookie.setPath("/");
			getResponse().addCookie(uCookie);
		}
		getResponse().sendRedirect(getRedirectUrl());
		return null;
	}

	
}
