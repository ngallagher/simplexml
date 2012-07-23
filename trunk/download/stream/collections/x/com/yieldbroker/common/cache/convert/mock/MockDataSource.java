package com.yieldbroker.common.cache.convert.mock;

import java.util.Set;

public interface MockDataSource<T> {	
	Set<T> createMockData(int preferredSize);
}