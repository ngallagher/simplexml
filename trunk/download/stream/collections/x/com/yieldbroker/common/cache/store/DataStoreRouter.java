package com.yieldbroker.common.cache.store;

import java.util.Collections;
import java.util.Map;

import com.yieldbroker.common.cache.Data;

public class DataStoreRouter implements DataRouter {
	
	private final Map<String, DataStore> dataStores;
	
	public DataStoreRouter(Map<String, DataStore> dataStores) {
		this.dataStores = Collections.unmodifiableMap(dataStores);		
	}

	@Override
	public void route(Data data) {
		String dataType = data.getType();
		DataStore dataStore = dataStores.get(dataType);
		
		if(dataStore != null) {
			dataStore.updateData(data);
		}
	}
}
