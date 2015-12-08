package com.fantingame.pay.action.ecenter;

import com.fantingame.pay.action.ecenter.vo.UserTradeHistory;
import com.fantingame.pay.entity.User;
import com.fantingame.pay.manager.PayEbManager;
import com.fantingame.pay.utils.PageData;
import com.fantingame.pay.utils.PageUtil;
import com.fantingame.pay.utils.StringTools;

public class UserAction extends ECenterBaseAction {

	private static final long serialVersionUID = 1L;
//	private static Logger logger = Logger.getLogger(UserAction.class);
	
	private PageUtil dealflows;//交易流水记录
	
	private PageData<UserTradeHistory> data;

	public String userDetail() {
		int page = StringTools.strToInt(getParam("page"), 1);
		int size = StringTools.strToInt(getParam("size"), 3);
		User user = getUser();
		PayEbManager manager = (PayEbManager) getBean("payEbManager");
		dealflows = manager.getTradeHistoryByUserId(page, size, user.getId());
		return SUCCESS;
	}

	public String tradeHistory() {
		int page = StringTools.strToInt(getParam("page"), 1);
		int size = StringTools.strToInt(getParam("size"), 20);
		User user = getUser();
		PayEbManager manager = (PayEbManager) getBean("payEbManager");
		data = manager.getTradeHisByUserId(page, size, user.getId());
		return SUCCESS;
	}
	
	public PageUtil getDealflows() {
		return dealflows;
	}

	public void setDealflows(PageUtil dealflows) {
		this.dealflows = dealflows;
	}

	public PageData<UserTradeHistory> getData() {
		return data;
	}

	public void setData(PageData<UserTradeHistory> data) {
		this.data = data;
	}
	
}
