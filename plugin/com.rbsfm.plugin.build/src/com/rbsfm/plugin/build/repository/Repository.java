package com.rbsfm.plugin.build.repository;

import java.io.File;
import java.util.List;

/**
 * The <code>Repository</code> representation interface to an SCM. This is
 * used to simplify common commands used to branch, tag, and commit resources.
 * Also information on the resource can be acquired.
 * 
 * @author Niall Gallagher
 */
public interface Repository {
   public boolean update(File resource) throws Exception;
   public boolean commit(File resource, String tag) throws Exception;
	public Location tag(File resource, String tag) throws Exception;
	public Location branch(File resource, String branch) throws Exception;
	public Status status(File resource) throws Exception;
	public List<Change> log(File resource) throws Exception;
	public Info info(File resource) throws Exception;
}
