package com.yieldbroker.common.socket.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;

import org.apache.log4j.Logger;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

import com.yieldbroker.common.socket.Acceptor;
import com.yieldbroker.common.socket.Connection;
import com.yieldbroker.common.socket.Connector;
import com.yieldbroker.common.thread.ThreadPool;

@ManagedResource(description="Proxy for connections")
public class ProxyServer {

	private static final Logger LOG = Logger.getLogger(ProxyServer.class);

	private final Set<Connection> currentConnections;	
	private final Set<String> blockedAddresses;
	private final ProxyListener proxyListener;
	private final Executor threadPool;
	private final Connector connector;
	private final Acceptor acceptor;	
	
	public ProxyServer(Acceptor acceptor, Connector connector, Proxy proxy) {
		this.currentConnections = new CopyOnWriteArraySet<Connection>();
		this.blockedAddresses = new CopyOnWriteArraySet<String>();
		this.proxyListener = new ProxyListener(proxy);
		this.threadPool = new ThreadPool();
		this.connector = connector;
		this.acceptor = acceptor;
	}	

	@ManagedOperation(description="Start the proxy server")
	public void start() {
		threadPool.execute(proxyListener);
	}
	
	@ManagedOperation(description="Stop the proxy server")
	public void stop() {		
		acceptor.close();
		closeAll();
	}
	
	@ManagedOperation(description="Shows current connections")
	public String showConnections() {
		return currentConnections.toString();
	}
	
	@ManagedOperation(description="Close any connections not connected")
	public void closeNotConnected() {
		for(Connection connection : currentConnections) {
			if(!connection.isConnected()) {
				connection.close();
			}
		}
	}
	
	@ManagedOperation(description="Close all connections")
	public void closeAll() {
		for(Connection connection : currentConnections) {
			connection.close();			
		}
	}	
	
	@ManagedOperation(description="Closes any address that matches")
	@ManagedOperationParameters({
		@ManagedOperationParameter(name="pattern", description="Regular expression")
	})
	public void closeAndBlock(String pattern) {
		for(Connection connection : currentConnections) {
			String address = connection.getAddress();
			
			if(address.matches(pattern)) {
				connection.close();
			}
		}
		blockedAddresses.add(pattern);
	}
	
	@ManagedOperation(description="Removes address from blocked list")
	@ManagedOperationParameters({
		@ManagedOperationParameter(name="pattern", description="Regular expression")
	})	
	public void allowAddress(String pattern) {
		blockedAddresses.remove(pattern);
	}
	
	@ManagedOperation(description="Adds address to blocked list")
	@ManagedOperationParameters({
		@ManagedOperationParameter(name="pattern", description="Regular expression")
	})
	public void blockAddress(String pattern) {
		blockedAddresses.add(pattern);
	}
	
	@ManagedOperation(description="Check if an address is blocked")
	@ManagedOperationParameters({
		@ManagedOperationParameter(name="address", description="Address to match")
	})
	public boolean isBlocked(String address) {
		for(String blockedAddress : blockedAddresses) {
			if(address.matches(blockedAddress)) {
				return true;
			}
		}
		return false;
	}	
	
	private class ProxyListener implements Runnable {
		
		private final Proxy proxy;
		
		public ProxyListener(Proxy proxy) {
			this.proxy = proxy;
		}

		@Override
		public void run() {
			while(acceptor.isConnected()) {
				try {
					Connection source = accept();
					Connection destination = connect();
					
					currentConnections.add(source);
					proxy.connect(source, destination);
				} catch(Exception e) {
					LOG.info("Could not accept connection", e);
				}
			}
		}	
		
		private Connection connect() throws IOException {
			return connector.connect();
		}
		
		private Connection accept() throws IOException {
			while(true) {
				Connection source = acceptor.accept();
				String address = source.getAddress();
				
				if(!isBlocked(address)) {
					return new ProxyConnection(source);
				} else {
					source.close();
				}
			}	
		}		
	}
	
	private class ProxyConnection implements Connection {
		
		private final Connection connection;
		
		public ProxyConnection(Connection connection) {
			this.connection = connection;
		}
		
		@Override
		public String getAddress() {
			return connection.getAddress();
		}

		@Override
		public InputStream getInputStream() throws IOException {
			return connection.getInputStream();
		}

		@Override
		public OutputStream getOutputStream() throws IOException {
			return connection.getOutputStream();
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
			currentConnections.remove(this);	
			return connection.close();
		}
		
		@Override
		public String toString() {
			return connection.toString();
		}
	}
}
