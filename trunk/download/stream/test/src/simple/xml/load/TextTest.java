package simple.xml.load;

import java.io.FileOutputStream;
import java.io.StringWriter;

import simple.xml.ValidationTestCase;
import simple.xml.ElementArray;
import simple.xml.Element;
import simple.xml.Attribute;
import simple.xml.Text;
import simple.xml.Root;

public class TextTest extends ValidationTestCase {
        
   private static final String SOURCE =
   "<test>\n"+
   "   <array>\n"+
   "     <text name='a' version='ONE'>Example 1</text>\r\n"+
   "     <text name='b' version='TWO'>Example 2</text>\r\n"+
   "     <text name='c' version='THREE'>Example 3</text>\r\n"+
   "   </array>\n\r"+
   "</test>";
   

   @Root(name="test")
   private static class TextList {

      @ElementArray(name="array")
      private TextEntry[] array;
   }

   @Root(name="text")
   private static class TextEntry {

      @Attribute(name="name")
      private String name;

      @Attribute(name="version")
      private Version version;        

      @Text
      private String text;
   }

   private enum Version {
           
      ONE,
      TWO,
      THREE
   }

   private Persister persister;

   public void setUp() throws Exception {
      persister = new Persister();
   }
	
   public void testText() throws Exception {    
      TextList list = persister.read(TextList.class, SOURCE);

      assertEquals(list.array[0].version, Version.ONE);
      assertEquals(list.array[0].name, "a");
      assertEquals(list.array[0].text, "Example 1");
      assertEquals(list.array[1].version, Version.TWO);
      assertEquals(list.array[1].name, "b");
      assertEquals(list.array[1].text, "Example 2");
      assertEquals(list.array[2].version, Version.THREE);
      assertEquals(list.array[2].name, "c");
      assertEquals(list.array[2].text, "Example 3");
      
      StringWriter buffer = new StringWriter();
      persister.write(list, buffer);
      validate(list, persister);

      list = persister.read(TextList.class, buffer.toString());

      assertEquals(list.array[0].version, Version.ONE);
      assertEquals(list.array[0].name, "a");
      assertEquals(list.array[0].text, "Example 1");
      assertEquals(list.array[1].version, Version.TWO);
      assertEquals(list.array[1].name, "b");
      assertEquals(list.array[1].text, "Example 2");
      assertEquals(list.array[2].version, Version.THREE);
      assertEquals(list.array[2].name, "c");
      assertEquals(list.array[2].text, "Example 3");
      
      validate(list, persister);
   }
}
