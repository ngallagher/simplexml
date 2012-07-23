package com.yieldbroker.common.manage.log4j;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource(description="Configures Log4j using the specified file")
public class LogConfigurer {

	private final long refreshInterval;
	private final File logSettings;

	public LogConfigurer(File logSettings) {
		this(logSettings, 5000);
	}
	
	public LogConfigurer(File logSettings, long refreshInterval) {
		this.refreshInterval = refreshInterval;
		this.logSettings = logSettings;
	}
	
	@ManagedAttribute(description="The configuration refresh interval")
	public long getRefreshInterval() {
		return refreshInterval;
	}
	
	@ManagedAttribute(description="The configuration file")
	public String getConfigurationFile() {
		try {
			return logSettings.getCanonicalPath();
		} catch(Exception e) {
			return logSettings.getAbsolutePath();
		}
	}
	
	@ManagedOperation(description="Configures Log4j with the specified file")
	public void configure() throws IOException {
		String canonicalPath = getConfigurationFile();
		
		if(canonicalPath.toLowerCase().endsWith(".xml")) { 
			DOMConfigurator.configureAndWatch(canonicalPath, refreshInterval);
		} else {
			PropertyConfigurator.configureAndWatch(canonicalPath, refreshInterval);
		}
	}
}
