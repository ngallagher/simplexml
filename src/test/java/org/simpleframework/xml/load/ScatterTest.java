package org.simpleframework.xml.load;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.ValidationTestCase;

public class ScatterTest extends ValidationTestCase {
   
   private static final String INLINE_LIST =
   "<test version='ONE'>\n"+
   "   <text name='a' version='ONE'>Example 1</text>\r\n"+
   "   <message>Some example message</message>\r\n"+
   "   <text name='b' version='TWO'>Example 2</text>\r\n"+
   "   <double>1.0</double>\n" +
   "   <double>2.0</double>\n"+
   "   <text name='c' version='THREE'>Example 3</text>\r\n"+
   "   <double>3.0</double>\n"+
   "</test>";
   
   private static final String INLINE_PRIMITIVE_LIST =
   "<test version='ONE'>\n"+
   "   <string>Example 1</string>\r\n"+
   "   <message>Some example message</message>\r\n"+
   "   <string>Example 2</string>\r\n"+
   "   <string>Example 3</string>\r\n"+
   "</test>";
   
   @Root(name="test")
   private static class InlineTextList {
      
      @Element
      private String message;        

      @ElementList(inline=true)
      private List<TextEntry> list;
      
      @ElementList(inline=true)
      private List<Double> numbers;

      @Attribute
      private Version version;
      
      private List<Double> getNumbers() {
         return numbers;
      }

      public TextEntry get(int index) {
         return list.get(index);              
      }
   }
   
   @Root(name="test")
   private static class InlinePrimitiveList {
      
      @Element
      private String message;

      @ElementList(inline=true)
      private List<String> list;

      @Attribute
      private Version version;              

      public String get(int index) {
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

   @Root
   public class SimpleInlineList {

      @ElementList(inline=true)           
      private ArrayList<SimpleEntry> list = new ArrayList<SimpleEntry>();           
   }

   @Root
   private static class SimpleEntry {

      @Attribute            
      private String content;           
   }        
   
   @Root
   public class SimplePrimitiveInlineList {
      
      @ElementList(inline=true)
      private ArrayList<String> list = new ArrayList<String>();
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
      assertEquals(list.message, "Some example message");
      assertEquals(list.get(0).version, Version.ONE);
      assertEquals(list.get(0).name, "a");
      assertEquals(list.get(0).text, "Example 1");
      assertEquals(list.get(1).version, Version.TWO);
      assertEquals(list.get(1).name, "b");
      assertEquals(list.get(1).text, "Example 2");
      assertEquals(list.get(2).version, Version.THREE);
      assertEquals(list.get(2).name, "c");
      assertEquals(list.get(2).text, "Example 3");
      assertTrue(list.getNumbers().contains(1.0));
      assertTrue(list.getNumbers().contains(2.0));
      assertTrue(list.getNumbers().contains(3.0));
      
      StringWriter buffer = new StringWriter();
      persister.write(list, buffer);
      validate(list, persister);

      list = persister.read(InlineTextList.class, buffer.toString());

      assertEquals(list.version, Version.ONE);
      assertEquals(list.message, "Some example message");
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
   
   public void testPrimitiveList() throws Exception {    
      InlinePrimitiveList list = persister.read(InlinePrimitiveList.class, INLINE_PRIMITIVE_LIST);

      assertEquals(list.version, Version.ONE);
      assertEquals(list.message, "Some example message");

      assertEquals(list.get(0), "Example 1");
      assertEquals(list.get(1), "Example 2");
      assertEquals(list.get(2), "Example 3");
      
      StringWriter buffer = new StringWriter();
      persister.write(list, buffer);
      validate(list, persister);

      list = persister.read(InlinePrimitiveList.class, buffer.toString());

      assertEquals(list.get(0), "Example 1");
      assertEquals(list.get(1), "Example 2");
      assertEquals(list.get(2), "Example 3");
      
      validate(list, persister);
   }   

   public void testSimpleList() throws Exception{
      SimpleInlineList list = new SimpleInlineList();
      SimpleEntry entry = new SimpleEntry();

      entry.content = "test";
      list.list.add(entry);

      validate(list, persister);      
   }
   
   public void testSimplePrimitiveList() throws Exception{
      SimplePrimitiveInlineList list = new SimplePrimitiveInlineList();
      
      list.list.add("test");

      validate(list, persister);      
   }
}
