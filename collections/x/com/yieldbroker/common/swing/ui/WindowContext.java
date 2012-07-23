package com.yieldbroker.common.swing.ui;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

public class WindowContext implements Context {

	private final Map<String, Component> widgets;

	public WindowContext() {
		this.widgets = new HashMap<String, Component>();
	}

	public void add(String id, Component component) {
		widgets.put(id, component);
	}

	public <T extends Component> T get(String id) {
		return (T) widgets.get(id);
	}

	public <T extends Component> T remove(String id) {
		return (T) widgets.remove(id);
	}

	public boolean contains(String id) {
		return widgets.containsKey(id);
	}

	public String getId() {
		return null;
	}

	public String toString() {
		return null;
	}
}
