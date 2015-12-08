package com.fantingame.pay.action.module;

import org.apache.log4j.Logger;

import com.fantingame.pay.action.common.BaseAction;
import com.fantingame.pay.entity.PayChannel;
import com.fantingame.pay.entity.PayEb;
import com.fantingame.pay.entity.PayTrade;
import com.fantingame.pay.entity.TradeCode;
import com.fantingame.pay.exception.EasouPayException;
import com.fantingame.pay.manager.PayChannelManager;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.StringTools;
import com.fantingame.pay.utils.bill99.Bill99Url;
import com.fantingame.pay.utils.yeepay.YeePayUtil;

public class CardAction extends BaseAction{

	private static final long serialVersionUID = -1936642025433531784L;
	private static Logger logger = Logger.getLogger(CardAction.class);
	
	public String selectCardPage(){
		payTrade = getTradeByInvoice(invoice);
		return StringTools.getStr(getParam("type"),"phonecard");
	}
	
	/**
	 * 1 页面以JSON方式提交卡密和面额还有订单号和卡种到此接口
	 * 2 根据卡种从数据库检出当前有效的渠道
	 * 3 用HTTPCLIENT直连获取返回结果
	 * 4 返回结果为成功则开始循环遍历数据库对应订单的支付状态
	 * 5 前端页面等待返回结果并根据结果驱动页面到另一个
	 */
	public String execute() {
		try {

			String channelType    = StringTools.getValue("channelType",getParam("channelType"),8,true,true);
			String cardAmt = StringTools.getValue("cardAmt",getParam("cardAmt"),8,true,false);
			String cardNumber  = StringTools.getValue("cardNumber",getParam("cardNumber"),32,true,false);
			String cardPwd     = StringTools.getValue("cardPwd",getParam("cardPwd"),32,true,false);
			String invoice     = StringTools.getValue("invoice",getInvoice(),32,true,false);
			PayChannelManager channelManager = (PayChannelManager)getBean("payChannelManager");
			PayChannel channel = channelManager.getChannelByType(channelType);
			payTrade = getTradeByInvoice(invoice);
			PayEb payEb = newPayEbOrder(invoice , channel.getId(),Constants.isTest?payTrade.getReqFee():cardAmt,"卡号:"+cardNumber); 
			
			if(Constants.CHANNEL_ID_YEEPAY== channel.getParentId()){//易宝-手机卡支付
				YeePayUtil.getYeePayResult(this, payTrade, payEb, channel,  cardAmt, cardNumber, cardPwd);
			}else if(Constants.CHANNEL_ID_99BILL == channel.getParentId()){
				Bill99Url.bill99Resopnse(this, payTrade, payEb, channel, cardAmt, cardNumber, cardPwd);
			}
			setInvoice(invoice);  //JS中会用到；
		}catch (EasouPayException e) {
			setStatus(FAIL);
			setMsg(e.getMessage());
			logger.error(e.getMessage(),e);
		}catch (Exception e) {
			setStatus(FAIL);
			setMsg(TradeCode.EASOU_CODE_NEG_1.msgC);
			logger.error(TradeCode.EASOU_CODE_NEG_1.msgC);
		}
		return JSON;
	}
	
	
	
	//卡类充值,先生成订单，再走正常流程
	public String cardCharge(){
		try {
			isCharge = true;
			String backUrl = (String)getRequest().getSession().getAttribute("charge_backurl");
			String money = getParam("cardAmt");
			String invoiceId=getParam(Constants.FIELD_INVOICE_ID);
			//测试环境，不能消卡啊
			if(Constants.isTest) money = "0.1";
			PayTrade trade = newChargeTrade(money,backUrl,"module",invoiceId);
			if(trade!=null) invoice = trade.getInvoice();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
		}
		return this.execute();
	}


}
