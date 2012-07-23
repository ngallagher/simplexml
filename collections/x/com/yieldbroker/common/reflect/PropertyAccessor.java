package com.yieldbroker.common.reflect;

import java.lang.reflect.Method;

public class PropertyAccessor implements Accessor {

	private final Method method;

	public PropertyAccessor(String name, Class type) {
		this.method = getMethod(name, type);
	}

	public Class getType() {
		return method.getReturnType();
	}

	public <T> T getValue(Object source) {
		try {
			if (!method.isAccessible()) {
				method.setAccessible(true);
			}
			return (T) method.invoke(source);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static Method getMethod(String name, Class type) {
		Method method = getMethod(name, type, Prefix.GET);

		if (method == null) {
			return getMethod(name, type, Prefix.IS);
		}
		return method;
	}

	private static Method getMethod(String name, Class type, Prefix prefix) {
		Method[] methods = type.getDeclaredMethods();
		String property = prefix.getProperty(name);

		for (Method method : methods) {
			Class[] parameterTypes = method.getParameterTypes();
			String methodName = method.getName();

			if (parameterTypes.length == 0) {
				if (methodName.equals(property)) {
					return method;
				}
			}
		}
		return null;
	}

	private static enum Prefix {
		IS("is"), GET("get");

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
