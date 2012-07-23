package com.yieldbroker.common.mail;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource(description="Mail message")
public class MailMessage {

	private MailType mailType;
	private String message;
	private String subject;
	private List<String> to;
	private String from;
	
	public MailMessage(String from, String to, String subject, String message) {
		this(from, to, subject, message, MailType.TEXT);
	}
	
	public MailMessage(String from, List<String> to, String subject, String message) {
		this(from, to, subject, message, MailType.TEXT);
	}
	
	public MailMessage(String from, String to, String subject, String message, MailType mailType) {
		this(from, Arrays.asList(to), subject, message, mailType);
	}
	
	public MailMessage(String from, List<String> to, String subject, String message, MailType mailType) {
		this.mailType = mailType;
		this.message = message;
		this.subject = subject;		
		this.from = from;
		this.to = to;
	}	
	
	@ManagedOperation(description="Set the mail subject")
	@ManagedOperationParameters({ 
		@ManagedOperationParameter(name="subject", description="Mail message subject") 
	})
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	@ManagedAttribute(description="Mail message subject")
	public String getSubject() {
		return subject;
	}
	
	@ManagedOperation(description="Set a single recipient")
	@ManagedOperationParameters({ 
		@ManagedOperationParameter(name="recipient", description="To address") 
	})
	public void setTo(String recipient) {
		to.clear();
		to.add(recipient);
	}
	
	@ManagedOperation(description="Add a recipient")
	@ManagedOperationParameters({ 
		@ManagedOperationParameter(name="recipient", description="To address") 
	})
	public void addTo(String recipient) {
		to.add(recipient);
	}	
	
	public MailMessage getMessageCopy() {
		return new MailMessage(from, to, subject, message, mailType);
	}
	
	public Address getFrom() throws AddressException {
		return new InternetAddress(from);
	}
	
	public List<Address> getTo() throws AddressException {
		List<Address> addresses = new LinkedList<Address>();
		
		for(String recepient : to) {
			Address address = new InternetAddress(recepient);
			addresses.add(address);
		}
		return addresses;
	}
	
	public void setMailType(MailType mailType) {
		this.mailType = mailType;
	}
	
	public MailType getMailType() {
		return mailType;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
