package com.rbsfm.plugin.build.ivy;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
@Root
public class Project{
   @Element
   private Info info;
   @ElementList(required=false) 
   private List<Dependency> dependencies;
   @Root
   private static class Info{
      private @Attribute String module;
      private @Attribute String organisation;
   }
   public String getProject(){
      return info.module;
   }
   public String getOrganisation(){
      return info.organisation;
   }
   public List<Dependency> getDependencies(){
      return dependencies;
   }  
}
