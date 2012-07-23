package com.yieldbroker.common.log;

import java.util.List;

import org.apache.log4j.Appender;
import org.apache.log4j.AsyncAppender;
import org.apache.log4j.Logger;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource(description="Analyser for log events")
public class LogAnalyser extends AsyncAppender {
	
	public LogAnalyser(List<Appender> logAppenders) {
		for(Appender appender : logAppenders) {
			addAppender(appender);
		}
	}
	
	@ManagedAttribute(description="Determines if this analyser is registered")
	public synchronized boolean isAnalyserRegistered() {
		return Logger.getRootLogger().isAttached(this);
	}

	@ManagedOperation(description="Remove this log analyser")
	public synchronized void removeAnalyser() {
		Logger.getRootLogger().removeAppender(this);
	}

	@ManagedOperation(description="Register this log analyser")
	public synchronized void registerAnalyser() {
		Logger.getRootLogger().addAppender(this);
	}
}
