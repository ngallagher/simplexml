package com.rbsfm.plugin.build.repository;

import java.io.File;
import java.util.List;

/**
 * The <code>Repository</code> representation interface to an SCM. This is
 * used to simplify common commands used to branch, tag, and commit files.
 * Also information on the file can be acquired.
 * 
 * @author Niall Gallagher
 */
public interface Repository {
	public void tag(File file, String tag) throws Exception;
	public void branch(File file, String branch) throws Exception;
	public void commit(File file, String tag) throws Exception;
	public void update(File file) throws Exception;
	public Status status(File file) throws Exception;
	public List<Change> log(File file) throws Exception;
	public Info info(File file) throws Exception;
}
