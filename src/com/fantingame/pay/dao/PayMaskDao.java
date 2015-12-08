package com.fantingame.pay.dao;

import com.fantingame.pay.entity.PayMask;

public interface PayMaskDao extends BaseDao<PayMask> {

	String getMaskChannleByCondiction(String maskCondition);}
