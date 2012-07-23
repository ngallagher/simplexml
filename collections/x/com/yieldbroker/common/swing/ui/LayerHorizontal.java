package com.yieldbroker.common.swing.ui;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.simpleframework.xml.core.Commit;

public class LayerHorizontal extends LayerVertical {

	@Commit
	public void stretch() {
		int scale = 0;

		for (Panel section : sections) {
			if (section.height == 0) {
				throw new WidgetException("Row width must not be zero");
			}
			if (section.width == 0) {
				throw new WidgetException("Row height must not be zero");
			}
			if (height < section.height) {
				height = section.height;
			}
			scale += section.width;
		}
		if (width < scale) {
			width = scale;
		}
	}

	@Override
	public JPanel build(Controller controller, Context context, Dimension size) {
		return build(controller, context, size, BoxLayout.X_AXIS);
	}
}
