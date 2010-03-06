package com.rbsfm.plugin.build.ivy;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
@Root
public class Module{
   public @Element Info info;
   @Root
   public static class Info{
      public @Attribute String revision;
      public @Attribute String name;
   }
}
