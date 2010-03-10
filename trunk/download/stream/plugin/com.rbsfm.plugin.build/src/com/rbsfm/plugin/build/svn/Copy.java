package com.rbsfm.plugin.build.svn;
public class Copy {
   public final Location from;
   public final Location to;
   public Copy(Location from,Location to){
      this.from=from;
      this.to=to;
   }
   public String from(){
      return from.getParent();
   }
   public String to(){
      return to.getParent();
   }
}
