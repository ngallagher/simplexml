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
import static com.rbsfm.plugin.build.svn.LocationType.BRANCHES;
import static com.rbsfm.plugin.build.svn.LocationType.TAGS;
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
class Client implements Repository{
   private final SVNClientManager manager;
   private final Context context;
   public Client(Context context) throws Exception{
      this.manager=context.getClientManager();
      this.context=context;
   }
   public Location tag(File file,String tag,String message,boolean dryRun) throws Exception{
      return copy(file,tag,message,dryRun,TAGS);
   }
   public Location branch(File file,String branch,String message,boolean dryRun) throws Exception{
      return copy(file,branch,message,dryRun,BRANCHES);
   }
   private Location copy(File file,String prefix,String message,boolean dryRun,LocationType type) throws Exception{
      SVNCopyClient client=manager.getCopyClient();
      Location from=context.getLocation(file);
      String absolute=from.getAbsolutePath();
      Copy copy=LocationType.copy(absolute,prefix,type);
      if(!dryRun && !exists(copy.from())){
         SVNURL destination=SVNURL.parseURIDecoded(copy.to());
         SVNURL current=SVNURL.parseURIDecoded(copy.from());
         SVNCopySource source=new SVNCopySource(HEAD,HEAD,current);
         SVNCommitInfo info=client.doCopy(new SVNCopySource[]{source},
               destination,
               IS_MOVE,
               MAKE_PARENTS,
               FAIL_WHEN_EXISTS,
               message,
               null);
         info.getErrorMessage();
      }
      return copy.to;
   }
   private boolean exists(String location)throws Exception{
      ChangeLog log=new ChangeLog();
      SVNLogClient client=manager.getLogClient();
      client.doLog(SVNURL.parseURIEncoded(location),
            new String[]{},
            SVNRevision.create(0),
            HEAD,
            SVNRevision.create(0),           
            STOP_ON_COPY,
            DISCOVER_CHANGED_PATHS,
            INCLUDE_MERGED_REVISIONS,
            1,
            null,
            log);
      return !log.isEmpty();
   }
   public boolean commit(File file,String message) throws Exception{
      SVNCommitClient client=manager.getCommitClient();
      client.doCommit(new File[]{file},
            KEEP_LOCKS,
            message,
            null,
            new String[]{},
            KEEP_CHANGE_LIST,
            FORCE_COMMIT,
            INFINITY);
      return true;
   }
   public Info info(File file) throws Exception{
      SVNWCClient client=context.getLocalClient();
      SVNInfo info=client.doInfo(file,BASE);
      String location=info.getURL().getURIEncodedPath();
      long revision=info.getRevision().getNumber();
      return new Info(info.getAuthor(),location,revision);
   }
   public List<Change> log(File file) throws Exception{
      return log(file,CHANGE_LOG_DEPTH);
   }
   public List<Change> log(File file, long depth) throws Exception{
      ChangeLog log=new ChangeLog();
      Location repository=context.getLocation(file);
      SVNLogClient client=manager.getLogClient();
      String location=repository.getAbsolutePath();
      client.doLog(SVNURL.parseURIEncoded(location),
           new String[]{},
           SVNRevision.create(0),
           HEAD,
           SVNRevision.create(0),           
           STOP_ON_COPY,
           DISCOVER_CHANGED_PATHS,
           INCLUDE_MERGED_REVISIONS,
           depth,
           null,
           log);
      return Collections.unmodifiableList(log);
   }
   private Change lastChange(File file)throws Exception{
      List<Change> list=log(file,1);
      if(!list.isEmpty()) {
         return list.get(0);
      }
      return null;
   }
   public Status status(File file) throws Exception{
      SVNStatusClient client=manager.getStatusClient();
      SVNStatus status=client.doStatus(file,REMOTE_STATUS);
      if(status.getContentsStatus()==STATUS_MODIFIED){
         return Status.MODIFIED;
      }
      if(status.getContentsStatus()==STATUS_CONFLICTED){
         return Status.CONFLICT;
      }
      if(status.getContentsStatus()==STATUS_NORMAL){
         Change change=lastChange(file);
         Info info=info(file);
         if(change!=null){
            if(change.version>info.version) {
               return Status.STALE;
            }
         }
         return Status.NORMAL;
      }
      if(status.getContentsStatus()==LOCK_LOCKED){
         return Status.LOCKED;
      }
      return Status.OTHER;
   }
   public boolean update(File file) throws Exception{
      SVNUpdateClient client=manager.getUpdateClient();
      client.doUpdate(file,
            HEAD,
            INFINITY,
            ALLOW_OBSTRUCTIONS,
            DEPTH_IS_STICKY);
      return true;
   }
   private class ChangeLog extends ArrayList<Change> implements ISVNLogEntryHandler{
      public void handleLogEntry(SVNLogEntry entry) throws SVNException{
         String message=entry.getMessage();
         String author=entry.getAuthor();
         Date date=entry.getDate();
         long revision=entry.getRevision();
         add(new Change(author,message,revision,date));
      }
   }
}