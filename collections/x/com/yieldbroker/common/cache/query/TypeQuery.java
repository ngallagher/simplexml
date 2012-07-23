package com.yieldbroker.common.cache.query;

import static java.util.Arrays.asList;

import java.util.Collection;

import com.yieldbroker.common.cache.Data;
import com.yieldbroker.common.time.Time;

public class TypeQuery implements Query {

	private final Collection<String> dataTypes;

	public TypeQuery(String... dataTypes) {
		this.dataTypes = asList(dataTypes);
	}
	
	public TypeQuery(Collection<String> dataTypes) {
		this.dataTypes = dataTypes;
	}

	@Override
	public boolean matches(Data data, Time updateTime) {
		String dataType = data.getType();

		if (dataType != null) {
			for (String typeOption : dataTypes) {
				if (typeOption.equals(dataType)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TYPE( ");
		for (String type : dataTypes) {
			builder.append(type);
			builder.append(" ");
		}
		builder.append(")");
		return builder.toString();
	}

}
