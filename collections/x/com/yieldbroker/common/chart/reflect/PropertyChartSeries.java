package com.yieldbroker.common.chart.reflect;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.yieldbroker.common.chart.Chart;
import com.yieldbroker.common.chart.ChartSeries;
import com.yieldbroker.common.reflect.Accessor;
import com.yieldbroker.common.reflect.PropertyAccessor;

public class PropertyChartSeries implements ChartSeries {
	
	private final Map<String, Accessor> accessors;
	private final Object source;
	private final Class type;
	
	public PropertyChartSeries(Object source) {
		this.accessors = new ConcurrentHashMap<String, Accessor>();
		this.type = source.getClass();
		this.source = source;
	}

	@Override
	public Chart getChart(String name) {
		try {
			Accessor chartAccessor = getAccessor(name);
			Object chart = chartAccessor.getValue(source);
			
			return (Chart) chart;
		} catch(Exception e) {
			throw new IllegalStateException(e);
		}
	}
	
	private Accessor getAccessor(String name) throws Exception {
		Accessor accessor = accessors.get(name);
		
		if(accessor == null) {
			accessor = new PropertyAccessor(name, type);
			accessors.put(name,  accessor);
		}
		return accessor;
	}
}
