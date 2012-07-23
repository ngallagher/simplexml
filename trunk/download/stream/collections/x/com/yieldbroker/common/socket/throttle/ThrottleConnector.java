package com.yieldbroker.common.socket.throttle;

import java.io.IOException;

import com.yieldbroker.common.socket.Connection;
import com.yieldbroker.common.socket.Connector;

public class ThrottleConnector implements Connector {
	
	private final Throttler throttler;
	private final Connector connector;
	
	private ThrottleConnector(Throttler throttler, Connector connector) {
		this.throttler = throttler;
		this.connector = connector;
	}
	
	@Override
	public Connection connect() throws IOException {
		Connection connection = connector.connect();
		return throttler.throttle(connection);
	}
}
