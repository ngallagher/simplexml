package com.yieldbroker.common.time;

public class SystemClock {

	public static Time currentTime() {
		long timeInMillis = System.currentTimeMillis();
		long timeInNanos = System.nanoTime();
	
		return new Time(timeInMillis, timeInNanos);
	}
}
