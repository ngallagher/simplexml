package simple.xml.load;

import java.io.FileOutputStream;
import java.io.StringWriter;

import simple.xml.ValidationTestCase;
import simple.xml.ElementArray;
import simple.xml.Element;
import simple.xml.Root;

public class StrictTest extends ValidationTestCase {
        
   private static final String SOURCE =
    "<root version='2.1' id='234'>\n" +
    "   <list type='sorted'>\n" +
    "      <entry name='1'>\n" +
    "         <value>value 1</value>\n" +
    "      </entry>\n" +
    "      <entry name='2'>\n" +
    "         <value>value 2</value>\n" +
    "      </entry>\n" +
    "      <entry name='3'>\n" +
    "         <value>value 3</value>\n" +
    "      </entry>\n" +                   
    "   </list>\n" +
    "   <object name='name'>\n" +
    "      <integer>123</integer>\n" +
    "      <object name='key'>\n" +
    "         <integer>12345</integer>\n" +
    "      </object>\n" +
    "   </object>\n" +
    "</root>";
   
   @Root(name="root", strict=false)
   private static class StrictExample {

      @ElementArray(name="list")
      private StrictEntry[] list;

      @Element(name="object")
      private StrictObject object;
   }

   @Root(name="entry", strict=false)
   private static class StrictEntry {

      @Element(name="value")
      private String value;
   }

   @Root(name="object", strict=false)
   private static class StrictObject {

      @Element(name="integer")
      private int integer;
   }  

   private Persister persister;

   public void setUp() throws Exception {
      persister = new Persister();
   }
	
   public void testStrict() throws Exception {    
      StrictExample example = persister.read(StrictExample.class, SOURCE);

      assertEquals(example.list.length, 3);
      
      validate(example, persister);
   }
}
