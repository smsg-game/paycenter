package com.fantingame.pay.action.mobile.confirm;

import org.apache.log4j.Logger;

import com.fantingame.pay.action.common.BaseAction;
import com.fantingame.pay.entity.PayOrder;
import com.fantingame.pay.entity.TradeCode;
import com.fantingame.pay.exception.EasouPayException;
import com.fantingame.pay.manager.PayOrderManager;
import com.fantingame.pay.utils.StringTools;

/**
 * 客户端通知接口，用于处理客户端所反馈的订单状态信息
 * */
public class PayConfirmOfYeePayAction extends BaseAction {
	private static final long serialVersionUID = -1933669797143913506L;
	private static Logger logger = Logger.getLogger(PayConfirmOfYeePayAction.class);
	
	public String execute() throws Exception {
		try {
			getRequest().setCharacterEncoding("UTF-8");
			
			logger.info("invoice:"+getParam("invoice") +"....data:"+getParam("data"));
			String invoice = StringTools.getValue("invoice",getParam("invoice"),32,true,false);
            String data    = getParam("data");

			PayOrderManager orderManager = (PayOrderManager)getBean("payOrderManager");
			PayOrder order = orderManager.getEntityByInvoice(invoice);
			if(order != null){//订单存在
				if(order.getClientStatus()!=1){//订单未出理
					if("0".equals(data)){//0代表成功
						order.setClientStatus(1);
					}else{
						order.setClientStatus(-1);
					}
					order.setClientMsg(data);
					orderManager.update(order);
				}else{//订单已经被成功处理
					logger.error(TradeCode.EASOU_CODE_180.msgE+"...invoice:"+invoice);
				}
			}else{//订单不存在
				logger.error(TradeCode.EASOU_CODE_140.msgE+"...invoice:"+invoice);
			}
		} catch (EasouPayException e) {
			logger.error(e.getCode()+":"+e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} 
		return null;
	}

	
}
