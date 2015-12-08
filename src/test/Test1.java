package test;

import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.fantingame.pay.utils.HttpUtil;

public class Test1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		String charset = "UTF-8";
		String content = "";
		HttpClient client = new DefaultHttpClient();
		
		//登录
		HttpPost post = new HttpPost("http://g.xxsy.net/login.asp");
	    HttpClientParams.setCookiePolicy(client.getParams(),CookiePolicy.BROWSER_COMPATIBILITY);
	    HashMap<String,String> loginForm = new HashMap<String, String>();
	    loginForm.put("username", "13760712439");
	    loginForm.put("password", "123456");
	    loginForm.put("act", "1");
	    loginForm.put("lailu","");
	    post.setHeader("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3");
	    HttpResponse response = HttpUtil.doPost(client, post, loginForm, "UTF-8");
	    if (response.getEntity() != null) {
			charset = EntityUtils.getContentCharSet(response.getEntity()) == null ? charset : EntityUtils.getContentCharSet(response.getEntity());
			content = new String(EntityUtils.toByteArray(response.getEntity()), charset);
		}
	    post.abort();
	    //System.out.println(content);
	    

	    HttpGet get = new HttpGet("http://pay.xxsy.net/channel/UmpayForMobile.aspx?mobile=18719455204&amount=200&channel=28&uid=5348876");
	    get.setHeader("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3");
	    HttpResponse response2 = client.execute(get);
	    if (response2.getEntity() != null) {
			charset = EntityUtils.getContentCharSet(response2.getEntity()) == null ? charset : EntityUtils.getContentCharSet(response2.getEntity());
			content = new String(EntityUtils.toByteArray(response2.getEntity()), charset);
		}
	    System.out.println(content);
	    System.out.println("---------------------------------------------------------------");
	    get.abort();
	    HttpGet get2 = new HttpGet("http://payment.umpay.com/hfwebbusi/pay/wapSaveOrder.do?bankId=MW791000");
	    get2.setHeader("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3");
	    HttpResponse response23 = client.execute(get2);
	    if (response23.getEntity() != null) {
			charset = EntityUtils.getContentCharSet(response23.getEntity()) == null ? charset : EntityUtils.getContentCharSet(response23.getEntity());
			content = new String(EntityUtils.toByteArray(response23.getEntity()), charset);
		}
	    System.out.println(content);
	    System.out.println("---------------------------------------------------------------");
	    get2.abort();
	    HttpGet get3 = new HttpGet("http://payment.umpay.com/hfwebbusi/pay/wapSms.do");
	    get3.setHeader("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3");
	    HttpResponse response4 = client.execute(get3);
	    if (response4.getEntity() != null) {
			charset = EntityUtils.getContentCharSet(response4.getEntity()) == null ? charset : EntityUtils.getContentCharSet(response4.getEntity());
			content = new String(EntityUtils.toByteArray(response4.getEntity()), charset);
		}
	    System.out.println(content);
	    get3.abort();
	    /*支付
	    HashMap<String,String> payForm = new HashMap<String, String>();
	    payForm.put("amount","200");
	    payForm.put("mobileId","13767890764");
	    payForm.put("act","1");
	    HttpPost post2 = new HttpPost("http://g.xxsy.net/pay/sjdx/sj_pay.asp");
	    post2.setHeader("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3");
	    HttpResponse response2 = HttpUtil.doPost(client, post2, payForm, "UTF-8");
	    */
	    /*
	    BufferedReader in = null;
	    if(response2.getEntity().getContentEncoding()!=null && response2.getEntity().getContentEncoding().getValue().contains("gzip")){
			  in = new BufferedReader(new InputStreamReader(new GZIPInputStream(response2.getEntity().getContent()),"GBK")); 
		  }else{
			  in = new BufferedReader(new InputStreamReader(response2.getEntity().getContent(),"GBK")); 
		  }
	    
	    String inputLine = null; 
	    while ((inputLine = in.readLine()) != null) { 
	    	content += inputLine +"\n";
	    }
	    post2.abort();
	    */
	    /*
	    UrlConnection conn = new UrlConnection();
	    conn.setCharset("UTF-8");
	    conn.setUrl("http://pay.xxsy.net/channel/UmpayForMobile.aspx?mobile=13622392013&amount=200&channel=28&uid=5348876");
	    System.out.println(conn.getContent());
	    */
	    /*
	    System.out.println(httpClient2.getCookieStore().getCookies());
	    if(httpClient2.getCookieStore().getCookies().isEmpty()){
        	logger.error("login fail..........siteKey:"+siteId);
        	return false;
        }
        */
	}

}
