package com.fantingame.pay.interceptor;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import com.easou.common.util.StringUtil;
import com.fantingame.pay.utils.LoggerUtil;

//访问日志记录拦截器，记录get和post请求过来的!
public class RequestFilter extends HttpServlet implements Filter {

	
	private static final long serialVersionUID = -2947396490326845497L;
	
	
	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest)request;
		httpRequest.setCharacterEncoding("UTF-8");
		String ua = 	httpRequest.getHeader("User-Agent");
//		if(ua!=null){
//			ua=ua.replaceAll("\\s+", "");
//		}
		StringBuffer sb = new StringBuffer();
		Enumeration params = request.getParameterNames();
		while (params.hasMoreElements()) {
			String paramName = (String) params.nextElement();
			sb.append(paramName).append("=").append(request.getParameter(paramName)).append("&");
		}
		if(sb.toString().length()>0) sb.deleteCharAt(sb.length()-1);
		
		if(!StringUtil.isEmpty(httpRequest.getQueryString())){
			LoggerUtil.error("(GET) ua="+ua+" ~~~~~~~~~  url="+httpRequest.getRequestURI()+"?"+sb.toString());
		}else{
			LoggerUtil.error("(POST) ua="+ua+" ~~~~~~~~~  url="+httpRequest.getRequestURI()+"?"+sb.toString());
		}
		chain.doFilter(request, response);
	}

	
	public void init(FilterConfig config) throws ServletException {}

	
}
