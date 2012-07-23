package com.yieldbroker.common.cache.convert;

import java.util.List;

import com.yieldbroker.common.cache.Data;

public interface DataConverter<T> {
	List<Data> convert(T value);
}
