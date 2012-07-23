package com.yieldbroker.common.cache.convert;

import static java.util.Collections.unmodifiableMap;

import java.util.List;
import java.util.Map;

import com.yieldbroker.common.cache.Data;
import com.yieldbroker.common.cache.store.DataRouter;

public class DataConsumer {

	private final Map<Class, DataConverter> dataConverters;
	private final DataRouter dataRouter;
	
	public DataConsumer(DataRouter dataRouter, Map<Class, DataConverter> dataConverters) {
		this.dataConverters = unmodifiableMap(dataConverters);
		this.dataRouter = dataRouter;
	}
	
	public void consume(Object anything) {
		if(anything != null) {
			Class type = anything.getClass();
			DataConverter converter = dataConverters.get(type);;
			
			if(converter == null) {
				throw new IllegalArgumentException("No message converter could be found for " + type);
			}
			List<Data> dataList = converter.convert(anything);
					
			for(Data data : dataList) {
				dataRouter.route(data);				
			}
		}
	}
}
