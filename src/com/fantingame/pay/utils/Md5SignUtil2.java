package com.fantingame.pay.utils;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 签名
 * 
 * @author damon
 * @since 2013.01.24
 * @version 1.0
 *
 */
public class Md5SignUtil2 {
	
	public static String digest(String src){
		  try {
			     byte[] btInput = src.getBytes("UTF-8");
			     MessageDigest mdInst = MessageDigest.getInstance("MD5");
			     mdInst.update(btInput);
			     byte[] md = mdInst.digest();
			     StringBuffer sb = new StringBuffer();
			     for (int i = 0; i < md.length; i++) {
			       int val = ((int) md[i]) & 0xff;
			       if (val < 16) sb.append("0");
			       sb.append(Integer.toHexString(val));
			     }
			     return sb.toString();
			}catch (Exception e) {
			     e.printStackTrace();
		    }
			return null;
	  }

	/*签名*/
	public static String sign(Map<String, String> map,String key) {
		String content = getStringForSign(map);
	    return sign(content, key);
	}
	

	/*签名*/
	public static String sign(String content,String key) {
			String veriCode = digest(content+key);
			return veriCode;
	}
	
	
	/*验签不区分大小写*/
	public static boolean doCheck(Map<String,String> map,String sign,String key){
		String content = getStringForSign(map);
		return doCheck(content, sign, key);
	}
	
	/*验签不区分大小写*/
	public static boolean doCheck(String content,String sign,String key){
			String veriCode = digest(content+key);
			if(veriCode.equalsIgnoreCase(sign)) 
				return true;
		return false;
	}
	
	/*获取待签名字符串*/
	public static String getStringForSign(Map<String,String> map){
		StringBuilder sb = new StringBuilder();
		TreeMap<String, String> treeMap = new TreeMap<String, String>(map);
		if (treeMap != null) {
			for(Map.Entry<String,String> entity : treeMap.entrySet()){
				if(entity.getKey()!=null && entity.getValue()!=null){
					sb.append(entity.getKey()).append("=").append(String.valueOf(entity.getValue())).append("&");
				}
			}
		}
		if(sb.length()>0){
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("EASOUTGC", "TGT-1-GNPsm5EQZfvnT7twtM54Of4YzxzpKICrSH6enMnRu1H1YasmSi-sso");
		map.put("U", "7b92bf99c66d37c49065acaf760d975eddc8bad3766c8e0c15545de3e6bfca74480218b1a0cb4bdcc38e2c1b97c61018");
		String content=getStringForSign(map);
		String key="";
		System.out.println(digest(content+key));
	}
	
}
