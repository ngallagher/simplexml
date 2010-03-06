package com.rbsfm.plugin.build.repository.subversion;

import java.io.File;

import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNInfo;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class Helper {
   
   private final String login;
   private final String password;
   
   public Helper(String login, String password){
      this.login = login;
      this.password = password;
   }
   
   public SVNWCClient getLocalClient() {
      ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
      ISVNAuthenticationManager manager = SVNWCUtil.createDefaultAuthenticationManager(login, password);
      return new SVNWCClient(manager, options);
   }
   
   public SVNClientManager getRemoteClient() {
      DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(true);
      return SVNClientManager.newInstance(options, login, password);
   } 
   
   public SVNURL getLocation(File file) throws Exception {
      SVNWCClient client = getLocalClient();
      SVNInfo info = client.doInfo(file, SVNRevision.BASE);
      return info.getURL();
   }
}
