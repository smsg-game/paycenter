package com.fantingame.pay.action.wap;

import com.fantingame.pay.action.common.BaseAction;
import com.fantingame.pay.entity.User;
import com.fantingame.pay.manager.PayEbManager;
import com.fantingame.pay.utils.PageUtil;
import com.fantingame.pay.utils.StringTools;

public class UserAction extends BaseAction {

	private static final long serialVersionUID = 1L;
//	private static Logger logger = Logger.getLogger(UserAction.class);
	
	private PageUtil dealflows;//交易流水记录

	public String userDetail() {
		int page = StringTools.strToInt(getParam("page"), 1);
		int size = StringTools.strToInt(getParam("size"), 3);
		User user = getUser();
		PayEbManager manager = (PayEbManager) getBean("payEbManager");
		dealflows = manager.getTradeHistoryByUserId(page, size, user.getId());
		return SUCCESS;
	}

	public PageUtil getDealflows() {
		return dealflows;
	}

	public void setDealflows(PageUtil dealflows) {
		this.dealflows = dealflows;
	}
	
}
