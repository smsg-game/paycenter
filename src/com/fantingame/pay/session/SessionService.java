/**
 * SessionService.java
 *
 * Copyright 2007 easou, Inc. All Rights Reserved.
 */
package com.fantingame.pay.session;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;
import com.easou.common.util.PropertiesUtil;

/**
 * TODO
 * 
 * Revision History
 *
 * 2007-11-6,norbys,created it
 */
public class SessionService {
    private static final Log LOG = LogFactory.getLog(SessionService.class);

    private static SessionService instance = null;

    private int sessionTimeout = 1800000; // 单位为毫秒

    private SockIOPool pool = null;

    private String poolName = "sesssock";

    public static synchronized SessionService getInstance() {
        if (instance == null) {
            instance = new SessionService();
        }
        return instance;
    }

    private SessionService() {
        InputStream infile = SessionService.class
                .getResourceAsStream("/sessionservice.properties");
        Properties props = new Properties();
        try {
            props.load(infile);
        } catch (IOException ex) {
            LOG.error("load property file fail:" + ex.getMessage());
        }
        initSockIOPool(props);
        sessionTimeout = PropertiesUtil.getIntProperty(props, "sessionTimeout",
                SessionContent.SESSION_LACEL_TIMEOUT_DEFAULT);
        if (LOG.isDebugEnabled()) {
            LOG.debug("sessionTimeout=" + sessionTimeout);
        }
    }

    public void initSockIOPool(Properties props) {
        String serverlist = props.getProperty("serverlist", "127.0.0.1:11211");
        poolName = props.getProperty("poolname", "sesssock");
        String[] servers = serverlist.split(",");
        pool = SockIOPool.getInstance(poolName);
        pool.setServers(servers);
        pool.setFailover(PropertiesUtil.getBooleanProperty(props, "failover",
                true));
        pool.setInitConn(PropertiesUtil.getIntProperty(props, "initconn", 10));
        pool.setMinConn(PropertiesUtil.getIntProperty(props, "minconn", 5));
        pool.setMaxConn(PropertiesUtil.getIntProperty(props, "maxconn", 250));
        pool.setMaintSleep(PropertiesUtil.getIntProperty(props, "maintsleep",
                30));
        pool.setNagle(PropertiesUtil.getBooleanProperty(props, "nagle", false));
        pool
                .setSocketTO(PropertiesUtil.getIntProperty(props, "socketTO",
                        3000));
        pool.setAliveCheck(PropertiesUtil.getBooleanProperty(props,
                "alivecheck", true));
        pool.initialize();
        if (LOG.isDebugEnabled()) {
            LOG.debug("servers=");
            servers = pool.getServers();
            for (int i = 0; i < servers.length; i++) {
                LOG.debug(servers[i]);
            }
            LOG.debug("poolname=" + poolName);
            LOG.debug("failover=" + pool.getFailover());
            LOG.debug("initconn=" + pool.getInitConn());
            LOG.debug("minconn=" + pool.getMinConn());
            LOG.debug("maxconn=" + pool.getMaxConn());
            LOG.debug("maintsleep=" + pool.getMaintSleep());
            LOG.debug("nagle=" + pool.getNagle());
            LOG.debug("socketTO=" + pool.getSocketTO());
            LOG.debug("alivecheck=" + pool.getAliveCheck());
        }
    }

    public Map getSession(String id) {
        MemCachedClient mc = this.getMemCachedClient();
        if (LOG.isDebugEnabled()) {
            LOG.debug(id);
        }
        return (Map) mc.get(id);
    }

    public boolean saveSession(String id, Map session) {
        return saveSession(id, session, new Date(new Date().getTime()
                + sessionTimeout));
    }

    public boolean saveSession(String id, Map session, Date expiry) {
        MemCachedClient mc = this.getMemCachedClient();
        if (LOG.isDebugEnabled()) {
            LOG.debug("id=" + id);
            LOG.debug("session=" + session);
            LOG.debug("expiry=" + expiry);
        }
        return mc.set(id, session, expiry);
    }

    public boolean removeSession(String id) {
        MemCachedClient mc = this.getMemCachedClient();
        if (LOG.isDebugEnabled()) {
            LOG.debug(id);
        }
        return mc.delete(id);
    }

    private MemCachedClient getMemCachedClient() {
        MemCachedClient mc = new MemCachedClient(poolName);
        if (LOG.isDebugEnabled()) {
            LOG.debug(poolName);
        }
        mc.setCompressEnable(false);
        mc.setCompressThreshold(0);
        return mc;
    }

    protected void finalize() {
        if (this.pool != null) {
            this.pool.shutDown();
        }
    }

}
