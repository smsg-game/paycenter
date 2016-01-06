package com.alipay.config;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.3
 *日期：2012-08-10
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
	
 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”

 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 */

public class AlipayConfig {
	
	//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	public static String partner = "2088811562217302";
	// 商户的私钥
	public static String private_key = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAMv8+tqh9fSD0aKr4uWtgjIvKZnbKbFFttGWqEWiVnB1dM4MQ6hMGvZDvYr29ZUs3I3k37ELqvkMxXHrvFez/ZvLhaFjsIcG9iJa7Vh0H1LC6lTDqUaI8y/H3m8iFvmpW0t8GGWoigNbvcwjjxjbt/mT/FdUeCITmKXHhedE1DTZAgMBAAECgYAHhRv6U14t1W41AQEsVmkmUHbIJTw0PM2wjE1Hw+RL4QidT/Zujd+wlG64YMJM7Ypz8KwOas+yvrXIrVgUI56FvvGRXRxPmVdWo8pWMpcWqa0+OGQ1bCgb1LeVEPLkKsJ8vt50vSYpU8IuIGkaezSfp98tuYL/U5XZ7PA/M9rYAQJBAOZHqmH8mAPBkb1A61RQvuLCf+ZjDscvDU7b0LCetRXrmdzi4T3vBEjNLayhAzMFDycoEVKm6kCJuK82n3UsxNECQQDixY+6vnRY7w6WyeJuFM1v2QIKyrAWy8PeXtPKex3U2bTecisHHoxu9zNh0c3/Qd7tzOYlV+kj4xxFAbPhPRGJAkALOQ7qu57FABgUOye88jp7XrNRDR7ZQkfJwhjHotR8fwD+rOgBVrbEvYsuUKKRR/vXErLVbmEYSB4CHA1lboshAkA/VCjFX/ah93C9j3eagli0yYeWd/AhDZqPeS/wNd2o2Xt3O67keBWDz6kbwjmi9URKaJRFDX3gNcZpRwZy+ljxAkBkAenkFwFGKPD66EB5jkAZOphKm2O8+gpIfQpHyLvKII0f3hjqXxDjnjz1jIAF1qWPSHpA8bWZidGzSJ2+GUJ/";
	
	// 支付宝的公钥，无需修改该值
	public static String ali_public_key  = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
	                                        
	//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	

	// 调试用，创建TXT日志文件夹路径
	public static String log_path = "/log/ali/";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";
	
	// 签名方式 不需修改
	public static String sign_type = "RSA";

}
