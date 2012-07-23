package com.yieldbroker.common.socket.proxy.analyser;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

import com.yieldbroker.common.time.SampleAverager;
import com.yieldbroker.common.time.Time;

@ManagedResource(description="Analyser for byte streams")
public class SampleAnalyser implements Analyser {

	private final SampleAverager packetLatencyNanos;
	private final SampleAverager packetLatencyMillis;
	private final SampleAverager packetSize;
	
	public SampleAnalyser() {
		this.packetLatencyNanos = new SampleAverager();
		this.packetLatencyMillis = new SampleAverager();
		this.packetSize = new SampleAverager();
	}
	
	@ManagedAttribute(description="The number of samples taken")
	public long getSampleSize() {
		return packetLatencyNanos.count();
	}
	
	@ManagedAttribute(description="The average latency in nanoseconds")
	public long getAverageLatencyNanos() {
		return packetLatencyNanos.average();
	}
	
	@ManagedAttribute(description="The maximum latency in nanoseconds")
	public long getMaximumLatencyNanos() {
		return packetLatencyNanos.maximum();
	}
	
	@ManagedAttribute(description="The minimum latency in nanoseconds")
	public long getMinimumLatencyNanos() {
		return packetLatencyNanos.minimum();
	}

	@ManagedAttribute(description="The average latency in milliseconds")
	public long getAverageLatencyMillis() {
		return packetLatencyMillis.average();
	}
	
	@ManagedAttribute(description="The maximum latency in milliseconds")
	public long getMaximumLatencyMillis() {
		return packetLatencyMillis.maximum();
	}
	
	@ManagedAttribute(description="The minimum latency in milliseconds")
	public long getMinimumLatencyMillis() {
		return packetLatencyMillis.minimum();
	}	
	
	@ManagedAttribute(description="The average packet size")
	public long getAveragePacketSize() {
		return packetSize.average();
	}
	
	@ManagedAttribute(description="The maximum packet size")
	public long getMaximumPacketSize() {
		return packetSize.maximum();
	}
	
	@ManagedAttribute(description="The minimum packet size")
	public long getMinimumPacketSize() {
		return packetSize.minimum();
	}	

	@ManagedAttribute(description="The total number of bytes sent")
	public long getSumPacketSize() {
		return packetSize.sum();
	}
	
	@ManagedOperation(description="Resent the analyser")
	public void reset() {
		packetLatencyNanos.reset();
		packetLatencyMillis.reset();
		packetSize.reset();		
	}

	@Override
	public void analyse(Time startTime, Time endTime, int payloadSize) {
		long startTimeNanos = startTime.getNanoTime();
		long startTimeMillis = startTime.getMillisTime();
		long endTimeNanos = endTime.getNanoTime();
		long endTimeMillis = endTime.getMillisTime();
		
		packetLatencyNanos.sample(endTimeNanos - startTimeNanos);
		packetLatencyMillis.sample(endTimeMillis - startTimeMillis);
		packetSize.sample(payloadSize);
		
	}

}
