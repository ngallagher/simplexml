package com.yieldbroker.common.lease;

import java.util.concurrent.TimeUnit;

/**
 * A lease manager is used to provide {@link Lease} objects that can 
 * be used to manage specific resources. Each lease will own a specific
 * resource, it must be renewed before it expires or it will be released
 * and dispatched to the {@link Cleaner} for disposal.
 * 
 * @author Niall Gallagher
 * 
 * @see com.yieldbroker.common.lease.Lease<T>
 * @see com.yieldbroker.common.lease.Cleaner<T>
 */
public class LeaseManager<T> implements LeaseProvider<T> {

	private Controller<T> handler;

	public LeaseManager(Cleaner<T> cleaner) {
		this.handler = new Maintainer<T>(cleaner);
	}

	public Lease<T> lease(T key, long duration, TimeUnit unit) {
		Contract<T> contract = new Entry<T>(key, duration, unit);

		try {
			handler.issue(contract);
		} catch (Exception e) {
			return null;
		}
		return new ContractLease<T>(handler, contract);
	}

	public void close() {
		try {
			handler.close();
		} catch (Exception e) {
			return;
		}
	}
}
