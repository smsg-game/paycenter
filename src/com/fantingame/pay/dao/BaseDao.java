package com.fantingame.pay.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface BaseDao<T> {
	//public int count(@Param("params") Object ...params);
	public int count(T t);
	public List<T> getEntity(T t);
	public T getEntityById(long id);
	public int save(T t);
	public int update(T t);
	public int delete(@Param("ids") List<String> ids);
}
