package com.yieldbroker.common.lease;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

interface Contract<T> extends Delayed {
	T getKey();
	long getDelay(TimeUnit unit);
	void setDelay(long delay, TimeUnit unit);
	String toString();
}
