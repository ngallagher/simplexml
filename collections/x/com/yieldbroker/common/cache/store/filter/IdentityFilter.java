package com.yieldbroker.common.cache.store.filter;

import java.util.Set;

import com.yieldbroker.common.cache.Data;

public class IdentityFilter implements DataFilter<Data> {

	@Override
	public Set<Data> process(Set<Data> dataSet) {
		return dataSet;
	}

}
