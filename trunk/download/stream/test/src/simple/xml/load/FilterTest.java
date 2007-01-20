package simple.xml.load;

import java.io.StringReader;
import java.util.HashMap;

import junit.framework.TestCase;
import simple.xml.Attribute;
import simple.xml.Element;
import simple.xml.Root;

public class FilterTest extends TestCase {
        
   private static final String ENTRY =
   "<?xml version=\"1.0\"?>\n"+
   "<root number='1234' flag='true'>\n"+
   "   <name>${example.name}</name>  \n\r"+
   "   <path>${example.path}</path>\n"+
   "   <constant>${no.override}</constant>\n"+
   "   <text>\n"+
   "     Some example text where ${example.name} is replaced"+
   "     with the system property value and the path is "+
   "     replaced with the path ${example.path}"+
   "   </text>\n"+
   "</root>";
   
   @Root(name="root")
   private static class Entry {

      @Attribute(name="number")
      private int number;     

      @Attribute(name="flag")
      private boolean bool;
      
      @Element(name="constant")
      private String constant;

      @Element(name="name")
      private String name;

      @Element(name="path")
      private String path;

      @Element(name="text")
      private String text;
   }

   static {
      System.setProperty("example.name", "some name");
      System.setProperty("example.path", "/some/path");
      System.setProperty("no.override", "some constant");
   } 
        
   private Persister systemSerializer;

   private Persister mapSerializer;

   public void setUp() {
      HashMap map = new HashMap();
      map.put("example.name", "override name");
      map.put("example.path", "/some/override/path");      
      systemSerializer = new Persister();
      mapSerializer = new Persister(map);
   }
	
   public void testSystem() throws Exception {    
      Entry entry = systemSerializer.read(Entry.class, new StringReader(ENTRY));
      
      assertEquals(entry.number, 1234);
      assertEquals(entry.bool, true);
      assertEquals(entry.name, "some name");
      assertEquals(entry.path, "/some/path");
      assertEquals(entry.constant, "some constant");      
      assertTrue(entry.text.indexOf(entry.name) > 0);
      assertTrue(entry.text.indexOf(entry.path) > 0);
   }

   public void testMap() throws Exception {
      Entry entry = mapSerializer.read(Entry.class, new StringReader(ENTRY));
      
      assertEquals(entry.number, 1234);
      assertEquals(entry.bool, true);
      assertEquals(entry.name, "override name");
      assertEquals(entry.path, "/some/override/path");
      assertEquals(entry.constant, "some constant");      
      assertTrue(entry.text.indexOf(entry.name) > 0);
      assertTrue(entry.text.indexOf(entry.path) > 0);           
   }
}
