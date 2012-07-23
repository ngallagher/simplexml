package com.yieldbroker.common.lease;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

class LeaseCleaner<T> extends Thread {

	private ContractQueue<T> queue;
	private Cleaner<T> cleaner;
	private volatile boolean dead;

	public LeaseCleaner(Cleaner<T> cleaner) {
		this.queue = new ContractQueue<T>();
		this.cleaner = cleaner;
		this.start();
	}

	public boolean revoke(Contract<T> contract) throws LeaseException {
		if (dead) {
			throw new LeaseException("Lease can not be revoked");
		}
		return queue.remove(contract);
	}

	public boolean issue(Contract<T> contract) throws LeaseException {
		if (dead) {
			throw new LeaseException("Lease can not be issued");
		}
		return queue.offer(contract);
	}

	public void run() {
		while (!dead) {
			try {
				clean();
			} catch (Throwable e) {
				continue;
			}
		}
		purge();
	}

	private void clean() throws Exception {
		Contract<T> next = queue.take();
		T key = next.getKey();

		if (key != null) {
			cleaner.clean(key);
		}
	}

	private void purge() {
		for (Contract<T> next : queue) {
			T key = next.getKey();

			try {
				next.setDelay(0L, NANOSECONDS);
				cleaner.clean(key);
			} catch (Throwable e) {
				continue;
			}
		}
	}

	public void close() {
		dead = true;
		interrupt();
	}
}
