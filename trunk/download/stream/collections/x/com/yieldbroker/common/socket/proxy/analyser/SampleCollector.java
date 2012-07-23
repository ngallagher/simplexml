package com.yieldbroker.common.socket.proxy.analyser;

import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

import com.yieldbroker.common.thread.ThreadPool;
import com.yieldbroker.common.time.DateTime;
import com.yieldbroker.common.time.DateTime.Duration;

@ManagedResource(description="Collects samples from a sample analyser")
public class SampleCollector {

	private final RecordSampler recordSampler;
	private final Executor executor;

	public SampleCollector(SampleAnalyser sampleAnalyser, SampleRecorder sampleRecorder, int sampleFrequency) {
		this.recordSampler = new RecordSampler(sampleAnalyser, sampleRecorder, sampleFrequency);
		this.executor = new ThreadPool();
	}

	@ManagedOperation(description="Starts sample collection")
	public void start() {
		recordSampler.start();
		executor.execute(recordSampler);
	}

	@ManagedOperation(description="Stops sample collection")
	public void stop() {
		recordSampler.stop();
	}

	private static class Record implements Sample {

		private final long averageLatencyNanos;	
		private final long averageSize;
		private final long totalSize;
		private final long relativeTime;
		private final long actualTime;

		public Record(SampleAnalyser sampleAnalyser, Duration recordDuration) {
			this.averageLatencyNanos = sampleAnalyser.getAverageLatencyNanos();
			this.averageSize = sampleAnalyser.getAveragePacketSize();
			this.totalSize = sampleAnalyser.getSumPacketSize();
			this.relativeTime = recordDuration.getDifference();
			this.actualTime = System.currentTimeMillis();
		}
		
		@Override
		public long getActualTime() {
			return actualTime;
		}

		@Override
		public long getRelativeTime() {
			return relativeTime;
		}

		@Override
		public long getAverageLatencyNanos() {
			return averageLatencyNanos;
		}
		
		@Override
		public long getAverageLatencyMicros() {
			return MICROSECONDS.convert(averageLatencyNanos, NANOSECONDS);
		}

		@Override
		public long getAverageLatencyMillis() {
			return MILLISECONDS.convert(averageLatencyNanos, NANOSECONDS);
		}
		
		@Override
		public long getAverageSize() {
			return averageSize;
		}

		@Override
		public long getTotalSize() {
			return totalSize;
		}
	}

	private static class RecordSampler implements Runnable {

		private final SampleAnalyser sampleAnalyser;
		private final SampleRecorder sampleRecorder;
		private final AtomicBoolean collectorActive;
		private final long sampleFrequency;

		public RecordSampler(SampleAnalyser sampleAnalyser, SampleRecorder sampleRecorder, int sampleFrequency) {
			this.collectorActive = new AtomicBoolean();
			this.sampleFrequency = sampleFrequency;
			this.sampleRecorder = sampleRecorder;
			this.sampleAnalyser = sampleAnalyser;
		}
		
		public void start() {
			collectorActive.set(true);			
		}

		public void stop() {
			collectorActive.set(false);
		}

		@Override
		public void run() {
			DateTime startTime = DateTime.now();
			
			while (collectorActive.get()) {
				try {
					DateTime currentTime = DateTime.now();
					Duration recordDuration =  currentTime.timeDifference(startTime);

					recordSample(recordDuration);
					Thread.sleep(sampleFrequency);
				} catch (Exception e) {
					continue;
				}
			}
		}

		private void recordSample(Duration recordDuration) {
			Record record = new Record(sampleAnalyser, recordDuration);
			
			sampleAnalyser.reset();
			sampleRecorder.record(record);			
		}
	}
}
