package test.crawler;

import java.io.Serializable;


public class UrlSeed implements Serializable{

 private int     retry             = 0;
 private int     deep              = 0;
 private String  url               = "";
 private String  errMsg            = "";

 
 
 public UrlSeed (String url,int deep,int retry){
	 this.url   = url;
	 this.deep  = deep;
	 this.retry  = retry;
 }

public int getRetry() {
	return retry;
}

public void setRetry(int retry) {
	this.retry = retry;
}

public int getDeep() {
	return deep;
}

public void setDeep(int deep) {
	this.deep = deep;
}

public String getErrMsg() {
	return errMsg;
}

public void setErrMsg(String errMsg) {
	this.errMsg = errMsg;
}

public String getUrl() {
	return url;
}

public void setUrl(String url) {
	this.url = url;
}
 
}
