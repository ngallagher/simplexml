package com.yieldbroker.common.swing.ui;

public class WidgetException extends RuntimeException {

	public WidgetException(Throwable cause) {
		super(cause);
	}

	public WidgetException(String message, Object... arguments) {
		super(String.format(message, arguments));
	}

}
