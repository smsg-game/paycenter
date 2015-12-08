package com.fantingame.pay.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPInputStream;

public class UrlConnection {
    private String userAgentName  = "User-Agent";
    private String userAgentValue = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3";
    private HttpURLConnection httpurlconn = null;
    private StringBuffer resultBuffer = new StringBuffer();
    private int httpResutl = 0;
    private String charset = "ISO-8859-1";
    private String threadUrl= null;
    private String redirectUrl = null;
    private String content = null;
    private String cookie  = "";
    private String requestMethod = "GET";
    
	public UrlConnection(){
			
	}
	
    public UrlConnection(String charset){
		this.charset = charset;
	}
    
    public UrlConnection(String charset,String cookie){
		this.charset = charset;
		this.cookie  = cookie;
	}
    
    public UrlConnection(String charset,String cookie,String requestMethod){
		this.charset = charset;
		this.cookie  = cookie;
		this.requestMethod = requestMethod;
	}
    
    public void setCharset(String charset){
    	this.charset = charset;
    }
    
	public int setUrl(String url){
		//200=OK
		try {
			this.threadUrl = url;
			this.httpurlconn = (HttpURLConnection)new URL(url).openConnection();
			this.httpurlconn.setConnectTimeout(30000);
			this.httpurlconn.setReadTimeout(30000);
			this.httpurlconn.addRequestProperty(this.userAgentName, this.userAgentValue);
			this.httpurlconn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			this.httpurlconn.setRequestProperty("Cookie",this.cookie);
			this.httpResutl = this.httpurlconn.getResponseCode();
			this.content = "";
			this.redirectUrl = this.httpurlconn.getURL().toString();
			return httpResutl;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
		
	}
	
	/*for post*/
	public int setUrl(String url,String postData){
		//200=OK
		try {
			this.threadUrl = url;
			this.httpurlconn = (HttpURLConnection)new URL(url).openConnection();
			this.httpurlconn.setConnectTimeout(30000);
			this.httpurlconn.setReadTimeout(30000);
			this.httpurlconn.addRequestProperty(this.userAgentName, this.userAgentValue);
			this.httpurlconn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			this.httpurlconn.setRequestProperty("Cookie",this.cookie);
			this.httpurlconn.setDoOutput(true);   
			this.httpurlconn.setDoInput(true);   
			this.httpurlconn.setRequestMethod("POST");   
			this.httpurlconn.setUseCaches(false);   
			this.httpurlconn.setInstanceFollowRedirects(true);
			this.httpurlconn.getOutputStream().write(postData.getBytes());
			this.httpurlconn.getOutputStream().flush();
			this.httpurlconn.getOutputStream().close();
			this.httpResutl = this.httpurlconn.getResponseCode();
			this.content = "";
			this.threadUrl = this.httpurlconn.getURL().toString();
			return httpResutl;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
		
	}
	
	public String getUrl(){
		return this.threadUrl;
	}
	
	public String getRedirectURL(){
		return this.httpurlconn.getURL().toString();
	}
	
	public int getLength(){
		if(this.httpurlconn.getContentLength()==-1){
			this.content = getContent();
			return this.content.length();
		}else{
			return this.httpurlconn.getContentLength();
		}
	}
	
	
	public InputStream getInputStream() throws Exception{
		return this.httpurlconn.getInputStream();
	}
	
	public String getContent(){
		if(this.content!=null && !"".equals(this.content)){
			return this.content;
		}
		try{
		   if (httpResutl == HttpURLConnection.HTTP_OK) {
			  resultBuffer.delete(0, resultBuffer.length());
			  BufferedReader in = null;
			  if(this.httpurlconn.getContentEncoding()!=null && this.httpurlconn.getContentEncoding().contains("gzip")){
				  in = new BufferedReader(new InputStreamReader(new GZIPInputStream(this.httpurlconn.getInputStream()),this.charset)); 
			  }else{
				  in = new BufferedReader(new InputStreamReader(this.httpurlconn.getInputStream(),this.charset)); 
			  }
		      String inputLine = null; 
		      while ((inputLine = in.readLine()) != null) { 
		    	  resultBuffer.append(inputLine+"\n");
		      }
		      this.content = resultBuffer.toString();
		      in.close();
		    } 
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return this.content;
	}
	
}
