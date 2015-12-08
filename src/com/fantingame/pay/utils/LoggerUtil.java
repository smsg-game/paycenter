package com.fantingame.pay.utils;

import org.apache.log4j.Logger;

//用于打印一些比较特别的日志,windows平台，打印到控制台，非window，打印到文件
public class LoggerUtil {
	
	private static boolean logToFile = true;
	
	private static Logger logger = Logger.getLogger("cusAccess");
	
	static{
		if (System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1) {
			logToFile = false;
		}
	}
	
	public static void info(String msg){
		if(logToFile){
			logger.info(msg);
		}else{
			System.err.println(msg);
		}
	}
	
	public static void error(String msg){
		if(logToFile){
			logger.error(msg);
		}else{
			System.err.println(msg);
		}
	}
	
	public static void error(String msg,Exception e){
		if(logToFile){
			logger.error(msg,e);
		}else{
			System.err.println(msg);
		}
	}
}
