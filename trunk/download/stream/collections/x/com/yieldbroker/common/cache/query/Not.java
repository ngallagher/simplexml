package com.yieldbroker.common.cache.query;

import com.yieldbroker.common.cache.Data;
import com.yieldbroker.common.time.Time;

public class Not implements Query {

	private final Query query;

	public Not(Query query) {
		this.query = query;
	}

	@Override
	public boolean matches(Data data, Time updateTime) {
		return !query.matches(data, updateTime);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NOT( ");
		builder.append(query);
		builder.append(" )");
		return builder.toString();
	}
}
