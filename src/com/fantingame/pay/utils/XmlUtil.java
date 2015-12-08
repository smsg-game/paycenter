package com.fantingame.pay.utils;

import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
/**
 * xml工具类
 * @author elvis
 *
 */
public class XmlUtil {
	private String xml=null;
	public XmlUtil(String xml){
		this.xml=xml;
	}
//	public XmlUtil(File xmlFile){
//		
//	}
	
	public String getSimpleXmlValue(String attrName){
		String attrH="<"+attrName+">";
		String attrE="</"+attrName+">";
		if(xml!=null&&xml.split(attrH).length>=2){
			String temp1=xml.split(attrH)[1];
			String value=temp1.split(attrE)[0];
			return value;
		}
		return null;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String xml="<head>this is test </head>";
		XmlUtil util=new XmlUtil(xml);
		System.out.println(util.getSimpleXmlValue("head"));
		
	}

}
