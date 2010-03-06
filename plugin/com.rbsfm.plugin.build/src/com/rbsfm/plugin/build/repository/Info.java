package com.rbsfm.plugin.build.repository;
public class Info{
   public final String location;
   public final String version;
   public final String author;
   public Info(String version,String author,String location){
      this.location=location;
      this.version=version;
      this.author=author;
   }
   public String toString(){
      return location;
   }
}
