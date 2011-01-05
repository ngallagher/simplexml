package com.rbsfm.plugin.build.svn;
import java.io.File;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNInfo;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
class Context{
   private final DefaultSVNOptions options;
   private final SVNClientManager manager;
   private final String login;
   private final String password;
   private final Scheme scheme;
   public Context(Scheme scheme,String login,String password) throws Exception{
      this.options = SVNWCUtil.createDefaultOptions(true);
      this.manager = SVNClientManager.newInstance(options, login, password);
      this.scheme = scheme;
      this.login = login;
      this.password = password;
   }
   public SVNClientManager getClientManager(){
      return manager;
   }
   public SVNWCClient getLocalClient(){
      ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
      ISVNAuthenticationManager manager = SVNWCUtil.createDefaultAuthenticationManager(login, password);
      return new SVNWCClient(manager, options);
   }
   public Location getLocation(File file) throws Exception{
      SVNWCClient client = getLocalClient();
      SVNInfo info = client.doInfo(file, SVNRevision.BASE);
      String location = info.getURL().toDecodedString();
      if(file.isDirectory()) {
        return LocationParser.parse(location + "/.");
      }
      return LocationParser.parse(location);
   }
   public String getLogin(){
      return login;
   }
   public String getPassword(){
      return password;
   }
   public Scheme getScheme(){
      return scheme;
   }
}
