package com.yieldbroker.common.socket.throttle;

import java.util.concurrent.atomic.AtomicLong;

public class ThrottleMonitor {

	private volatile AtomicLong sendCapacity;
	private volatile AtomicLong readCapacity;
	private volatile Throttle sendThrottle;
	private volatile Throttle readThrottle;
	private volatile TimeSlot timeSlot;
	private volatile long sentThisSlot;
	private volatile long readThisSlot;
	private volatile int slotCount;

	public ThrottleMonitor(AtomicLong sendCapacity, AtomicLong readCapacity) {
		this(sendCapacity, readCapacity, 10);
	}

	public ThrottleMonitor(AtomicLong sendCapacity, AtomicLong readCapacity, int slotCount) {
		this.readThrottle = new ReadThrottle();
		this.sendThrottle = new SendThrottle();
		this.sendCapacity = sendCapacity;
		this.readCapacity = readCapacity;
		this.slotCount = slotCount;
	}

	public Throttle getReadThrottle() {
		return readThrottle;
	}

	public Throttle getSendThrottle() {
		return sendThrottle;
	}

	private void sentBytes(long payloadSize) throws InterruptedException {
		TimeSlot currentTimeSlot = currentTimeSlot(sendCapacity);

		if (currentTimeSlot.same(timeSlot)) {
			sentThisSlot += payloadSize;
		} else {
			timeSlot = currentTimeSlot;
			sentThisSlot = payloadSize;
		}
		throttleDelay(currentTimeSlot, sentThisSlot);
	}

	private void readBytes(long payloadSize) throws InterruptedException {
		TimeSlot currentTimeSlot = currentTimeSlot(readCapacity);

		if (currentTimeSlot.same(timeSlot)) {
			readThisSlot += payloadSize;
		} else {
			timeSlot = currentTimeSlot;
			readThisSlot = payloadSize;
		}
		throttleDelay(currentTimeSlot, readThisSlot);
	}

	private void throttleDelay(TimeSlot currentTimeSlot, long bytesThisSlot) throws InterruptedException {
		long slotCapacity = currentTimeSlot.capacityPerSlot();

		if (bytesThisSlot > slotCapacity) {
			long slotsExceeded = bytesThisSlot / slotCapacity;
			long millisPerSlot = 1000 / slotCount;
			long delayInMillis = slotsExceeded * millisPerSlot;

			if (delayInMillis > 0) {
				Thread.sleep(delayInMillis);
			}
		}
	}

	private TimeSlot currentTimeSlot(AtomicLong byteCapacity) {
		long timeInMillis = System.currentTimeMillis();
		long timeInSeconds = timeInMillis / 1000;
		long millisInSecond = timeInMillis - timeInSeconds;
		long timeSlot = millisInSecond / slotCount;
		long capacityPerSecond = byteCapacity.get();
		long capacityPerSlot = capacityPerSecond / slotCount;

		return new TimeSlot(timeInSeconds, timeSlot, capacityPerSecond, capacityPerSlot);
	}

	private class TimeSlot {

		private final long currentSecond;
		private final long currentTimeSlot;
		private final long capacityPerSecond;
		private final long capacityPerSlot;

		public TimeSlot(long currentSecond, long currentTimeSlot, long capacityPerSecond, long capacityPerSlot) {
			this.capacityPerSecond = capacityPerSecond;
			this.capacityPerSlot = capacityPerSlot;
			this.currentSecond = currentSecond;
			this.currentTimeSlot = currentTimeSlot;
		}

		public long capacityPerSlot() {
			return capacityPerSlot;
		}

		public long capacityPerSecond() {
			return capacityPerSecond;
		}

		public boolean same(TimeSlot timeSlot) {
			return sameSecond(timeSlot) && sameSlot(timeSlot);
		}

		public boolean sameSlot(TimeSlot timeSlot) {
			return timeSlot != null && timeSlot.currentTimeSlot == currentTimeSlot;
		}

		public boolean sameSecond(TimeSlot timeSlot) {
			return timeSlot != null && timeSlot.currentSecond == currentSecond;
		}

		public long currentSecond() {
			return currentSecond;
		}

		public long currentSlot() {
			return currentTimeSlot;
		}
	}

	private class ReadThrottle implements Throttle {

		@Override
		public void update(long payloadSize) throws InterruptedException {
			readBytes(payloadSize);
		}
	}

	private class SendThrottle implements Throttle {

		@Override
		public void update(long payloadSize) throws InterruptedException {
			sentBytes(payloadSize);
		}
	}
}