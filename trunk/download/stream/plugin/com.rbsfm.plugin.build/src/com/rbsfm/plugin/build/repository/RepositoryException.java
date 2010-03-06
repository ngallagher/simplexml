package com.rbsfm.plugin.build.repository;
@SuppressWarnings("serial")
public class RepositoryException extends Exception{
   public RepositoryException(String message,Object...args){
      super(String.format(message,args));
   }
}
