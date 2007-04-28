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
    "   <list size='3' type='sorted'>\n" +
    "      <item>\n"+
    "         <entry name='1'>\n" +
    "            <value>value 1</value>\n" +
    "         </entry>\n" +
    "      </item>\n"+
    "      <item>\n"+
    "         <entry name='2'>\n" +
    "            <value>value 2</value>\n" +
    "         </entry>\n" +
    "      </item>\n"+
    "      <item>\n"+
    "         <entry name='3'>\n" +
    "            <value>value 3</value>\n" +
    "         </entry>\n" +
    "      </item>\n"+
    "   </list>\n" +
    "   <object name='name'>\n" +
    "      <integer>123</integer>\n" +
    "      <object name='key'>\n" +
    "         <integer>12345</integer>\n" +
    "      </object>\n" +
    "   </object>\n" +
    "</root>";

   private static final String SIMPLE =
    "<object name='name'>\n" +
    "   <integer>123</integer>\n" +
    "   <object name='key'>\n" +
    "      <integer>12345</integer>\n" +
    "   </object>\n" +
    "</object>\n";
   
   @Root(name="root", strict=false)
   private static class StrictExample {

      @ElementArray(name="list", parent="item")
      private StrictEntry[] list;

      @Element(name="object")
      private StrictObject object;
   }

   @Root(name="entry", strict=false)
   private static class StrictEntry {

      @Element(name="value")
      private String value;
   }

   @Root(strict=false)
   private static class StrictObject {

      @Element(name="integer")
      private int integer;
   }  

   @Root(name="object", strict=false)
   private static class NamedStrictObject extends StrictObject {
   }

   private Persister persister;

   public void setUp() throws Exception {
      persister = new Persister();
   }
	
   public void testStrict() throws Exception {    
      StrictExample example = persister.read(StrictExample.class, SOURCE);

      assertEquals(example.list.length, 3);
      assertEquals(example.list[0].value, "value 1");   
      assertEquals(example.list[1].value, "value 2");     
      assertEquals(example.list[2].value, "value 3");     
      assertEquals(example.object.integer, 123);
      
      validate(example, persister);
   }

   public void testUnnamedStrict() throws Exception {    
      boolean success = false; 
      
      try {      
         persister.read(StrictObject.class, SIMPLE);
      } catch(RootException e) {
         success = true;              
      }
      assertTrue(success);
   }

   public void testNamedStrict() throws Exception {    
      StrictObject object = persister.read(NamedStrictObject.class, SIMPLE);

      assertEquals(object.integer, 123);
      validate(object, persister);
   }   
}
