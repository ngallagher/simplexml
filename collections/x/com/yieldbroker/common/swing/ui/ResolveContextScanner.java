package com.yieldbroker.common.swing.ui;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.yieldbroker.common.swing.ui.annotation.ResolveContext;

/**
 * This annotation scanner is used with {@link ResolveContext} in order
 * to determine which {@link Context} to use when processing a controller
 * object. The annotation will contain serveral ids, each one uniquely
 * identifying a context to use.
 * 
 * @author Niall Gallagher
 * 
 * @see com.yieldbroker.common.swing.ui.annotation.ResolveContext
 */
public class ResolveContextScanner {

	private final Map<String, Object> valuesCache;
	private final List<Object> values;

	public ResolveContextScanner(List<Object> values) {
		this.valuesCache = new ConcurrentHashMap<String, Object>();
		this.values = values;
	}

	public Object lookup(String name) throws Exception {
		if (valuesCache.isEmpty()) {
			scan();
		}
		return valuesCache.get(name);
	}

	private void scan() throws Exception {
		for (Object value : values) {
			String[] names = extract(value);

			for(String name : names) {
				Object existing = valuesCache.get(value);

				if (existing != null) {
					throw new WidgetException("Object with context value %s declared more than once", name);
				}
				valuesCache.put(name, value);
			}
		}
	}

	private String[] extract(Object object) throws Exception {
		Class<?> type = object.getClass();
		ResolveContext context = type.getAnnotation(ResolveContext.class);
		
		if(context != null) {
			return context.value();
		}
		return new String[]{};
	}
}
