package com.yieldbroker.common.log;

import java.util.regex.Pattern;

import org.apache.log4j.AppenderSkeleton;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource(description="Filter for log events")
public abstract class LogEventFilter extends AppenderSkeleton {
	
	private Pattern eventFilter;
	
	protected LogEventFilter() {
		super();
	}
	
	@ManagedAttribute(description="Regular rexpression used for filtering")
	public synchronized String getEventFilter() {
		if(eventFilter != null) {
			return eventFilter.pattern();
		}
		return null;
	}	
	
	@ManagedOperation(description="Filter events based on a regular expression")
	@ManagedOperationParameters({ 
		@ManagedOperationParameter(name="pattern", description="The regular expresion to use") 
	})
	public synchronized void setEventFilter(String pattern) {
		if(pattern != null) {
			String extendedPattern = String.format(".*%s.*", pattern);
			Pattern filterPattern = Pattern.compile(extendedPattern, Pattern.MULTILINE | Pattern.DOTALL);
		
			eventFilter = filterPattern;
		}
	}

	@ManagedOperation(description="Clear the event filter")
	public synchronized void clearEventFilter() {
		eventFilter = null;
	} 	
	
	public synchronized Pattern getFilterPattern() {
		return eventFilter;
	}
}
