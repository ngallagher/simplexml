package com.yieldbroker.common.cache.convert.mock;

import java.util.Set;
import java.util.concurrent.Executor;

import org.apache.log4j.Logger;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

import com.yieldbroker.common.cache.convert.DataConsumer;
import com.yieldbroker.common.thread.ThreadPool;

@ManagedResource(description="Generator of mock data")
public class MockDataGenerator {
	
	private static final Logger LOG = Logger.getLogger(MockDataGenerator.class);

	private MockDataUpdater mockDataUpdater;
	private MockDataSource mockDataSource;
	private DataConsumer dataConsumer;
	private volatile long nextSilentPeriod;
	private volatile long silentPeriod;
	private volatile int dataSize;
	
	public MockDataGenerator(MockDataSource mockDataSource, DataConsumer dataConsumer, int dataSize, long updateFrequency, long silentPeriod) {
		this.mockDataUpdater = new MockDataUpdater(this, updateFrequency);
		this.nextSilentPeriod = System.currentTimeMillis() + silentPeriod;
		this.mockDataSource = mockDataSource;
		this.dataConsumer = dataConsumer;
		this.silentPeriod = silentPeriod;
		this.dataSize = dataSize;
	}
	
	@ManagedAttribute(description="Determines if the generator is running")
	public boolean isRunning() {
		return mockDataUpdater.isRunning();
	}
	
	@ManagedAttribute(description="Frequency with which data is generated")
	public long getUpdateFrequency() {
		return mockDataUpdater.getUpdateFrequency();
	}
	
	@ManagedOperation(description="Frequency with which data is generated")
	@ManagedOperationParameters({
		@ManagedOperationParameter(name="updateFrequency", description="Frequency in milliseconds")		
	})	
	public void setUpdateFrequency(long updateFrequency) {
		mockDataUpdater.setUpdateFrequency(updateFrequency);
	}	
	
	@ManagedAttribute(description="Optional silence period")
	public long getSilentPeriod() {
		return silentPeriod;
	}	

	@ManagedOperation(description="Optional silence period")
	@ManagedOperationParameters({
		@ManagedOperationParameter(name="silentPeriod", description="Silent period in milliseconds")		
	})	
	public void setSilentPeriod(long silentPeriod) {
		this.silentPeriod = silentPeriod;
	}
	
	@ManagedAttribute(description="Number of mock data values to create")
	public int getDataSize() {
		return dataSize;
	}
	
	@ManagedOperation(description="Number of mock data values to create")
	@ManagedOperationParameters({
		@ManagedOperationParameter(name="dataSize", description="Size of mock data batches")		
	})	
	public void setDataSize(int dataSize) {
		this.dataSize = dataSize;
	}
	
	@ManagedOperation(description="Start the generator")
	public void startGenerator() {
		mockDataUpdater.start();
	}	
	
	@ManagedOperation(description="Stop the generator")
	public void stopGenerator() {
		mockDataUpdater.stop();
	}
	
	@SuppressWarnings("unchecked")
	private void createMockData() {		
		Set<Object> mockData = mockDataSource.createMockData(dataSize);
		long currentTime = System.currentTimeMillis();
		
		if(nextSilentPeriod <= currentTime) {
			try {
				if(silentPeriod > 0) {
					Thread.sleep(silentPeriod);
				}
			} catch(Exception e) {
				LOG.info("Update thread has been interrupted", e);
			}
			nextSilentPeriod = System.currentTimeMillis() + silentPeriod;
		}
		for(Object mockMessage : mockData) {
			dataConsumer.consume(mockMessage);
		}	
	}
	
	public static class MockDataUpdater implements Runnable {
		
		private MockDataGenerator mockDataGenerator;
		private Executor mockDataExecutor;
		private volatile long updateFrequency;
		private volatile boolean stopRequested;
		private volatile boolean currentlyRunning;
		
		public MockDataUpdater(MockDataGenerator mockDataGenerator, long updateFrequency) {
			this.mockDataExecutor = new ThreadPool();
			this.mockDataGenerator = mockDataGenerator;
			this.updateFrequency = updateFrequency;
		}
		
		public void start() {
			stopRequested = false;
			currentlyRunning = true;
			mockDataExecutor.execute(this);
		}
		
		public void stop() {
			stopRequested = true;
		}
		
		public boolean isRunning() {
			return currentlyRunning;
		}
		
		public long getUpdateFrequency() {
			return updateFrequency;
		}
		
		public void setUpdateFrequency(long updateFrequency) {
			this.updateFrequency = updateFrequency;
		}
		
		public void run() {	
			try {
				while(!stopRequested) {
					mockDataGenerator.createMockData();
					
					if(updateFrequency > 0) {
						Thread.sleep(updateFrequency);
					}
				}
			} catch(Exception e) {
				LOG.info("Could not create mock data", e);
			} finally {
				currentlyRunning = false;
			}
		}
	}
}
