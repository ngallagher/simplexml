package com.yieldbroker.common.socket.throttle;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

import com.yieldbroker.common.socket.Connection;

@ManagedResource(description="Throttles on a byte per second scale")
public class Throttler {
	
	private final AtomicLong readCapacity;
	private final AtomicLong sendCapacity;
	
	public Throttler(long sendCapacity, long readCapacity) {
		this.readCapacity = new AtomicLong(readCapacity);
		this.sendCapacity = new AtomicLong(sendCapacity);
		
		if(sendCapacity <= 0 || readCapacity <= 0) {
			throw new IllegalArgumentException("Send and read capacity must be greater than zero");
		}
	}
	
	@ManagedAttribute(description="Bytes that can be read in one second")
	public long getReadCapacity() {
		return readCapacity.get();
	}

	@ManagedAttribute(description="Bytes that can be sent in one second")
	public long getSendCapacity() {
		return sendCapacity.get();
	}
	
	@ManagedOperation(description="Throttle number of bytes read per second")
	@ManagedOperationParameters({
		@ManagedOperationParameter(name="byteCount", description="Number of bytes read per second")
	})	
	public void setReadCapacity(long byteCount) {
		if(byteCount <= 0) {
			throw new IllegalArgumentException("Read capacity must greater than zero");
		}
		readCapacity.set(byteCount);
	}
	
	@ManagedOperation(description="Throttle number of bytes sent per second")
	@ManagedOperationParameters({
		@ManagedOperationParameter(name="byteCount", description="Number of bytes sent per second")
	})	
	public void setSendCapacity(long byteCount) {
		if(byteCount <= 0) {
			throw new IllegalArgumentException("Send capacity must greater than zero");
		}
		sendCapacity.set(byteCount);
	}
	
	public Connection throttle(Connection connection) {
		ThrottleMonitor throttleMonitor = new ThrottleMonitor(sendCapacity, readCapacity);
		Connection throttleConnection = new ThrottleConnection(throttleMonitor, connection);
		
		return throttleConnection;
	}
}
