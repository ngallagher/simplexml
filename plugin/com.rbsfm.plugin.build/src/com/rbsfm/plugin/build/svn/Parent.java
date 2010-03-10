package com.rbsfm.plugin.build.svn;
enum Parent {
   TAGS("tags"), BRANCHES("branches"), TRUNK("trunk");
   public final String type;
   public final int size;
   private Parent(String type){
      this.size = type.length();
      this.type = type;
   }
}
