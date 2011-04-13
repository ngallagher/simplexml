package org.simpleframework.xml.stream;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.xmlpull.v1.XmlPullParser;

public class PullReaderTest extends TestCase {

   private static final String SOURCE =
   "<?xml version=\"1.0\" encoding='UTF-8'?>\n"+
   "<x:override id='12' flag='true' xmlns:x='http://www.x.com/x'>\n"+
   "   <x:text>entry text</x:text>  \n\r"+
   "   <name>some name</name> \n"+
   "   <third>added to schema</third>\n"+
   "</x:override>";
   
   public void testInputStream() throws Exception {
      Provider provider = new PullProvider();
      byte[] array = SOURCE.getBytes("UTF-8");
      InputStream source = new ByteArrayInputStream(array);
      EventReader reader = provider.provide(source);
      EventNode node = reader.peek();
      
      assertEquals(node.getName(), "override");
      assertEquals(node.getLine(), 2);
      assertEquals(node.getPrefix(), "x");
      assertEquals(node.getReference(), "http://www.x.com/x");
      assertTrue(XmlPullParser.class.isAssignableFrom(node.getSource().getClass()));
      
      List<Attribute> attributes = new ArrayList<Attribute>();
      
      for(Attribute attribute : node) {
         attributes.add(attribute);
      }
      assertEquals(attributes.size(), 2);
      assertEquals(attributes.get(0).getName(), "id");
      assertEquals(attributes.get(0).getValue(), "12");
      assertTrue(XmlPullParser.class.isAssignableFrom(attributes.get(0).getSource().getClass()));
      assertEquals(attributes.get(1).getName(), "flag");
      assertEquals(attributes.get(1).getValue(), "true");
      assertTrue(XmlPullParser.class.isAssignableFrom(attributes.get(1).getSource().getClass()));
   }
   
   public void testReader() throws Exception {
      Provider provider = new PullProvider();
      Reader source = new StringReader(SOURCE);
      EventReader reader = provider.provide(source);
      EventNode node = reader.peek();
      
      assertEquals(node.getName(), "override");
      assertEquals(node.getLine(), 2);
      assertEquals(node.getPrefix(), "x");
      assertEquals(node.getReference(), "http://www.x.com/x");
      assertTrue(XmlPullParser.class.isAssignableFrom(node.getSource().getClass()));
      
      List<Attribute> attributes = new ArrayList<Attribute>();
      
      for(Attribute attribute : node) {
         attributes.add(attribute);
      }
      assertEquals(attributes.size(), 2);
      assertEquals(attributes.get(0).getName(), "id");
      assertEquals(attributes.get(0).getValue(), "12");
      assertTrue(XmlPullParser.class.isAssignableFrom(attributes.get(0).getSource().getClass()));
      assertEquals(attributes.get(1).getName(), "flag");
      assertEquals(attributes.get(1).getValue(), "true");
      assertTrue(XmlPullParser.class.isAssignableFrom(attributes.get(1).getSource().getClass()));
      attributes.clear();
      
      node = reader.next();
      
      assertEquals(node.getName(), "override");
      assertEquals(node.getLine(), 2);
      assertTrue(XmlPullParser.class.isAssignableFrom(node.getSource().getClass()));
      
      node = reader.next();
      
      assertEquals(node.getName(), null);
      assertTrue(node.isText());
      
      node = reader.next();
      
      assertEquals(node.getName(), "text");
      assertEquals(node.getLine(), 3);
      assertEquals(node.getPrefix(), "x");
      assertEquals(node.getReference(), "http://www.x.com/x");
      assertEquals(node.getValue(), null);
      assertTrue(node.isStart());
      
      node = reader.next();
      
      assertEquals(node.getName(), null);
      assertEquals(node.getValue(), "entry text");
      assertTrue(node.isText());
      
      
   }
}
