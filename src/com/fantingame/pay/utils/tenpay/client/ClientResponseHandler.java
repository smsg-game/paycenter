package com.fantingame.pay.utils.tenpay.client;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import java.io.IOException;
import org.jdom.JDOMException;

import com.fantingame.pay.utils.tenpay.util.MD5Util;
import com.fantingame.pay.utils.tenpay.util.XMLUtil;
public class ClientResponseHandler {
	
	private String content;
	
	private SortedMap parameters; 
	
	private String debugInfo;
	
	private String key;
	
	private String charset;
	
	public ClientResponseHandler() {
		this.content = "";
		this.parameters = new TreeMap();
		this.debugInfo = "";
		this.key = "";
		this.charset = "GBK";
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) throws Exception {
		this.content = content;
		
		this.doParse();
	}
	
	public String getParameter(String parameter) {
		String s = (String)this.parameters.get(parameter); 
		return (null == s) ? "" : s;
	}
	public void setParameter(String parameter, String parameterValue) {
		String v = "";
		if(null != parameterValue) {
			v = parameterValue.trim();
		}
		this.parameters.put(parameter, v);
	}
	
	public SortedMap getAllParameters() {
		return this.parameters;
	}	

	public String getDebugInfo() {
		return debugInfo;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	public String getCharset() {
		return this.charset;
	}
	
	public void setCharset(String charset) {
		this.charset = charset;
	}	
	
	public boolean isTenpaySign() {
		StringBuffer sb = new StringBuffer();
		Set es = this.parameters.entrySet();
		Iterator it = es.iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();
			String k = (String)entry.getKey();
			String v = (String)entry.getValue();
			if(!"sign".equals(k) && null != v && !"".equals(v)) {
				sb.append(k + "=" + v + "&");
			}
		}
		
		sb.append("key=" + this.getKey());
		
		String sign = MD5Util.MD5Encode(sb.toString(), this.charset).toLowerCase();
		
		String tenpaySign = this.getParameter("sign").toLowerCase();
		
		this.setDebugInfo(sb.toString() + " => sign:" + sign +
				" tenpaySign:" + tenpaySign);
		
		return tenpaySign.equals(sign);
	}
	
	protected boolean isTenpaySign(String signParameterArray[]) {

		StringBuffer signPars = new StringBuffer();
		for(int index = 0; index < signParameterArray.length; index++) {
			String k = signParameterArray[index];
			String v = this.getParameter(k);
			if(null != v && !"".equals(v)) {
				signPars.append(k + "=" + v + "&");
			}
		}
		
		signPars.append("key=" + this.getKey());
				
		String sign = MD5Util.MD5Encode(
				signPars.toString(), this.charset).toLowerCase();
		
		String tenpaySign = this.getParameter("sign").toLowerCase();
		
		this.setDebugInfo(signPars.toString() + " => sign:" + sign +
				" tenpaySign:" + tenpaySign);
		
		return tenpaySign.equals(sign);
	}
	

	protected void setDebugInfo(String debugInfo) {
		this.debugInfo = debugInfo;
	}
	
	protected void doParse() throws JDOMException, IOException {
		String xmlContent = this.getContent();
		
		Map m = XMLUtil.doXMLParse(xmlContent);
		
		Iterator it = m.keySet().iterator();
		while(it.hasNext()) {
			String k = (String) it.next();
			String v = (String) m.get(k);
			this.setParameter(k, v);
		}
		
	}
	

}
