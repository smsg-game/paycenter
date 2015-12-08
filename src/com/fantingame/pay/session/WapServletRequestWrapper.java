/**
 * WapServletRequestWrapper.java
 *
 * Copyright 2007 easou, Inc. All Rights Reserved.
 */
package com.fantingame.pay.session;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easou.common.util.URLTools;

/**
 * HttpServletRequestWrapper扩展，用于对session Map对象的生成 Revision History
 * 2007-11-6,norbys,created it
 */
public class WapServletRequestWrapper extends HttpServletRequestWrapper {
	private static final Log LOG = LogFactory
			.getLog(WapServletRequestWrapper.class);

	private WapSession session;

	private Map<String, String[]> parameterMap = new HashMap<String, String[]>();

	/**
	 * 构造函数
	 * 
	 * @param esid
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	public WapServletRequestWrapper(HttpServletRequest request) {
		super(request);
		String queryString = URLTools.urlDecode(request.getQueryString());
		/*if (queryString != null && !"".equals(queryString.trim())) {
			if (queryString.indexOf("&amp;") < 0)
				parameterMap = request.getParameterMap();
			else {// 解析url中&amp;分隔符参数
				parameterMap = new HashMap<String, String[]>();
				parameterMap.putAll(request.getParameterMap());
				String[] queryArr = queryString.split("&amp;|&");
				for (int i = 0; i < queryArr.length; i++) {
					String[] valueArr = queryArr[i].split("=");
					if (valueArr.length == 2) {
						parameterMap.put(valueArr[0],
								new String[] { valueArr[1] });
					} else if (valueArr.length == 1) {
						parameterMap.put(valueArr[0], new String[] { "" });
					}
					LOG.info("parameterMap value: " + queryArr[i]);
				}
			}
		}
		*/
		
		try {
		    if(null==session){
				session = new WapSession(request.getSession().getId());
		    }
			
		} catch (Exception e) {
			LOG.error("******init wap session is error!\n", e);
			session = null;
		}
	}

	/*public String getParameter(String name) {
		if (parameterMap != null) {
			String values[] = (String[]) parameterMap.get(name);
			return values != null && values.length > 0 ? values[0] : null;
		}
		return null;
	}*/

	/**
	 * 获取默认构造对象
	 * 
	 * @return WapSession
	 */
	public WapSession getWapSession() {
		return session;
	}

	/**
	 * 获取默认构造对象
	 * 
	 * @return WapSession
	 */
	public HttpSession getSession() {
		return session;
	}
}
