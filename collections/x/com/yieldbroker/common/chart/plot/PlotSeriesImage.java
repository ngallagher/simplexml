package com.yieldbroker.common.chart.plot;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

import com.yieldbroker.common.chart.Axis;

public class PlotSeriesImage {

	private final RectangleInsets imageInsets;
	private final PlotSeries plotSeries;
	private final String title;
	private final int width;
	private final int height;

	public PlotSeriesImage(PlotSeries plotSeries, int width, int height) {		
		this.imageInsets = new RectangleInsets(5.0, 5.0, 5.0, 5.0);
		this.title = plotSeries.getTitle();
		this.plotSeries = plotSeries;
		this.height = height;		
		this.width = width;
	}
	
	public BufferedImage createImage() {
		JFreeChart lineChart = createChart();
		XYPlot lineGraph = lineChart.getXYPlot();
		
		lineChart.setBackgroundPaint(Color.WHITE);
		lineGraph.setBackgroundPaint(Color.LIGHT_GRAY);
		lineGraph.setDomainGridlinePaint(Color.WHITE);
		lineGraph.setRangeGridlinePaint(Color.WHITE);
		lineGraph.setAxisOffset(imageInsets);
		lineGraph.setDomainCrosshairVisible(true);
		lineGraph.setRangeCrosshairVisible(true);
		
        return lineChart.createBufferedImage(width, height);
	}

	private JFreeChart createChart() {
		DefaultXYDataset graphData = new DefaultXYDataset();
		
		for(Plot plot : plotSeries) {
			double[][] graphPoints = plot.getPlot();
			String name = plot.getName();

			if(graphPoints.length > 0) {
				graphData.addSeries(name, graphPoints);
			}
		}
		return createChart(graphData);
	}

	private JFreeChart createChart(XYDataset data) {
		return ChartFactory.createXYLineChart(title, 
				plotSeries.getName(Axis.X),
				plotSeries.getName(Axis.Y), 
				data, 
				PlotOrientation.VERTICAL, 
				true, 
				false, 
				false);
	}
}