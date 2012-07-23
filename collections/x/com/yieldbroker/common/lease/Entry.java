package com.yieldbroker.common.lease;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

class Entry<T> implements Contract<T> {

	private volatile long time;
	private T key;

	public Entry(T key, long lease, TimeUnit scale) {
		this.time = getTime() + scale.toNanos(lease);
		this.key = key;
	}

	public T getKey() {
		return key;
	}

	public long getDelay(TimeUnit unit) {
		return unit.convert(time - getTime(), NANOSECONDS);
	}

	public void setDelay(long delay, TimeUnit unit) {
		this.time = getTime() + unit.toNanos(delay);
	}

	private long getTime() {
		return System.nanoTime();
	}

	public int compareTo(Delayed other) {
		Entry entry = (Entry) other;

		if (other == this) {
			return 0;
		}
		return compareTo(entry);
	}

	private int compareTo(Entry entry) {
		long diff = time - entry.time;

		if (diff < 0) {
			return -1;
		} else if (diff > 0) {
			return 1;
		}
		return 0;
	}

	public String toString() {
		return String.format("contract %s", key);
	}
}
