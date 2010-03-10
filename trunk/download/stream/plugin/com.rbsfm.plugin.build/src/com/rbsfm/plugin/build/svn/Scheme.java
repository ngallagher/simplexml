package com.rbsfm.plugin.build.svn;
/**
 * The <code>Scheme</code> is used to identify the scheme that the repository is
 * using. This is required to ensure that we initialize the client correctly.
 * @author Niall Gallagher
 * @see com.rbsfm.plugin.build.svn.Subversion#login
 */
public enum Scheme {
   SVN("svn"), HTTP("http");
   public String scheme;
   private Scheme(String scheme){
      this.scheme = scheme;
   }
   public static Scheme scheme(String location) throws RepositoryException{
      for(Scheme scheme : values()){
         if(location.startsWith(scheme.scheme)){
            return scheme;
         }
      }
      throw new RepositoryException("Invalid scheme for %s", location);
   }
}
