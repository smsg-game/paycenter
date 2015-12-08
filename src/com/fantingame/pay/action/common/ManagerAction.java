package com.fantingame.pay.action.common;

import java.io.PrintWriter;
import java.util.List;

import org.apache.log4j.Logger;

import com.fantingame.pay.entity.PayTrade;
import com.fantingame.pay.job.DelayItem;
import com.fantingame.pay.manager.PayTradeManager;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.StringTools;

/**
 * 重新将失败的NOTIFY加入到任务队列里
 * */

public class ManagerAction extends BaseAction {

	private static final long serialVersionUID = 5490792191825343426L;
	private static final Logger logger = Logger.getLogger(ManagerAction.class);

	public String execute() throws Exception {
		//设置编码
	    getResponse().setContentType("text/html;charset=UTF-8");
		PrintWriter out = getResponse().getWriter();
		try{
			String account = getRequest().getParameter("account");
			String password = getRequest().getParameter("password");
			
			PayTradeManager tradeManager = (PayTradeManager) getBean("payTradeManager");
			if("brian".equals(account) && "123456".equals(password)){
				List<PayTrade> list =  tradeManager.getNotifyTask();
				int size = (list!=null)?list.size():0;
				if(list!=null){
					//往DelayQueue里添加任务
					for(PayTrade trade : list){
						DelayItem di = new DelayItem(1);
						di.setPaidFee(trade.getPaidFee());
						di.setNotifyUrl(trade.getNotifyUrl());
						di.setTradeId(trade.getTradeId());
						di.setTradeStatus("TRADE_SUCCESS");
						di.setInvoice(trade.getInvoice());
						di.setPartnerId(trade.getPartnerId());
						di.setAppId(trade.getAppId());
						di.setReqFee(trade.getReqFee());
						di.setPayerId(trade.getPayerId());
						di.setTradeName(trade.getTradeName());
						di.setNotifyDatetime(StringTools.getSecondFormat_().format(trade.getSuccessDatetime()));
						Constants.DQ.add(di);//添加到队列任务里去
					}
				}
				out.print("成功添加Notify任务："+size);
			}else{
				out.print("帐号密码错误！！！");
			}
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	public String queueStatus() throws Exception {
		getResponse().getWriter().write("queue size:"+Constants.DQ.size());
		return null;
	}
}
