package com.yieldbroker.common.log;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

import com.yieldbroker.common.collections.LeastRecentlyUsedMap;

@ManagedResource(description="Appender that enables recent log messages to be viewed by JMX")
public class LogEventCollector extends LogEventFilter {

	private final LeastRecentlyUsedMap<Long, LoggingEvent> recentEvents;
	private final AtomicInteger eventSequenceId;
	private final AtomicInteger failureCount;
	private final PatternLayout patternLayout;
	private final int eventCapacity;
	
	public LogEventCollector(String pattern) {
		this(pattern, 200);
	}

	public LogEventCollector(String pattern, int eventCapacity) {
		this.recentEvents = new LeastRecentlyUsedMap<Long, LoggingEvent>(eventCapacity);		
		this.patternLayout = new PatternLayout(pattern);
		this.eventSequenceId = new AtomicInteger();
		this.failureCount = new AtomicInteger();
		this.eventCapacity = eventCapacity;
	}

	@ManagedOperation(description="Show recent logging events")
	public synchronized String showRecentEvents() {
		StringBuilder builder = new StringBuilder();
		Set<Long> eventIds = recentEvents.keySet();

		builder.append("<PRE>");

		for (Long sequenceId : eventIds) {
			LoggingEvent loggingEvent = recentEvents.get(sequenceId);
			String formattedEvent = patternLayout.format(loggingEvent);

			builder.append(formattedEvent);
		}
		builder.append("</PRE>");
		return builder.toString();
	}
	
	@ManagedOperation(description="Clears the recent events")
	public synchronized void clearRecentEvents() {
		recentEvents.clear();
	}
	
	public synchronized List<LoggingEvent> getLoggingEvents() {
		List<LoggingEvent> loggingEvents = new ArrayList<LoggingEvent>();
		
		for(Long time : recentEvents.keySet()) {
			LoggingEvent event = recentEvents.get(time);
			loggingEvents.add(event);
		}
		return loggingEvents;
	}

	@Override
	protected synchronized void append(LoggingEvent event) {
		try {
			Pattern eventFilter = getFilterPattern();
			long sequenceId = eventSequenceId.getAndIncrement();
			
			if(eventFilter != null) {
				String formattedEvent = patternLayout.format(event);
				Matcher matcher = eventFilter.matcher(formattedEvent);
				
				if(matcher.matches()) {
					recentEvents.put(sequenceId, event);
				}			
			} else {
				recentEvents.put(sequenceId, event);
			}
		} catch(Throwable e) {
			failureCount.getAndIncrement();
		}
	}	

	@Override
	public synchronized boolean requiresLayout() {
		return false;
	}

	@Override
	public synchronized void close() {
		recentEvents.clear();
	}

}
