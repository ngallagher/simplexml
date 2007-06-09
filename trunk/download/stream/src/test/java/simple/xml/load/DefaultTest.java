package simple.xml.load;

import java.io.StringWriter;
import java.util.List;

import simple.xml.Attribute;
import simple.xml.ElementList;
import simple.xml.Root;
import simple.xml.Text;
import simple.xml.ValidationTestCase;

public class DefaultTest extends ValidationTestCase {
        
   private static final String SOURCE =
   "<defaultTextList version='ONE'>\n"+
   "   <list>\r\n" +
   "      <textEntry name='a' version='ONE'>Example 1</textEntry>\r\n"+
   "      <textEntry name='b' version='TWO'>Example 2</textEntry>\r\n"+
   "      <textEntry name='c' version='THREE'>Example 3</textEntry>\r\n"+
   "   </list>\r\n"+
   "</defaultTextList>";

   @Root
   private static class DefaultTextList {

      @ElementList
      private List<TextEntry> list;

      @Attribute
      private Version version;              

      public TextEntry get(int index) {
         return list.get(index);              
      }
   }

   @Root
   private static class TextEntry {

      @Attribute
      private String name;

      @Attribute
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
   
   public void testList() throws Exception {    
      DefaultTextList list = persister.read(DefaultTextList.class, SOURCE);

      assertEquals(list.version, Version.ONE);
      assertEquals(list.get(0).version, Version.ONE);
      assertEquals(list.get(0).name, "a");
      assertEquals(list.get(0).text, "Example 1");
      assertEquals(list.get(1).version, Version.TWO);
      assertEquals(list.get(1).name, "b");
      assertEquals(list.get(1).text, "Example 2");
      assertEquals(list.get(2).version, Version.THREE);
      assertEquals(list.get(2).name, "c");
      assertEquals(list.get(2).text, "Example 3");
      
      StringWriter buffer = new StringWriter();
      persister.write(list, buffer);
      validate(list, persister);

      list = persister.read(DefaultTextList.class, buffer.toString());

      assertEquals(list.version, Version.ONE);
      assertEquals(list.get(0).version, Version.ONE);
      assertEquals(list.get(0).name, "a");
      assertEquals(list.get(0).text, "Example 1");
      assertEquals(list.get(1).version, Version.TWO);
      assertEquals(list.get(1).name, "b");
      assertEquals(list.get(1).text, "Example 2");
      assertEquals(list.get(2).version, Version.THREE);
      assertEquals(list.get(2).name, "c");
      assertEquals(list.get(2).text, "Example 3");
      
      validate(list, persister);
   }
}
