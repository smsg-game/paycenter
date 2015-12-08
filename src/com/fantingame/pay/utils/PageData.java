package com.fantingame.pay.utils;

import java.io.Serializable;
import java.util.List;

public class PageData<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private int countRow;

	private int thisPage;

	private List<T> list;

	private int everyPageRow = 15;

	public PageData() {
	}

	public int getCountPage() {
		int tempcount = this.countRow / this.everyPageRow
				+ (((this.countRow % this.everyPageRow) == 0) ? 0 : 1);
		return tempcount > 200 ? 200 : tempcount;
	}

	public String getCountRow() {
		return Integer.toString(countRow);
	}

	public int getCountIntRow() {
		return countRow;
	}

	public void setCountRow(int countRow) {
		this.countRow = countRow;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public int getThisPage() {
		return thisPage > this.getCountPage() ? this.getCountPage() : thisPage;
	}

	public void setThisPage(int thisPage) {
		this.thisPage = thisPage;
	}

	public int getThisPageRow() {
		return this.getList().size();
	}

	public int getEveryPageRow() {
		return everyPageRow;
	}

	public void setEveryPageRow(int everyPageRow) {
		this.everyPageRow = everyPageRow;
	}

	public boolean getHasNext() {
		return this.getThisPage() >= this.getCountPage() ? false : true;
	}

	public boolean getHasPrevious() {
		return this.getThisPage() - 1 < 1 ? false : true;
	}

	public int getNextPage() {
		return this.getThisPage() >= this.getCountPage()
				? this.getThisPage()
				: this.getThisPage() + 1;
	}

	public int getPreviousPage() {
		return this.getThisPage() < 1 ? 0 : this.getThisPage() - 1;
	}
	
}
