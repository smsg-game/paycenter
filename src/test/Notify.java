package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.net.ssl.TrustManager;

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


public class Notify {
	
	public HttpClient httpClient = new DefaultHttpClient();
	
	private static TrustManager truseAllManager = new MyTrustManager();
	
	public static void main(String[] args) throws Exception {
	    
		
		String url = "http://service.pay.easou.com/service/notifyFromAlipay.e";
		Notify notify = new Notify();
		
		Map<String,String> formEntity = new TreeMap<String, String>();
		Map<String,String> headerEntity = new TreeMap<String, String>();
		headerEntity.put("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3");

		formEntity.put("notify_data", "<notify><partner>2088202274264810</partner><discount>0.00</discount><payment_type>1</payment_type><subject>元宝 x60</subject><trade_no>2013013109337202</trade_no><buyer_email>334509675@qq.com</buyer_email><gmt_create>2013-01-31 14:09:15</gmt_create><quantity>1</quantity><out_trade_no>79f8562a56bd403d9835933207fb2cee</out_trade_no><seller_id>2088202274264810</seller_id><trade_status>TRADE_FINISHED</trade_status><is_total_fee_adjust>N</is_total_fee_adjust><total_fee>6.00</total_fee><gmt_payment>2013-01-31 14:09:15</gmt_payment><seller_email>taobao@staff.easou.com</seller_email><gmt_close>2013-01-31 14:09:15</gmt_close><price>6.00</price><buyer_id>2088302395447029</buyer_id><use_coupon>N</use_coupon></notify>");
		formEntity.put("sign_type", "RSA");
		formEntity.put("sign", "EIPnhU5p9QJ0q1se/Aq+QojYAtXYdBdPe6N58aHHspjoXYWZCTtGxqRpykYDj+f88nlHAk/ou2oeBK2UPTfCniKRsINKCwPF4Knbo7N720fhM1XAzIhldrTXGV7No+6cUr0aUHycAfPBgpTCIAZqB9UegMNpROg2PD/Peaqw2sc=");
//		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-dao.xml");
//		PayPartnerManager partnerManager = (PayPartnerManager)context.getBean("payPartnerManager");	
//		formEntity.put("notifyDatetime", "2013-01-23 16:16:32");
//		formEntity.put("reqFee", "0.01");
//		formEntity.put("invoice", "2d4187034975499685b67ca6083fa134");
//		formEntity.put("tradeName", "越南妹");
//		formEntity.put("tradeStatus", "TRADE_SUCCESS");
//		formEntity.put("paidFee", "0.1");
//		formEntity.put("appId", "123456");
//		formEntity.put("payerId", "123456");
//		formEntity.put("sign", "P+AnpO7bvGaWmOLFImHQRiGHPAYBW/tynxT/AjDBSuduof99FMFOtd2MIFlT0p2yXnukS+tWbztD8YzH+ezhLoqKk3Lsujy5Ty6F39XYDwYNi9dF+mv5lrqOPup5Eq+D2hz51KxQZxh1lbAfLqFpX+8TgZdvxm+lr72CRFbDtO0=");
//		formEntity.put("tradeId", "1358928938990");
//		SSLContext sslcontext = SSLContext.getInstance("TLS");  
//        sslcontext.init(null, new TrustManager[] {truseAllManager}, null);  
//        SSLSocketFactory sf = new SSLSocketFactory(sslcontext);  
//        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);  
//	    Scheme sch = new Scheme("https", 443, sf);
//	    notify.httpClient.getConnectionManager().getSchemeRegistry().register(sch);
		
		HttpPost httpPost = new HttpPost(url);
		
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
