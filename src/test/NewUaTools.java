package test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Note : 推广链接上需要带上参数:&uc_param_str=veupssntdnmi
 */
public class NewUaTools {
	
	private static final Pattern ucVersionPattern = Pattern.compile("(ucweb|ucbrowser)/?([0-9.]*)");
	private static final Pattern qqVersionPattern = Pattern.compile("qqbrowser/?([0-9.]*)");
	private static final Pattern chromeVersionPattern = Pattern.compile("chrome/?([0-9.]*)");
	private static final Pattern operaVersionPattern = Pattern.compile("version/?([0-9.]*)");
	private static final Pattern iphoneVersionPattern = Pattern.compile("iphoneos([0-9_]*)");
	
	
	public static boolean isSupportWebSocketWell(String version,String u){
		if(version != null && version.length()>1 ){  //证明是UC浏览器
			return isUcVersionSupportWebSocketWell(version);
		}
		String ua = u.replaceAll("\\s+", "").toLowerCase(); //去掉所有空格，转换成小写
		if(ua.contains("ucbrowser") || ua.contains("ucapplewebkit") || ua.contains("ucweb")){
			return isUcVersionSupportWebSocketWell(getUcVersion(ua));
		}else if(ua.contains("qqbrowser")){
			return isQQVersionSupportWebSocketWell(getQQBrowserVersion(ua), ua);
		}else if(ua.contains("chrome")){
			return isChromeSupportWebSocketWell(getChromeBrowserVersion(ua));
		}else if(ua.contains("opera")){
			return isOperaSupportWebSocketWell(getOperaVersion(ua));
		}else if(ua.contains("iphone")){
			return isMacOsSupportWebSocketWell(getIphoneVersion(ua));
		}
		return false;
	}

	private static boolean isUcVersionSupportWebSocketWell(String ucVersion){
		return getShortVersion(ucVersion)>=8.3?true:false;
	} 
	
	private static boolean isQQVersionSupportWebSocketWell(String qqVersion,String ua){
		return (ua.contains("aurora") || qqVersion.startsWith("4."))?true:false;
	} 
	
	private static boolean isChromeSupportWebSocketWell(String chromeVersion){
		return getShortVersion(chromeVersion)>=4.0?true:false;
	}
	
	
	private static boolean isOperaSupportWebSocketWell(String operaVersion){
		return getShortVersion(operaVersion)>=12.1?true:false;
	}
	
	private static boolean isMacOsSupportWebSocketWell(String iphoneVersion){
		return true;
		//return getShortVersion(iphoneVersion)>=4.2?true:false;
	}
	

	private static String getUcVersion(String ua){
		ua = (ua.contains("ucbrowser") && ua.contains("ucweb"))?ua.replaceAll("ucweb", ""):ua;
		Matcher m = ucVersionPattern.matcher(ua);
		return m.find()?m.group(2):null;
	}
	

	private static String getQQBrowserVersion(String ua){
		Matcher m = qqVersionPattern.matcher(ua);
		return m.find()?m.group(1):null;
	}
	

	private static String getChromeBrowserVersion(String ua){
		Matcher m = chromeVersionPattern.matcher(ua);
		return m.find()?m.group(1):null;
	}
	
	private static String getOperaVersion(String ua){
		Matcher m = operaVersionPattern.matcher(ua);
		return m.find()?m.group(1):null;
	}
	
	
	private static String getIphoneVersion(String ua){
		Matcher m = iphoneVersionPattern.matcher(ua);
		return m.find()?m.group(1):null;
	}
	
	
	private static float getShortVersion(String version){
		if(version==null) return 0.0f;
		int pointPos = version.indexOf(".");
		return 
				(pointPos>0 && version.length()>pointPos+1)?
				Float.valueOf (version.substring(0, pointPos+2)):Float.parseFloat(version);
	}
	
}
