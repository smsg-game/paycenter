package test;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.fantingame.pay.utils.StringTools;


public class Test8 {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
	    Set<String> set = new HashSet<String>();
		File[] file1 = new File("D:\\log\\40").listFiles();
		for(File file : file1){
			 java.util.List<String> list = FileUtils.readLines(file,"UTF-8");
			 for(String str : list){
				 if(str.contains("/notifyFromMo9.e?payer_id")) set.add(StringTools.getSpecVlaue(str,"url=",""));
			 }
		}
		
		File[] file2 = new File("D:\\log\\41").listFiles();
		for(File file : file2){
			 java.util.List<String> list = FileUtils.readLines(file,"UTF-8");
			 for(String str : list){
				 if(str.contains("/notifyFromMo9.e?payer_id")) set.add(StringTools.getSpecVlaue(str,"url=",""));
			 }
		}
		
		for(String str : set){
			System.out.println(str);
		}
		
	}
}
