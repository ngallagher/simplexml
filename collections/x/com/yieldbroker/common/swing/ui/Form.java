package com.yieldbroker.common.swing.ui;

import java.awt.Dimension;

import javax.swing.JPanel;

public class Form extends Panel {

	@Override
	public JPanel build(Controller control, Context context, Dimension size) {
		Context form = new FormContext(context, id);

		if (id == null) {
			throw new WidgetException("All forms require an id");
		}
		return super.build(control, form, size);
	}
}
