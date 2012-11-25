package org.simpleframework.xml.core;

import java.util.List;

import junit.framework.TestCase;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

public class HtmlParseTest extends TestCase {
   
   private static final String HTML =
   "<html>\n"+
   "<body>\n"+
   "<p>\n"+
   "This is a test <b>Bold text</b> other text"+
   "</p>\n"+
   "</body>\n"+
   "</html>\n";
   
   @Root
   private static class Bold {
      
      @Text
      private String text;
   }
   
   @Root
   private static class Paragraph {
      
      @ElementListUnion({
         @ElementList(entry="b", type=Bold.class, required=false, inline=true),
         @ElementList(entry="i", type=String.class, required=false, inline=true)
      })
      @Text
      private List<Object> list;
   }
   
   @Root
   private static class Body {
      
      @ElementList(entry="p", inline=true)
      private List<Paragraph> list;
   }
   
   @Root
   private static class Document {
      
      @Element
      private Body body;
   }

   public void testDocument() throws Exception {
      Persister persister = new Persister();
      Document doc = persister.read(Document.class, HTML);
      
      assertNotNull(doc.body);
      assertEquals((((Paragraph)doc.body.list.get(0)).list.get(0)), "\nThis is a test ");
      assertEquals(((Bold)((Paragraph)doc.body.list.get(0)).list.get(1)).text, "Bold text");
      assertEquals((((Paragraph)doc.body.list.get(0)).list.get(3)), " other text");
   }
}
