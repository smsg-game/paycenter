package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


public class OrderFrom {
	private HttpClient httpClient = new DefaultHttpClient();
	
	public static void main(String[] args) throws Exception {
	    
		OrderFrom notify = new OrderFrom();
		
		Map<String,String> formEntity = new TreeMap<String, String>();
		Map<String,String> headerEntity = new TreeMap<String, String>();
		headerEntity.put("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3");

		formEntity.put("partnerId","1000100010001001"); 
		formEntity.put("tradeId","tradeId"); 
		formEntity.put("fee","fee"); 
		formEntity.put("notifyUrl","notifyUrl");
		formEntity.put("tradeName","tradeName"); 
		formEntity.put("tradeDescription","tradeDescription"); 
		
		HttpPost httpPost = new HttpPost("http://localhost/json/orderFrom.e");
		
		HttpResponse response = notify.doPost(httpPost, formEntity, headerEntity,"UTF-8");
		
		String content = EntityUtils.toString(response.getEntity(),"UTF-8");
		System.out.println(content);
	}
	
	
	public String getContent(String url) throws Exception{
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = this.httpClient.execute(httpGet);
		String content = EntityUtils.toString(response.getEntity(),"UTF-8");
		httpGet.abort();
		return content;
	}
	
	
	public HttpResponse doPost(HttpPost httpPost,Map<String,String> formEntity,Map<String,String> headerEntity,String charset){
		try{
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			for(Map.Entry<String,String> entity : formEntity.entrySet()){//account and password
				nvps.add(new BasicNameValuePair(entity.getKey(), entity.getValue()));
			}
			if(headerEntity!=null){//不为空就添加到header里面，以键值对的形式
		      for(Map.Entry<String,String> entity : headerEntity.entrySet()){//header参数
		    	  httpPost.setHeader(entity.getKey(), entity.getValue());
		      }
			}
		    HttpClientParams.setCookiePolicy(httpClient.getParams(),CookiePolicy.BROWSER_COMPATIBILITY);
		    httpPost.setEntity(new UrlEncodedFormEntity(nvps,charset));
            HttpResponse response = httpClient.execute(httpPost);
            return response;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
		
	}
	 
	
}
