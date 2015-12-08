package com.fantingame.pay.session;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.easou.common.util.PropertiesUtil;

public class MemSessionFilter extends HttpServlet implements Filter {
	private static final long serialVersionUID = -9085992742706078625L;
	private static final Log LOG = LogFactory.getLog(MemSessionFilter.class);
	//public static final String SESSION_ID_NAME = "JSESSIONID";
	public static final String SESSION_PROPERTIES_FILE = "/sessionservice.properties";
	public static final String SESSION_TIME_OUT_NAME = "sessionTimeout";
	public static int sessionTimeout = 7200000; // 单位为毫秒

	public void init(FilterConfig filterConfig) throws ServletException {
		InputStream in = MemSessionFilter.class.getResourceAsStream(SESSION_PROPERTIES_FILE);
		Properties props = new Properties();
		try {
			props.load(in);
			//获取session超时时间，默认是sessionTimeout的值
			sessionTimeout = PropertiesUtil.getIntProperty(props,SESSION_TIME_OUT_NAME, sessionTimeout);
		} catch (Exception ex) {
			LOG.error("load property file fail:" + ex.getMessage());
		}
	}

	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		//把一个请求的普通HttpServletRequest包装成一个HttpServletRequestWrapper（含有wapSession）
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		WapServletRequestWrapper wapRequest = new WapServletRequestWrapper(request);
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		//把HttpServletRequestWrapper传递到下一层
		chain.doFilter(wapRequest, response);
		//保存session属性到memcache
		wapRequest.getWapSession().saveSession();
	}

}
