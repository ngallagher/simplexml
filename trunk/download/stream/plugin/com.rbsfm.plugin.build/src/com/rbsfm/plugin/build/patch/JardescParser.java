package com.rbsfm.plugin.build.patch;
import java.io.InputStream;
import java.io.Reader;
import org.simpleframework.xml.core.Persister;
public class JardescParser{
   public static Jardesc parse(InputStream source) throws Exception{
      return new Persister().read(Jardesc.class, source, false);
   }
   public static Jardesc parse(Reader source) throws Exception{
      return new Persister().read(Jardesc.class, source, false);
   }
}
