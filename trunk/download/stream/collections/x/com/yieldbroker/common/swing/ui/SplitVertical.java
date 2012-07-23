package com.yieldbroker.common.swing.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.Border;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;

public class SplitVertical extends Widget<JPanel> {

	@ElementList(inline=true, entry="section")
	protected List<Panel> sections;

	@Attribute(required=false)
	protected String boundary;

	public SplitVertical() {
		this.sections = new LinkedList<Panel>();
	}

	public void add(Panel panel) {
		sections.add(panel);
	}

	@Override
	public JPanel build(Controller controller, Context context, Dimension size) {
		return build(controller, context, size, 0, 1);
	}

	public JPanel build(Controller controller, Context context, Dimension size, int rows, int columns) {
		GridLayout layout = new GridLayout(rows, columns);
		JPanel split = new JPanel();

		if (boundary != null) {
			Boundary type = Boundary.resolve(boundary);
			Border border = type.create(id);
			split.setBorder(border);
		}
		for (Panel section : sections) {
			Component component = section.build(controller, context);
			split.add(component);
		}
		split.setLayout(layout);
		split.setPreferredSize(size);
		return split;
	}
}
