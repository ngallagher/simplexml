package com.yieldbroker.common.lease;

public interface Cleaner<T> {
	void clean(T key) throws Exception;
}
