/**
 * Alipay.com Inc.
 * Copyright (c) 2005-2008 All Rights Reserved.
 */
package com.fantingame.pay.utils.alipay;

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

/**
 * 
 * 
 * @author feng.chenf
 * @version $Id: CreateDirectTrade.java, v 0.1 2008-11-17 上午09:49:02 feng.chenf Exp $
 */
@XObject("direct_trade_create_res")
public class DirectTradeCreateRes {

    /**
     * 获得的创建交易的RequestToken
     */
    @XNode("request_token")
    private String requestToken;

    public String getRequestToken() {
        return requestToken;
    }

}
