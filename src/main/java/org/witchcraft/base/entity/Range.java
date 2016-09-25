package org.witchcraft.base.entity;

public class Range<T> {
	
	private T begin;
	private T end;
	
	public T getBegin() {
		return begin;
	}
	public void setBegin(T begin) {
		this.begin = begin;
	}
	public T getEnd() {
		return end;
	}
	public void setEnd(T end) {
		this.end = end;
	}

}
