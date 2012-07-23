package com.yieldbroker.common.socket.throttle;

import java.io.IOException;

import com.yieldbroker.common.socket.Acceptor;
import com.yieldbroker.common.socket.Connection;

public class ThrottleAcceptor implements Acceptor {
	
	private final Throttler throttler;
	private final Acceptor acceptor;
	
	private ThrottleAcceptor(Throttler throttler, Acceptor acceptor) {
		this.throttler = throttler;
		this.acceptor = acceptor;
	}

	@Override
	public Connection accept() throws IOException {
		Connection connection = acceptor.accept();
		return throttler.throttle(connection);
	}

	@Override
	public boolean isConnected() {
		return acceptor.isConnected();
	}

	@Override
	public boolean close() {
		return acceptor.close();
	}

}
