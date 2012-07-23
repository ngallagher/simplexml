package com.yieldbroker.common.swing.ui.graph;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

import com.yieldbroker.common.chart.plot.PlotSeries;
import com.yieldbroker.common.chart.plot.PlotSeriesImage;

public class Plotter {
	
	private final PlotSeries series;
	private final Component panel;	
	private final int pad;
	
	public Plotter(PlotSeries series, Component panel, int pad) {
		this.series = series;
		this.panel = panel;
		this.pad = pad;	
	}
	
	public BufferedImage plot() {
		Dimension size = panel.getSize();
		
		if(size.height > pad || size.width > pad) {
			return plot(size.width - pad, size.height - pad);			
		}
		return null;
	}
	
	private BufferedImage plot(int width, int height) {
		PlotSeriesImage plotImage = new PlotSeriesImage(series, width, height);
	
		if(height > 0 && width > 0) {
			return plotImage.createImage();
		}
		return null;
	}
}
