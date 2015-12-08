package test;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import org.apache.commons.io.FileUtils;



public class Test5 {

   public static void main(String[] args) throws Exception{
	   /*
	   Class.forName("org.gjt.mm.mysql.Driver");
	   Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?useUnicode=true&amp;characterEncoding=utf8","root","123456");
	   
	   PreparedStatement pstmt = conn.prepareStatement("update sms_location set province=?,city=?,type=? where phone=?");
	   
		File file = new File("/mbcodelocus.txt");
		 
		int count=0;
		BufferedReader bw = new BufferedReader(new FileReader(file));
		String line = null;
		long start = System.currentTimeMillis();
		//因为不知道有几行数据，所以先存入list集合中
		while((line = bw.readLine()) != null){
			try {
				count++;
				String[] arr = line.split("\\s+");
				if(arr.length!=4){
					System.out.println("");
					throw new Exception("error of arr length");
				}
				pstmt.setString(1,arr[2]);
				pstmt.setString(2,arr[1]);
				pstmt.setString(3,arr[3]);
				pstmt.setString(4,arr[0]);
				pstmt.addBatch();
				if(count%10000==0){
					pstmt.executeBatch();
					System.out.println(count);
				}
			}catch(Exception e) {
				System.out.println("error:"+count);
				e.printStackTrace();
			}
	    }
		pstmt.executeBatch();
		bw.close();
		System.out.println("finished:"+(System.currentTimeMillis()-start));
	    */
	   
	   Class.forName("org.gjt.mm.mysql.Driver");
	   Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/easoupay?useUnicode=true&amp;characterEncoding=utf8","root","123456");
	   
	   PreparedStatement pstmt = conn.prepareStatement("insert into phone_type(url) values(?)");
	   
	   java.util.List<String> list = FileUtils.readLines(new File("/phone_type.txt"));
		 
	   
	   for(String str: list){
		   try{
		     pstmt.setString(1,str);
		     pstmt.execute();
		   }catch (Exception e) {
			 System.out.println(e.getMessage()+"..."+str);
		   }
	   }
	   pstmt.close();
	   conn.close();
	   
	   
	   /*
	   Class.forName("org.gjt.mm.mysql.Driver");
	   Connection conn = DriverManager.getConnection("jdbc:mysql://120.197.137.9:3306/easoupay?useUnicode=true&amp;characterEncoding=utf8","youxi123","123456");
	   PreparedStatement pstmt = conn.prepareStatement("insert into sms_location(mobile,province,city,operator,type,createDatetime) values(?,?,?,?,?,?)");
	   
	   
	   String aaa = "183";
	   String bbb[] = aaa.split("、");
	   
	   int count = 0;
	   for(String ccc: bbb){
		   for(int i=0;i<10000;i++){
			   int phone = Integer.valueOf(ccc)*10000 + i;
			   pstmt.setInt(1,phone);
			   pstmt.setString(2,"");
			   pstmt.setString(3,"");
			   pstmt.setString(4,"");
			   pstmt.setString(5,"");
			   pstmt.setTimestamp(6,new Timestamp(new java.util.Date().getTime()));
			   pstmt.addBatch();
				if(count%10000==0){
					pstmt.executeBatch();
					System.out.println(count);
				}
		   }
	   }
	   pstmt.addBatch(); 
	   */
   }
}
