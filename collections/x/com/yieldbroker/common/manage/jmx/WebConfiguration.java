package com.yieldbroker.common.manage.jmx;

public class WebConfiguration {

	private final String color;
	private final String password;
	private final String login;
	private final int port;

	public WebConfiguration(String color, String login, String password, int port) {
		this.password = password;
		this.color = color;
		this.login = login;
		this.port = port;
	}

	public String getColor() {
		return color;
	}

	public String getPassword() {
		return password;
	}

	public String getLogin() {
		return login;
	}

	public int getPort() {
		return port;
	}
}
