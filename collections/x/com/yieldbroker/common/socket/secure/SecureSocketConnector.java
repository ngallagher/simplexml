package com.yieldbroker.common.socket.secure;

import java.io.IOException;
import java.net.Socket;

import javax.net.SocketFactory;

import com.yieldbroker.common.socket.Connection;
import com.yieldbroker.common.socket.Connector;
import com.yieldbroker.common.socket.SocketConnection;

public class SecureSocketConnector implements Connector {

	private final SocketFactory socketFactory;
	private final String host;
	private final int port;

	public SecureSocketConnector(SecureSocketContext secureContext, String host, int port) throws Exception {
		this.socketFactory = secureContext.getSocketFactory();
		this.host = host;
		this.port = port;
	}

	@Override
	public Connection connect() throws IOException {
		Socket socket = socketFactory.createSocket(host, port);
		return new SocketConnection(socket);
	}
}
