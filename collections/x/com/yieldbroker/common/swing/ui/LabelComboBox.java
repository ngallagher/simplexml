package com.yieldbroker.common.swing.ui;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.simpleframework.xml.Element;

public class LabelComboBox extends Widget<JPanel> {

	@Element
	private Label label;

	@Element
	private ComboBox comboBox;

	@Override
	public JPanel build(Controller controller, Context context, Dimension size) {
		JPanel panel = new JPanel();
		BoxLayout layout = new BoxLayout(panel, BoxLayout.X_AXIS);
		Dimension boxSize = new Dimension(size.width - label.width, size.height);
		Dimension nameSize = new Dimension(label.width, size.height);
		JLabel name = label.build(controller, context);
		JComboBox box = comboBox.build(controller, context);

		box.setPreferredSize(boxSize);
		box.setMaximumSize(boxSize);
		name.setPreferredSize(nameSize);
		name.setMaximumSize(nameSize);
		panel.add(name);
		panel.add(box);
		panel.setLayout(layout);
		panel.setPreferredSize(size);
		return panel;
	}
}

