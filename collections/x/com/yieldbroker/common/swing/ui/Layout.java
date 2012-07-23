package com.yieldbroker.common.swing.ui;

import javax.swing.JFrame;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.util.Dictionary;

@Root
public class Layout {

	@ElementList(inline=true, required=false)
	private Dictionary<Property> properties;

	@Element
	private Screen screen;

	public Layout() {
		super();
	}

	public Property lookup(String name) {
		return properties.get(name);
	}

	public JFrame build(Controller controller, Context context) {
		return screen.build(controller, context);
	}

	public String getId() {
		return screen.getId();
	}
}
