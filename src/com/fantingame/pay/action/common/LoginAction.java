package com.fantingame.pay.action.common;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.easou.cas.auth.EucAuthResult;
import com.easou.cas.client.EucApiAuthCall;
import com.easou.cas.client.EucApiCall;
import com.easou.common.api.CodeConstant;
import com.easou.common.api.EucApiResult;
import com.easou.common.api.ExpJUser;
import com.easou.common.api.RequestInfo;
import com.fantingame.pay.entity.PayUserAccount;
import com.fantingame.pay.entity.User;
import com.fantingame.pay.manager.PayUserAccountManager;
import com.fantingame.pay.utils.StringTools;

public class LoginAction extends BaseAction {
	/**
	 * 注释内容
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(LoginAction.class);
	private ExpJUser euser;
	private String bu;
	private String errMsg;

	//登录操作
	public String login() throws IOException {
		try {
			RequestInfo info = new RequestInfo();
			info.setSource("33");
			String username = getParam("username");
			String password = getParam("password");
			EucApiResult<EucAuthResult> apiResult = EucApiAuthCall.login(getRequest(), getResponse(), username, password,true, info);
			if (apiResult.getResult() != null) {
				User user = new User(apiResult.getResult().getUser(),apiResult.getResult().getToken().getToken());
				//登录成功，写session
				getWapSession().setAttribute("user", user);
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
			}else if(apiResult.getResult() ==null){//登录失败
				//不是来自拦截器的转发
				bu = StringTools.urlEncode(bu);
				if(!"interceptor".equals(getParam("source"))) errMsg = "login_error";
				return FAIL;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			bu = StringTools.urlEncode(bu);
			return FAIL;
		}
		return SUCCESS;
	}
	
	//登录失败，转跳到登录页面
	public String loginFail() throws Exception{
		return SUCCESS;
	}
	

	// 一键注册
	public String register() throws IOException {
		RequestInfo info = null;
		try {
			// 只注册不登录
			EucApiResult<ExpJUser> r1 = EucApiCall.autoAKeyRegist(info);
			if (CodeConstant.OK.equals(r1.getResultCode())) {
				euser = r1.getResult();
			}
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			bu = StringTools.urlEncode(bu);
			errMsg = "register_error";
			return FAIL;
		}
		getRequest().setAttribute("username",euser.getName());
		getRequest().setAttribute("password",euser.getPasswd());
		return SUCCESS;
	}
	
	public ExpJUser getEuser() {
		return euser;
	}

	public void setEuser(ExpJUser euser) {
		this.euser = euser;
	}

	public String getBu() {
		return bu;
	}

	public void setBu(String bu) {
		this.bu = bu;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

}
