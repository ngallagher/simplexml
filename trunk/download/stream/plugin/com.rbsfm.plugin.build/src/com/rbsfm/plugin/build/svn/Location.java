package com.rbsfm.plugin.build.svn;
/**
 * The <code>Location</code> object is used to represent the location of a
 * resource within the repository. This provides a means to acquire certain
 * parts of the location such as the repository root and the path relative to
 * its parent. Providing these parts allows the location to be used without the
 * need to parse the absolute URI location.
 * @author Niall Gallagher
 * @see com.rbsfm.plugin.build.svn.Repository
 */
public class Location{
   public final String path;
   public final String root;
   public final String prefix;
   public Location(String path,String root,String prefix){
      this.prefix=prefix;
      this.path=path;
      this.root=root;
   }
   public String getParent(){
      return String.format("%s/%s",root,prefix);
   }
   public String getAbsolutePath(){
      return String.format("%s/%s%s",root,prefix,path);
   }
   public String getRelativePath(){
      return path;
   }
   public String getRoot(){
      return root;
   }
}
