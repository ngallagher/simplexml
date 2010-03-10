package com.rbsfm.plugin.build.svn;
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
