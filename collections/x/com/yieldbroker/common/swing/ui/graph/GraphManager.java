package com.yieldbroker.common.swing.ui.graph;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.Icon;
import javax.swing.border.Border;

import com.yieldbroker.common.swing.ui.Boundary;

public class GraphManager extends Thread implements ComponentListener {

	private final PlotDrawer chartDrawer;
	private final GraphPanel graphPanel;
	private final String boundary;
	
	public GraphManager(GraphPanel graphPanel, PlotDrawer chartDrawer, String boundary) {
		this.chartDrawer = chartDrawer;
		this.graphPanel = graphPanel;
		this.boundary = boundary;
	}	
	
	public void run() {
		while(true) {
			try {
				refresh();
				Thread.sleep(1000);
			} catch(Exception e) {				
				Thread.yield();
			}
		}
	}	

	@Override
	public void componentResized(ComponentEvent e) {
		refresh();
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		refresh();
	}

	@Override
	public void componentShown(ComponentEvent e) {
		refresh();
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		refresh();
	}
	
	private void refresh() {
		Icon icon = chartDrawer.draw();
		Boundary type = Boundary.resolve(boundary);
		Border border = type.create(boundary);
	
		graphPanel.setBorder(border);
		graphPanel.setIcon(icon);
	}
}
