package com.yieldbroker.common.swing.ui;

import java.lang.reflect.Method;

public class Function {

	private final Extractor[] extractors;
	private final Method method;
	private final int count;

	public Function() {
		this(null);
	}

	public Function(Method method) {
		this(method, new Extractor[0]);
	}

	public Function(Method method, Extractor[] extractors) {
		this.count = extractors.length;
		this.extractors = extractors;
		this.method = method;
	}

	public Object call(Object source, Context context) throws Exception {
		Object[] arguments = new Object[count];

		for (int i = 0; i < count; i++) {
			Extractor extractor = extractors[i];

			if (extractor != null) {
				Object value = extractor.extract(context);
				
				if(value == null) {
					throw new WidgetException("Could not extract parameter at index %s for %s", i, method);
				}
				arguments[i] = value;
			}
		}
		return invoke(source, arguments);
	}

	private Object invoke(Object source, Object[] arguments) throws Exception {
		if (method != null) {
			if (count > 0) {
				return method.invoke(source, arguments);
			}
			return method.invoke(source);
		}
		return null;
	}
}
