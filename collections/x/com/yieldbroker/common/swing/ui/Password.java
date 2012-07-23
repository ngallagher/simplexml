package com.yieldbroker.common.swing.ui;

import java.awt.Dimension;

import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.simpleframework.xml.Attribute;


public class Password extends Text {

	public Password() {
		super();
	}

	public Password(@Attribute(name="text") String text) {
		super(text);
	}

	@Override
	public JTextField build(Controller controller, Context context, Dimension size) {
		TextChangeDispatcher handler = new TextChangeDispatcher(controller, context, id);
		JPasswordField field = new JPasswordField();

		if (id != null) {
			field.addFocusListener(handler);
			field.addActionListener(handler);
			context.add(id, field);
		}
		if (text != null) {
			field.setText(text);
		}
		field.setPreferredSize(size);
		return field;
	}
}
