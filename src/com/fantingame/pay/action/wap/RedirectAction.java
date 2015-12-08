package com.fantingame.pay.action.wap;

import org.apache.log4j.Logger;

import com.easou.common.util.StringUtil;
import com.fantingame.pay.action.common.BaseAction;
import com.fantingame.pay.entity.PayTrade;

//接口不可靠，仅用于驱动前端页面，故不加签名；文档中需注明；
public class RedirectAction extends BaseAction {

	private static Logger logger = Logger.getLogger(RedirectAction.class);

	private static final long serialVersionUID = 2414766626391918361L;

	public String execute() {
		try {
			//如果invoice有值
			if(!StringUtil.isEmpty(invoice)){
				PayTrade payTrade = getTradeByInvoice(invoice);
				if(payTrade!=null && !StringUtil.isEmpty(payTrade.getRedirectUrl())){
					setRedirectUrl(payTrade.getRedirectUrl());
					return REDIRECT;
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
			return ERROR;
		}
	}

	/*
	 * 梵町SDK--支付结果
	 * */
	public String showResult(){
		return SUCCESS;
	}

}
