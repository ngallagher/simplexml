package com.yieldbroker.common.chart.plot;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

import com.yieldbroker.common.chart.Axis;

/** 
 * A plot image is an image that is generated using a {@link Plot}. The
 * plot contains a series of X Y points that are used to generate a line
 * graph as a {@link java.awt.image.BufferedImage} of the specified size.
 * 
 * @author Niall Gallagher
 */
public class PlotImage {

	private final RectangleInsets imageInsets;
	private final String title;
	private final Plot plot;
	private final int width;
	private final int height;

	public PlotImage(Plot plot, int width, int height) {
		this.imageInsets = new RectangleInsets(5.0, 5.0, 5.0, 5.0);
		this.title = plot.getName();
		this.height = height;
		this.width = width;
		this.plot = plot;
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
		double[][] graphPoints = plot.getPlot();

		if(graphPoints.length > 0) {
			graphData.addSeries(title, graphPoints);
		}
		return createChart(graphData);
	}

	private JFreeChart createChart(XYDataset data) {
		return ChartFactory.createXYLineChart(title, 
				plot.getName(Axis.X),
				plot.getName(Axis.Y), 
				data, 
				PlotOrientation.VERTICAL, 
				true, 
				false, 
				false);
	}
}
