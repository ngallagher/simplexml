package com.yieldbroker.common.swing.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.border.Border;

import org.simpleframework.xml.Attribute;

public class AlignVertical extends Panel {

	@Attribute(required=false)
	private int pad;

	public AlignVertical() {
		this.pad = 5;
	}

	@Override
	public JPanel build(Controller controller, Context context, Dimension size) {
		return build(controller, context, size, false);
	}

	public JPanel build(Controller controller, Context context, Dimension size, boolean horizontal) {
		GridBagConstraints constraints = new GridBagConstraints();
		Insets insets = new Insets(pad, pad, pad, pad);
		GridBagLayout grid = new GridBagLayout();
		JPanel panel = new JPanel(grid);

		if (boundary != null) {
			Boundary type = Boundary.resolve(boundary);
			Border border = type.create(id);
			panel.setBorder(border);
		}
		for (int i = 0; i < widgets.size(); i++) {
			Widget widget = widgets.get(i);
			Component component = widget.build(controller, context);

			if (horizontal) {
				constraints.gridx = i;
				constraints.gridy = 0;
				constraints.insets = insets;
				constraints.fill = GridBagConstraints.BOTH;
			} else {
				constraints.gridx = 0;
				constraints.gridy = i;
				constraints.insets = insets;
				constraints.fill = GridBagConstraints.BOTH;
			}
			panel.add(component, constraints);
		}
		panel.setPreferredSize(size);
		return panel;
	}
}
