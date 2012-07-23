package com.yieldbroker.common.log;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

import com.yieldbroker.common.mail.MailClient;
import com.yieldbroker.common.mail.MailMessage;

@ManagedResource(description="Appender that enables interesting log events to be mailed")
public class LogEventAlerter extends LogEventFilter {

	private final PatternLayout patternLayout;
	private final AtomicInteger alertThreshold;
	private final AtomicInteger failureCount;
	private final AtomicInteger alertCount;
	private final StringBuilder logEvents;
	private final MailMessage mailMessage;
	private final MailClient mailClient;

	public LogEventAlerter(MailClient mailClient, MailMessage mailMessage, String logFormat) {	
		this(mailClient, mailMessage, logFormat, 0);
	}
	
	public LogEventAlerter(MailClient mailClient, MailMessage mailMessage, String logFormat, int alertThreshold) {
		this.alertThreshold = new AtomicInteger(alertThreshold);
		this.patternLayout = new PatternLayout(logFormat);
		this.failureCount = new AtomicInteger();
		this.alertCount = new AtomicInteger();
		this.logEvents = new StringBuilder();
		this.mailMessage = mailMessage;
		this.mailClient = mailClient;
	}
	
	@ManagedAttribute(description="Repeat frequency before mail is sent")
	public synchronized int getAlertThreshold() {
		return alertThreshold.get();	
	}
	
	@ManagedOperation(description="Set repeat frequency before mail is sent")
	@ManagedOperationParameters({ 
		@ManagedOperationParameter(name="repeatCount", description="Threshold for repeat events") 
	})
	public synchronized void setAlertThreshold(int repeatCount) {
		alertThreshold.set(repeatCount);
	}

	@Override
	public synchronized void append(LoggingEvent event) {
		try {
			Pattern eventFilter = getFilterPattern();
			
			if(eventFilter != null) {
				String formattedEvent = patternLayout.format(event);
				Matcher matcher = eventFilter.matcher(formattedEvent);
				
				if(matcher.matches()) {
					logEvents.append(formattedEvent);
					alertCount.getAndIncrement();
				}
				int eventThreshold = alertThreshold.get();
				int eventCount = alertCount.get();
				
				if(eventCount >= eventThreshold) {				
					MailMessage alertMessage = mailMessage.getMessageCopy();
					String logHistory = logEvents.toString();
					
					alertMessage.setMessage(logHistory);
					alertCount.getAndSet(0);
					logEvents.setLength(0);
					mailClient.send(alertMessage);
				}	
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
		alertCount.getAndSet(0);
		logEvents.setLength(0);
	}
}
