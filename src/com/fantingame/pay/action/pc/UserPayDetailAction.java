package com.fantingame.pay.action.pc;

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
import com.fantingame.pay.action.common.BaseAction;
import com.fantingame.pay.action.ecenter.vo.UserTradeHistory;
import com.fantingame.pay.entity.PayUserAccount;
import com.fantingame.pay.manager.PayEbManager;
import com.fantingame.pay.utils.LoginUtil;
import com.fantingame.pay.utils.PageData;
import com.fantingame.pay.utils.StringTools;

/**
 * 用户充值记录查询
 * @author easou
 *
 */
public class UserPayDetailAction extends BaseAction{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(UserPayDetailAction.class);
	private PageData<UserTradeHistory> data;
	private Long userEb = 0L;

	@Override
	public String execute() throws Exception {
		String tgt = getParam(CookieUtil.COOKIE_TGC);
		Long userId = LoginUtil.doLogin(tgt);;
        if(userId != null){
        	int page = StringTools.strToInt(getParam("page"), 1);
    		int size = StringTools.strToInt(getParam("size"), 20);
    		String startTime = getParam("startTime");
    		String endTime = getParam("endTime");
    		PayEbManager manager = (PayEbManager) getBean("payEbManager");
    		data = manager.getTradeHisPCByUserId(page, size,userId,startTime,endTime);
    		
        }
        return JSON;
	}
	
	public String getUserAllEb(){
		String tgt = getParam(CookieUtil.COOKIE_TGC);
		Long userId = LoginUtil.doLogin(tgt);
		if(userId != null){
			PayUserAccount payUserAccount = getUserAccountById(userId);
			if(payUserAccount != null){
				userEb = payUserAccount.getFee();
			}
		}
		return "eb";
	}

	public PageData<UserTradeHistory> getData() {
		return data;
	}

	public void setData(PageData<UserTradeHistory> data) {
		this.data = data;
	}

	public Long getUserEb() {
		return userEb;
	}

	public void setUserEb(Long userEb) {
		this.userEb = userEb;
	}
	
}
