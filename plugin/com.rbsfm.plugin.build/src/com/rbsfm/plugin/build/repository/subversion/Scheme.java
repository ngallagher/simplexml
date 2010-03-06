package com.rbsfm.plugin.build.repository.subversion;

public enum Scheme {
   SVN("svn"),
   HTTP("http");
   public String scheme;
   private Scheme(String scheme) {
      this.scheme = scheme;
   }
   public static Scheme scheme(String location) throws Exception{
      for(Scheme scheme : values()){
         if(location.startsWith(scheme.scheme)) {
            return scheme;
         }
      }
      throw new Exception("Invalid scheme");
   }
}
