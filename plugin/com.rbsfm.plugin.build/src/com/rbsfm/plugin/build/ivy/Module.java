package com.rbsfm.plugin.build.ivy;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
@Root
public class Module{
   @Element
   private Info info;
   @ElementList(required=false) 
   private List<Dependency> dependencies;
   @Root
   private static class Info{
      private @Attribute String revision;
      private @Attribute String module;
      private @Attribute String organisation;
   }
   public String getModule(){
      return info.module;
   }
   public String getOrganisation(){
      return info.organisation;
   } 
   public List<Dependency> getDependencies(){
      return dependencies;
   }  
   public String getRevision(){
      String[] split = info.revision.split("-");
      if(split.length > 0){
         return split[0];
      }
      return null;
   }
   public String getBranch(){
      String[] split = info.revision.split("-");
      if(split.length > 1){
         return split[1];
      }
      return null;
   }
   public String getBranchRevision(){
      String[] split = info.revision.split("-");
      if(split.length > 2){
         return split[2];
      }
      return null;
   }
}
