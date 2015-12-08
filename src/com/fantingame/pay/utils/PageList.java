/**
 * PageList.java
 *
 * Copyright 2007 easou, Inc. All Rights Reserved.
 */
package com.fantingame.pay.utils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * TODO
 * 
 * Revision History
 * 
 * 2007-10-30,norby_easou,created it
 */
@SuppressWarnings({"unchecked", "serial"})
public class PageList extends ArrayList {
	protected int pageCount = 0;
	protected int currentPage = 0;
	protected int totalRowCount = 0;

	/*
	 * Constructs an empty list with an initial capacity of ten. The pageCount,
	 * currentPage and totalRowCount are initialized to zero.
	 */
	public PageList() {
		super();
	}

	/**
	 * 
	 * @param c ����
	 */
	public PageList(final Collection c) {
		super(c);
	}

	/**
	 * 
	 * @param initialCapacity init
	 */
	public PageList(final int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * 
	 * @param pageCount
	 */
	public void setPageCount(int pageCount) {
		if (pageCount < 0) {
			pageCount = 0;
		}
		this.pageCount = pageCount;
	}

	/**
	 * 
	 * @param currentPage ��ǰҳ
	 */
	public void setCurrentPage(int currentPage) {
		if (currentPage < 0) {
			currentPage = 0;
		}
		this.currentPage = currentPage;
	}

	/**
	 * 
	 * @param totalRowCount �ܼ�¼��
	 */
	public final void setTotalRowCount(final int totalRowCount) {
		this.totalRowCount = totalRowCount;
	}

	/**
	 * 
	 * @return ��ȡ��ҳ��
	 */
	public final int getPageCount() {
		return pageCount;
	}

	/**
	 * @return ��ȡ��ǰҳ
	 */
	public final int getCurrentPage() {
		return currentPage;
	}

	/**
	 * @return row count
	 */
	public final int getTotalRowCount() {
		return totalRowCount;
	}

	/**
	 * ��ݼ�¼����õ�ҳ��
	 * 
	 * @param pageSize ��ҳ��
	 */
	public final void calcPageCount(int pageSize) {
		if (pageSize < 1) {
			pageSize = 1;
		}
		int n = (int) (totalRowCount % pageSize);
		if (n == 0) {
			setPageCount((int) (totalRowCount / pageSize));
		} else {
			setPageCount((int) ((totalRowCount - n) / pageSize + 1));
		}
	}

}
