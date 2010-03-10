package com.rbsfm.plugin.build.svn;
public class RepositoryException extends Exception{
   public RepositoryException(String message,Object... args){
      super(String.format(message, args));
   }
}
