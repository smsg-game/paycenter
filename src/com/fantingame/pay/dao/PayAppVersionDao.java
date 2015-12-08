package com.fantingame.pay.dao;

import com.fantingame.pay.entity.PayAppVersion;


public interface PayAppVersionDao extends BaseDao<PayAppVersion> {
	public PayAppVersion getLatestVersion();
	public PayAppVersion getLatestForcedVersion();
}
