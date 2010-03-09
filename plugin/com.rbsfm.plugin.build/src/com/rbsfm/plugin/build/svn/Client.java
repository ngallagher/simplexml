package com.rbsfm.plugin.build.svn;
import static com.rbsfm.plugin.build.svn.Configuration.ALLOW_OBSTRUCTIONS;
import static com.rbsfm.plugin.build.svn.Configuration.CHANGE_LOG_DEPTH;
import static com.rbsfm.plugin.build.svn.Configuration.COMMIT_MESSAGE;
import static com.rbsfm.plugin.build.svn.Configuration.COPY_MESSAGE;
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
import static org.tmatesoft.svn.core.wc.SVNStatusType.STATUS_CONFLICTED;
import static org.tmatesoft.svn.core.wc.SVNStatusType.STATUS_MODIFIED;
import java.io.File;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.tmatesoft.svn.core.ISVNLogEntryHandler;
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
   public Client(SVNClientManager manager,Context context) throws Exception{
      this.manager=manager;
      this.context=context;
   }
   public Location tag(File file,String tag,boolean dryRun) throws Exception{
      return copy(file,tag,dryRun,TAGS);
   }
   public Location branch(File file,String branch,boolean dryRun) throws Exception{
      return copy(file,branch,dryRun,BRANCHES);
   }
   private Location copy(File file,String prefix,boolean dryRun,LocationType type) throws Exception{
      SVNCopyClient client=manager.getCopyClient();
      SVNURL current=context.getLocation(file);
      String decoded=current.toDecodedString();
      Location location=LocationType.build(decoded,prefix,type);
      String target=location.toString();
      if(!dryRun){
         SVNURL destination=SVNURL.parseURIDecoded(target);
         SVNCopySource source=new SVNCopySource(HEAD,HEAD,current);
         client.doCopy(new SVNCopySource[]{source},
               destination,
               IS_MOVE,
               MAKE_PARENTS,
               FAIL_WHEN_EXISTS,
               COPY_MESSAGE,
               null);
      }
      return location;
   }
   public boolean commit(File file) throws Exception{
      SVNCommitClient client=manager.getCommitClient();
      client.doCommit(new File[]{file},
            KEEP_LOCKS,
            COMMIT_MESSAGE,
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
      return new Info(location,info.getRevision().toString(),info.getAuthor());
   }
   public List<Change> log(File file) throws Exception{
      ChangeLog log=new ChangeLog();
      SVNURL repository=context.getLocation(file);
      SVNLogClient client=manager.getLogClient();
      client.doLog(repository,
           new String[]{},
           SVNRevision.create(0),
           SVNRevision.create(0),
           HEAD,
           STOP_ON_COPY,
           DISCOVER_CHANGED_PATHS,
           INCLUDE_MERGED_REVISIONS,
           CHANGE_LOG_DEPTH,
           null,
           log);
      return Collections.unmodifiableList(log);
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
      return Status.OK;
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
   private class ChangeLog extends LinkedList<Change> implements ISVNLogEntryHandler{
      public void handleLogEntry(SVNLogEntry entry) throws SVNException{
         String message=entry.getMessage();
         String author=entry.getAuthor();
         Date date=entry.getDate();
         long revision=entry.getRevision();
         addFirst(new Change(author,message,revision,date));
      }
   }
}