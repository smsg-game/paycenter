package test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.fantingame.pay.utils.HttpUtil;
import com.fantingame.pay.utils.yeepay.YeePayDigestUtil;


public class NotifyToYeePay {
	
	public static HttpClient httpClient = new DefaultHttpClient();
	
	public static void main(String[] args) throws Exception {
	    
		
		String url = "https://localhost/service/notifyFromYeepay.e";
		Map<String,String> formEntity = new LinkedHashMap<String, String>();
		Map<String,String> headerEntity = new TreeMap<String, String>();
		headerEntity.put("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3");

		formEntity.put("r0_Cmd","ChargeCardDirect");
		formEntity.put("r1_Code","-1");
		formEntity.put("p1_MerId","10000352717");
		formEntity.put("p2_Order","2638ea8a4c25438eb7b74f98fa48b30f");
		formEntity.put("p3_Amt","10.0");
		formEntity.put("p4_FrpId","SZX");
		formEntity.put("p5_CardNo","123");
		formEntity.put("p6_confirmAmount","30");
		formEntity.put("p7_realAmount","30");
		formEntity.put("p8_cardStatus","0");
		formEntity.put("p9_MP","");
		formEntity.put("pb_BalanceAmt","");
		formEntity.put("pc_BalanceAct","");
		StringBuffer sb = new StringBuffer();
		for(Map.Entry<String,String> entity : formEntity.entrySet()){
			sb.append(entity.getValue());
		}
		String hmac = YeePayDigestUtil.hmacSign(sb.toString(),"78m3J3hU5046q521t116vdBDYtLxK86qX2cR4471I03wK9zzanh83GofR235");
		formEntity.put("hmac", hmac);
		
		
		HttpPost post = new HttpPost(url);
		HttpClient httpClient = HttpUtil.getThreadSafeHttpClient();
		HttpResponse response = HttpUtil.doPost(httpClient, post, formEntity, "UTF-8");
		
		String content = null;
		if (response.getEntity() != null) {
			String charset = EntityUtils.getContentCharSet(response.getEntity()) == null ? "UTF-8" : EntityUtils.getContentCharSet(response.getEntity());
			content = new String(EntityUtils.toByteArray(response.getEntity()), charset);
		}
		System.out.println(content);
	}
	
	public static HttpResponse doPost(HttpPost httpPost,Map<String,String> formEntity,Map<String,String> headerEntity,String charset){
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
