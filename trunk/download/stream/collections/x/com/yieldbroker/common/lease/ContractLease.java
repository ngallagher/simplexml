package com.yieldbroker.common.lease;

import java.util.concurrent.TimeUnit;

class ContractLease<T> implements Lease<T> {

	private final Controller<T> handler;
	private final Contract<T> contract;

	public ContractLease(Controller<T> handler, Contract<T> contract) {
		this.handler = handler;
		this.contract = contract;
	}

	public long getExpiry(TimeUnit unit) throws LeaseException {
		return contract.getDelay(unit);
	}

	public void renew(long duration, TimeUnit unit) throws LeaseException {
		if (duration >= 0) {
			contract.setDelay(duration, unit);
		}
		handler.renew(contract);
	}

	public void cancel() throws LeaseException {
		handler.cancel(contract);
	}

	public T getKey() {
		return contract.getKey();
	}
}
