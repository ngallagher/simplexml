package com.yieldbroker.common.cache.store.filter;

import java.util.Set;

import com.yieldbroker.common.cache.Data;

public interface DataFilter<T extends Data> {
	Set<Data> process(Set<T> dataSet);
}
