package com.fantingame.pay.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FileTools {

	public static void saveFile(String saveString,File file,String charset,boolean append){
        try { 
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file,append),charset);
            out.write(saveString); 
            out.flush(); 
            out.close(); 
            out = null;
        } catch (UnsupportedEncodingException e){ 
            // TODO Auto-generated catch block 
            e.printStackTrace(); 
        } catch (FileNotFoundException e){ 
            // TODO Auto-generated catch block 
            e.printStackTrace(); 
        } catch (IOException e){ 
            // TODO Auto-generated catch block 
            e.printStackTrace(); 
        } 
	
	}
	
	public static void saveFile(String saveString,String filePath,String fileName,String charset,boolean append){
        try { 
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(filePath+fileName,append),charset);
            out.write(saveString); 
            out.flush(); 
            out.close(); 
            out = null;
        } catch (UnsupportedEncodingException e){ 
            // TODO Auto-generated catch block 
            e.printStackTrace(); 
        } catch (FileNotFoundException e){ 
            // TODO Auto-generated catch block 
            e.printStackTrace(); 
        } catch (IOException e){ 
            // TODO Auto-generated catch block 
            e.printStackTrace(); 
        } 

 }
	
	/** 取得文件夹大�?*/
	   public static long getFilesSize(File f)throws Exception{
		   
	       long size = 0;
	       File flist[] = f.listFiles();
	       for (int i = 0; i < flist.length; i++){
	           if(flist[i].isDirectory()){
	               size = size + getFilesSize(flist[i]);
	           }else{
	               size = size + flist[i].length();
	           }
	       }
	       return size;
	   }
	   
	   /** 取得文件大小 */
	   public long getFileSize(File f) throws Exception{
	       long s=0;
	       if (f.exists()) {
	           FileInputStream fis = null;
	           fis = new FileInputStream(f);
	          s= fis.available();
	       } else {
	           f.createNewFile();
	           System.out.println("文件不存�?");
	       }
	       return s;
	   }
	   
	   /** 递归求取目录文件个数 */
	   public long getlist(File f){
	       long size = 0;
	       File flist[] = f.listFiles();
	       size=flist.length;
	       for (int i = 0; i < flist.length; i++) {
	           if (flist[i].isDirectory()) {
	               size = size + getlist(flist[i]);
	               size--;
	           }
	       }
	       return size;

	   }
	   
	   /** 转换文件大小*/
	   public static String FormetFileSize(long fileS) {
	       DecimalFormat df = new DecimalFormat("#.00");
	       String fileSizeString = "";
	       if (fileS < 1024) {
	           fileSizeString = df.format((double) fileS) + "B";
	       } else if (fileS < 1048576) {
	           fileSizeString = df.format((double) fileS / 1024) + "K";
	       } else if (fileS < 1073741824) {
	           fileSizeString = df.format((double) fileS / 1048576) + "M";
	       } else {
	           fileSizeString = df.format((double) fileS / 1073741824) + "G";
	       }
	       return fileSizeString;
	   }
	   
	   /** 文件夹内模糊查询*/
	   public static ArrayList searchFiles(File f1,String extent,ArrayList fileList){
		  String arr[] = f1.list();
		  for(int i=0;i<arr.length;i++){
		   File f2 = new File(f1,arr[i]);
		   if(f2.isFile() && f2.getPath().contains(extent)){
			  fileList.add(f2.getPath());
		   }else if(!f2.isFile()){
		     searchFiles(f2,extent,fileList);
		   }
	      }
		 return fileList; 
	   }   
	
	   public static String[] readText(String path) {
		   String[] result = null;
		         String str=readTextFile(path);
		         result=str.split("\n");
		   return result;
	   }

	   public static  String readTextFile(String path) {
		 StringBuffer buffer = new StringBuffer();
	   try {
		    BufferedReader in = new BufferedReader(new FileReader(path));
		    String s;
		    while ((s = in.readLine()) != null) {
		     buffer.append(s.trim());
		     buffer.append("\n");
		    }
		    in.close();
		    buffer.append("\n");
		 } catch (Exception ex) {
		    ex.printStackTrace();
		    return null;
		 }
		   return buffer.toString();
		 }	   
	   
	   public static StringBuilder readSingleLineTextFile(String path) {
			 StringBuilder buffer = new StringBuilder();
		   try {
			    BufferedReader in = new BufferedReader(new FileReader(path));
			    String s;
			    while ((s = in.readLine()) != null) {
			        s.replace("\n", "");
			    	buffer.append(s);
			    }
			    in.close();
			 } catch (Exception ex) {
			    ex.printStackTrace();
			    return null;
			 }
			   return buffer;
	  }	
	   
	   public static StringBuilder readSingleLineTextFile(String path,String charset) {
			 StringBuilder buffer = new StringBuilder();
		   try {
			   File f = new File(path);   
			   InputStreamReader read = new InputStreamReader (new FileInputStream(f),charset);   
			   BufferedReader reader=new BufferedReader(read);   
			    String s;
			    while ((s = reader.readLine()) != null) {
			        s.replace("\n", "");
			    	buffer.append(s);
			    }
			    reader.close();
			 } catch (Exception ex) {
			    ex.printStackTrace();
			    return null;
			 }
			   return buffer;
	  }	   
	   
	   public static void readSingleLineTextFile(String path,StringBuilder sb) {
		   try {
			  BufferedReader in = new BufferedReader(new FileReader(path));
			  sb.append(in.readLine());
			  in.close();
		   }catch (FileNotFoundException e) {
			  sb.delete(0, sb.length());
			  System.out.println("can not find this file");
		   } catch (IOException e) {
			  sb.delete(0, sb.length());
		   }
	  }	 
	   
	   public static Set<String> readFile(String filePath) {
		 Set<String> set = new HashSet<String>();
		 try{
		     File f = new File(filePath);   
		     BufferedReader reader = new BufferedReader(new FileReader(f));
		     
		     String s = null;
		      while ((s = reader.readLine()) != null) {
		        set.add(s.replace("\n", ""));
		      }
		      reader.close();
	       }catch (Exception e) {
			  System.out.println("can not find this file");
		   }
		    return set;
	  }	 

	   public static void saveObject(Object obj,String filePath,String fileName){
			FileOutputStream ostream;
			ObjectOutputStream p;
			try {
				File file = new File(filePath);
				if(!file.exists()) file.mkdirs();
				ostream = new  FileOutputStream(filePath+fileName);
				p  =   new  ObjectOutputStream(ostream); 
				p.writeObject(obj);
				p.flush();
				p.close();
			} catch (FileNotFoundException e) {
                e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
		
		public static Object readObject(String filePath,String fileName){
			Object obj = null;
			FileInputStream istream;
			try {
				istream = new  FileInputStream(filePath+fileName);
				ObjectInputStream q  =   new  ObjectInputStream(istream); 
				obj =  q.readObject();
				q.close();
			} catch (FileNotFoundException e) {
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return obj;
		} 
   
}
