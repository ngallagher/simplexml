package com.yieldbroker.common.swing.ui.table;

import static com.yieldbroker.common.swing.ui.table.Gradient.NONE;

import java.text.Format;

import com.yieldbroker.common.reflect.Accessor;
import com.yieldbroker.common.reflect.PropertyAccessor;

public class Column {
	
	private final Gradient gradient;
	private final Accessor accessor;
	private final String property;
	private final String title;
	private final Format format;
	private final boolean ignoreNull; 

	public Column(Class type, String title, String property, boolean ignoreNull) throws Exception {
		this(type, title, property, ignoreNull, NONE);
	}
	
	public Column(Class type, String title, String property, boolean ignoreNull, Gradient gradient) throws Exception {
		this(type, title, property, ignoreNull, gradient, null);
	}
	
	public Column(Class type, String title, String property, boolean ignoreNull, Gradient gradient, Format format) throws Exception {
		this.accessor = new PropertyAccessor(property, type);
		this.ignoreNull = ignoreNull;
		this.gradient = gradient;
		this.property = property;	
		this.title = title;
		this.format = format;
	}
	
	public Class getType() {
		return accessor.getType();
	}

	public Value getValue(Object row) {
		Object value = accessor.getValue(row);
		
		if(value != null) {
			String text = String.valueOf(value);
		
			if(format != null) {
				text = format.format(value);
			}
			return new Value(text, gradient);
		}
		return new Value("", gradient, ignoreNull);		
	}
	
	public Gradient getGradient() {
		return gradient;
	}

	public String getTitle() {
		return title;
	}
}
