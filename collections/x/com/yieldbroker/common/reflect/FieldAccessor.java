package com.yieldbroker.common.reflect;

import java.lang.reflect.Field;

public class FieldAccessor implements Accessor {

	private final Field field;

	public FieldAccessor(String name, Class type) {
		this.field = getField(name, type);
	}

	public Class getType() {
		return field.getType();
	}

	public <T> T getValue(Object source) {
		try {
			if (!field.isAccessible()) {
				field.setAccessible(true);
			}
			return (T) field.get(source);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static Field getField(String name, Class type) {
		Field[] fields = type.getDeclaredFields();

		while (type != null) {
			for (Field field : fields) {
				String fieldName = field.getName();

				if (fieldName.equals(name)) {
					return field;
				}
			}
			type = type.getSuperclass();
		}
		return null;
	}

	private static enum Prefix {
		IS("is"), 
		GET("get");

		private final String prefix;

		private Prefix(String prefix) {
			this.prefix = prefix;
		}

		public String getProperty(String name) {
			char initial = name.charAt(0);
			char upperCase = Character.toUpperCase(initial);
			String end = name.substring(1);

			return String.format("%s%s%s", prefix, upperCase, end);
		}
	}
}
