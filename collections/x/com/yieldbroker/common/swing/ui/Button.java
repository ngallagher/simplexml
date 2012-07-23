package com.yieldbroker.common.swing.ui;

import java.awt.Dimension;

import javax.swing.JButton;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.core.Validate;


public class Button extends Widget<JButton> {

	@Attribute
	private final String name;

	public Button(@Attribute(name="name") String name) {
		this.name = name;
	}

	@Validate
	public void validate() {
		if (height == 0) {
			throw new WidgetException("Height must not be zero");
		}
		if (width == 0) {
			throw new WidgetException("Width must not be zero");
		}
	}

	@Override
	public JButton build(Controller controller, Context context, Dimension size) {
		ButtonClickDispatcher handler = new ButtonClickDispatcher(controller, context, id);
		JButton button = new JButton(name);

		if (id != null) {
			button.addActionListener(handler);
			context.add(id, button);
		}
		button.setPreferredSize(size);
		return button;
	}
}
