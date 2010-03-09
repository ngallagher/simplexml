package com.rbsfm.plugin.build.svn;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
public final class Subversion{
   public static Repository login(String repository,String login,String password) throws Exception{
      Context context=new Context(repository,login,password);
      if(context.getScheme()==Scheme.HTTP){
         DAVRepositoryFactory.setup();
      }else{
         SVNRepositoryFactoryImpl.setup();
      }
      DefaultSVNOptions options=SVNWCUtil.createDefaultOptions(true);
      SVNClientManager manager=SVNClientManager.newInstance(options,login,password);
      return new Client(manager,context);
   }
}