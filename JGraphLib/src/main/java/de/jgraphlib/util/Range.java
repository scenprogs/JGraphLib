package de.jgraphlib.util;

public abstract class Range<T> {	
	private T min;
	private T max;
	public Range(T minMax) {
		this.min = minMax;
		this.max = minMax; 
	}
	
	public Range(T min, T max) {
		this.min = min;
		this.max = max;
	}
	
	
	public abstract boolean contains(T t);
	
	public abstract T abs();
	
	public T getMax() {
		return this.max;
	}
	
	public void setMax(T max) {
		this.max = max;
	}
	
	public T getMin() {
		return this.min;
	} 
	
	public void setMin(T min) {
		this.min = min;
	}
	
	public T min() {
		return this.min;
	} 
	
	public T max() {
		return this.max;
	}
}
