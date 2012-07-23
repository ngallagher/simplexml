package com.yieldbroker.common.cache;

public interface Data<T> {
	T getValue();
	String getType();
	String getCategory();
	Object getKey();
}
