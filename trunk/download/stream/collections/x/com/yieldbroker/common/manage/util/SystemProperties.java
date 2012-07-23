package com.yieldbroker.common.manage.util;

import java.util.Map;

import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource(description="Custom system properties for the application")
public class SystemProperties {
	
	private final Map<String, String> systemProperties;
	
	public SystemProperties(Map<String, String> systemProperties) {
		this.systemProperties = systemProperties;
	}
	
	@ManagedOperation(description="Set a custom property")
	@ManagedOperationParameters({
		@ManagedOperationParameter(name="propertyName", description="Property name"),
		@ManagedOperationParameter(name="propertyValue", description="Property value")		
	})	
	public void setProperty(String propertyName, String propertyValue) {
		systemProperties.put(propertyName, propertyValue);
	}
	
	@ManagedOperation(description="Get a custom property")
	@ManagedOperationParameters({
		@ManagedOperationParameter(name="propertyName", description="Property name")		
	})	
	public String getProperty(String propertyName) {
		return systemProperties.get(propertyName);
	}

	@ManagedOperation(description="Apply to system properties")
	public void applyProperties() {
		System.getProperties().putAll(systemProperties);
	}
}
