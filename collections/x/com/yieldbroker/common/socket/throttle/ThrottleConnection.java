package com.yieldbroker.common.socket.throttle;

import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.yieldbroker.common.socket.Connection;

public class ThrottleConnection implements Connection {

	private final Throttle readThrottle;
	private final Throttle sendThrottle;
	private final Connection connection;
	
	public ThrottleConnection(ThrottleMonitor throttleMonitor, Connection connection) {
		this.readThrottle = throttleMonitor.getReadThrottle();
		this.sendThrottle = throttleMonitor.getSendThrottle();
		this.connection = connection;
	}		

	@Override
	public String getAddress() {
		return connection.getAddress();
	}

	@Override
	public InputStream getInputStream() throws IOException {
		InputStream in = connection.getInputStream();
		
		if(in == null) {
			throw new IOException("Coult not acquire input stream"); 
		}
		return new ThrottleInputStream(in);
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		OutputStream out = connection.getOutputStream();
		
		if(out == null) {
			throw new IOException("Coult not acquire input stream"); 
		}
		return new ThrottleOutputStream(out);
	}

	@Override
	public boolean isReadFailure() {
		return connection.isReadFailure();
	}

	@Override
	public boolean isWriteFailure() {
		return connection.isWriteFailure();
	}

	@Override
	public boolean isConnected() {
		return connection.isConnected();
	}

	@Override
	public boolean close() {
		return connection.close();
	}
	
	private class ThrottleInputStream extends FilterInputStream {

		public ThrottleInputStream(InputStream in) {
			super(in);
		}
		
		@Override
		public int read() throws IOException {
			int octet = in.read();
			
			try {
				if(octet > 0) {
					readThrottle.update(1);
				}
			} catch(InterruptedException e) {
				throw new IllegalThreadStateException("Thread has been interrupted");
			}
			return octet;
			
		}
		
		@Override
		public int read(byte[] buf, int off, int len) throws IOException {
			int read = in.read(buf, off, len);
			
			try {
				if(read > 0) {
					readThrottle.update(read);
				}
			} catch(InterruptedException e) {
				throw new IllegalThreadStateException("Thread has been interrupted");
			}
			return read;
		}
		
		public long skip(long skip) throws IOException {
			long skipped = in.skip(skip);
			
			try {
				readThrottle.update(skipped);
			} catch(InterruptedException e) {
				throw new IllegalThreadStateException("Thread has been interrupted");
			}
			return skipped;
		}
	}
	
	private class ThrottleOutputStream extends FilterOutputStream {

		public ThrottleOutputStream(OutputStream out) {
			super(out);
		}
		
		@Override
		public void write(int octet) throws IOException {
			out.write(octet);
			
			try {
				sendThrottle.update(1);				
			} catch(InterruptedException e) {
				throw new IllegalThreadStateException("Thread has been interrupted");
			}
		}

		@Override
		public void write(byte[] buf, int off, int len) throws IOException {
			out.write(buf, off, len);
			
			try {
				sendThrottle.update(len);				
			} catch(InterruptedException e) {
				throw new IllegalThreadStateException("Thread has been interrupted");
			}
		}
	}
}
