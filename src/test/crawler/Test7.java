package test.crawler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import com.fantingame.pay.utils.UrlConnection;


public class Test7 {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
	
		Class.forName("org.gjt.mm.mysql.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/easoupay?useUnicode=true&amp;characterEncoding=utf8","root","123456");
		Statement stmt=conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
		ResultSet rs = stmt.executeQuery("select * from mobile_brand order by updateDatetime limit 500");
		int count = 0;
		UrlConnection urlconn = new UrlConnection("GBK");
		HtmlCleaner cleaner = new HtmlCleaner();
		while(rs.next()){
			try{
				String url = rs.getString("url").replace("http://detail.zol.com.cn/cell_phone/index","").replace(".shtml","");
				url = "http://detail.zol.com.cn/327/"+url+"/param.shtml";
				
				System.out.println(url +"..."+(count++));
				urlconn.setUrl(url);
				String content = urlconn.getContent();
				if(StringUtils.isEmpty(content)) continue;
				
				TagNode tagNode = cleaner.clean(content);
				
				Object nameArr[] = tagNode.evaluateXPath("//div[@class='breadcrumb']//a[contains(@href,'.shtml')]//text()");//名称
				Object ratioArr[] = tagNode.evaluateXPath("//tr[th[contains(.,'主屏分辨率')]]//span//text()");//主屏分辨率
				Object osArr[] = tagNode.evaluateXPath("//tr[th[contains(.,'操作系统')]]//span//text()");//操作系统
				Object coreArr[] = tagNode.evaluateXPath("//tr[th[contains(.,'核心数')]]//span//text()");//核心数
				Object speedArr[] = tagNode.evaluateXPath("//tr[th[contains(.,'CPU频率')]]//span//text()");//CPU频率
				Object ramArr[] = tagNode.evaluateXPath("//tr[th[contains(.,'RAM容量')]]//span//text()");//RAM容量
				Object romArr[] = tagNode.evaluateXPath("//tr[th[contains(.,'ROM容量')]]//span//text()");//ROM容量
				Object sellTimeArr[] = tagNode.evaluateXPath("//tr[th[contains(.,'上市日期')]]//span//text()");//上市日期
				Object sizeArr[] = tagNode.evaluateXPath("//tr[th[contains(.,'主屏尺寸')]]//span//text()");//主屏尺寸
				
				
				String  name =  nameArr!=null && nameArr.length>0?nameArr[0].toString():"";
				String ratio = ratioArr!=null && ratioArr.length>0?ratioArr[0].toString():"";
				String    os = osArr!=null && osArr.length>0?osArr[0].toString():"";
				String  core = coreArr!=null && coreArr.length>0?coreArr[0].toString():"";
				String speed = speedArr!=null && speedArr.length>0?speedArr[0].toString():"";
				String   ram = ramArr!=null && ramArr.length>0?ramArr[0].toString():"";
				String   rom = romArr!=null && romArr.length>0?romArr[0].toString():"";
				String sellTime = sellTimeArr!=null && sellTimeArr.length>0?sellTimeArr[0].toString():"";
				String  size = sizeArr!=null && sizeArr.length>0?sizeArr[0].toString():"";
				
				rs.updateString("name",name);
				rs.updateString("ratio",ratio);
				rs.updateString("os",os);
				rs.updateString("core",core);
				rs.updateString("cpuSpeed",speed);
				rs.updateString("ram",ram);
				rs.updateString("rom",rom);
				rs.updateString("sellTime",sellTime);
				rs.updateString("size",size);
				rs.updateTimestamp("updateDatetime",new Timestamp(new Date().getTime()));
				rs.updateRow();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

}
