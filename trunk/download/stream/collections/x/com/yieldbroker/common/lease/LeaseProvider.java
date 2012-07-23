package com.yieldbroker.common.lease;

import java.util.concurrent.TimeUnit;

public interface LeaseProvider<T> {
	Lease<T> lease(T key, long duration, TimeUnit unit);
	void close();
}
