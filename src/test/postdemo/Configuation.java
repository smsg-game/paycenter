package test.postdemo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuation {
    private Properties property = null;
	private String filePath = null;
	
	private Configuation(){
		
	}
	
	public Configuation(String propertyFilePath){
		try{
		   this.property = new Properties();
		   this.filePath = propertyFilePath;
		   //InputStream in = Configuation.class.getResourceAsStream(propertyFilePath);
		   FileInputStream in = new FileInputStream(propertyFilePath);
		   this.property.load(in);
		   in.close();
		}catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public String getStringValue(String key){
		if(this.property.containsKey(key)){
			return property.getProperty(key).trim();
		}else{
			return "";
		}
	}
	
	public String getStringValue(String key,String charset){
		try{
			if(this.property.containsKey(key)){
				return new String(property.getProperty(key).trim().getBytes("iso-8859-1"),charset);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return "";
	}
	
	public Integer getIntValue(String key){
		String result = "";
		if(this.property.containsKey(key)){
			result = property.getProperty(key).trim();
		}else{
			result =  "";
		}
		try{
			return Integer.valueOf(result);
		}catch (Exception e) {
			return null;
		}
	}
	
	public void clearKey(){
		this.property.clear();
	}
	
	public void setValue(String key,String value) {
		this.property.setProperty(key, value);
	}
	
	public void saveFile(String description){
	  try{
		  FileOutputStream outputStream = new FileOutputStream(this.filePath);
		  this.property.save(outputStream, description);
		  outputStream.close();
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
}
