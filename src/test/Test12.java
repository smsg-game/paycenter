package test;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.easou.common.util.StringUtil;
import com.fantingame.pay.utils.StringTools;



public class Test12 {
  
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		 
		   Class.forName("oracle.jdbc.driver.OracleDriver");
		   Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@120.197.137.9:1521:ppfm","ppfm_user","easou_play");
		   PreparedStatement pstmt = conn.prepareStatement("insert into tmp_visit(id,src,ua,esid,gameid,qn,create_datetime,v_day,v_hour) values(stat_sequence.nextval,?,?,?,?,?,?,?,?)");
		   int count = 0;
		   
		    File[] files1 = new File("D:\\log\\42\\").listFiles();
			for(File file : files1){
				System.out.println(file.getAbsolutePath());
				List<String> lines = FileUtils.readLines(file);
				for(String line : lines){
					count++;
					String[] arr = line.split("\\{\\]");
					String ve = StringTools.getSpecVlaue(arr[2],"&ve=","&");
					String gameId = StringTools.getSpecVlaue(arr[2],"gameId=","&");
					String qn = StringTools.getSpecVlaue(arr[2],"&qn=","&");
					String time = arr[4].replace(" ","");
					String esid = arr[16];
					String ua = arr[14];
					
					pstmt.setString(1,line);
					pstmt.setString(2,ua);
					pstmt.setString(3,esid);
					pstmt.setString(4,gameId);
					pstmt.setString(5,qn);
					pstmt.setTimestamp(6,new Timestamp(sdf.parse(time).getTime()));
					pstmt.setDate(7,new Date(sdf.parse(time).getTime()));
					pstmt.setString(8,time.substring(time.length()-2));
					
					pstmt.addBatch();
					if(count % 1000==0) pstmt.executeBatch();
					
				}
			}
			
			
			File[] files2 = new File("D:\\log\\43\\").listFiles();
			for(File file : files2){
				System.out.println(file.getAbsolutePath());
				List<String> lines = FileUtils.readLines(file);
				for(String line : lines){
					count++;
					String[] arr = line.split("\\{\\]");
					String ve = StringTools.getSpecVlaue(arr[2],"&ve=","&");
					String gameId = StringTools.getSpecVlaue(arr[2],"gameId=","&");
					String qn = StringTools.getSpecVlaue(arr[2],"&qn=","&");
					String time = arr[4].replace(" ","");
					String esid = arr[16];
					String ua = arr[14];
					
					pstmt.setString(1,line);
					pstmt.setString(2,ua);
					pstmt.setString(3,esid);
					pstmt.setString(4,gameId);
					pstmt.setString(5,qn);
					pstmt.setTimestamp(6,new Timestamp(sdf.parse(time).getTime()));
					pstmt.setDate(7,new Date(sdf.parse(time).getTime()));
					pstmt.setString(8,time.substring(time.length()-2));
					
					pstmt.addBatch();
					if(count % 1000==0) pstmt.executeBatch();
					
				}
			}
			
			
			pstmt.executeBatch();
			pstmt.close();
			conn.close();
		   
		   
	}
}
