package com.yieldbroker.common.cache.query;


import static java.util.Arrays.asList;

import java.util.Collection;

import com.yieldbroker.common.cache.Data;
import com.yieldbroker.common.time.Time;

public class CategoryLikeQuery implements Query {

	private final Collection<String> patterns;

	public CategoryLikeQuery(String... patterns) {
		this.patterns = asList(patterns);
	}

	public CategoryLikeQuery(Collection<String> patterns) {
		this.patterns = patterns;
	}
	
	@Override
	public boolean matches(Data data, Time updateTime) {
		String category = data.getCategory();

		if (category != null) {
			for (String pattern : patterns) {
				if (category.matches(pattern)) {
					return true;
				}
			}
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CATEGORY-LIKE( ");
		for (String pattern : patterns) {
			builder.append(pattern);
			builder.append(" ");
		}
		builder.append(")");
		return builder.toString();
	}

}
