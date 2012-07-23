package com.yieldbroker.common.manage.jmx;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebPageParser implements WebPageParserMBean {

	private final WebObjectIntrospector introspector;
	private final WebConfiguration config;

	public WebPageParser(WebConfiguration config) {
		this(config, null);
	}

	public WebPageParser(WebConfiguration config, WebObjectIntrospector introspector) {
		this.introspector = introspector;
		this.config = config;
	}

	public String parsePage(String page) {
		if (introspector == null) {
			return page;
		}
		if (page == null) {
			return page;
		}
		return replaceData(page);
	}

	public String parseRequest(String request) {
		return null;
	}

	private String replaceData(String source) {
		String result = replaceColor(source);

		if (result != null) {
			return replaceForm(result);
		}
		return source;
	}

	private String replaceColor(String source) {
		String color = config.getColor();

		if (color != null) {
			return replaceMatch(source, ".*(<BODY).*", "<BODY BGCOLOR='" + color + "'");
		}
		return source;
	}

	private String replaceForm(String source) {
		String objectLink = buildObjectReference(source);

		if (objectLink != null) {
			return replaceMatch(source, ".*(<FORM.*\"/Admin.*Unregister&\".*?</FORM>).*", objectLink);
		}
		return source;
	}

	private String findObjectName(String source) {
		return findMatch(source, ".*<TITLE>MBean View of .*name=(\\w+).*</TITLE>.*");
	}

	private String findMatch(String source, String expression) {
		Pattern pattern = Pattern.compile(expression, Pattern.DOTALL | Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(source);

		if (matcher.matches()) {
			return matcher.group(1);
		}
		return null;
	}

	private String replaceMatch(String source, String expression, String text) {
		String form = findMatch(source, expression);

		if (form != null) {
			return source.replaceFirst(form, text);
		}
		return source;
	}

	private String buildObjectReference(String source) {
		String objectLink = buildObjectLink(source);

		if (objectLink != null) {
			return String.format("<A HREF='%s'>Look Inside</A>", objectLink);
		}
		return null;
	}

	private String buildObjectLink(String source) {
		String objectName = findObjectName(source);

		try {
			if (objectName != null) {
				return introspector.getObjectLink(objectName);
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}
}
