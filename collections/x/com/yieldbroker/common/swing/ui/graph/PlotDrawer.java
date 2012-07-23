package com.yieldbroker.common.swing.ui.graph;

import java.awt.Component;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.plaf.basic.BasicIconFactory;

import com.yieldbroker.common.chart.plot.PlotSeries;

public class PlotDrawer {

	private final PlotSeries series;
	private final Plotter plotter;
	
	public PlotDrawer(PlotSeries series, Component panel, int pad) {
		this.plotter = new Plotter(series, panel, pad);
		this.series = series;	
	}

	public Icon draw() {
		BufferedImage image = plotter.plot();
		
		if(image == null) {
			return BasicIconFactory.createEmptyFrameIcon();
		}
		return new ImageIcon(image);
	}
}
