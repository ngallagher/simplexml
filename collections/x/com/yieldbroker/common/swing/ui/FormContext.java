package com.yieldbroker.common.swing.ui;

import java.awt.Component;

public class FormContext extends WindowContext {

	private Context parent;
	private String id;

	public FormContext(Context parent, String id) {
		this.parent = parent;
		this.id = id;
	}
	
	public void add(String id, Component component) {
		Object value = parent.get(id);
		
		if(value == null) {
			parent.add(id, component);			
		}
		super.add(id, component);
	}
	
	public <T extends Component> T get(String id) {
		Object value = super.get(id);
		
		if(value == null) {
			value = parent.get(id);
		}
		return (T) value;
	}

	public <T extends Component> T remove(String id) {
		Object value = super.remove(id);
		
		if(value == null) {
			value = parent.remove(id);			
		}
		return (T) value;
	}

	public boolean contains(String id) {
		return parent.contains(id) || super.contains(id);
	}

	public String getId() {
		return id;
	}

	public String toString() {
		return id;
	}
}
