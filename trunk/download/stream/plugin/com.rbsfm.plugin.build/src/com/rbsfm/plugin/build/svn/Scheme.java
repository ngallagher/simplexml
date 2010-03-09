package com.rbsfm.plugin.build.svn;
enum Scheme {
   SVN("svn"), HTTP("http");
   public String scheme;
   private Scheme(String scheme){
      this.scheme=scheme;
   }
   public static Scheme scheme(String location) throws RepositoryException{
      for(Scheme scheme:values()){
         if(location.startsWith(scheme.scheme)){
            return scheme;
         }
      }
      throw new RepositoryException("Invalid scheme for %s",location);
   }
}
