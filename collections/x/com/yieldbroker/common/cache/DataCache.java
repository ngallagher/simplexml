package com.yieldbroker.common.cache;

import java.util.Set;

import com.yieldbroker.common.cache.query.Query;

/** 
 * Provides a simple cache that can be queries using a set of query 
 * objects implementing the {@link Query} interface. Realtime updates to
 * the cache can be monitored with a {@link DataCacheListener}.
 * 
 * @author Niall Gallagher
 */
public interface DataCache {
	Set<Data> query(Query query);
	void updateData(Data data);
	void updateData(Set<Data> dataBatch);
	void registerListener(DataCacheListener cacheListener);
	void removeListener(DataCacheListener cacheListener);

}
