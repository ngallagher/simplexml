package com.yieldbroker.common.chart.plot;

import java.awt.image.BufferedImage;

import com.yieldbroker.common.chart.Chart;

public class ImageBuilder {
	
	private final int width;
	private final int height;
	
	public ImageBuilder(int width, int height) {
		this.height = height;
		this.width = width;
	}
	
	public BufferedImage build(Chart chart, String title) {
		Plot plot = new Plot(chart, title);
		PlotImage plotImage = new PlotImage(plot, width, height);
	
        return plotImage.createImage();
	}
}
