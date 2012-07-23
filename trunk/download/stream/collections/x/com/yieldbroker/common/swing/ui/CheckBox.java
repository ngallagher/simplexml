package com.yieldbroker.common.swing.ui;

import java.awt.Dimension;

import javax.swing.JCheckBox;

import org.simpleframework.xml.Attribute;


public class CheckBox extends Widget<JCheckBox> {

	@Attribute
	private String text;

	@Attribute(required=false)
	private boolean selected;

	@Override
	public JCheckBox build(Controller controller, Context context, Dimension size) {
		CheckBoxChangeDispatcher handler = new CheckBoxChangeDispatcher(controller, context, id);
		JCheckBox checkBox = new JCheckBox(text, selected);

		if (id != null) {
			checkBox.addItemListener(handler);
			context.add(id, checkBox);
		}
		checkBox.setPreferredSize(size);
		return checkBox;
	}

}
