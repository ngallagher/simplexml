package com.yieldbroker.common.lease;

public class LeaseException extends RuntimeException {

	public LeaseException(String template, Object... list) {
		super(String.format(template, list));
	}

	public LeaseException(Throwable cause, String template, Object... list) {
		super(String.format(template, list), cause);
	}
}
