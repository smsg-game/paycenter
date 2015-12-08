package test;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.easou.common.api.Md5SignUtil;
import com.fantingame.pay.utils.HttpUtil;

public class Test9 {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		

        Map<String,String> map = new HashMap<String, String>();
        map.put("appId","1394");
        map.put("notifyUrl","http://dmxys.sinaapp.com/page/charge/charge_reback_easou.php");
        map.put("partnerId","1000100010001016");
        map.put("payerId","2150");
        map.put("qn","1000");
        map.put("redirectUrl","http://dmxys.sinaapp.com/enter_from_easou.php");
        map.put("reqFee","10.00");
        map.put("separable","false");
        map.put("tradeId","136271003500");
        map.put("tradeDesc","charge100gold");
        map.put("tradeName","100gold");
        
        System.out.println(Md5SignUtil.getStringForSign(map));
        
        map.put("sign",Md5SignUtil.sign(map,"A58BA3A5BB4E464EAEA2D4134F0364D8"));
        
        
        /*
        //map.put("sign", value)
        //Map<String,String> map2 = new HashMap<String, String>();
        
       // HttpPost post = new HttpPost("http://ddz.game.easou.com:8089/store/notify");
        HttpPost post = new HttpPost("http://42.121.58.231:8005/easou/purchase/callback/?t=1362191046");
        HttpClient client = new DefaultHttpClient();
        String content = HttpUtil.getPostContent(client, post, map,"UTF-8");
        System.out.println("content:"+content);
        */
        
        
        
        
	}

}
