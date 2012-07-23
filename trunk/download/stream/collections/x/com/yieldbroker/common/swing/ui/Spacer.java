package com.yieldbroker.common.swing.ui;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JComponent;

public class Spacer extends Widget<JComponent> {

	@Override
	public JComponent build(Controller controller, Context context, Dimension size) {
		return new Box.Filler(size, size, size);
	}

}
