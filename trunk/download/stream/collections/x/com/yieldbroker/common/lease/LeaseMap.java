package com.yieldbroker.common.lease;

import java.util.concurrent.ConcurrentHashMap;

public class LeaseMap<T> extends ConcurrentHashMap<T, Lease<T>> {

	public LeaseMap() {
		super();
	}

	public LeaseMap(int capacity) {
		super(capacity);
	}

	public Lease<T> get(Object key) {
		return super.get(key);
	}

	public Lease<T> remove(Object key) {
		return super.remove(key);
	}
}
