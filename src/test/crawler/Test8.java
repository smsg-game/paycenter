package test.crawler;

import java.io.File;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import com.fantingame.pay.utils.FileTools;
import com.fantingame.pay.utils.UrlConnection;


public class Test8 {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		List<String> list = FileUtils.readLines(new File("/model.txt"),"UTF-8");
		UrlConnection conn = new UrlConnection("GBK");
		HtmlCleaner cleaner = new HtmlCleaner();
		Map<String,Param> map = new LinkedHashMap<String, Param>();
		
		for(String str : list){
			conn.setUrl("http://detail.zol.com.cn/index.php?c=SearchList&keyword="+URLEncoder.encode(str,"GBK"));
			String content = conn.getContent();
			if(StringUtils.isEmpty(content)){
				conn.setUrl("http://detail.zol.com.cn/index.php?c=SearchList&keyword="+URLEncoder.encode(str,"GBK"));
				content = conn.getContent();
			}
			TagNode tagNode = cleaner.clean(content);
			Object obj[] = tagNode.evaluateXPath("//a[contains(@href,'param.shtml')]/@href");
			
			map.put(str,new Param());
			if(obj.length>0){
				conn.setUrl("http://detail.zol.com.cn"+obj[0]);
				content = conn.getContent();
				if(StringUtils.isEmpty(content)){
					conn.setUrl("http://detail.zol.com.cn"+obj[0]);
					content = conn.getContent();
				}
				TagNode tagNode2 = cleaner.clean(content);
				Object fenbianlv[] = tagNode2.evaluateXPath("//tr[th[contains(.,'主屏分辨率')]]//span//text()");//分辨率
				Object os[] = tagNode2.evaluateXPath("//tr[th[contains(.,'操作系统')]]//span//text()");//操作系统
				Object core[] = tagNode2.evaluateXPath("//tr[th[contains(.,'核心数')]]//span//text()");//核心数
				Object speed[] = tagNode2.evaluateXPath("//tr[th[contains(.,'CPU频率')]]//span//text()");//CPU频率
				Object memory[] = tagNode2.evaluateXPath("//tr[th[contains(.,'RAM容量')]]//span//text()");//RAM容量
				
				String para1 = fenbianlv!=null && fenbianlv.length>0?fenbianlv[0].toString():"empty";
				String para2 = os!=null && os.length>0?os[0].toString():"empty";
				String para3 = core!=null && core.length>0?core[0].toString():"empty";
				String para4 = speed!=null && speed.length>0?speed[0].toString():"empty";
				String para5 = memory!=null && memory.length>0?memory[0].toString():"empty";
				
				String c = str+"\t\t"+ para1 +"\t\t" + para2 +"\t\t" + para3 +"\t\t" + para4 +"\t\t"+para5;
				
				System.out.println(c);
				FileTools.saveFile(c+"\r\n", new File("/aaa.txt"), "UTF-8", true);
			}else{
				FileTools.saveFile(str+"\r\n", new File("/aaa.txt"), "UTF-8", true);
			}
		}
		
	}
	
	

}
