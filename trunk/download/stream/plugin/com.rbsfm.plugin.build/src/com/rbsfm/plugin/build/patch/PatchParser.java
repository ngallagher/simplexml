package com.rbsfm.plugin.build.patch;
import java.io.InputStream;
import java.io.Reader;
import org.simpleframework.xml.core.Persister;
public class PatchParser {
  public static Patch parse(InputStream source) throws Exception{
    return new Persister().read(Patch.class, source, false);
 }
 public static Patch parse(Reader source) throws Exception{
    return new Persister().read(Patch.class, source, false);
 }
}
