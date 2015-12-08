package test.postdemo;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


public class PostDemo {
	private static HttpClient httpClient = new DefaultHttpClient();
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		Configuation config = null;
		try{	
			
			if(args!=null && args.length>0){
			    config = new Configuation(args[0]);
			}else{
				config = new Configuation("post_data.properties");
			}
			
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
	        	
	         }
	        }, null);  
	        SSLSocketFactory sf = new SSLSocketFactory(sslcontext);  
	        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);  
		    Scheme sch = new Scheme("https", 443, sf);
		    httpClient.getConnectionManager().getSchemeRegistry().register(sch);
			
		
			//发送Notify
			HttpPost httpPost = new HttpPost(config.getStringValue("notifyUrl"));
			Map<String,String> formEntity = new TreeMap<String, String>();
			formEntity.put("appId", config.getStringValue("appId"));
			formEntity.put("paidFee",config.getStringValue("paidFee"));
			formEntity.put("invoice", config.getStringValue("invoice"));
			formEntity.put("payerId",config.getStringValue("payerId"));
			formEntity.put("reqFee",config.getStringValue("reqFee"));
			formEntity.put("tradeId", config.getStringValue("tradeId"));
			formEntity.put("tradeStatus", config.getStringValue("tradeStatus"));
			formEntity.put("tradeName", config.getStringValue("tradeName","UTF-8"));
			formEntity.put("sign", config.getStringValue("sign"));
			
			
			
			
			HttpResponse response = doPost(httpPost, formEntity,"UTF-8");
			String content = EntityUtils.toString(response.getEntity(),"UTF-8");
			System.out.println("response:"+content);
			httpPost.abort();
	    }catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	public static HttpResponse doPost(HttpPost httpPost,Map<String,String> formEntity,String charset){
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
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	

}
