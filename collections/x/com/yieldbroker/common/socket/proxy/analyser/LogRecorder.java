package com.yieldbroker.common.socket.proxy.analyser;

import org.apache.log4j.Logger;

public class LogRecorder implements SampleRecorder {

	private static final Logger LOG = Logger.getLogger(LogRecorder.class);
	
	private final String tag;
	
	public LogRecorder(String tag) {
		this.tag = tag;
	}

	@Override
	public void record(Sample sample) {
		StringBuilder builder = new StringBuilder();

		long relativeTime = sample.getRelativeTime();
		long actualTime = sample.getActualTime();
		long latencyMillis = sample.getAverageLatencyMillis();
		long latencyMicros = sample.getAverageLatencyMicros();
		long averageSize = sample.getAverageSize();
		long totalSize = sample.getTotalSize();

		builder.append(tag);
		builder.append(" relative-time=").append(relativeTime);
		builder.append(" actual-time=").append(actualTime);
		builder.append(" latency-millis=").append(latencyMillis);
		builder.append(" latency-micros=").append(latencyMicros);
		builder.append(" average-size=").append(averageSize);
		builder.append(" total-size=").append(totalSize);		

		LOG.info(builder);
	}
}
