package com.yieldbroker.common.socket.proxy.analyser;

public interface Sample {
	long getActualTime();
	long getRelativeTime();
	long getAverageLatencyNanos();
	long getAverageLatencyMicros();
	long getAverageLatencyMillis();
	long getAverageSize();
	long getTotalSize();
}
