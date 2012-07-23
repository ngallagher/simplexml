package com.yieldbroker.common.lease;

interface Controller<T> {
	void issue(Contract<T> contract) throws LeaseException;
	void renew(Contract<T> contract) throws LeaseException;
	void cancel(Contract<T> contract) throws LeaseException;
	void close();
}
