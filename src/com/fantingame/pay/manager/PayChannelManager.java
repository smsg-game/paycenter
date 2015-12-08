package com.fantingame.pay.manager;

import com.fantingame.pay.entity.PayChannel;

public interface PayChannelManager extends BaseManager<PayChannel>{
	
     public PayChannel getChannelByType(String type) throws Exception;
     
}
