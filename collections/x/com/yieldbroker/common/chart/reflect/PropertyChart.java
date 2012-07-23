package com.yieldbroker.common.chart.reflect;

import com.yieldbroker.common.chart.Axis;
import com.yieldbroker.common.chart.Chart;
import com.yieldbroker.common.chart.Series;

public class PropertyChart implements Chart {
	
	private final PropertySeries x;
	private final PropertySeries y;
	
	public PropertyChart(Series series, String x, String y, Class type) {
		this.x = new PropertySeries(series, x, type);
		this.y = new PropertySeries(series, y, type);
	}

	@Override
	public String getName(Axis axis) {
		if(axis == Axis.X) {
			return x.getName();
		}
		return y.getName();
	}

	@Override
	public Series getSeries(Axis axis) {
		if(axis == Axis.X) {
			return x;
		}
		return y;
	}
}
