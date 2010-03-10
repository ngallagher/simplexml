package com.rbsfm.plugin.build.svn;
/**
 * The <code>Info</code> object represents information on the working copy of a
 * file. This is used to determine the repository the resource belongs to as
 * well as the version the resource is on.
 * @author Niall Gallagher
 * @see com.rbsfm.plugin.build.svn.Repository
 */
public class Info{
   public final String location;
   public final String author;
   public final long version;
   public Info(String author,String location,long version){
      this.location = location;
      this.version = version;
      this.author = author;
   }
   public String toString(){
      return location;
   }
}
