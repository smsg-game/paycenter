package com.fantingame.pay.utils;

import com.easou.common.util.StringUtil;
import com.fantingame.pay.manager.PayMaskManager;

public class MaskUtil {
	
	public static String getMaskChannelByCondition(String condition){
		PayMaskManager manager = (PayMaskManager) Constants.CTX.getBean("maskManager");
		String maskChannel = manager.getMaskChannleByCondiction(condition);
		return maskChannel==null?"":maskChannel;
	}
	
	public static int getInt(String str) {
		if(StringUtil.isNumber(str)) {
			return Integer.parseInt(str);
		}
		return 0;
	}
}
