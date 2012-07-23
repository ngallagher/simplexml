package com.yieldbroker.common.swing.ui;

import java.awt.Dimension;
import java.util.Vector;

import javax.swing.JComboBox;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.core.Validate;


public class ComboBox extends Widget<JComboBox> {

	@ElementList(entry="option", inline=true)
	private Vector<String> options;
	
	@Validate
	public void validate() {
		if(height > 0 && width <= 0) {
			throw new WidgetException("Height of %s specified but width of %s is invalid", height, width);
		}
	}

	@Override
	public JComboBox build(Controller controller, Context context, Dimension size) {
		ComboBoxChangeDispatcher handler = new ComboBoxChangeDispatcher(controller, context, id);
		JComboBox comboBox = new JComboBox(options);

		if (id != null) {
			comboBox.addActionListener(handler);
			context.add(id, comboBox);
		}
		comboBox.setPreferredSize(size);
		
		if(height > 0) {
			comboBox.setMaximumSize(size);
		}
		return comboBox;
	}
}
