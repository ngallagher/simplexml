package com.rbsfm.plugin.build.ivy;
import static org.junit.Assert.assertEquals;
import java.io.StringReader;
import org.junit.Test;
public class ModuleParserTest{
   private static final String SOURCE="<ivy-module>\n<info name='egpricing' revision='2009WK49-12'/>\n</ivy-module>";
   @Test
   public void parse() throws Exception{
      StringReader reader=new StringReader(SOURCE);
      Module module=ModuleParser.parse(reader);
      assertEquals(module.info.name,"egpricing");
      assertEquals(module.info.revision,"2009WK49-12");
   }
}
