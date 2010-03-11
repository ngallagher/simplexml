package com.rbsfm.plugin.build.ivy;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
@Root
public class Project{
   private @Element Info info;
   @Root
   private static class Info{
      private @Attribute String module;
      private @Attribute String organisation;
   }
   public String getOrganisation(){
      return info.organisation;
   }
   public String getProject(){
      return info.module;
   }
}
