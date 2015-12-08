package com.fantingame.pay.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;


public class RequestUtils {

	public static <T> T convertRequestDataToBean(String reqData, Class<T> valueType) throws Exception {
		Map<String, String> map = parseRequestString(reqData);
		
		T t = valueType.newInstance();
		BeanUtils.populate(t, map);
		
		return t;
		
		
	}
	
	public static String getRequestData(HttpServletRequest req) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(req.getInputStream(), "utf-8"));
		String ln = null;
		StringBuffer stringBuffer = new StringBuffer();
		while ((ln = in.readLine()) != null) {
			stringBuffer.append(ln);
			stringBuffer.append("\r\n");
		}

		return stringBuffer.toString().replaceAll("\r\n", "");
	}

	/**
	 * 解析以“&”分隔的请求字符串，例如：
	 * 
	 * <p>
	 * orderNo=791000012PP016140210105937000001&userId=18178
	 * &gameOrderNo=NONE&product=com.winggod.jingzhuan&extend=NONE
	 * &time=1392004960&sign=ad08d9e2d7b7df6603bc431392d1c707
	 * </p>
	 * 
	 * @return
	 */
	public static Map<String, String> parseRequestString(String str) {
		Map<String, String> map = new HashMap<String, String>();
		int len = str.length();
		StringBuilder temp = new StringBuilder();
		char curChar;
		String key = null;
		boolean isKey = true;
		for (int i = 0; i < len; i++) {// 遍历整个带解析的字符串
			curChar = str.charAt(i);// 取当前字符
			if (curChar == '&') {// 如果读取到&分割符
				putKeyValueToMap(temp, isKey, key, map);
				temp.setLength(0);
				isKey = true;
			} else {
				if (isKey) {// 如果当前生成的是key
					if (curChar == '=') {// 如果读取到=分隔符
						key = temp.toString();
						temp.setLength(0);
						isKey = false;
					} else {
						temp.append(curChar);
					}
				} else {// 如果当前生成的是value
					temp.append(curChar);
				}
			}
		}
		putKeyValueToMap(temp, isKey, key, map);
		return map;
	}

	private static void putKeyValueToMap(StringBuilder temp, boolean isKey, String key, Map<String, String> map) {
		if (isKey) {
			key = temp.toString();
			if (key.length() == 0) {
				throw new RuntimeException("QString format illegal");
			}
			map.put(key, "");
		} else {
			if (key.length() == 0) {
				throw new RuntimeException("QString format illegal");
			}
			map.put(key, temp.toString());
		}
	}
	
	public static void main(String[] args) throws Exception {
		
		String str = "applicationCode=Zzelp17qlgEfBOJbXXgBLxGoyIxSW5n1&referenceId=d1399f3e95644534809c1bee2093a884&paymentId=MPO5542387&version=v1&amount=3000&currencyCode=TWD&paymentStatusCode=00&paymentStatusDate=2015-01-30T08:37:23Z&channelId=1&customerId=24089774&signature=b6a3bab9522d50b5cda25bc536f17766";
		
//		MOLPayment molPayment = RequestUtils.convertRequestDataToBean(str, MOLPayment.class);

//		System.out.println(molPayment);
	}

}
