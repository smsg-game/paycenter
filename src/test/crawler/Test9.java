package test.crawler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.htmlparser.Parser;
import org.htmlparser.filters.LinkRegexFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

import com.fantingame.pay.utils.UrlConnection;


/**
 * 爬取ZOL的最新手机页面
 * */

public class Test9 {
    private static Logger logger = Logger.getLogger(Test9.class);
    private static LinkedList<UrlSeed> queue = new LinkedList<UrlSeed>();
    private static Set<String> urlSet = new HashSet<String>();
    private static Set<String> set = new HashSet<String>();
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		Class.forName("org.gjt.mm.mysql.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/easoupay?useUnicode=true&amp;characterEncoding=utf8","root","123456");
		PreparedStatement pstmt = conn.prepareStatement("insert into mobile_brand(url) values(?)");
		
		
		String root = "http://detail.zol.com.cn/cell_phone_advSearch/subcate57_1_m98-m544-m297-m33080-m1763-m295-m1795-m1606-m1632-m1434-m12772-m32729-m1673-m642-m34023-m34821-m34485-m34884-m159-m1069-m34645-m33941-m613-m34943-m143-m171-m34987-m34951-m1099-m34686-m34549-m34927-m19-m1081-m1589-m599-m221-m33855-m35005-m531-m563-m34584-m34866-m227-m1826-m34096-m35029-m33668-m34985-m34966-m34298-m34547-m34857-m34906-m35000-m34380-m34986-m218-m1528-m33665-m342-m34828-m34202-m34969-m34679-m715-m34964-m33382-m34952-m34741-m34913-m34571-m33756-m486-m34981-m34941-m34200-m34851-m23-m34874-m33540-m35035-m32777-m34551-m34794-m33964-m283-m35004-m34954-m33139-m34639-m34923-m34988-m355-m34998-m34680-m34520-m34660-m34677-m29-m125-m209-m223-m234-m314-m35046-m34691-m21-m1591-m33242-m1073-m1071-m1041-m33477-m34391-m34445-m34050-m33912-m33969-m34048-m300_1_1_0_1.html?#showc";
		queue.add(new UrlSeed(root,0,1));
		UrlConnection urlconn = new UrlConnection("GBK");
		
		while(queue.size()>0){
			UrlSeed seed = queue.removeFirst();
			System.out.println(seed.getUrl()+"..."+queue.size());
			urlconn.setUrl(seed.getUrl());
			String content = urlconn.getContent();
			if(StringUtils.isEmpty(content)) doRetry(seed);
			Parser parser = new Parser();
			parser.setInputHTML(content);
			LinkRegexFilter pageFilter  = new LinkRegexFilter("cell_phone_advSearch/subcate57_1_.*m300_9_1__\\d+\\.html");
			NodeList pageList = parser.extractAllNodesThatMatch(pageFilter);
			addAllToQueue(pageList,seed.getUrl(),seed.getDeep()+1,seed.getRetry());
			
			parser.setInputHTML(content);
			LinkRegexFilter urlFilter  = new LinkRegexFilter("index\\d+\\.shtml");
			NodeList urlList = parser.extractAllNodesThatMatch(urlFilter);
			for(int i=0;i<urlList.size();i++){
				String url = ((LinkTag)urlList.elementAt(i)).extractLink();
    		    String text = ((LinkTag)urlList.elementAt(i)).getLinkText();
    		    url = fullUrl(seed.getUrl(), url);
    		    if(!set.contains(url)){
    		       set.add(url);
			       pstmt.setString(1,url.trim());
			       pstmt.execute();
			    }
			}
			
		}
		
	}
	
	
	//提取详细页翻页
	
    public static void addAllToQueue(NodeList list,String fatherUrl,int deep,int retry){
    	for(int i=0;i<list.size();i++){
    		String url = ((LinkTag)list.elementAt(i)).extractLink();
    		String text = ((LinkTag)list.elementAt(i)).getLinkText();
    		url = fullUrl(fatherUrl, url);
    		addToQueue(url.trim().replace(" ","%20"), deep,retry);
    	}
    }
    
    
    public static void addToQueue(String url,int deep,int retry){
    	if(!urlSet.contains(url)){
    		urlSet.add(url);
    		queue.add(new UrlSeed(url,deep,retry));
    	}
    }
	
	
	 /**
 	 * 符合重试条件的，回塞到队列里
 	 * */
 	public static void doRetry(UrlSeed seed){
 		try{
	 		if(seed.getRetry()>3){
	 			logger.error(seed.getUrl()+"...retried:"+seed.getRetry()+"times...errMsg:"+seed.getErrMsg()+"...ignore");
	 		}else{
	 			seed.setRetry(seed.getRetry()+1);
	 			queue.add(seed);
	 		}
 		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
 	}
 	
 	
 	public static String fullUrl(String fartherUrl, String url) {
	    if(isFullUrl(url)) {
	    	return url;
	    }else if(url != null && url.startsWith("?") ) {
	       int qindex = fartherUrl.indexOf('?');
	       int len = fartherUrl.length();
	       if (qindex < 0) {
	           return fartherUrl + url;
	       } else if (qindex == len - 1) {
	           return fartherUrl.substring(0, len - 1) + url;
	       } else {
	           return fartherUrl + "&" + url.substring(1);
	       }
	    }
	    	
	    boolean isLinkAbsolute = url.startsWith("/");
	    	
	    if (!isFullUrl(fartherUrl)) {
	    	fartherUrl = "http://" + fartherUrl;
	    }
	    	
	    int slashIndex = isLinkAbsolute ? fartherUrl.indexOf("/", 8) : fartherUrl.lastIndexOf("/");
	    if (slashIndex <= 8) {
	    	fartherUrl += "/";
	    } else {
	    	fartherUrl = fartherUrl.substring(0, slashIndex+1);
	    }
	       return isLinkAbsolute ? fartherUrl + url.substring(1) : fartherUrl + url; 
	    }
	
 	public static boolean isFullUrl(String link) {
        if (link == null) {
            return false;
        }
        link = link.trim().toLowerCase();
        return link.startsWith("http://") || link.startsWith("https://") || link.startsWith("file://");
    }

}
