package com.rbsfm.plugin.build.repository.subversion;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNInfo;
import org.tmatesoft.svn.core.wc.SVNLogClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCClient;

import com.rbsfm.plugin.build.repository.Change;
import com.rbsfm.plugin.build.repository.Info;
import com.rbsfm.plugin.build.repository.Location;
import com.rbsfm.plugin.build.repository.Repository;
import com.rbsfm.plugin.build.repository.Status;

class Implementation implements Repository {
   private Helper helper;

   public Implementation(String login, String password) {
      this.helper = new Helper(login, password);
   }

   public Location tag(File file, String tag) throws Exception {
      return null;
   }

   public Location branch(File file, String branch) throws Exception {
      return null;
   }

   public boolean commit(File file, String tag) throws Exception {
      return false;
   }

   public Info info(File file) throws Exception {
      SVNWCClient client = helper.getLocalClient();
      SVNInfo info = client.doInfo(file, SVNRevision.BASE);
      return new Info(info.getRepositoryRootURL().toDecodedString(), info
            .getRevision().toString(), info.getAuthor(), info.getURL()
            .getURIEncodedPath());
   }

   public List<Change> log(File file) throws Exception {
      ChangeLog log = new ChangeLog();
      SVNClientManager clientManager = helper.getRemoteClient();
      SVNURL repository = helper.getLocation(file);
      SVNRevision revision = SVNRevision.create(0);
      SVNLogClient client = clientManager.getLogClient();
      client.doLog(
            repository, // location
            new String[] {}, revision, revision, SVNRevision.HEAD, true, true,
            false, // merge information
            Long.MAX_VALUE, // depth
            null, // properties
            log); // change log
      return Collections.unmodifiableList(log);
   }

   public Status status(File file) throws Exception {
      return null;
   }

   public boolean update(File file) throws Exception {
      SVNClientManager manager = helper.getRemoteClient();
      SVNUpdateClient client = manager.getUpdateClient();
      client.doUpdate(file, SVNRevision.HEAD, SVNDepth.INFINITY, true, true);
      return true;
   }
}