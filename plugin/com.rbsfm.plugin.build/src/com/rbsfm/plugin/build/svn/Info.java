package com.rbsfm.plugin.build.svn;
public class Info{
   public final String location;
   public final String author;
   public final long version;
   public Info(String author,String location,long version){
      this.location=location;
      this.version=version;
      this.author=author;
   }
   public String toString(){
      return location;
   }
}
