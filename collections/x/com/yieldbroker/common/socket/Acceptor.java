package com.yieldbroker.common.socket;

import java.io.IOException;

/**
 * An acceptor is acts much like a {@link java.net.ServerSocket} in
 * that it listens for incoming connections and accepts them. 
 * 
 * @author Niall Gallagher
 */
public interface Acceptor {
	Connection accept() throws IOException;
	boolean isConnected();
	boolean close();
}
