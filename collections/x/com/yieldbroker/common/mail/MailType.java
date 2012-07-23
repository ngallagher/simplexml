package com.yieldbroker.common.mail;

public enum MailType {
	HTML("text/html"),
	TEXT("text/plain");
	
	private final String contentType;
	
	private MailType(String contentType) {
		this.contentType = contentType;
	}
	
	public String getContentType(){
		return contentType;
	}
}
