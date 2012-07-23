package com.yieldbroker.common.socket;

import java.io.IOException;
import java.net.Socket;

public class SocketConnector implements Connector {
	
	private final String host;
	private final int port;

	public SocketConnector(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	@Override
	public Connection connect() throws IOException {
		Socket socket = new Socket(host, port);		
		return new SocketConnection(socket);
	}
}