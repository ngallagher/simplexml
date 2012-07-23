package com.yieldbroker.common.lease;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

class Maintainer<T> implements Controller<T> {

	private LeaseCleaner<T> queue;

	public Maintainer(Cleaner<T> cleaner) {
		this.queue = new LeaseCleaner<T>(cleaner);
	}

	public synchronized void issue(Contract<T> contract) throws LeaseException {
		queue.issue(contract);
	}

	public synchronized void renew(Contract<T> contract) throws LeaseException {
		boolean active = queue.revoke(contract);

		if (!active) {
			throw new LeaseException("Lease has expired for %s", contract);
		}
		queue.issue(contract);
	}

	public synchronized void cancel(Contract<T> contract) throws LeaseException {
		boolean active = queue.revoke(contract);

		if (!active) {
			throw new LeaseException("Lease has expired for %s", contract);
		}
		contract.setDelay(0, MILLISECONDS);
		queue.issue(contract);
	}

	public synchronized void close() {
		queue.close();
	}
}
