package com.rbsfm.plugin.build.ivy;
import static org.junit.Assert.assertEquals;
import java.io.StringReader;
import org.junit.Test;
public class ModuleParserTest{
   private static final String SOURCE = 
   "<ivy-module>\n" + 
   "<info organisation='com.rbsfm' module='egpricing' revision='2009WK52-ceemea-7' status='release' publication='20091221002033'/>\n" +
   "</ivy-module>";
   @Test
   public void parse() throws Exception{
      StringReader reader = new StringReader(SOURCE);
      Module module = ModuleParser.parse(reader);
      assertEquals(module.getModule(), "egpricing");
      assertEquals(module.getRevision(), "2009WK52");
      assertEquals(module.getBranchRevision(), "7");
      assertEquals(module.getBranch(), "ceemea");
   }
}
