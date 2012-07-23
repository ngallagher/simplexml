package com.yieldbroker.common.swing.ui;

import java.awt.Component;

public interface Context {
	void add(String id, Component component);
	<T extends Component> T get(String id);
	<T extends Component> T remove(String id);
	boolean contains(String id);
	String getId();
}
