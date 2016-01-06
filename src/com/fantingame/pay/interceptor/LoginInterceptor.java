package com.fantingame.pay.interceptor;

import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.StrutsRequestWrapper;
import org.springframework.beans.BeansException;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.easou.cas.auth.EucAuthResult;
import com.easou.cas.client.EucApiAuthCall;
import com.easou.common.api.CodeConstant;
import com.easou.common.api.EucApiResult;
import com.easou.common.api.EucParserException;
import com.easou.common.api.RequestInfo;
import com.easou.common.util.CookieUtil;
import com.fantingame.pay.action.common.LoginAction;
import com.fantingame.pay.action.ecenter.ECenterBaseAction;
import com.fantingame.pay.action.mobile.notify.NotifyFromMolAction;
import com.fantingame.pay.action.mobile.notify.NotifyFromMyCardAction;
import com.fantingame.pay.action.mobile.notify.NotifyFromNewMobileAlipayAction;
import com.fantingame.pay.action.mobile.notify.NotifyFromUnionPayAction;
import com.fantingame.pay.action.mobile.old.NotifyFromAlipayAction;
import com.fantingame.pay.action.mobile.old.NotifyFromMo9Action;
import com.fantingame.pay.action.module.PayTradeAction;
import com.fantingame.pay.action.pc.UserPayDetailAction;
import com.fantingame.pay.action.pc.nofity.NotifyFromTenpayAction;
import com.fantingame.pay.entity.PayUserAccount;
import com.fantingame.pay.entity.User;
import com.fantingame.pay.manager.PayUserAccountManager;
import com.fantingame.pay.session.WapServletRequestWrapper;
import com.fantingame.pay.session.WapSession;
import com.fantingame.pay.utils.LoggerUtil;
import com.fantingame.pay.utils.StringTools;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
/**
 * 
 * 登录拦截器
 * 
 * @version 1.0, 2012-11-30
 */
@SuppressWarnings("serial")
public class LoginInterceptor extends AbstractInterceptor {

	private static Logger logger = Logger.getLogger(LoginInterceptor.class);
	private static final String FAIL_MSG = "{\"status\":\"NOT_LOGIN\",\"msg\":\"请先登录\"}";

	public String intercept(ActionInvocation invocation) throws Exception{

		StrutsRequestWrapper strutsRequest = (StrutsRequestWrapper) ServletActionContext.getRequest();
		// 自定义封装request对象strutsRequest.
		WapServletRequestWrapper request = (WapServletRequestWrapper) strutsRequest.getRequest();
		/*
		 * ServletContext servletContext = ServletActionContext
		 * .getServletContext();
		 */
		/*
		 * ApplicationContext ctx = WebApplicationContextUtils
		 * .getRequiredWebApplicationContext(servletContext);
		 */
		WapSession session = request.getWapSession();
		Object action = invocation.getAction();
		//如果登录、注册
        if(action instanceof LoginAction || action instanceof NotifyFromAlipayAction 
        		|| action instanceof NotifyFromMo9Action || action instanceof PayTradeAction
        		|| action instanceof NotifyFromTenpayAction || action instanceof com.fantingame.pay.action.pc.nofity.NotifyFromAlipayAction
        		|| action instanceof UserPayDetailAction || action instanceof NotifyFromUnionPayAction || action instanceof NotifyFromMyCardAction || action instanceof NotifyFromMolAction || action instanceof NotifyFromNewMobileAlipayAction){
        	return invocation.invoke();
        }
		//判断用户有没有登录,
        String tgc = request.getParameter(CookieUtil.COOKIE_TGC);
        Cookie cookie = CookieUtil.getCookie(request, CookieUtil.COOKIE_TGC);
        if(cookie != null && StringUtils.isNotBlank(tgc) && !cookie.getValue().equals(tgc)){
        	session.removeAttribute("user");
        	cookie = null;
        }
		if (session == null || session.getAttribute("user") == null) {
			if(cookie == null){
				LoggerUtil.info("tgt:"+tgc);
				if(StringUtils.isEmpty(tgc) || StringUtils.isBlank(tgc)){
					invocation.getInvocationContext().put(InterceptorUtil.PAY_KEY_TARGET_URL,getGoingURL(request, session));
					return Action.LOGIN;
				}
				CookieUtil.addCookie(CookieUtil.COOKIE_TGC,tgc,".fantingame.com","/",1*60*60*24,ServletActionContext.getResponse());
				StringBuffer sb = new StringBuffer();
				Enumeration params = request.getParameterNames();
				while (params.hasMoreElements()) {
					String paramName = (String) params.nextElement();
					sb.append(paramName).append("=").append(request.getParameter(paramName)).append("&");
				}
				if(sb.toString().length()>0) sb.deleteCharAt(sb.length()-1);
				String url = request.getRequestURI()+"?"+sb.toString();
				invocation.getInvocationContext().put(InterceptorUtil.PAY_KEY_REQ_FEE,url);
				return "reqFee";
			}
			if(!doLogin(request)){// 没有登录
				//tgc过期
//				if(StringUtils.isNotBlank(tgc) && LoginUtil.doLogin(tgc) != null){
//					CookieUtil.addCookie(CookieUtil.COOKIE_TGC,tgc,".easou.com","/",1*60*60*24,ServletActionContext.getResponse());
//					StringBuffer sb = new StringBuffer();
//					Enumeration params = request.getParameterNames();
//					while (params.hasMoreElements()) {
//						String paramName = (String) params.nextElement();
//						sb.append(paramName).append("=").append(request.getParameter(paramName)).append("&");
//					}
//					if(sb.toString().length()>0) sb.deleteCharAt(sb.length()-1);
//					String url = request.getRequestURI()+"?"+sb.toString();
//					invocation.getInvocationContext().put(InterceptorUtil.PAY_KEY_REQ_FEE,url);
//					return "reqFee";
//				}
				if(action instanceof ECenterBaseAction) {
					ServletActionContext.getResponse().setContentType("application/json;charset=UTF-8");
					PrintWriter writer = ServletActionContext.getResponse().getWriter();
					writer.write(FAIL_MSG);
					writer.flush();
					writer.close();
					return null;
				}
				// 将登陆后后要跳转的地址存入到值栈对象中
				invocation.getInvocationContext().put(InterceptorUtil.PAY_KEY_TARGET_URL,getGoingURL(request, session));
				return Action.LOGIN;
			}
		}else{//有session
			//二次校验,如果用户没关闭浏览器，session值还在，用户去用户中心注册了一个新的帐号，并且来到梵付通，
			//这样会导致用户中心是用新帐号，梵付通用的是旧帐号
			User user = (User)session.getAttribute("user");
			if(cookie==null) {
				invocation.getInvocationContext().put(InterceptorUtil.PAY_KEY_TARGET_URL,getGoingURL(request, session));
				return Action.LOGIN;
			}else if(!user.getUserToken().equals(cookie.getValue())){
			    if(!doLogin(request)){
			    	// 将登陆后后要跳转的地址存入到值栈对象中
					invocation.getInvocationContext().put(InterceptorUtil.PAY_KEY_TARGET_URL,getGoingURL(request, session));
					return Action.LOGIN;
			    }
			}
		}

		// 已经登录了直接返回
        //System.out.println("登陆用户ID为:"+ ((User) session.getAttribute("user")).getId());
		return invocation.invoke();

	}
	
	// 组装地址
	private String getGoingURL(HttpServletRequest request, HttpSession session) {
		// 分别获得命名空间，action名,以及请求参数从新构造成一个URL保存在session中
		StringBuffer url = new StringBuffer(request.getRequestURI());
		String qs = request.getQueryString();
		if (null != qs && qs.length() > 0) {
			url.append("?").append(qs);
		}
		return StringTools.urlEncode(url.toString());

	}
	
	private boolean doLogin(HttpServletRequest request){
		// 判断是否是自动登录
		RequestInfo info = new RequestInfo();
		info.setSource("33");
		EucApiResult<EucAuthResult> apiResult;
		try {
			apiResult = EucApiAuthCall.autoLogin(request,ServletActionContext.getResponse(), info);
			// 已经自动登录
			if (apiResult != null && CodeConstant.OK.equals(apiResult.getResultCode())) {
				User user = new User(apiResult.getResult().getUser(),apiResult.getResult().getToken().getToken());
				request.getSession().setAttribute("user", user);
				
				//测试
				//user.setMobile("13900161234");
				
				//如果不存在就添加帐号
				PayUserAccountManager payUserAccountManager = (PayUserAccountManager)WebApplicationContextUtils.getRequiredWebApplicationContext(ServletActionContext.getServletContext()).getBean("payUserAccountManager");
				PayUserAccount payUserAccount = payUserAccountManager.getUserAcountByJuserId(user.getId());
				if(payUserAccount==null){
					payUserAccount = new PayUserAccount();
					payUserAccount.setFee(0L);
					payUserAccount.setJuserId(user.getId());
					payUserAccount.setUsername("ft."+user.getId());
					payUserAccountManager.save(payUserAccount);
				}
				return true;
			}
		} catch (BeansException e) {
			logger.error(e, e);
		} catch (EucParserException e) {
			logger.error(e, e);
		}
		return false;
	}
	
	

}