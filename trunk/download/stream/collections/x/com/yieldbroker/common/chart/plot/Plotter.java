package com.yieldbroker.common.chart.plot;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.jfree.chart.encoders.SunPNGEncoderAdapter;

import com.yieldbroker.common.chart.Chart;

/** 
 * This is used to plot a line graph using the {@link Chart} specified.
 * Once plotted the graph is encoded as a PNG image that can be either
 * saved to disk or displayed in a web page.
 * 
 * @author Niall Gallagher
 */
public class Plotter {
	
    private final SunPNGEncoderAdapter imageEncoder;
    private final ImageBuilder imageBuilder;
	
	public Plotter(int width, int height) {
		this.imageBuilder = new ImageBuilder(width, height);
		this.imageEncoder = new SunPNGEncoderAdapter();
	}
	
	public byte[] plot(Chart chart, String title) throws IOException {
		BufferedImage bufferedImage = imageBuilder.build(chart, title);

		if(bufferedImage == null) {
			throw new IOException("Could not plot chart " + title);
		}
        return encode(bufferedImage);
	}
	
	private byte[] encode(BufferedImage bufferedImage) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		imageEncoder.encode(bufferedImage, buffer);
		return buffer.toByteArray();
	}
}
