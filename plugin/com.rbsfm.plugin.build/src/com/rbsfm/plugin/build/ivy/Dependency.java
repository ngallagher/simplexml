package com.rbsfm.plugin.build.ivy;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
@Root
public class Dependency {
   private @Attribute String name;
   private @Attribute String rev;
   public String getName(){
      return name;
   }
   public String getRevision(){
      return rev;
   }
}
