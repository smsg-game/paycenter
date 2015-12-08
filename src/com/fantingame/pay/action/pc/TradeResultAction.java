package com.fantingame.pay.action.pc;

import org.apache.log4j.Logger;

import com.fantingame.pay.action.common.BaseAction;
import com.fantingame.pay.entity.PayEb;

//根据订单号查询可靠支付结果接口
public class TradeResultAction extends BaseAction{

	private static final long serialVersionUID = -5374056321974509593L;
	private static Logger logger = Logger.getLogger(TradeResultAction.class);

	public String execute(){
		try{
			if("".equals(invoice)){
				setStatus("-1");
				this.msg = "invoice参数不能为空";
				return JSON;
			}
			//获取订单
			PayEb payEb = getEbOrderByInvoice(invoice);
			if(payEb == null){
				this.msg = "订单不存在";
				setStatus("-1");
				return JSON;
			}
			setStatus(payEb.getStatus()+"");
			setMsg(payEb.getReceiveMsg());
		}catch (Exception e) {
			setMsg(e.getMessage());
			logger.error(e.getMessage(),e);
		}
		return JSON;
	}

}
