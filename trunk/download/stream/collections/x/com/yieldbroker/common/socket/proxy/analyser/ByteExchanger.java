package com.yieldbroker.common.socket.proxy.analyser;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import com.yieldbroker.common.socket.Connection;
import com.yieldbroker.common.time.SystemClock;
import com.yieldbroker.common.time.Time;

/**
 * A byte exchanger acts as a pipe transferring an input stream to an
 * output stream asynchronously. Each packet exchanged results in an
 * update to the {@link Analyser} which can record the traffic and
 * perform performance measurements such as latency and throughput.
 * 
 * @author Niall Gallagher
 * 
 * @see com.yieldbroker.common.socket.proxy.analyser.AnalyserProxy
 */
public class ByteExchanger implements Runnable {
	
	private static final Logger LOG = Logger.getLogger(ByteExchanger.class);
	
	private final Connection source;
	private final Connection destination;
	private final Analyser analyser;
	
	public ByteExchanger(Connection source, Connection destination, Analyser analyser) {
		this.destination = destination;
		this.analyser = analyser;
		this.source = source;
	}

	@Override
	public void run() {
		try {
			renameThread();
			exchangeBytes();
		} catch(Throwable cause) {
			reportFailure(cause);
		} finally {
			close(source);
			close(destination);
		}
	}
	
	private void exchangeBytes() throws Exception {
		InputStream sourceStream = source.getInputStream();
		OutputStream destinationStream = destination.getOutputStream();
		byte[] buffer = new byte[2048];
		int count = 0;
		
		while((count = sourceStream.read(buffer)) != -1) {
			Time startTime = SystemClock.currentTime();
			
			try {
				destinationStream.write(buffer, 0, count);
			} finally {
				Time finishTime = SystemClock.currentTime();
				analyser.analyse(startTime, finishTime, count);
			}
		}
		LOG.info("Finished reading from source '" + source + "'");
	}	
	
	private void reportFailure(Throwable cause) {
		if(destination.isWriteFailure()) {
			LOG.info("Error writing to destination '" + destination + "'");
		}
		if(source.isReadFailure()) {
			LOG.info("Error reading from source '" + source + "'");
		}
		LOG.info("Error during byte exchange", cause);
	}
	
	private void renameThread() {
		Thread exchangeThread = Thread.currentThread();
		String fromAddress = source.getAddress();
		String toAddress = destination.getAddress();
		String threadName = String.format("%s -> %s", fromAddress, toAddress);
		
		exchangeThread.setName(threadName);		
	}
	
	private void close(Connection connection) {
		try {			
			connection.close();
		} catch(Exception e) {
			LOG.info("Could not close connection", e);
		}
	}
}
