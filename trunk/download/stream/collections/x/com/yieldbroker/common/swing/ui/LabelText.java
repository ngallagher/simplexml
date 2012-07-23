package com.yieldbroker.common.swing.ui;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementUnion;
import org.simpleframework.xml.core.Validate;

public class LabelText extends Widget {

	@Element
	private Label label;
	
	@ElementUnion({
		@Element(name="text", type=Text.class),
		@Element(name="password", type=Password.class)
	})	
	private Text text;
	
	@Validate
	public void validate() {
		if(height == 0) {
			throw new WidgetException("Height must not be zero");			
		}
		if(width == 0) {
			throw new WidgetException("Width must not be zero");
		}
		if(label.width == 0) {
			throw new WidgetException("Width of label must not be zero");			
		}
	}
	
	@Override
	public JPanel build(Controller controller, Context context, Dimension size) {
		JPanel panel = new JPanel();
		BoxLayout layout = new BoxLayout(panel, BoxLayout.X_AXIS);		
		Dimension fieldSize = new Dimension(size.width - label.width, size.height);
		Dimension nameSize = new Dimension(label.width, size.height);
		JLabel name = label.build(controller, context);
		JTextField field = text.build(controller, context);
		
		field.setPreferredSize(fieldSize);
		field.setMaximumSize(fieldSize);
		name.setPreferredSize(nameSize);
		name.setMaximumSize(nameSize);
		panel.add(name);
		panel.add(field);
		panel.setLayout(layout);
		panel.setPreferredSize(size);		
		return panel;
	}
}
