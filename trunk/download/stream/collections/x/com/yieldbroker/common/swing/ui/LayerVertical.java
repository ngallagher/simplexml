package com.yieldbroker.common.swing.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.core.Commit;

public class LayerVertical extends Widget<JPanel> {

	@ElementList(inline=true, entry="section")
	protected List<Panel> sections;

	@Attribute(required = false)
	protected String boundary;

	public LayerVertical() {
		this.sections = new LinkedList<Panel>();
	}

	public void add(Panel panel) {
		sections.add(panel);
	}

	@Commit
	public void stretch() {
		int scale = 0;

		for (Panel section : sections) {
			if (section.height == 0) {
				throw new WidgetException("Row width must not be zero");
			}
			if (section.width < 0) {
				throw new WidgetException("Row height must not be zero");
			}
			if (width < section.width) {
				width = section.width;
			}
			scale += section.height;
		}
		if (height < scale) {
			height = scale;
		}
	}

	@Override
	public JPanel build(Controller controller, Context context, Dimension size) {
		return build(controller, context, size, BoxLayout.Y_AXIS);

	}

	public JPanel build(Controller controller, Context context, Dimension size, int axis) {
		JPanel layer = new JPanel();
		BoxLayout layout = new BoxLayout(layer, axis);

		if (boundary != null) {
			Boundary type = Boundary.resolve(boundary);
			Border border = type.create(id);
			layer.setBorder(border);
		}
		for (Panel section : sections) {
			Component component = section.build(controller, context);
			layer.add(component);
		}
		layer.setLayout(layout);
		layer.setPreferredSize(size);
		return layer;
	}
}
