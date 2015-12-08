package test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.http.client.methods.HttpPost;

import com.easou.common.api.Md5SignUtil;
import com.fantingame.pay.utils.HttpUtil;


public class Test6 {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		
		System.out.println(MD5.digest(UUID.randomUUID().toString()).toUpperCase());
		
		
		Map<String,String> params = new HashMap<String, String>();
		params.put("userId","userId=22041509");
		params.put("tradeId","dbc3eb92-3eb2-4d75-be33-294596d73479");
		params.put("reqFee","0.09");
		params.put("sign",Md5SignUtil.sign(params,"8C97398507CCD24342C835D7AB09077D"));

		//HttpPost post = new HttpPost("http://localhost.easou.com/json/tradeByEbForBookMall.e");
		
		//String content = HttpUtil.getPostContent(HttpUtil.getThreadSafeHttpClient(), post, params, "UTF-8");
		
		//System.out.println(content);
	}

}
