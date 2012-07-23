package com.yieldbroker.common.cache.query;

import com.yieldbroker.common.cache.Data;
import com.yieldbroker.common.time.Time;

/** 
 * A query is used to acquire data from a {@link DataCache} instance.
 * Any criteria can be used to examine the data item and either accept
 * or reject it based on the logic of the query implementation.
 * 
 * @author Niall Gallagher
 */
public interface Query<T> {	
	boolean matches(Data<T> data, Time updateTime);
}
