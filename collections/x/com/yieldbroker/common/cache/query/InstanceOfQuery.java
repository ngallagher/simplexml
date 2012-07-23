package com.yieldbroker.common.cache.query;

import static java.util.Arrays.asList;

import java.util.Collection;

import com.yieldbroker.common.cache.Data;
import com.yieldbroker.common.time.Time;

public class InstanceOfQuery implements Query {

	private final Collection<Class> types;

	public InstanceOfQuery(Class... types) {
		this.types = asList(types);
	}

	public InstanceOfQuery(Collection<Class> types) {
		this.types = types;
	}

	@Override
	public boolean matches(Data data, Time updateTime) {
		Object instance = data.getValue();

		for (Class type : types) {
			return type.isInstance(instance);
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("INSTANCEOF( ");
		for (Class type : types) {
			String name = type.getSimpleName();
			builder.append(name);
			builder.append(" ");
		}
		builder.append(")");
		return builder.toString();
	}
}
