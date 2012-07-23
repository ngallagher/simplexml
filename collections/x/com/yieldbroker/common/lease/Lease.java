package com.yieldbroker.common.lease;

import java.util.concurrent.TimeUnit;

/**
 * A lease is used to manage a resource by renewing its lease on it.
 * If the lease expires the resource is released an handed to a 
 * {@link Cleaner} implementation to be recycled or disposed.
 * 
 * @author Niall Gallagher
 * 
 * @see com.yieldbroker.common.lease.LeaseManager<T>
 */
public interface Lease<T> {
	long getExpiry(TimeUnit unit);
	void renew(long duration, TimeUnit unit);
	void cancel();
	T getKey();
}
