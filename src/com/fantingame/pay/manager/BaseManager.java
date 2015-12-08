package com.fantingame.pay.manager;

import java.util.List;

public interface BaseManager<T> {
	public int count(T t);
	public List<T> getEntity(T t);
	public T getEntityById(Long id) throws Exception;
	public int save(T t);
	public int update(T t);
	public int delete(List<String> ids);
}
