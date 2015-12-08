package com.fantingame.pay.servlet;

import java.net.InetAddress;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fantingame.pay.job.ThreadWorker;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.HttpUtil;

public class InitServlet extends HttpServlet{
	private static final long serialVersionUID = -8615238153261272900L;
	private Logger logger = Logger.getLogger(InitServlet.class);
	public void init() throws ServletException {
		//获取spring的context
		Constants.CTX=WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		Constants.HTTPCLIENT = HttpUtil.getThreadSafeHttpClient();
		logger.info("======================================================支付线程启动了======================================================");
		try{
			//初始化工作线程
			for(int i=0;i<Constants.THREADPOOL_CORE_SIZE;i++){
				Constants.THREAD_EXECUTOR.execute(new ThreadWorker(Constants.DQ,HttpUtil.getThreadSafeHttpClient(Constants.THREADPOOL_CORE_SIZE,200,10000,10000)));
			}
			//加载上次未完成的通知
		    
			
			//获取本机IP，以判断是否测试环境
			InetAddress addr = InetAddress.getLocalHost();
			String ip=addr.getHostAddress().toString();//获得本机IP
			logger.info("local ip address:"+ip);
			if(ip!=null && (ip.endsWith(".40") || ip.endsWith(".41"))){
				 Constants.isTest = false;
			}else{
				Constants.isTest = true;
			}
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}	
	}

}
