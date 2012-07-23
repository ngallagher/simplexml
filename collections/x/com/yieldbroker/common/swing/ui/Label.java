package com.yieldbroker.common.swing.ui;

import java.awt.Dimension;

import javax.swing.JLabel;

import org.simpleframework.xml.Attribute;

public class Label extends Widget<JLabel> {

	@Attribute(required=false)
	private String text = "";

	@Override
	public JLabel build(Controller controller, Context context, Dimension size) {
		JLabel label = new JLabel(text);

		if (id != null) {
			context.add(id, label);
		}
		label.setPreferredSize(size);
		return label;
	}

}
