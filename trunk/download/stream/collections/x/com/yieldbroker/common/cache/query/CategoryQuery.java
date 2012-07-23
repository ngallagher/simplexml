package com.yieldbroker.common.cache.query;


import static java.util.Arrays.asList;

import java.util.Collection;

import com.yieldbroker.common.cache.Data;
import com.yieldbroker.common.time.Time;

public class CategoryQuery implements Query {

	private final Collection<String> categories;

	public CategoryQuery(String... categories) {
		this.categories = asList(categories);
	}

	public CategoryQuery(Collection<String> categories) {
		this.categories = categories;
	}
	
	@Override
	public boolean matches(Data data, Time updateTime) {
		String category = data.getCategory();

		if (category != null) {
			for (String catetgoryOption : categories) {
				if (category.equals(catetgoryOption)) {
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
		builder.append("CATEGORY( ");
		for (String category : categories) {
			builder.append(category);
			builder.append(" ");
		}
		builder.append(")");
		return builder.toString();
	}

}
