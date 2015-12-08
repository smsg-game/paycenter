package com.fantingame.pay.utils;

import java.util.Map;
import java.util.TreeMap;

public class RsaSignUtil {
	/**
	 * 将treeMap中的key-value对取出，格式化为 key1=value2&key2=value2的形式 <br>
	 * 抛弃key为null的值对，将value为null的值转化为空字符串<br>
	 * <b>多线程环境中，调用者需保证此方法执行完成前，treeMap不被修改</b>
	 * @param map
	 * @return
	 */
	public static String sign(Map<String, String> treeMap,String privateKey) {
		return Rsa.sign(preSignStr(treeMap), privateKey);
	}
	
	public static boolean doCheck(Map<String, String> treeMap,String sign,String publicKey){
		return Rsa.doCheck(preSignStr(treeMap), sign, publicKey);
	}
	
	public static String sign(String content,String privateKey){
		return Rsa.sign(content, privateKey);
	}
	
	public static boolean doCheck(String content,String sign,String publicKey){
		return Rsa.doCheck(content, sign, publicKey);
	}
	
	public static String preSignStr(Map<String, String> map){
		StringBuilder sb = new StringBuilder();
		TreeMap<String,String> treeMap = new TreeMap<String, String>(map);
		if (treeMap != null) {
			for(Map.Entry<String,String> entity : treeMap.entrySet()){
				if(entity.getKey()!=null && entity.getValue()!=null && !entity.getValue().isEmpty()){
				   if(!"sign".equals(entity.getKey()) && !"extInfo".equals(entity.getKey())) sb.append(entity.getKey()).append("=").append(entity.getValue()).append("&");
				}
			}
			if(sb.length()>0){
				sb.deleteCharAt(sb.length()-1);
			}
		}
		return sb.toString();
	}
	
	
}
