package com.fantingame.pay.manager;

import com.fantingame.pay.entity.PayUserBindHistory;



public interface PayUserBindHistoryManager extends BaseManager<PayUserBindHistory>{
     public PayUserBindHistory getEntityByMobile(String mobile);
}
