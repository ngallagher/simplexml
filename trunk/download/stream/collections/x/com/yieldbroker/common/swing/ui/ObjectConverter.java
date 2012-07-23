package com.yieldbroker.common.swing.ui;

import java.util.Arrays;
import java.util.List;

/**
 * This is used to convert a string value to a specific type. The
 * can be done for a number of types, including the Java primitive 
 * types and several other convenience types.
 * 
 * @author Niall Gallagher
 */
public class ObjectConverter {

	private final Class type;

	public ObjectConverter(Class type) {
		this.type = type;
	}

	public Object convert(String value) {
		Class box = box(type);

		if (box == String.class) {
			return value;
		}
		if (box == Integer.class) {
			return Integer.parseInt(value);
		}
		if (box == Double.class) {
			return Double.parseDouble(value);
		}
		if (box == Float.class) {
			return Float.parseFloat(value);
		}
		if (box == Boolean.class) {
			return Boolean.parseBoolean(value);
		}
		if (box == Byte.class) {
			return Byte.parseByte(value);
		}
		if (box == Short.class) {
			return Short.parseShort(value);
		}
		if (box == Long.class) {
			return Long.parseLong(value);
		}
		if (box == Character.class) {
			return value.charAt(0);
		}
		if (box == List.class) {
			return Arrays.asList(value);
		}
		return value;
	}

	private Class box(Class type) {
		if (type == int.class) {
			return Integer.class;
		}
		if (type == double.class) {
			return Double.class;
		}
		if (type == float.class) {
			return Float.class;
		}
		if (type == boolean.class) {
			return Boolean.class;
		}
		if (type == byte.class) {
			return Byte.class;
		}
		if (type == short.class) {
			return Short.class;
		}
		if (type == long.class) {
			return Long.class;
		}
		if (type == char.class) {
			return Character.class;
		}
		return type;
	}
}
