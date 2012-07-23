package com.yieldbroker.common.mail;

import static javax.mail.Message.RecipientType.TO;

import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;

@ManagedResource(description="Sends a mail over SMTP")
public class MailSender implements MailClient {
	
	private static final Logger LOG = Logger.getLogger(MailSender.class);
	
	private final Properties properties;
	private final Session session;	
	
	public MailSender(Properties properties) {
		this.session = Session.getDefaultInstance(properties); 
		this.properties = properties;
	}	
	
	@ManagedOperation(description="Send a mail")
	@ManagedOperationParameters({ 
		@ManagedOperationParameter(name="from", description="From address"),
		@ManagedOperationParameter(name="to", description="To address"),
		@ManagedOperationParameter(name="subject", description="Mail subject"),
		@ManagedOperationParameter(name="message", description="Mail message") 
	})
	public void send(String from, String to, String subject, String message) {
		send(new MailMessage(from, to, subject, message));
	}
	
	public void send(MailMessage mail) {
		MimeMessage message = new MimeMessage(session);
		
		try {
			MailType mailType = mail.getMailType();
			String contentType = mailType.getContentType();
			Address from = mail.getFrom();
			List<Address> to = mail.getTo();
			String subject = mail.getSubject();
			String body = mail.getMessage();
		
			for(Address address : to) {
				message.addRecipient(TO, address);
			}
			message.setFrom(from);
			message.setSubject(subject);
			
			if(mailType == MailType.TEXT) {
				message.setText(body);
			} else {
				message.setContent(body, contentType);
			}
			Transport.send(message);
		} catch (MessagingException e) {
			LOG.info("Could not sent mail message", e);
		}
				
	}
}
