package com.yieldbroker.common.swing.ui;

import java.util.Map;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Commit;
import org.simpleframework.xml.util.Entry;

@Root
public class Property implements Entry {

	@Attribute
	private final String name;

	@Attribute
	private final String value;

	public Property(@Attribute(name="name") String name, @Attribute(name="value") String value) {
		this.name = name;
		this.value = value;
	}

	@Commit
	public void commit(Map context) {
		context.put(name, value);
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}
}
