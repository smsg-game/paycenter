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
import org.apache.struts2.json.JSONUtil;

import com.fantingame.pay.entity.TradeBean;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.HttpUtil;


public class Order {
	
	public HttpClient httpClient = new DefaultHttpClient();
	
	public static void main(String[] args) throws Exception {
	    
		//String url = "https://service.pay.easou.com/json/trade.e";
		String url = "https://testservice.pay.easou.com/json/order.e";
		//String url = "https://localhost/json/order.e";
		Order notify = new Order();
		
		Map<String,String> formEntity = new TreeMap<String, String>();
		Map<String,String> headerEntity = new TreeMap<String, String>();
		headerEntity.put("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3");

		//ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-dao.xml");
		//PayPartnerManager partnerManager = (PayPartnerManager)context.getBean("payPartnerManager");
		
		TradeBean trade = new TradeBean();
		trade.setInvoice("6980cc1ab66543fd83530039328593cd");
		trade.setChannelId(Constants.CHANNEL_ID_YEEPAY_GAME_CARD+"");
		
		formEntity.put("invoice", trade.getInvoice());
		formEntity.put("channelId", trade.getChannelId());
		
		HttpPost post = new HttpPost(url);
		HttpClient httpClient = HttpUtil.getThreadSafeHttpClient();
		HttpResponse response = HttpUtil.doPost(httpClient, post, formEntity, "UTF-8");
		
		String content = null;
		if (response.getEntity() != null) {
			String charset = EntityUtils.getContentCharSet(response.getEntity()) == null ? "UTF-8" : EntityUtils.getContentCharSet(response.getEntity());
			content = new String(EntityUtils.toByteArray(response.getEntity()), charset);
		}
		System.out.println(content);
		
		//访问mo9
		Map<String,Map<String,String>> rootMap = (Map<String,Map<String,String>>)JSONUtil.deserialize(content);
		Map<String,String> map = rootMap.get("data");
		//mo9地址
		//String fullUrl = "https://sandbox.mo9.com.cn/gateway/mobile.shtml?m=mobile&"+map.get("url");
		System.out.println(map.get("url"));
		//HttpGet httpGet = new HttpGet(fullUrl);
		//HttpResponse r2 = notify.httpClient.execute(httpGet);
		//String result = EntityUtils.toString(r2.getEntity(),"UTF-8");
		//System.out.println(result);
		
		
		/*
		Map<String,String> formEntity2 = new TreeMap<String, String>();
		Map<String,String> headerEntity2 = new TreeMap<String, String>();
		headerEntity2.put("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3");
		String fullUrl = "https://sandbox.mo9.com.cn/gateway/mobile.shtml";
		formEntity2.put("m","mobile");
		formEntity2.put("sign","6D3159589999E8C249CE1E13E8EA3DD8");
		formEntity2.put("amount","1.00");
		formEntity2.put("payer_id","123");
		formEntity2.put("item_name","100元宝");
		formEntity2.put("pay_to_email","mo9_billing@staff.easou.com");
		formEntity2.put("invoice","881d36e647c64b63a99ab6b654451b51");
		formEntity2.put("notify_url","http://120.197.137.6:8080/service/notifyFromMO9.e");
		formEntity2.put("app_id","1253");
		formEntity2.put("lc","CN");
		formEntity2.put("currency","CNY");
		formEntity2.put("extra_param","1000100010001001");
		formEntity2.put("version","2.1");
        HttpPost httpPost2 = new HttpPost(fullUrl);
		HttpResponse response2 = notify.doPost(httpPost2, formEntity2, headerEntity2,"UTF-8");
		String content2 = EntityUtils.toString(response2.getEntity(),"UTF-8");
		System.out.println(content2);
		*/
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
