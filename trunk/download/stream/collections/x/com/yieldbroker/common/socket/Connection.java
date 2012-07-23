package com.yieldbroker.common.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * An connection is acts much like a {@link java.net.Socket} in 
 * that it provides a fully duplex byte stream for exchanging messages
 * with a party. A connection can be a TCP/UDP socket based stream or
 * a simple byte/data queue within an application.  
 * 
 * @author Niall Gallagher
 */
public interface Connection {
	String getAddress();
	InputStream getInputStream() throws IOException;
	OutputStream getOutputStream() throws IOException;
	boolean isReadFailure();
	boolean isWriteFailure();
	boolean isConnected();
	boolean close();
}
