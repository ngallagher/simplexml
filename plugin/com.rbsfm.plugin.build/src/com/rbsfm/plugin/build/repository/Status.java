package com.rbsfm.plugin.build.repository;

/**
 * Provides a status for resources under version control. The status
 * values here indicate whether the file is synchronized with the 
 * repository. This determines whether a commit or update is needed. 
 * 
 * @author Niall Gallagher
 */
public enum Status {
	MODIFIED,
	CONFLICT,
	STALE
}
