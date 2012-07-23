package com.yieldbroker.common.cache.store;

import com.yieldbroker.common.cache.DataCache;
import com.yieldbroker.common.cache.DataSet;
import com.yieldbroker.common.cache.query.Query;

/** 
 * This provides a simple facade that can be used to query any number
 * of {@link DataCache} instances. Any results matched by the query
 * specified will be returned in the {@link DataSet} result. 
 * 
 * @author Niall Gallagher
 * 
 * @see com.yieldbroker.common.cache.DataCache
 */
public interface DataProvider {
   DataSet query(Query query);
   DataSet query(Query query, long timeout);
}
