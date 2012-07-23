package com.yieldbroker.common.swing.ui;

import java.awt.Dimension;

import javax.swing.JTextField;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.core.Validate;


public class Text extends Widget<JTextField> {

	@Attribute(required=false)
	protected String text;

	public Text() {
		super();
	}

	public Text(@Attribute(name="text") String text) {
		this.text = text;
	}
	
	@Validate
	public void validate() {
		if(height > 0 && width <= 0) {
			throw new WidgetException("Height of %s specified but width of %s is invalid", height, width);
		}
	}

	@Override
	public JTextField build(Controller controller, Context context, Dimension size) {
		TextChangeDispatcher handler = new TextChangeDispatcher(controller, context, id);
		JTextField field = new JTextField();

		if (id != null) {
			field.addFocusListener(handler);
			field.addActionListener(handler);
			context.add(id, field);
		}
		if (text != null) {
			field.setText(text);
		}
		field.setPreferredSize(size);
		
		if(height > 0) {
			field.setMaximumSize(size);
		}
		return field;
	}
}
