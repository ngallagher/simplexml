package com.yieldbroker.common.time;

public class StopWatch {

	private SampleAverager sampler;
	private long startTime;
	private long timeElapsed;
	
	public StopWatch() {
		this.sampler = new SampleAverager();
	}

	public long samples() {
		return sampler.count();
	}

	public long average() {
		return sampler.average();
	}

	public long longest() {
		return sampler.maximum();
	}

	public long elapsed() {
		if(startTime > 0) {
			return System.currentTimeMillis() - startTime;
		}
		return timeElapsed;
	}

	public long shortest() {
		return sampler.minimum();
	}

	public void start() {
		if(startTime > 0) {
			throw new IllegalStateException("Stop watch has already been started");
		}
		timeElapsed = 0;
		startTime = System.currentTimeMillis();
	}

	public void stop() {
		if (startTime <= 0) {
			throw new IllegalStateException("Stop watch was Not started");
		}
		timeElapsed = System.currentTimeMillis() - startTime;
		startTime = 0;
		sampler.sample(timeElapsed);		
	}
}
