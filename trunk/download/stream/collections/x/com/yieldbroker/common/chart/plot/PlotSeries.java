package com.yieldbroker.common.chart.plot;

import java.util.Iterator;
import java.util.List;

import com.yieldbroker.common.chart.Axis;

public class PlotSeries implements Iterable<Plot> {
	
	private final List<Plot> plots;
	private final String title;
	private final String x;
	private final String y;
	
	public PlotSeries(List<Plot> plots, String title, String x, String y) {
		this.plots = plots;
		this.title = title;
		this.x = x;
		this.y = y;		
	}	

	public String getTitle() {
		return title;
	}	
	
	public Iterator<Plot> iterator() {
		return plots.iterator();
	}
	
	public String getName(Axis axis) {
		if(axis == Axis.X) {
			return x;
		}
		return y;
	}	
}
