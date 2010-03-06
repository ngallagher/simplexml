package com.rbsfm.plugin.build.repository.subversion;

import java.io.File;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.tmatesoft.svn.core.ISVNLogEntryHandler;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNInfo;
import org.tmatesoft.svn.core.wc.SVNLogClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.rbsfm.plugin.build.repository.Change;
import com.rbsfm.plugin.build.repository.Info;
import com.rbsfm.plugin.build.repository.Repository;
import com.rbsfm.plugin.build.repository.Status;

public final class Subversion {
   static {
      SVNRepositoryFactoryImpl.setup();
   }
   public static Repository login(String login, String password) {
      return new Implementation(login, password);
   }
   private static class Implementation implements Repository {
      private final String login;
      private final String password;
      public Implementation(String login, String password){
         this.login = login;
         this.password = password;
      }
      private SVNWCClient local() {
         ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
         ISVNAuthenticationManager manager = SVNWCUtil.createDefaultAuthenticationManager(login, password);
         return new SVNWCClient(manager, options);
      }
      private SVNClientManager remote() {
         DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(true);
         return SVNClientManager.newInstance(options, login, password);
      }
      private SVNURL root(File file) throws Exception {
         SVNWCClient client = local();
         SVNInfo info = client.doInfo(file, SVNRevision.BASE);
         return info.getRepositoryRootURL();
      }
      public void tag(File file, String tag) throws Exception {

      }

      public void branch(File file, String branch) throws Exception {
      }

      public void commit(File file, String tag) throws Exception {
      }

      public Info info(File file) throws Exception {
         SVNWCClient client = local();
         SVNInfo info = client.doInfo(file, SVNRevision.BASE);
         return new Info(
               info.getRepositoryRootURL().toDecodedString(),
               info.getRevision().toString(),
               info.getAuthor(),
               info.getURL().getURIEncodedPath());
      }
      
      public List<Change> log(File file) throws Exception {
         ChangeLog log = new ChangeLog();
         SVNClientManager clientManager = remote();
         SVNURL repository = root(file);
         SVNRevision revision = SVNRevision.create(0);
         SVNLogClient client = clientManager.getLogClient();       
         client.doLog(
               repository, 
               new String[]{}, 
               revision, 
               revision, 
               SVNRevision.HEAD, 
               true, 
               true, 
               true, 
               Long.MAX_VALUE, 
               null, 
               log);   
         return log;
      }

      public Status status(File file) throws Exception {
         return null;
      }

      public void update(File file) throws Exception {
         SVNClientManager manager = remote();
         SVNUpdateClient client = manager.getUpdateClient();       
         client.doUpdate(file, SVNRevision.HEAD, true);
      }
   }
   private static class ChangeLog extends LinkedList<Change> implements ISVNLogEntryHandler {
      public void handleLogEntry(SVNLogEntry entry) throws SVNException {
         String message = entry.getMessage();
         String author = entry.getAuthor();
         Date date = entry.getDate();
         long revision = entry.getRevision();
         addFirst(new Change(author, message, revision, date));
      } 
   }
}