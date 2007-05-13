package simple.xml.load;

import java.io.StringWriter;
import java.util.List;

import simple.xml.Attribute;
import simple.xml.ElementList;
import simple.xml.Root;
import simple.xml.Text;
import simple.xml.ValidationTestCase;

public class InlineTest extends ValidationTestCase {
        
   private static final String INLINE_LIST =
   "<test version='ONE'>\n"+
   "   <text name='a' version='ONE'>Example 1</text>\r\n"+
   "   <text name='b' version='TWO'>Example 2</text>\r\n"+
   "   <text name='c' version='THREE'>Example 3</text>\r\n"+
   "</test>";

   @Root(name="test")
   private static class InlineTextList {

      @ElementList(type=TextEntry.class, inline=true)
      private List<TextEntry> list;

      @Attribute
      private Version version;              

      public TextEntry get(int index) {
         return list.get(index);              
      }
   }

   @Root(name="text")
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
      InlineTextList list = persister.read(InlineTextList.class, INLINE_LIST);

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

      list = persister.read(InlineTextList.class, buffer.toString());

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
