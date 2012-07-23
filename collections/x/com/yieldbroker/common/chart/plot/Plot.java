package com.yieldbroker.common.chart.plot;

import com.yieldbroker.common.chart.Axis;
import com.yieldbroker.common.chart.Chart;
import com.yieldbroker.common.chart.ObjectConverter;
import com.yieldbroker.common.chart.Series;

public class Plot implements Chart {
	
	private final ObjectConverter converter;
	private final Chart chart;
	private final String name;
	
	public Plot(Chart chart, String name) {
		this.converter = new ObjectConverter();
		this.chart = chart;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}	

	@Override
	public String getName(Axis axis) {
		return chart.getName(axis);
	}

	@Override
	public Series getSeries(Axis axis) {
		return chart.getSeries(axis);
	}
	
	public double[][] getPlot() {
		double[] x = getPlot(Axis.X);
		double[] y = getPlot(Axis.Y);
		
	   return new double[] []{x, y};
	}	
	
	public double[] getPlot(Axis axis) {
		Series series = chart.getSeries(axis);
		
		if(series != null) {
			int length = series.getLength();
			double[] values = new double[length];
			
			for(int i = 0; i < length; i++) {
				Object value = series.getValue(i);
				Double result = converter.convert(value);
				
				values[i] = result;
			}
			return values;
		}
		return new double[] {};
	}
}
