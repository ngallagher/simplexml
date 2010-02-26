package org.simpleframework.xml.stream;

import java.io.StringReader;

import junit.framework.TestCase;

import org.xmlpull.mxp1.MXParser;
import org.xmlpull.v1.XmlPullParser;

public class PullReaderTest extends TestCase {
   
   private static final String SOURCE =
   "<root name='top'>\n"+
   "    <!-- child node -->\n"+
   "    <child a='A' b='B'>\n"+
   "        <leaf>leaf node</leaf>\n"+
   "    </child>\n"+
   "</root>";

   
   public void testPull() throws Exception {
      XmlPullParser parser = new MXParser();
      StringReader source = new StringReader(SOURCE);
      parser.setInput(source);
      PullReader reader = new PullReader(parser);
      
      assertEquals(reader.peek().getName(), "root");
      assertEquals(reader.next().getName(), "root");
      assertEquals(reader.peek().getClass(), TextEvent.class);
      assertEquals(reader.next().getClass(), TextEvent.class);
      assertEquals(reader.peek().getName(), "child");
      assertEquals(reader.next().getName(), "child"); 
      assertEquals(reader.peek().getClass(), TextEvent.class);
      assertEquals(reader.next().getClass(), TextEvent.class);
      assertEquals(reader.peek().getName(), "leaf");
      assertEquals(reader.next().getName(), "leaf"); 
      assertEquals(reader.peek().getClass(), TextEvent.class);
      assertEquals(reader.peek().getValue(), "leaf node");
      assertEquals(reader.next().getValue(), "leaf node");
      assertEquals(reader.next().getClass(), EndEvent.class);
      assertEquals(reader.next().getClass(), TextEvent.class);
      assertEquals(reader.next().getClass(), EndEvent.class); 
      assertEquals(reader.next().getClass(), TextEvent.class);
   }

}
