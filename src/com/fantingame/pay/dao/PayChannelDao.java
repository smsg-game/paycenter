package com.fantingame.pay.dao;

import com.fantingame.pay.entity.PayChannel;


public interface PayChannelDao extends BaseDao<PayChannel> {

	PayChannel getChannelByType(String type);
     
}
