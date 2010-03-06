package com.rbsfm.plugin.build.repository.svn;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
import com.rbsfm.plugin.build.repository.Repository;
public final class Subversion{
   private static Repository REPOSITORY;
   public static synchronized Repository login(String repository,String login,String password) throws Exception{
      if(REPOSITORY==null){
         Context context=new Context(repository,login,password);
         if(context.getScheme()==Scheme.HTTP){
            DAVRepositoryFactory.setup();
         }else{
            SVNRepositoryFactoryImpl.setup();
         }
         DefaultSVNOptions options=SVNWCUtil.createDefaultOptions(true);
         SVNClientManager manager=SVNClientManager.newInstance(options,login,password);
         REPOSITORY=new SubversionClient(manager,context);
      }
      return REPOSITORY;
   }
}