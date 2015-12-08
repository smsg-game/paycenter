package com.fantingame.pay.manager;

import com.fantingame.pay.entity.PayMask;

public interface PayMaskManager extends BaseManager<PayMask>{
	
	public String getMaskChannleByCondiction(String maskCondition);
	
}
