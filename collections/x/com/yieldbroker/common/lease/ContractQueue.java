package com.yieldbroker.common.lease;

import java.util.concurrent.DelayQueue;

class ContractQueue<T> extends DelayQueue<Contract<T>> {

	public ContractQueue() {
		super();
	}
}
