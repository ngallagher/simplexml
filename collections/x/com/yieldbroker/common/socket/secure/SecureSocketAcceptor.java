package com.yieldbroker.common.socket.secure;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ServerSocketFactory;

import com.yieldbroker.common.socket.Acceptor;
import com.yieldbroker.common.socket.Connection;
import com.yieldbroker.common.socket.SocketConnection;

public class SecureSocketAcceptor implements Acceptor {

	private final ServerSocketFactory socketFactory;
	private final ServerSocket serverSocket;

	public SecureSocketAcceptor(SecureSocketContext secureContext, int port) throws Exception {
		this.socketFactory = secureContext.getServerSocketFactory();
		this.serverSocket = socketFactory.createServerSocket(port);
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
