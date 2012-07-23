package com.yieldbroker.common.socket.secure;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

public class SecureSocketContext {

	private final X509TrustManager trustManager;
	private final X509TrustManager[] trustManagers;
	private final KeyStoreReader keyStoreReader;
	private final SecureProtocol secureProtocol;

	public SecureSocketContext(KeyStoreReader keyStoreReader, SecureProtocol secureProtocol) {
		this.trustManager = new AnonymousTrustManager();
		this.trustManagers = new X509TrustManager[]{trustManager};
		this.keyStoreReader = keyStoreReader;
		this.secureProtocol = secureProtocol;
	}

	public SocketFactory getSocketFactory() throws Exception {
		KeyManager[] keyManagers = keyStoreReader.getKeyManagers();
		SSLContext secureContext = secureProtocol.getContext();
		
		secureContext.init(keyManagers, trustManagers, null);      
	    
		return secureContext.getSocketFactory();
	}
	
	public ServerSocketFactory getServerSocketFactory() throws Exception {
		KeyManager[] keyManagers = keyStoreReader.getKeyManagers();
		SSLContext secureContext = secureProtocol.getContext();
		
		secureContext.init(keyManagers, trustManagers, null);      
	    
		return secureContext.getServerSocketFactory();
	}
}
