package com.yieldbroker.common.socket;

import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class SocketConnection implements Connection {

	private final AtomicInteger readFailure;
	private final AtomicInteger writeFailure;
	private final Socket socket;
	
	public SocketConnection(Socket socket) {
		this.readFailure = new AtomicInteger();
		this.writeFailure = new AtomicInteger();
		this.socket = socket;
	}
	
	@Override
	public String getAddress() {
		return socket.toString();
	}	
	
	@Override
	public InputStream getInputStream() throws IOException {
		InputStream in = socket.getInputStream();
		
		if(in == null) {
			throw new IOException("Count not get input stream");
		}
		return new ConnectionInputStream(in);
	}
	
	@Override
	public OutputStream getOutputStream() throws IOException {
		OutputStream out = socket.getOutputStream();
		
		if(out == null) {
			throw new IOException("Count not get output stream");
		}
		return new ConnectionOutputStream(out);
	}
	
	@Override
	public boolean isReadFailure() {
		return readFailure.get() > 0;
	}
	
	@Override
	public boolean isWriteFailure() {
		return writeFailure.get() > 0;
	}
	
	@Override
	public boolean isConnected() {
		return socket.isConnected() && !socket.isClosed();
	}
	
	@Override
	public boolean close() {
		try {
			socket.close();
		} catch(Exception e) {
			return !isConnected();
		}
		return true;
	}
	
	@Override
	public String toString() {
		return socket.toString();
	}
	
	private class ConnectionInputStream extends FilterInputStream {

		public ConnectionInputStream(InputStream in) {
			super(in);
		}
		
		@Override
		public int read() throws IOException {
			try {
				return in.read();
			} catch(IOException cause) {
				readFailure.getAndIncrement();
				throw cause;
			}			
		}
		
		@Override
		public int read(byte[] buf, int off, int len) throws IOException {
			try {
				return in.read(buf, off, len);
			} catch(IOException cause) {
				readFailure.getAndIncrement();
				throw cause;
			}			
		}
		
		@Override
		public int available() throws IOException {
			try {
				return in.available();
			} catch(IOException cause) {
				readFailure.getAndIncrement();
				throw cause;
			}			
		}
	}
	
	private class ConnectionOutputStream extends FilterOutputStream {

		public ConnectionOutputStream(OutputStream out) {
			super(out);
		}
		
		@Override
		public void write(int octet) throws IOException {
			try {
				out.write(octet);
			} catch(IOException cause) {
				writeFailure.getAndIncrement();
				throw cause;
			}
		}

		@Override
		public void write(byte[] buf, int off, int len) throws IOException {
			try {
				out.write(buf, off, len);
			} catch(IOException cause) {
				writeFailure.getAndIncrement();
				throw cause;
			}
		}
		
		@Override
		public void flush() throws IOException {
			try {
				out.flush();
			} catch(IOException cause) {
				writeFailure.getAndIncrement();
				throw cause;
			}
		}
	}
}
