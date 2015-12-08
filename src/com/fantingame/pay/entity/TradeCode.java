package com.fantingame.pay.entity;

import com.fantingame.pay.utils.Constants;

public enum TradeCode {

	//梵町CODE
	EASOU_CODE_NEG_1("easou_code","-1","FAIL","操作失败"),
	EASOU_CODE_1("easou_code","1","SUCCESS","操作成功"),
	EASOU_CODE_100("easou_code","100","ILLEGAL_ARGUMENT","参数非法"),
	EASOU_CODE_110("easou_code","110","ILLEGAL_SIGN","验签错误"),
	EASOU_CODE_120("easou_code","120","ILLEGAL_PARTNER","非法游戏商"),
	EASOU_CODE_130("easou_code","130","ILLEGAL_CHANNEL","非法支付商"),
	EASOU_CODE_140("easou_code","140","ORDER_NOT_EXIST","订单号不存在"),
	EASOU_CODE_150("easou_code","150","ORDER_EXISTED","订单号已存在"),
	EASOU_CODE_160("easou_code","160","ORDER_INVALID","订单号已失效"),
	EASOU_CODE_170("easou_code","170","INVALID_PROTOCOL","协议错误"),
	EASOU_CODE_180("easou_code","180","TRADE_DUPLICATE","订单重复处理"),
	EASOU_CODE_190("easou_code","190","ORDER_AMOUNT_INVALID","订单金额有误"),
	EASOU_CODE_200("easou_code","200","ORDER_CREATE_ERROR","订单创建失败"),
	EASOU_CODE_201("easou_code","201","ORDER_CREATE_ERROR","创建EB充值订单失败"),
	
	//梵町STATUS
	EASOU_ORDER_NEG_2("easou_order","-2","UNKNOW_ERROR","未知错误"),
	EASOU_ORDER_NEG_1("easou_order","-1","TRADE_FAIL","订单支付失败"),
	EASOU_ORDER_0("easou_order","0","CREATE_SUCCESS","订单创建成功"),
	EASOU_ORDER_1("easou_order","1","TRADE_SUCCESS","订单支付成功"),
	EASOU_ORDER_2("easou_order","2","HALF_SUCCESS","订单部分支付"),
	EASOU_ORDER_3("easou_order","3","PAY_WAITING","订单等待支付"),
	
	//ALIPAY
	ALIPAY_CODE_9000("alipay_code","9000","SUCCESS","操作成功"),
	ALIPAY_CODE_4000("alipay_code","4000","SYSTEM_ERROR","系统异常"),
	ALIPAY_CODE_4001("alipay_code","4001","ILLEGAL_DATA","数据格式不正确"),
	ALIPAY_CODE_4003("alipay_code","4003","ILLEGAL_ACCOUNT1","该用户绑定的支付宝账户被冻结或不允许支付"),
	ALIPAY_CODE_4004("alipay_code","4004","ACCOUNT_NOT_BIND","该用户已解除绑定"),
	ALIPAY_CODE_4005("alipay_code","4005","BIND_ERROR","绑定失败或没有绑定"),
	ALIPAY_CODE_4006("alipay_code","4006","TRADE_FAIL","订单支付失败"),
	ALIPAY_CODE_4010("alipay_code","4010","REBIND","重新绑定账户"),
	ALIPAY_CODE_6000("alipay_code","6000","SERVICE_UPDATING","支付服务正在进行升级操作"),
	ALIPAY_CODE_6001("alipay_code","6001","TRADE_CANCEL","用户中途取消支付操作"),
	
	//YEEPAY_CODE
	YEEPAY_CODE_1("yeepay_code","1","SUCCESS","成功"),
	YEEPAY_CODE_NEG_1("yeepay_code","-1","ILLEGAL_SIGN","签名较验失败或未知错误"),
	YEEPAY_CODE_2("yeepay_code","2","TRADE_DUPLICATE","卡密成功处理过或者提交卡号过于频繁"),
	YEEPAY_CODE_5("yeepay_code","5","CARD_TO_MUCH","卡数量过多，目前最多支持10张卡"),
	YEEPAY_CODE_11("yeepay_code","11","INVOICE_EXISTED","订单号重复"),
	YEEPAY_CODE_25("yeepay_code","25","CARD_NUMBER_ERROR","卡号卡密或卡面额不符合规则"),
	YEEPAY_CODE_66("yeepay_code","66","AMT_ERROR","支付金额有误"),
	YEEPAY_CODE_95("yeepay_code","95","TRADE_CHANNEL_ERROR","支付方式未开通"),
	YEEPAY_CODE_112("yeepay_code","112","CARD_TYPE_ERROR","业务状态不可用，未开通此类卡业务"),
	YEEPAY_CODE_8001("yeepay_code","8001","AMT_GROUP_ERROR","卡面额组填写错误"),
	YEEPAY_CODE_8002("yeepay_code","8002","CARD_GROUP_ERROR","卡号密码为空或者数量不相等（使用组合支付时）"),
	//YEEPAY_STATUS
	YEEPAY_STATUS_0("yeepay_status","0","","销卡成功，订单成功"),
	YEEPAY_STATUS_1("yeepay_status","1","","销卡成功，订单失败"),
	YEEPAY_STATUS_7("yeepay_status","7","","卡号卡密或卡面额不符合规则"),
	YEEPAY_STATUS_1002("yeepay_status","1002","","本张卡密您提交过于频繁，请您稍后再试"),
	YEEPAY_STATUS_1003("yeepay_status","1003","","不支持的卡类型（比如电信地方卡）"),
	YEEPAY_STATUS_1004("yeepay_status","1004","","密码错误或充值卡无效"),
	YEEPAY_STATUS_1006("yeepay_status","1006","","充值卡无效"),
	YEEPAY_STATUS_1007("yeepay_status","1007","","卡内余额不足"),
	YEEPAY_STATUS_1008("yeepay_status","1008","","余额卡过期（有效期1个月）"),
	YEEPAY_STATUS_1010("yeepay_status","1010","","此卡正在处理中"),
	YEEPAY_STATUS_10000("yeepay_status","10000","","未知错误"),
	YEEPAY_STATUS_2005("yeepay_status","2005","","此卡已使用"),
	YEEPAY_STATUS_2006("yeepay_status","2006","","卡密在系统处理中"),
	YEEPAY_STATUS_2007("yeepay_status","2007","","该卡为假卡"),
	YEEPAY_STATUS_2008("yeepay_status","2008","","该卡种正在维护"),
	YEEPAY_STATUS_2009("yeepay_status","2009","","浙江省移动维护"),
	YEEPAY_STATUS_2010("yeepay_status","2010","","江苏省移动维护"),
	YEEPAY_STATUS_2011("yeepay_status","2011","","福建省移动维护"),
	YEEPAY_STATUS_2012("yeepay_status","2012","","辽宁省移动维护"),
	YEEPAY_STATUS_2013("yeepay_status","2013","","该卡已被锁定"),
	YEEPAY_STATUS_2014("yeepay_status","2014","","系统繁忙，请稍后再试"),
	YEEPAY_STATUS_3001("yeepay_status","3001","","卡不存在"),
	YEEPAY_STATUS_3002("yeepay_status","3002","","卡已使用过"),
	YEEPAY_STATUS_3003("yeepay_status","3003","","卡已作废"),
	YEEPAY_STATUS_3004("yeepay_status","3004","","卡已冻结"),
	YEEPAY_STATUS_3005("yeepay_status","3005","","卡未激活"),
	YEEPAY_STATUS_3006("yeepay_status","3006","","密码不正确"),
	YEEPAY_STATUS_3007("yeepay_status","3007","","卡正在处理中"),
	YEEPAY_STATUS_3101("yeepay_status","3101","","系统错误"),
	YEEPAY_STATUS_3102("yeepay_status","3102","","卡已过期"),
    ;  
    
	
	
	public String own;
	public String code;
	public String msgE;  
	public String msgC;
    
	
	//构造枚举值，比如RED(255,0,0)  
    private TradeCode(String own,String code,String msgE,String msgC){
    	this.own = own;
        this.code=code;  
        this.msgE=msgE;  
        this.msgC=msgC;
        Constants.mapE.put(this.own+"_"+code, msgE);
        Constants.mapC.put(this.own+"_"+code, msgC);
    }

  public String toString(){//自定义的public方法  
      return super.toString()+"("+code+","+msgE+","+msgC+")";  
  }
	
  public static String getMsgC(String own,String code){
	  return Constants.mapC.get(own+"_"+code);
  }
	
  public static String getMsgE(String own,String code){
	  return Constants.mapE.get(own+"_"+code);
  }
	
}
