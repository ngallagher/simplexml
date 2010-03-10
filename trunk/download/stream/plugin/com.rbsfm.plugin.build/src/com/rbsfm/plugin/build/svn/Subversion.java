package com.rbsfm.plugin.build.svn;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
public final class Subversion{
   public static Repository login(Scheme scheme,String login,String password) throws Exception{
      Context context=new Context(scheme,login,password);
      if(scheme==Scheme.HTTP){
         DAVRepositoryFactory.setup();
      }else{
         SVNRepositoryFactoryImpl.setup();
      }
      return new Client(context);
   }
}