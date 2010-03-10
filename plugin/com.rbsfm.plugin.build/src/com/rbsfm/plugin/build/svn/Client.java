package com.rbsfm.plugin.build.svn;
import static com.rbsfm.plugin.build.svn.Configuration.ALLOW_OBSTRUCTIONS;
import static com.rbsfm.plugin.build.svn.Configuration.CHANGE_LOG_DEPTH;
import static com.rbsfm.plugin.build.svn.Configuration.DEPTH_IS_STICKY;
import static com.rbsfm.plugin.build.svn.Configuration.DISCOVER_CHANGED_PATHS;
import static com.rbsfm.plugin.build.svn.Configuration.FAIL_WHEN_EXISTS;
import static com.rbsfm.plugin.build.svn.Configuration.FORCE_COMMIT;
import static com.rbsfm.plugin.build.svn.Configuration.INCLUDE_MERGED_REVISIONS;
import static com.rbsfm.plugin.build.svn.Configuration.IS_MOVE;
import static com.rbsfm.plugin.build.svn.Configuration.KEEP_CHANGE_LIST;
import static com.rbsfm.plugin.build.svn.Configuration.KEEP_LOCKS;
import static com.rbsfm.plugin.build.svn.Configuration.MAKE_PARENTS;
import static com.rbsfm.plugin.build.svn.Configuration.REMOTE_STATUS;
import static com.rbsfm.plugin.build.svn.Configuration.STOP_ON_COPY;
import static com.rbsfm.plugin.build.svn.Parent.BRANCHES;
import static com.rbsfm.plugin.build.svn.Parent.TAGS;
import static org.tmatesoft.svn.core.SVNDepth.INFINITY;
import static org.tmatesoft.svn.core.wc.SVNRevision.BASE;
import static org.tmatesoft.svn.core.wc.SVNRevision.HEAD;
import static org.tmatesoft.svn.core.wc.SVNStatusType.LOCK_LOCKED;
import static org.tmatesoft.svn.core.wc.SVNStatusType.STATUS_CONFLICTED;
import static org.tmatesoft.svn.core.wc.SVNStatusType.STATUS_MODIFIED;
import static org.tmatesoft.svn.core.wc.SVNStatusType.STATUS_NORMAL;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.tmatesoft.svn.core.ISVNLogEntryHandler;
import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCommitClient;
import org.tmatesoft.svn.core.wc.SVNCopyClient;
import org.tmatesoft.svn.core.wc.SVNCopySource;
import org.tmatesoft.svn.core.wc.SVNInfo;
import org.tmatesoft.svn.core.wc.SVNLogClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNStatusClient;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCClient;
/**
 * The <code>Client</code> represents an subversion repository client built on
 * top of the SVN Kit library. This implementation delegates to various specific
 * clients acquired from the SVN Kit client manager in order to execute the
 * requested operations. To keep the contract of the {@link Repository}
 * interface all operations are performed relative to the three parents.
 * <ul>
 * <li>trunk</li>
 * <li>branches</li>
 * <li>tags</li>
 * </ul>
 * Branching only direct descendants of these parents ensures operations are
 * simpler as only a prefix is required for any resource to be branched or
 * tagged. Also various other conventions are enforced such as the depth of log
 * messages acquired and that operations are performed relative to the HEAD.
 * @author Niall Gallagher
 * @see com.rbsfm.plugin.build.svn.LocationParser
 * @see com.rbsfm.plugin.build.svn.Subversion
 */
class Client implements Repository{
   private final SVNClientManager manager;
   private final Context context;
   /**
    * Constructor for the <code>Client</code> object. This requires a context
    * object containing the SVN Kit client manager. The client manager will be
    * used to perform the subversion operations requested using this.
    * @param context the context containing the SVN Kit client manager
    */
   public Client(Context context){
      this.manager = context.getClientManager();
      this.context = context;
   }
   public Location tag(File file, String tag, String message, boolean dryRun) throws Exception{
      return copy(file, tag, message, TAGS, dryRun);
   }
   public Location branch(File file, String branch, String message, boolean dryRun) throws Exception{
      return copy(file, branch, message, BRANCHES, dryRun);
   }
   /**
    * This performs a copy of the specified working copy resource to either the
    * branches or tags parent. In order to keep this operation as simple as
    * possible certain conventions are followed. Resources are only ever copied
    * to a direct descendant or either of the following parents.
    * <ul>
    * <li>branches</li>
    * <li>tags</li>
    * </ul>
    * If the resulting branch or tag already exists the will return quietly.
    * When complete the location of the newly created copy of the resource is
    * returned. All copies are from the HEAD of the working copy.
    * @param file this is the working copy that is to be branched by this
    * @param prefix this is the prefix of the branch or tag to be created
    * @param message the copy message that is used for the copy
    * @param dryRun if true the copy will not actually be performed
    * @param type the parent type used to determine the branch or tag path
    * @return this returns the new location of the resource that was copied
    */
   private Location copy(File file, String prefix, String message, Parent type, boolean dryRun) throws Exception{
      Location from = context.getLocation(file);
      Copy copy = LocationParser.copy(from, prefix, type);
      if(!dryRun && !exists(copy.from())){
         SVNCopyClient client = manager.getCopyClient();
         SVNURL destination = SVNURL.parseURIDecoded(copy.to());
         SVNURL current = SVNURL.parseURIDecoded(copy.from());
         SVNCopySource source = new SVNCopySource(HEAD, HEAD, current);
         SVNCopySource[] sources = new SVNCopySource[]{source};
         client.doCopy(sources, destination, IS_MOVE, MAKE_PARENTS, FAIL_WHEN_EXISTS, message, null);
      }
      return copy.to;
   }
   /**
    * In order to determine if a resource actually exists in the repository
    * without having a working copy of the resource this can be used. This is
    * used to determine if a copy operation can be performed without conflicting
    * with an existing branch or tag.
    * @param location this is an absolute path for some remote resource
    * @return this returns true if the specified absolute URI already exists
    */
   private boolean exists(String location) throws Exception{
      ChangeLog log = new ChangeLog();
      SVNLogClient client = manager.getLogClient();
      SVNURL address = SVNURL.parseURIEncoded(location);
      SVNRevision revision = SVNRevision.create(0);
      client.doLog(address, new String[]{}, revision, HEAD, revision, STOP_ON_COPY, DISCOVER_CHANGED_PATHS, INCLUDE_MERGED_REVISIONS, 1, null, log);
      return !log.isEmpty();
   }
   /**
    * This is used to commit any changes made in the working copy specified by
    * the file. If this returns false the changes may not have been made to the
    * file. The {@link status} method should be used to determine if the
    * resource still contains modifications.
    * @param file this is the resource that is to be committed
    * @param message this is the message that is used with the commit
    * @return returns true if the resource was committed without an error
    */
   public boolean commit(File file, String message) throws Exception{
      SVNCommitClient client = manager.getCommitClient();
      SVNCommitInfo info = client.doCommit(new File[]{file}, KEEP_LOCKS, message, null, new String[]{}, KEEP_CHANGE_LIST, FORCE_COMMIT, INFINITY);
      return info.getErrorMessage() != null;
   }
   /**
    * This acquires local information for the specified resource. The local
    * information can be acquired without a connection to the repository.
    * Information provides the version the resource is currently at as well as
    * the absolute URI location of the resource.
    * @param file this is the working copy to acquire the information for
    * @return this returns the local information for the specified file
    */
   public Info info(File file) throws Exception{
      SVNWCClient client = context.getLocalClient();
      SVNInfo info = client.doInfo(file, BASE);
      String location = info.getURL().getURIEncodedPath();
      long revision = info.getRevision().getNumber();
      return new Info(info.getAuthor(), location, revision);
   }
   public List<Change> log(File file) throws Exception{
      return log(file, CHANGE_LOG_DEPTH);
   }
   /**
    * This is used to acquire the changes for the resource specified. To limit
    * the number of changes acquired a depth can be specified. This will not
    * stop at the copy revision, which ensures changes can be acquired up to the
    * initial revision of the resource.
    * @param file this is the working copy resource to get the changes for
    * @param depth this is the maximum number of changes to be acquired
    * @return a list of the changes made during the lifetime of the resource
    */
   public List<Change> log(File file, long depth) throws Exception{
      ChangeLog log = new ChangeLog();
      Location repository = context.getLocation(file);
      String location = repository.getAbsolutePath();
      if(depth > 0){
         SVNLogClient client = manager.getLogClient();
         SVNRevision revision = SVNRevision.create(0);
         SVNURL address = SVNURL.parseURIEncoded(location);
         client.doLog(address, new String[]{}, revision, HEAD, revision, STOP_ON_COPY, DISCOVER_CHANGED_PATHS, INCLUDE_MERGED_REVISIONS, depth, null, log);
      }
      return Collections.unmodifiableList(log);
   }
   private Change lastChange(File file) throws Exception{
      List<Change> list = log(file, 1);
      if(!list.isEmpty()){
         return list.get(0);
      }
      return null;
   }
   public Status status(File file) throws Exception{
      SVNStatusClient client = manager.getStatusClient();
      SVNStatus status = client.doStatus(file, REMOTE_STATUS);
      if(status.getContentsStatus() == STATUS_MODIFIED){
         return Status.MODIFIED;
      }
      if(status.getContentsStatus() == STATUS_CONFLICTED){
         return Status.CONFLICT;
      }
      if(status.getContentsStatus() == STATUS_NORMAL){
         Change change = lastChange(file);
         Info info = info(file);
         if(change != null){
            if(change.version > info.version){
               return Status.STALE;
            }
         }
         return Status.NORMAL;
      }
      if(status.getContentsStatus() == LOCK_LOCKED){
         return Status.LOCKED;
      }
      return Status.OTHER;
   }
   /**
    * This is used to update the specified working copy to the HEAD. If the
    * resource is updated this returns true, if not this returns false. Any
    * resource that is currently on the HEAD returns false as its up to date.
    * @param file this is the working copy that is to be updated to HEAD
    * @return this returns true if the version of the resource has changed
    */
   public boolean update(File file) throws Exception{
      SVNUpdateClient client = manager.getUpdateClient();
      SVNWCClient local = context.getLocalClient();
      SVNInfo info = local.doInfo(file, BASE);
      long previous = info.getRevision().getNumber();
      long current = client.doUpdate(file, HEAD, INFINITY, ALLOW_OBSTRUCTIONS, DEPTH_IS_STICKY);
      return previous != current;
   }
   private class ChangeLog extends ArrayList<Change> implements ISVNLogEntryHandler{
      public void handleLogEntry(SVNLogEntry entry) throws SVNException{
         String message = entry.getMessage();
         String author = entry.getAuthor();
         Date date = entry.getDate();
         long revision = entry.getRevision();
         add(new Change(author, message, revision, date));
      }
   }
}