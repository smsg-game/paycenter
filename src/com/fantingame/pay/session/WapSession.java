/**
 * WapSession.java
 *
 * Copyright 2007 easou, Inc. All Rights Reserved.
 */
package com.fantingame.pay.session;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 构造wap session类 Revision History 2008-3-17,norby_easou,created it
 */
public class WapSession implements HttpSession {
    public Enumeration getAttributeNames() {
    	return Collections.enumeration(valueMap.keySet());    	
	}

	public long getCreationTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getId() {
		// TODO Auto-generated method stub
		return esid;
	}

	public long getLastAccessedTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getMaxInactiveInterval() {
		// TODO Auto-generated method stub
		return 0;
	}

	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return null;
	}


	public HttpSessionContext getSessionContext() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getValue(String arg0) {
        return valueMap.get(arg0);
	}

	public String[] getValueNames() { 
		 return (String[]) valueMap.keySet().toArray();
	}

	public void invalidate() {
        valueMap.clear();
        changed = true;	
	}

	public boolean isNew() {
		// TODO Auto-generated method stub
		return false;
	}

	public void putValue(String arg0, Object arg1) {
		setAttribute(arg0,arg1);		
	}

	public void removeAttribute(String arg0) {
		valueMap.remove(arg0);		
		changed = true;
	}

	public void removeValue(String arg0) {
		removeAttribute(arg0);		
	}

	public void setMaxInactiveInterval(int arg0) {
		// TODO Auto-generated method stub
		
	}

	private static final Log LOG = LogFactory.getLog(WapSession.class);

    private String esid;
    private Map valueMap;
    private boolean changed = false;// 记录属性是否变化

    /**
     * 构造函数
     * @param esid
     */
    @SuppressWarnings("unchecked")
    public WapSession(String esid) throws Exception {
        super();
        this.esid = esid;
        // 初始化 session Map对象
        valueMap = new HashMap();
        Map map = SessionService.getInstance().getSession(esid);// 从memcach中获取属性
        if (map != null) {// 缓存中存在,load memcach获取的map对象
            valueMap.putAll(map);
        }

    }

    /**
     * 获取属性值
     * @param key
     * @return Object
     */
    public Object getAttribute(String key) {
        return valueMap.get(key);
    }

    /**
     * @param key
     * @param value
     */
    @SuppressWarnings("unchecked")
    public void setAttribute(String key, Object value) {
        if ("isCache".equalsIgnoreCase(key) && null != value) {// fileter
            // 链处理完时，设置同步memcach的标记
        	saveSession();
        } else {
            changed = true;
            valueMap.put(key, value);
        }
    }

    /**
     * 将session缓存
     */
    public void saveSession() {
        if (changed) {
            boolean status = SessionService.getInstance().saveSession(esid,valueMap);
            changed = false;
            if (LOG.isDebugEnabled()) {
                LOG.debug("session save status:" + status);
            }
        }
    }

}
