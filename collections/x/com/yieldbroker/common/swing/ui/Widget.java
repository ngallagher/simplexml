package com.yieldbroker.common.swing.ui;

import java.awt.Dimension;

import javax.swing.JComponent;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * A widget represents a core builder object for an XML UI component.
 * Each widget is traversed an a component is extracted so that it
 * can be inserted in to a panel or frame.
 * 
 * @author Niall Gallagher
 */
@Root
public abstract class Widget<T extends JComponent> {

	@Attribute(required=false)
	protected String id;

	@Attribute(required=false)
	protected int width;

	@Attribute(required=false)
	protected int height;

	protected Widget() {
		super();
	}

	public T build(Controller controller, Context context) {
		Dimension size = new Dimension(width, height);

		if (context == null) {
			throw new WidgetException("Widget requires a context");
		}
		return build(controller, context, size);
	}

	public abstract T build(Controller controller, Context context, Dimension size);
}
