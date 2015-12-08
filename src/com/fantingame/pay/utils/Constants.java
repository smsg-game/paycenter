package com.fantingame.pay.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.HttpClient;
import org.springframework.web.context.WebApplicationContext;

import com.fantingame.pay.action.ecenter.vo.SystemMyCardCardPayment;
import com.fantingame.pay.job.DelayItem;


public class Constants {
	//含有windows字样就是测试环境啦
//	public static final boolean isTest  = System.getProperty("os.name").toLowerCase().contains("windows")?true:false;
	public static boolean isTest  = false;
	
	
	public static final boolean isTest(HttpServletRequest request){
		if(request.getHeader("USER-AGENT").contains("isTest")){
			return true;
		}else{
			return false;
		}
	}
	
    //用户中心密钥
	public static String SECRET_USER_CENTER = "1d6a2da1d3985b13efe7073f2acb1657";
	
	//区分测试和正式环境
	public static boolean IS_TEST_ENV=false;
   
   //支付渠道商
   public static final long CHANNEL_ID_99BILL_PHONE_CARD   = 1000200030000001L;//快钱-手机充值卡
   public static final long CHANNEL_ID_EB                  = 1000200050000001L;//eb
   public static final long CHANNEL_ID_SMS                 = 1000200060000001L;//梵町短信支付
   public static final long CHANNEL_ID_SMSYHXF             = 1000200070000001L;//盈华讯方
   public static final long CHANNEL_ID_MO9                 = 1000200010000001L;//MO9-网页版
   public static final long CHANNEL_ID_ALIPAY              = 1000200020000001L;//支付宝-客户端版
   public static final long CHANNEL_ID_ALIPAY_WAP          = 1000200020000002L;//支付宝-网页版
   public static final long CHANNEL_ID_ALIPAY_MODULE       = 1000200020000003L;//支付宝-组件版
   public static final long CHANNEL_ID_ALIPAY_MODULE_TER   = 1000200020000004L;//支付宝-组件版
   public static final long CHANNEL_ID_ALIPAY_PC           = 1000200020000005L;//支付宝-pc版
   public static final long CHANNEL_ID_YEEPAY              = 1000200040000000L;//易宝
   public static final long CHANNEL_ID_99BILL              = 1000200030000000l;//快钱
   public static final long CHANNEL_ID_YEEPAY_PHONE_CARD   = 1000200040000001L;//易宝-手机充值卡
   public static final long CHANNEL_ID_YEEPAY_GAME_CARD    = 1000200040000002L;//快钱-游戏点卡
   public static final long CHANNEL_ID_YEEPAY_QQ_CARD      = 1000200040000003L;//快钱-QQ卡
   public static final long CHANNEL_ID_TENPAY              = 1000200080000001L;// 腾讯-财付通
   public static final long CHANNEL_ID_UNIONPAY			   = 1000200090000001L;// 易联-银联支付
   public static final long CHANNEL_ID_UNIONPAY_TEST	   = 1000200090000002L;// 易联-银联支付测试
   public static final long CHANNEL_ID_MY_CARD             = 1000200100000001L;//MYCARD 点卡支付正式
   public static final long CHANNEL_ID_MY_CARD_TEST        = 1000200100000002L;//MYCARD 点卡支付测试
   public static final long CHANNEL_ID_MY_CARD_PACKAGE     = 1000200100000003L;//MYCARD 钱包支付正式
   public static final long CHANNEL_ID_MY_CARD_PACKAGE_TEST= 1000200100000004L;//MYCARD 钱包支付测试
   public static final long CHANNEL_ID_MK                  = 1000200110000001L;//MK
   public static final long CHANNEL_ID_MOL                 = 1000200120000001L;//MOL
   
   //游戏合作商
   public static final long PARTNER_ID_BOOK_MALL           = 1000100010001010L;//EASOU自身作为parnter的ID；
   public static final long PARTNER_ID_SELF                = 1000100010001006L;//EASOU自身作为parnter的ID；
   
   //public static final String APPID_SEPARABLE          = "520709395";//可拆分的APPID-5元信用额度
   //public static final String APPID_NOT_SEPARABLE      = "520709394";//不可拆分的APPID-5元信用额度
   

   
   
   
   //支付宝的状态码
   public static final String ALIPAY_STATUS_9000         ="9000:操作成功";
   public static final String ALIPAY_STATUS_4000         ="4000:系统异常";
   public static final String ALIPAY_STATUS_4001         ="4001:数据格式不正确";
   public static final String ALIPAY_STATUS_4003         ="4003:该用户绑定的支付宝账户被冻结或不允许支付";
   public static final String ALIPAY_STATUS_4004         ="4004:该用户已解除绑定";
   public static final String ALIPAY_STATUS_4005         ="4005:绑定失败或没有绑定";
   public static final String ALIPAY_STATUS_4006         ="4006:订单支付失败";
   public static final String ALIPAY_STATUS_4010         ="4010:重新绑定账户";
   public static final String ALIPAY_STATUS_6000         ="6000:支付服务正在进行升级操作";
   public static final String ALIPAY_STATUS_6001         ="6001:用户中途取消支付操作";
   
   //MO9状态码
   public static final String MO9_STATUS_SUCCESS         ="10";
   
   
   //public static final String EASOU_CODE_FAIL                   = "-1:操作失败";
   //public static final String EASOU_CODE_SUCCESS                = "0:操作成功";
   /*服务端
   public static final String EASOU_CODE_SERVER_PARAM_ERROR     = "100:参数错误";
   public static final String EASOU_CODE_SERVER_SIGN_ERROR      = "110:验签错误";
   public static final String EASOU_CODE_SERVER_PARTNER_ERROR   = "120:游戏商帐号不存在";
   public static final String EASOU_CODE_SERVER_CHANNEL_ERROR   = "130:支付渠道不存在";
   public static final String EASOU_CODE_SERVER_ORDER_NOT_EXIST = "140:订单号不存在";
   public static final String EASOU_CODE_SERVER_ORDER_EXIST     = "150:订单号已存在";
   public static final String EASOU_CODE_SERVER_ORDER_INVALID   = "160:订单号已失效";
   public static final String EASOU_CODE_SERVER_PROTOCOL_ERROR  = "170:协议错误";
   public static final String EASOU_CODE_SERVER_TRADE_DUPLICATE = "180:订单重复处理";
   public static final String EASOU_CODE_SERVER_MONEY_INVALID   = "190:订单金额有误";
   //客户端
   public static final String EASOU_CODE_CLIENT_PARAM_ERROR     = "200:参数错误";
   public static final String EASOU_CODE_CLIENT_SIGN_ERROR      = "210:验签错误";
   public static final String EASOU_CODE_CLIENT_PARTNER_ERROR   = "220:游戏商帐号不存在";
   public static final String EASOU_CODE_CLIENT_CHANNEL_ERROR   = "230:支付渠道不存在";
   public static final String EASOU_CODE_CLIENT_ORDER_NOT_EXIST = "240:订单号不存在";
   public static final String EASOU_CODE_CLIENT_ORDER_EXIST     = "250:订单号已存在";
   public static final String EASOU_CODE_CLIENT_ORDER_INVALID   = "260:订单号已失效";
   public static final String EASOU_CODE_CLIENT_PROTOCOL_ERROR  = "270:协议错误";
   public static final String EASOU_CODE_CLIENT_TRADE_DUPLICATE = "280:订单重复处理";
   */
   /*
   public static final String EASOU_STATUS_TRADE_FAIL         = "-1:订单支付失败";
   public static final String EASOU_STATUS_CREATE_SUCCESS     = "0:订单创建成功";
   public static final String EASOU_STATUS_TRADE_SUCCESS      = "1:订单支付成功";
   public static final String EASOU_STATUS_TRADE_HALF_SUCCESS = "2:订单部分支付";
   public static final String EASOU_STATUS_PAY_WAITING        = "3:订单等待支付";
   */
   public static final int EASOU_SERVER_STATUS_SUCCESS        = 1;
   public static final int EASOU_SERVER_STATUS_CREATE         = 0;
   public static final int EASOU_SERVER_STATUS_PARTPAY        = 2;
   public static final int EASOU_SERVER_STATUS_WAITING        = 3;
   public static final int EASOU_SERVER_STATUS_FAIL           = -1;
   
   
   public static final String EASOU_COOKIE_QN_NAME = "easou_qn";
   
   
   //================================================
   
   public static WebApplicationContext CTX = null;
   public static HttpClient HTTPCLIENT = null;
   public static final int THREADPOOL_CORE_SIZE  = 3;
   public static final int THREADPOOL_MAX_SIZE   = 25;
   public static final int THREADPOOL_KEEP_ALIVE = 30;
   public static final DelayQueue<DelayItem> DQ  = new DelayQueue<DelayItem>();
   public static final ThreadPoolExecutor THREAD_EXECUTOR = new ThreadPoolExecutor(THREADPOOL_CORE_SIZE,THREADPOOL_MAX_SIZE,THREADPOOL_KEEP_ALIVE,TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>());
   
   //游戏合作商
   public static final long PARTNER_ID_BIANFENG = 1000100010001001L;//边锋
   
   //public static final String APPID_SEPARABLE          = "520709395";//可拆分的APPID-5元信用额度
   //public static final String APPID_NOT_SEPARABLE      = "520709394";//不可拆分的APPID-5元信用额度
   public static final String APPID_SEPARABLE          = "520709397";//可拆分的APPID-6元信用额度
   public static final String APPID_NOT_SEPARABLE      = "520709396";//不可拆分的APPID-6元信用额度
   
   public static HashMap<String,String> mapC = new HashMap<String, String>();
   public static HashMap<String,String> mapE = new HashMap<String, String>();
   
   
   public static final String MO9_OK                   = "OK";//返回
   public static final String MO9_TRADE_SUCCESS        = "TRADE_SUCCESS";//交易成功
   public static final String MO9_ILLEGAL_SIGN         = "ILLEGAL_SIGN";//商家提交的签名有错
   public static final String MO9_ILLEGAL_CURRENCY     = "ILLEGAL_CURRENCY";//商家提交的金额单位格式错误
   public static final String MO9_BUSINESS_NOT_EXISTED = "BUSINESS_NOT_EXISTED";//商家的账号不存在
   public static final String MO9_ILLEGAL_AMOUNT_PARAM = "ILLEGAL_AMOUNT_PARAM";//商家提交的金额格式错误
   public static final String MO9_ERROR_LC_NOT_SUPPORT = "ERROR_LC_NOT_SUPPORT";//商家所在提交的地区不支持
   public static final String MO9_HAS_NO_PRIVILEGE     = "HAS_NO_PRIVILEGE";//商家没有交易权限
   public static final String MO9_ILLEGAL_ARGUMENT     = "ILLEGAL_ARGUMENT";//其它参数错误
   public static final String MO9_INVOICE_EXISTED      = "INVOICE_EXISTED";//订单已经提交
   public static final String MO9_ILLEGAL_APP_ID       = "ILLEGAL_APP_ID";//商家应用没有被创建
   public static final String MO9_CODE_SUCCESS         ="10";
   

   public static final String EASOU_MSG_TRADE_SUCCESS          = "TRADE_SUCCESS";
   public static final String EASOU_MSG_TRADE_FAIL             = "TRADE_FAIL";
   public static final String EASOU_MSG_TRADE_WAITTING         = "TRADE_WAITTING";
   
   public static final String OWN_EASOU_CODE                   = "easou_code";
   public static final String OWN_EASOU_ORDER                  = "easou_order";
   public static final String OWN_ALIPAY_CODE                  = "alipay_code";
   public static final String OWN_YEEPAY_CODE                  = "yeepay_code";
   public static final String OWN_YEEPAY_STATUS                = "yeepay_status";
   
   //FIELD
   public static final String FIELD_PARTNER_ID                  = "partnerId";
   public static final String FIELD_TRADE_ID                    = "tradeId";
   public static final String FIELD_INVOICE_ID                  = "invoiceId"; 
   public static final String FIELD_TRADE_NAME                  = "tradeName";
   public static final String FIELD_TRADE_DESC                  = "tradeDesc";
   public static final String FIELD_REQ_FEE                     = "reqFee";
   public static final String FIELD_PAID_FEE                    = "paidFee";
   public static final String FIELD_SEPARABLE                   = "separable";
   public static final String FIELD_NOTIFY_URL                  = "notifyUrl";
   public static final String FIELD_APP_ID                      = "appId";
   public static final String FIELD_PAYER_ID                    = "payerId";
   public static final String FIELD_QN                          = "qn";
   public static final String FIELD_SIGN                        = "sign";
   public static final String FIELD_REDIRECT_URL                = "redirectUrl";
   public static final String FIELD_EXT_INFO                    = "extInfo";
  
   public static final String FIELD_CODE                        = "code";
   public static final String FIELD_MSG                         = "msg";
   public static final String FIELD_STATUS                      = "status";
   public static final String FIELD_INVOICE                     = "invoice";
   public static final String FIELD_URL                         = "url";
   public static final String FIELD_NEED_UPDATE                 = "needUpdate";
   public static final String FIELD_UPDATE_URL                  = "updateUrl";
   public static final String FIELD_VERSION_INFO                = "versionInfo";
   public static final String FIELD_FORCED                      = "forced";
   public static final String FIELD_VERSION_NAME                = "versionName";
   
   
   public static final String FIELD_TRADE_STATUS                = "tradeStatus";
   public static final String FIELD_NOTIFY_DATETIME             = "notifyDatetime";
   
   //易联下订单返回
   public static final String FIELD_ORDER_ID					= "merchantOrderId";
   public static final String FIELD_ORDER_TIME					= "merchantOrderTime";
   public static final String FIELD_ORDER_AMT					= "merchantOrderAmt";
   public static final String FIELD_CONNECT_TYPE                = "connectType";//00为测试环境  01为正式环境
   
   //易联下单参数
   public static final String FIELD_BUSS_NAME					= "梵付通";
   
   //易联手机银联支付证书
   public static final String PATH_OF_CERTIFICATE				= "/com/fantingame/pay/utils/unionpay/assets/";
   public static final String NAME_OF_TEST_CER					= "312000100816359-Signature.cer";
   public static final String NAME_OF_TEST_PFX					= "312000100816359-Signature.pfx";
   
   public static final String NAME_OF_PRO_CER					= "806000101604778-Signature.cer";
   public static final String NAME_OF_PRO_PFX					= "806000101604778-Signature.pfx";
   
   //易联code
   public static final String UNION_PAY_ORDER_SUC				= "0000";
   public static final String UNION_PAY_PAY_SUC					= "02";
   //游戏订单id
   public static final String PAY_ORDER_ID                      = "pay_order_id";
   
	//TODO这里需要处理的地方
	public static final Map<Integer,SystemMyCardCardPayment> systemMyCardPayList = new HashMap<Integer,SystemMyCardCardPayment>();
	//初始化 点卡档.............  
	static{
		
	}
   
}
   
