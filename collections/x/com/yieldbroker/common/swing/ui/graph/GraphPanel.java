package com.yieldbroker.common.swing.ui.graph;

import javax.swing.JLabel;

public class GraphPanel extends JLabel {

	private final GraphUpdater graphUpdater;
	
	public GraphPanel(GraphUpdater graphUpdater) {
		this.graphUpdater = graphUpdater;
	}
	
	public GraphUpdater getUpdater() {
		return graphUpdater;
	}
}
