package com.yieldbroker.common.swing.ui;

import java.awt.Dimension;

import javax.swing.JPanel;

public class AlignHorizontal extends AlignVertical {

	public AlignHorizontal() {
		super();
	}

	@Override
	public JPanel build(Controller controller, Context context, Dimension size) {
		return build(controller, context, size, true);
	}

}
