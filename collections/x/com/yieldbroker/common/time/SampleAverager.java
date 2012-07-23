package com.yieldbroker.common.time;

public class SampleAverager {

	private long maximum;
	private long minimum;
	private long count;
	private long sum;
	
	public synchronized long sum() {
		return sum;
	}

	public synchronized long count() {
		return count;
	}

	public synchronized long maximum() {
		return maximum;
	}

	public synchronized long minimum() {
		return minimum;
	}

	public synchronized long average() {
		if (count > 0) {
			return sum / count;
		}
		return 0;
	}
	
	public synchronized void reset() {
		maximum = 0;
		minimum = 0;
		count = 0;
		sum = 0;
	}

	public synchronized void sample(long sample) {
		if (sample > maximum) {
			maximum = sample;
		}
		if (sample < minimum) {
			minimum = sample;
		}
		if (minimum == 0) {
			minimum = sample;
		}
		sum += sample;
		count++;
	}
}
