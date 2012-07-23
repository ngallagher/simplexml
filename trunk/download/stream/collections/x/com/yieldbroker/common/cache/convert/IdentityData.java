package com.yieldbroker.common.cache.convert;

import com.yieldbroker.common.cache.Data;

public class IdentityData<T> implements Data<T> {
	
	private final String dataType;
	private final String category;
	private final T value;
	
	public IdentityData(T value, String dataType) {
		this(value, dataType, null);
	}
	
	public IdentityData(T value, String dataType, String category) {
		this.value = value;
		this.dataType = dataType;
		this.category = category;
	}

	@Override
	public String getCategory() {
		return category;
	}

	@Override
	public Object getKey() {
		return value;
	}

	@Override
	public String getType() {
		return dataType;
	}

	@Override
	public T getValue() {
		return value;
	}
}