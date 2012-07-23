package com.yieldbroker.common.cache;

import com.yieldbroker.common.cache.Data;
import com.yieldbroker.common.time.Time;

public interface DataCacheListener {
	void onUpdate(Data data, Time updateTime);
}
