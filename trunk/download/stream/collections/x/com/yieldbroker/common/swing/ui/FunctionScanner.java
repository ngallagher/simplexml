package com.yieldbroker.common.swing.ui;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.yieldbroker.common.swing.ui.annotation.ComponentOf;
import com.yieldbroker.common.swing.ui.annotation.ValueOf;

/**
 * A function scanner is used to scan the signature of an annotated 
 * method to determine what, if any, annotations exist on its parameters.
 * Annotations such as the {@link ValueOf} or {@link ComponentOf} can
 * be used to enable the window builder to determine what needs to
 * be injected in to the method when called.
 * 
 * @author Niall Gallagher
 * 
 * @see com.yieldbroker.common.swing.ui.annotation.ComponentOf
 * @see com.yieldbroker.common.swing.ui.annotation.ValueOf
 */
public class FunctionScanner {

	private final Map<Class, Function> functions;
	private final Function missing;
	private final Class annotation;
	private final String id;

	public FunctionScanner(Class annotation, String id) {
		this.functions = new HashMap<Class, Function>();
		this.missing = new Function();
		this.annotation = annotation;
		this.id = id;
	}

	public Function scan(Object source) throws Exception {
		if (source != null) {
			Class type = source.getClass();
			Function function = functions.get(type);

			if (function == null) {
				scan(type);
			}
			return functions.get(type);
		}
		return missing;
	}

	private void scan(Class type) throws Exception {
		Method[] methods = type.getDeclaredMethods();

		for (Method method : methods) {
			Annotation label = method.getAnnotation(annotation);

			if (label != null) {
				Function function = scan(method, label);

				if (function != null) {
					functions.put(type, function);
				}
			}
		}
		if (!functions.containsKey(type)) {
			functions.put(type, missing);
		}
	}

	private Function scan(Method method, Annotation annotation) throws Exception {
		String value = extract(annotation);

		if (value.equals(id)) {
			return scan(method);
		}
		return null;
	}

	private Function scan(Method method) throws Exception {
		Extractor[] extractor = extract(method);

		if (extractor.length > 0) {
			return new Function(method, extractor);
		}
		return new Function(method);
	}

	private Extractor[] extract(Method method) throws Exception {
		Class[] parameters = method.getParameterTypes();

		if (parameters.length > 0) {
			Extractor[] extractors = new Extractor[parameters.length];

			for (int i = 0; i < extractors.length; i++) {
				extractors[i] = extract(method, parameters[i], i);
			}
			return extractors;
		}
		return new Extractor[0];

	}

	private Extractor extract(Method method, Class parameter, int index) throws Exception {
		Annotation[][] annotations = method.getParameterAnnotations();

		for (int i = 0; i < annotations[index].length; i++) {
			Annotation annotation = annotations[index][i];
			String name = extract(annotation);

			if (annotation instanceof ValueOf) {
				return new ValueExtractor(name, parameter);
			}
			if (annotation instanceof ComponentOf) {
				return new ComponentExtractor(name, parameter);
			}
		}
		return null;
	}

	private String extract(Annotation annotation) throws Exception {
		Class type = annotation.annotationType();
		Method[] list = type.getDeclaredMethods();
		Object value = list[0].invoke(annotation);

		return String.valueOf(value);
	}
}
