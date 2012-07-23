package com.yieldbroker.common.swing.ui;

import java.awt.Dimension;

import javax.swing.JTextArea;


public class TextArea extends Widget<JTextArea> {

	@Override
	public JTextArea build(Controller controller, Context context, Dimension size) {
		TextChangeDispatcher handler = new TextChangeDispatcher(controller, context, id);
		JTextArea area = new JTextArea();

		if (id != null) {
			area.addFocusListener(handler);
			context.add(id, area);
		}
		area.setPreferredSize(size);
		return area;
	}
}
