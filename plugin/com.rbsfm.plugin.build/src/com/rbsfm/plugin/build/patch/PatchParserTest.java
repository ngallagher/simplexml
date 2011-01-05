package com.rbsfm.plugin.build.patch;
import static org.junit.Assert.assertEquals;
import java.io.StringReader;
import java.util.List;
import org.junit.Test;
public class PatchParserTest {
  private static final String SOURCE = 
  "<jardesc>\n" +
  "<selectedElements exportClassFiles='true' exportJavaFiles='true' exportOutputFolder='false'>"+
  "<javaElement handleIdentifier='=common/src&lt;com.rbsfm.common.system{X.java'/>"+
  "<javaElement handleIdentifier='=common/src&lt;com.rbsfm.common.system{Y.java'/>"+
  "<file path='/cpbuild/config/templates/a.xml'/>"+
  "<file path='/cpbuild/config/templates/b.xml'/>"+  
  "</selectedElements>"+
  "</jardesc>";
  @Test
  public void parse() throws Exception{
     StringReader reader = new StringReader(SOURCE);
     Patch patch = PatchParser.parse(reader);
     List<String> elements = patch.getElements();
     assertEquals(elements.get(0), "=common/src<com.rbsfm.common.system{X.java");
     assertEquals(elements.get(1), "=common/src<com.rbsfm.common.system{Y.java");
     assertEquals(elements.get(2), "/cpbuild/config/templates/a.xml");
     assertEquals(elements.get(3), "/cpbuild/config/templates/b.xml");
     
  }
}
