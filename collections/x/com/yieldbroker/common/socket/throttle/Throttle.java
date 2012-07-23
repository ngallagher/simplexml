package com.yieldbroker.common.socket.throttle;

public interface Throttle {
	void update(long payloadSize) throws InterruptedException;
}
