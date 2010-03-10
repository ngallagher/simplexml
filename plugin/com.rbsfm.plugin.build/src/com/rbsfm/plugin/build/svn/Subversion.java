package com.rbsfm.plugin.build.svn;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
/**
 * The <code>Subversion</code> object is used login in to a subversion
 * repository and interact with it. This will return an implementation of the
 * {@link Repository} interface which can be used to perform normal operations
 * on the repository.
 * @author Niall Gallagher
 */
public final class Subversion{
   public static Repository login(Scheme scheme, String login, String password) throws Exception{
      Context context = new Context(scheme, login, password);
      if(scheme == Scheme.HTTP){
         DAVRepositoryFactory.setup();
      }else{
         SVNRepositoryFactoryImpl.setup();
      }
      return new Client(context);
   }
}