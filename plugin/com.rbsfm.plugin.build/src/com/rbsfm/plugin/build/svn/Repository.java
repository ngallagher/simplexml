package com.rbsfm.plugin.build.svn;
import java.io.File;
import java.util.List;
/**
 * The <code>Repository</code> interface is used to represent a client for a
 * subversion repository. This allows various common operations to be performed
 * on the repository. Various conventions are observed in order to simplify the
 * use of this interface. All operations are performed relative to the three
 * different parents available.
 * <ul>
 * <li>trunk</li>
 * <li>branches</li>
 * <li>tags</li>
 * </ul>
 * All tagging and branching is done relative to these parents. If a resource is
 * requested to be branched the parent, or a direct descendant of the parent is
 * branched, rather than the individual file. For example take the following.
 * <ul>
 * <li>svn://domain.com/svnroot/project/branches/branch-name/path/ivy.xml</li>
 * </ul>
 * In order to tag this file a prefix can be provided. This will tell the client
 * the direct descendant that is to be created as a result of tagging the
 * requested file. So if the prefix was "new-tag-name" the new tag would be.
 * <ul>
 * <li>svn://domain.com/svnroot/project/tags/new-branch-name/path/ivy.xml</li>
 * </ul>
 * Copying the entire parent tag or branch in such an operation simplifies these
 * operations. All other operations are performed on the working copy. Updates
 * will update to HEAD and logs will be taken from the HEAD to the initial
 * revision of the file.
 * @author Niall Gallagher
 */
public interface Repository{
   public boolean update(File resource) throws Exception;
   public boolean commit(File resource, String message) throws Exception;
   public Location tag(File resource, String prefix, String message, boolean dryRun) throws Exception;
   public Location branch(File resource, String prefix, String message, boolean dryRun) throws Exception;
   public Status status(File resource) throws Exception;
   public List<Change> log(File resource) throws Exception;
   public List<Change> log(File file, long depth) throws Exception;
   public Info info(File resource) throws Exception;
}
