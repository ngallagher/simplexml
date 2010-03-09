package com.rbsfm.plugin.build.svn;
import java.io.File;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNInfo;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
class Context{
   private final String login;
   private final String password;
   private final String repository;
   private final Scheme scheme;
   public Context(String repository,String login,String password) throws Exception{
      this.scheme=Scheme.scheme(repository);
      this.repository=repository;
      this.login=login;
      this.password=password;
   }
   public String getLogin(){
      return login;
   }
   public String getPassword(){
      return password;
   }
   public String getRepository(){
      return repository;
   }
   public Scheme getScheme(){
      return scheme;
   }
   public SVNWCClient getLocalClient(){
      ISVNOptions options=SVNWCUtil.createDefaultOptions(true);
      ISVNAuthenticationManager manager=SVNWCUtil.createDefaultAuthenticationManager(login,password);
      return new SVNWCClient(manager,options);
   }
   public SVNURL getLocation(File file) throws Exception{
      SVNWCClient client=getLocalClient();
      SVNInfo info=client.doInfo(file,SVNRevision.BASE);
      return info.getURL();
   }
}
