package com.yieldbroker.common.swing.ui;

import java.awt.Dimension;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;


public class TabPane extends Widget<JTabbedPane> {

	@ElementList(inline=true, entry="tab")
	private List<Tab> tabs;

	@Attribute(required=false)
	private int pad;

	public TabPane() {
		this.tabs = new LinkedList<Tab>();
	}

	public void add(Tab tab) {
		tabs.add(tab);
	}

	@Override
	public JTabbedPane build(Controller controller, Context context, Dimension size) {
		TabChangeDispatcher handler = new TabChangeDispatcher(controller, context, id);
		Dimension parent = new Dimension(size.width + pad, size.height + pad);
		JTabbedPane pane = new JTabbedPane();

		for (Tab tab : tabs) {
			JPanel entry = tab.build(controller, context);
			String name = tab.getName();

			entry.setPreferredSize(size);
			pane.addTab(name, entry);
		}
		if (id != null) {
			pane.addChangeListener(handler);
			context.add(id, pane);
		}
		pane.setPreferredSize(parent);
		return pane;
	}
}
