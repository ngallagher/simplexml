package com.rbsfm.plugin.build.repository.svn;
import static org.tmatesoft.svn.core.wc.SVNStatusType.STATUS_CONFLICTED;
import static org.tmatesoft.svn.core.wc.SVNStatusType.STATUS_MODIFIED;
import static org.tmatesoft.svn.core.SVNDepth.INFINITY;
import static org.tmatesoft.svn.core.wc.SVNRevision.BASE;
import static org.tmatesoft.svn.core.wc.SVNRevision.HEAD;
import java.io.File;
import java.util.Collections;
import java.util.List;
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
import com.rbsfm.plugin.build.repository.Change;
import com.rbsfm.plugin.build.repository.Info;
import com.rbsfm.plugin.build.repository.Location;
import com.rbsfm.plugin.build.repository.Repository;
import com.rbsfm.plugin.build.repository.Status;
class SubversionClient implements Repository{
   private final SVNClientManager manager;
   private final Context context;
   public SubversionClient(SVNClientManager manager,Context context) throws Exception{
      this.manager=manager;
      this.context=context;
   }
   public Location tag(File file,String tag,boolean dryRun) throws Exception{
      return copy(file,tag,dryRun,LocationType.BRANCHES);
   }
   public Location branch(File file,String branch,boolean dryRun) throws Exception{
      return copy(file,branch,dryRun,LocationType.BRANCHES);
   }
   private Location copy(File file,String prefix,boolean dryRun,LocationType type) throws Exception{
      SVNCopyClient client=manager.getCopyClient();
      SVNURL current=context.getLocation(file);
      Location location=LocationType.build(current.toDecodedString(),prefix,type);
      String target=location.toString();
      if(!dryRun){
         SVNURL destination=SVNURL.parseURIDecoded(target);
         SVNCopySource source=new SVNCopySource(HEAD,HEAD,current);
         client.doCopy(new SVNCopySource[]{source},destination,false,true,true,"",null);
      }
      return location;
   }
   public boolean commit(File file) throws Exception{
      SVNCommitClient client=manager.getCommitClient();
      client.doCommit(new File[]{file},true,"",null,new String[]{},false,false,INFINITY);
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
      SVNRevision revision=SVNRevision.create(0);
      SVNLogClient client=manager.getLogClient();
      client.doLog(repository,new String[]{},revision,revision,HEAD,true,true,false,20,null,log);
      return Collections.unmodifiableList(log);
   }
   public Status status(File file) throws Exception{
      SVNStatusClient client=manager.getStatusClient();
      SVNStatus status=client.doStatus(file,true);
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
      client.doUpdate(file,HEAD,INFINITY,true,true);
      return true;
   }
}