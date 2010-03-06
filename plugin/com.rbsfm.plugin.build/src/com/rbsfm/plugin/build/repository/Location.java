package com.rbsfm.plugin.build.repository;
public class Location{
   public final String path;
   public final String root;
   public final String prefix;
   public Location(String path,String root,String prefix){
      this.prefix=prefix;
      this.path=path;
      this.root=root;
   }
   public String toString(){
      return String.format("%s/%s%s",root,prefix,path);
   }
}
