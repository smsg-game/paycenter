package com.fantingame.pay.action.wapc;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.fantingame.pay.action.common.BaseAction;
import com.fantingame.pay.entity.PayEb;

//根据订单号查询可靠支付结果接口
public class TradeResultAction extends BaseAction{

	private static final long serialVersionUID = -5374056321974509593L;
	private static Logger logger = Logger.getLogger(TradeResultAction.class);

	public String execute(){
		try{
			if(StringUtils.isEmpty(invoice)){
				setStatus("-1");
				setMsg("invoice参数不能为空");
				return SUCCESS;
			}
			//获取订单
			PayEb payEb = getEbOrderByInvoice(invoice);
			if(payEb == null){
				setStatus("-1");
				setMsg("订单不存在");
				return SUCCESS;
			}
			if(payEb.getStatus()==1){
				setStatus("1");
				setMsg("支付成功");
			}else if(payEb.getStatus()==0){
				setStatus("0");
				setMsg("订单未支付");
			}else{
				setStatus("-1");
				setMsg("支付失败:"+payEb.getReceiveMsg());
			}
			return SUCCESS;
		}catch (Exception e) {
			setStatus("-1");
			setMsg("支付失败:"+e.getMessage());
			logger.error(e.getMessage(),e);
		}
		return SUCCESS;
	}

}
