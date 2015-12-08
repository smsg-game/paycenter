package com.fantingame.pay.manager;

import com.fantingame.pay.entity.PayAppVersion;



public interface PayAppVersionManager extends BaseManager<PayAppVersion>{
    public PayAppVersion getLatestVersion();
    public PayAppVersion getLatestForcedVersion();
}
