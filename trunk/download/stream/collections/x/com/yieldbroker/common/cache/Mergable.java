package com.yieldbroker.common.cache;

public interface Mergable<T> {
	public Data<T> merge(Data<T> data);
}	
