package com.rbsfm.plugin.build.repository;

import java.util.Date;

/**
 * This object is used to represent a change made to the file. It is
 * typically used to hold information taken from the repository log.
 * 
 * @author Niall Gallagher
 */
public class Change {
	public final String author;
	public final String message;
	public final String version;
	public final Date date;
	public Change(String author, String message, String version, Date date){
		this.author = author;
		this.message = message;
		this.version = version;
		this.date = date;
	}
}
