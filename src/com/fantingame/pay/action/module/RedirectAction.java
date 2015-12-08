package com.fantingame.pay.action.module;

import org.apache.log4j.Logger;

import com.easou.common.util.StringUtil;
import com.fantingame.pay.action.common.BaseAction;
import com.fantingame.pay.utils.Constants;

//接口不可靠，仅用于驱动前端页面，故不加签名；文档中需注明；
public class RedirectAction extends BaseAction {

	private static Logger logger = Logger.getLogger(RedirectAction.class);

	private static final long serialVersionUID = 2414766626391918361L;

	public String execute() {
		try {
			//如果invoice有值
			logger.info("redirection url=redirect.e, invoice=" + invoice);
			if(!StringUtil.isEmpty(invoice)){
				payTrade = getTradeByInvoice(invoice);
				if(payTrade != null && payTrade.getStatus() != Constants.EASOU_SERVER_STATUS_SUCCESS) {
					setPayTrade(payTrade);
					return FAIL;
				}
//				if(payTrade!=null && !StringUtil.isEmpty(payTrade.getRedirectUrl())){
//					setRedirectUrl(payTrade.getRedirectUrl());
//					return REDIRECT;
//				}
				if(payTrade!=null && payTrade.getStatus() == Constants.EASOU_SERVER_STATUS_SUCCESS){
					setPayTrade(payTrade);
					String isCharge =  getRequest().getParameter("ic");
					if("1".equals(isCharge)) {
						return CHARGE;
					} else {
						return SUCCESS;
					}
				} else if(payTrade!=null && payTrade.getStatus() == Constants.EASOU_SERVER_STATUS_FAIL) {
					return FAIL;
				}
			}else{
				String backUrl = (String)getRequest().getSession().getAttribute("charge_backurl");
				if(!StringUtil.isEmpty(backUrl)){
					setRedirectUrl(backUrl);
					return REDIRECT;
				}
			}
			//如果没回调地址怎么办?????
			return CHARGE;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return FAIL;
		}
	}

	/*
	 * 梵町SDK--支付结果
	 * */
	public String showResult(){
		logger.info("redirection url=redirect!showResult.e, invoice=" + invoice);
		payTrade = getTradeByInvoice(invoice);
		if(payTrade != null && payTrade.getStatus() == Constants.EASOU_SERVER_STATUS_FAIL) {
			return FAIL;
		} else if(payTrade != null && payTrade.getStatus() != Constants.EASOU_SERVER_STATUS_SUCCESS) {
			return FAIL;
		} else if(payTrade != null && payTrade.getStatus() == Constants.EASOU_SERVER_STATUS_SUCCESS) {
			return SUCCESS;
		}
		if("success".equals(getStatus())) {
			return SUCCESS;
		}
		if("fail".equals(getStatus())) {
			return FAIL;
		}
		return SUCCESS;
	}

}
