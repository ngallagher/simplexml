package com.yieldbroker.common.cache.convert;

import java.util.Arrays;
import java.util.List;

import com.yieldbroker.common.cache.Data;

public class IdentityDataConverter<T> implements DataConverter<T> {

	private final String dataType;
	
	public IdentityDataConverter(String dataType) {
		this.dataType = dataType;
	}
	
	@Override
	public List<Data> convert(T message) {
		Data messageData = new IdentityData<T>(message, dataType);
		return Arrays.asList(messageData);
	}	
}
