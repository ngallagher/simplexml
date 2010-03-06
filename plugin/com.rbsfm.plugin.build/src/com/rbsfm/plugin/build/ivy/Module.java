package com.rbsfm.plugin.build.ivy;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
@Root
public class Module{
   private @Element Info info;
   @Root
   private static class Info{
      private @Attribute String revision;
      private @Attribute String module;
   }
   public String getModule(){
      return info.module;
   }
   public String getRevision(){
      String[] split=info.revision.split("-");
      if(split.length>0){
         return split[0];
      }
      return null;
   }
   public String getBranch(){
      String[] split=info.revision.split("-");
      if(split.length>1){
         return split[1];
      }
      return null;
   }
   public String getBranchRevision(){
      String[] split=info.revision.split("-");
      if(split.length>2){
         return split[2];
      }
      return null;
   }
}
