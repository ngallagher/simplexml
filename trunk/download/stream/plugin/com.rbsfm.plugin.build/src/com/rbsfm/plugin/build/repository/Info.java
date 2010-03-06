package com.rbsfm.plugin.build.repository;

/**
 * This object is used to represent information about a file. It
 * determines whether the file is in sync with the repository.
 * 
 * @author Niall Gallagher
 */
public class Info {
	public final String repository;
	public final String version;
	public final String author;
	public final String path;
	public Info(String repository, String version, String author, String path){
		this.repository = repository;
		this.version = version;
		this.author = author;
		this.path = path;
	}
}
