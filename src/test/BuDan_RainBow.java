package test;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.easou.common.api.Md5SignUtil;
import com.fantingame.pay.entity.PayPartner;
import com.fantingame.pay.job.DelayItem;
import com.fantingame.pay.manager.PayPartnerManager;
import com.fantingame.pay.utils.Constants;
import com.fantingame.pay.utils.HttpUtil;
import com.fantingame.pay.utils.RsaSignUtil;
import com.fantingame.pay.utils.StringTools;





public class BuDan_RainBow {
		
	public static void main(String[] args) throws Exception{
	    
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-dao.xml");
		PayPartnerManager partnerManager = (PayPartnerManager)context.getBean("payPartnerManager");
		PayPartner payPartner  = partnerManager.getEntityById(1000100010001002l);
		
		Map<String,String> map = getFullUrl(payPartner);
		
		
		HttpPost httpPost = new HttpPost("http://api.gangker.com/paycall/easoucall");
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = HttpUtil.doPost(client,httpPost, map,"UTF-8");
		String content = EntityUtils.toString(response.getEntity(),"UTF-8");
		httpPost.abort();
		System.out.println(content);
	}
	
	
	
	/*返回一个含有sign的完整url,不是必要的参数如果为空则不参与签名，不参与签名*/
	private static Map<String,String> getFullUrl(PayPartner payPartner) throws Exception{
		Map<String,String> map = new HashMap<String, String>();
		map.put("appId", "1211060013");
		map.put("paidFee","6");
		map.put("invoice","66bad01650764981a4fa1347e7e53600");
		map.put("payerId", "100020");
		map.put("reqFee", "6");
		map.put("tradeId", "2013013100002758");
		map.put("tradeStatus","TRADE_SUCCESS");
		map.put("tradeName", "元宝 x60");
		map.put("notifyDatetime", "2013-01-31 14:50:41");

		//签名
		String sign = "";
		sign = Md5SignUtil.sign(map, payPartner.getSecretKey());
		StringTools.setValue(map,"sign",sign,172,true,false);
		return map;
	}
	
	
	
		

}
