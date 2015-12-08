package com.fantingame.pay.utils;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class HttpUtil {
    private static Logger logger = Logger.getLogger(HttpUtil.class);
	private static int MAX_PER_ROUTE = 10;
	private static int MAX_TOTAL     = 100;
	private static int SO_TIMEOUT    = 10000;
    private static int CONNECTION_TIMETOU = 10000;
    
    
    public static HttpClient getThreadSafeHttpClient(){
    	return getThreadSafeHttpClient(MAX_PER_ROUTE,MAX_TOTAL,SO_TIMEOUT,CONNECTION_TIMETOU);
    }
    
	public static HttpClient getThreadSafeHttpClient(int maxPerRoute,int maxTotal,int soTimeout,int connectionTimeout){
		//初始化一个httpClient
		DefaultHttpClient httpClient = null;
		try{
			PoolingClientConnectionManager cm = new PoolingClientConnectionManager();
			cm.setDefaultMaxPerRoute(maxPerRoute);
			cm.setMaxTotal(maxTotal);
			httpClient = new DefaultHttpClient(cm);
			httpClient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT,soTimeout);
			httpClient.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT,connectionTimeout);
			httpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(3,true));
			HttpClientParams.setCookiePolicy(httpClient.getParams(),CookiePolicy.BROWSER_COMPATIBILITY);
			
			//https不加载证书
			SSLContext sslcontext = SSLContext.getInstance("TLS");  
	        sslcontext.init(null, new TrustManager[] {new X509TrustManager(){
				@Override
				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
					// TODO Auto-generated method stub
					
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					// TODO Auto-generated method stub
					return null;
				}
	        }}, null);  
	        SSLSocketFactory sf = new SSLSocketFactory(sslcontext);  
	        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);  
		    Scheme sch = new Scheme("https", 443, sf);
		    Scheme sch2 = new Scheme("http", 80,  PlainSocketFactory.getSocketFactory());
		    httpClient.getConnectionManager().getSchemeRegistry().register(sch);
		    httpClient.getConnectionManager().getSchemeRegistry().register(sch2);
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
	    return httpClient;
	}
	
	
	public static HttpResponse doPost(HttpClient httpClient,HttpPost httpPost,Map<String,String> formEntity,String charset){
		try{
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			for(Map.Entry<String,String> entity : formEntity.entrySet()){//account and password
				nvps.add(new BasicNameValuePair(entity.getKey(), entity.getValue()));
			}
			//添加头部信息
		    httpPost.setHeader("User-Agent","easouPayNotify");
	
		    HttpClientParams.setCookiePolicy(httpClient.getParams(),CookiePolicy.BROWSER_COMPATIBILITY);
		    httpPost.setEntity(new UrlEncodedFormEntity(nvps,charset));
            HttpResponse response = httpClient.execute(httpPost);
            return response;
		}catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
		
	}
	
	public static HttpResponse doPost(HttpClient httpClient,HttpPost httpPost,String data){
		try{
			//添加头部信息
		    httpPost.setHeader("User-Agent","easouPayNotify");
	
		    HttpClientParams.setCookiePolicy(httpClient.getParams(),CookiePolicy.BROWSER_COMPATIBILITY);
		    httpPost.setEntity(new StringEntity(data));
            HttpResponse response = httpClient.execute(httpPost);
            return response;
		}catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
		
	}
	
	
	public static String tryToGetPostContent(HttpClient httpClient,HttpPost httpPost,Map<String,String> formEntity,String charset){
		String content = getPostContent(httpClient, httpPost, formEntity, charset);
		if(content == null){
			content = getPostContent(httpClient, httpPost, formEntity, charset);
		}
		if(content == null){
			content = getPostContent(httpClient, httpPost, formEntity, charset);
		}
		return content;
	}
	

	public static String getPostContent(HttpClient httpClient,HttpPost httpPost,Map<String,String> formEntity,String charset){
		try{
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			for(Map.Entry<String,String> entity : formEntity.entrySet()){//account and password
				nvps.add(new BasicNameValuePair(entity.getKey(), entity.getValue()));
			}
			//添加头部信息
		    httpPost.setHeader("User-Agent","easouPayNotify");
		    
		    HttpClientParams.setCookiePolicy(httpClient.getParams(),CookiePolicy.BROWSER_COMPATIBILITY);
		    httpPost.setEntity(new UrlEncodedFormEntity(nvps,charset));
            HttpResponse response = httpClient.execute(httpPost);
            String content = null;
            if (response.getEntity() != null) {
    			charset = EntityUtils.getContentCharSet(response.getEntity()) == null ? charset : EntityUtils.getContentCharSet(response.getEntity());
    			content = new String(EntityUtils.toByteArray(response.getEntity()), charset);
    		}
            httpPost.abort();
            return content;
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
		
	}
	
	
}
