package com.rbsfm.plugin.build.ivy;
import java.io.InputStream;
import java.io.Reader;
import org.simpleframework.xml.core.Persister;
public class ModuleParser{
   public static Module parse(InputStream source) throws Exception{
      return new Persister().read(Module.class, source, false);
   }
   public static Module parse(Reader source) throws Exception{
      return new Persister().read(Module.class, source, false);
   }
}
