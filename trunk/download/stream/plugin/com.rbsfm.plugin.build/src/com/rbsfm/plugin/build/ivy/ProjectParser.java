package com.rbsfm.plugin.build.ivy;
import java.io.InputStream;
import java.io.Reader;
import org.simpleframework.xml.core.Persister;
public class ProjectParser{
   public static Project parse(InputStream source) throws Exception{
      return new Persister().read(Project.class,source,false);
   }
   public static Project parse(Reader source) throws Exception{
      return new Persister().read(Project.class,source,false);
   }
}
