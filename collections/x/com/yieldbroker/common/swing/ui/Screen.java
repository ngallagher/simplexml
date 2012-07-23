package com.yieldbroker.common.swing.ui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JFrame;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementUnion;

/**
 * This is the main viewable screen that is created. It is responsible
 * for building the user interface by traversing the various widgets.
 * In order to build the user interface a {@link Controller} and a
 * {@link Context} are required to managed dependencies.
 * 
 * @author Niall Gallagher
 * 
 * @see com.yieldbroker.common.swing.ui.Context
 */
public class Screen {

	@ElementUnion({ 
		@Element(name="panel", type = Panel.class),  
		@Element(name="tabPane", type = TabPane.class)
	})
	private Widget widget;	

	@Attribute
	private String id;
	
	@Attribute
	private int width;
	
	@Attribute
	private int height;
	
	@Attribute
	private int x;
	
	@Attribute
	private int y;

	public Screen(@Element(name="panel") Panel panel) {
		this.widget = panel;
	}

	public Screen(@Element(name="tabPane") TabPane tabPane) {
		this.widget = tabPane;
	}

	public JFrame build(Controller controller, Context context) {
		PanelCreateDispatcher handler = new PanelCreateDispatcher(controller, context, id);
		BorderLayout layout = new BorderLayout();
		JFrame frame = new JFrame();

		try {
			Component component = widget.build(controller, context);
			
			frame.setLayout(layout);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setBounds(x, y, width, height);
			frame.add(component);
		} catch(Exception e) {
			throw new WidgetException(e);
		}
		handler.execute();
		return frame;
	}
	
	public String getId() {
		return id;
	}
}
