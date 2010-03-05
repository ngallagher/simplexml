package org.simpleframework.xml.stream;

import java.io.StringReader;

import junit.framework.TestCase;

public class EventReaderTest extends TestCase {
   
   private static final String SOURCE =
   "<root name='top'>\n"+
   "    <!-- child node -->\n"+
   "    <child a='A' b='B'>\n"+
   "        <leaf>leaf node</leaf>\n"+
   "    </child>\n"+
   "</root>";

   
   public void testPull() throws Exception {
      Provider provider = new PullProvider();
      StringReader source = new StringReader(SOURCE);
      EventReader reader = provider.provide(source);
      
      assertEquals(reader.peek().getName(), "root");
      assertEquals(reader.next().getName(), "root");
      assertTrue(reader.peek().isText());
      assertTrue(reader.next().isText());
      assertEquals(reader.peek().getName(), "child");
      assertEquals(reader.next().getName(), "child"); 
      assertTrue(reader.peek().isText());
      assertTrue(reader.next().isText());
      assertEquals(reader.peek().getName(), "leaf");
      assertEquals(reader.next().getName(), "leaf"); 
      assertTrue(reader.peek().isText());
      assertEquals(reader.peek().getValue(), "leaf node");
      assertEquals(reader.next().getValue(), "leaf node");
      assertTrue(reader.next().isEnd());
      assertTrue(reader.next().isText());
      assertTrue(reader.next().isEnd()); 
      assertTrue(reader.next().isText());
   }

}
