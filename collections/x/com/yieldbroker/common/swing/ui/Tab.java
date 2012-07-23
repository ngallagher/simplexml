package com.yieldbroker.common.swing.ui;

import org.simpleframework.xml.Attribute;

public class Tab extends Panel {

	@Attribute
	private String tabName;

	public Tab(@Attribute(name="tabName") String tabName) {
		this.tabName = tabName;
	}

	public String getName() {
		return tabName;
	}
}
