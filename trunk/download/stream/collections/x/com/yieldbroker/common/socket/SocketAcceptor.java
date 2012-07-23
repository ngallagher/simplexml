package com.yieldbroker.common.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketAcceptor implements Acceptor {
	
	private final ServerSocket serverSocket;
	
	public SocketAcceptor(int port) throws IOException {
		this.serverSocket = new ServerSocket(port);
	}

	@Override
	public Connection accept() throws IOException {
		Socket socket = serverSocket.accept();		
		return new SocketConnection(socket);
	}
	
	@Override
	public boolean isConnected() {
		return !serverSocket.isClosed();
	}

	@Override
	public boolean close() {
		try {
			serverSocket.close();
		} catch(Exception e) {
			return !isConnected();
		}
		return true;		
	}
}
