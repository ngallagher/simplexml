package com.yieldbroker.common.socket.proxy.analyser;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.yieldbroker.common.socket.Connection;
import com.yieldbroker.common.socket.proxy.Proxy;

public class AnalyserProxy implements Proxy {

	private final Analyser inputAnalyser;
	private final Analyser outputAnalyser;
	private final Executor executor;
	
	public AnalyserProxy(Analyser inputAnalyser, Analyser outputAnalyser) {
		this.executor = Executors.newCachedThreadPool();
		this.inputAnalyser = inputAnalyser;
		this.outputAnalyser = outputAnalyser;
	}
	
	@Override
	public void connect(Connection source, Connection destination) {
		ByteExchanger sourceDestination = new ByteExchanger(source, destination, inputAnalyser);
		ByteExchanger destinationSource = new ByteExchanger(destination, source, outputAnalyser);
		
		executor.execute(sourceDestination);
		executor.execute(destinationSource);
	}
}
