package com.yieldbroker.common.cache.query;

import static java.util.Arrays.asList;

import java.util.Collection;

import com.yieldbroker.common.cache.Data;
import com.yieldbroker.common.time.Time;

/** 
 * This can be used to group several queries that must be matched in
 * order to accept a {@link Data} item from a cache. Only one
 * query needs to match for this to match.
 * 
 * @author Niall Gallagher
 */
public class Or implements Query {
	
	private final Collection<Query> queries;
	
	public Or(Query... queries) {
		this.queries = asList(queries);
	}
	
	public Or(Collection<Query> queries) {
		this.queries = queries;
	}
	
	@Override
	public boolean matches(Data data, Time updateTime) {
		for(Query query : queries) {
			if(query.matches(data, updateTime)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("OR( ");
		for (Query query : queries) {
			builder.append(query);
			builder.append(" ");
		}
		builder.append(")");
		return builder.toString();
	}
}
