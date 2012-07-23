package com.yieldbroker.common.cache.query;

import java.util.Collection;

import com.yieldbroker.common.time.Time;

/** 
 * A query builder implementation is used to create a {@link Query} with
 * an array of criteria. This is provided for convenience.
 * 
 * @author Niall Gallagher
 */
public interface QueryBuilder {
	QueryBuilder clear();
	QueryBuilder matches(Query query);
	QueryBuilder instanceOf(Class... types);
	QueryBuilder instanceOf(Collection<Class> types);
	QueryBuilder typeOf(String... types);
	QueryBuilder typeOf(Collection<String> types);
	QueryBuilder categoryOf(String... categories);
	QueryBuilder categoryOf(Collection<String> categories);
	QueryBuilder categoryLike(String... patterns);
	QueryBuilder categoryLike(Collection<String> patterns);
	QueryBuilder updateAfter(long time);
	QueryBuilder updateAfter(Time time);
	QueryBuilder updateBefore(long time);
	QueryBuilder updateBefore(Time time);
	QueryBuilder group(int count);
	QueryBuilder group();
	QueryBuilder and();
	QueryBuilder or();
	QueryBuilder not();
	Query build();	
}
