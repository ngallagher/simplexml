package com.yieldbroker.common.swing.ui;

import java.awt.Dimension;

import javax.swing.JPanel;

public class SplitHorizontal extends SplitVertical {

	public SplitHorizontal() {
		super();
	}

	@Override
	public JPanel build(Controller controller, Context context, Dimension size) {
		return build(controller, context, size, 1, 0);
	}
}
