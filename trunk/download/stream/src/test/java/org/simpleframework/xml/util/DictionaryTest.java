package simple.xml.util;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.Set;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.load.Persister;
import org.simpleframework.xml.util.Dictionary;
import org.simpleframework.xml.util.Entry;

import simple.xml.ValidationTestCase;

public class DictionaryTest extends ValidationTestCase {
        
   private static final String LIST = 
   "<?xml version=\"1.0\"?>\n"+
   "<test name='example'>\n"+
   "   <list>\n"+   
   "      <property name='1'>\n"+
   "         <text>one</text>  \n\r"+
   "      </property>\n\r"+
   "      <property name='2'>\n"+
   "         <text>two</text>  \n\r"+
   "      </property>\n"+
   "      <property name='3'>\n"+
   "         <text>three</text>  \n\r"+
   "      </property>\n"+
   "   </list>\n"+
   "</test>";  
   
   @Root(name="property")
   private static class Property extends Entry {

      @Element(name="text")
      private String text;        
   }
   
   @Root(name="test")
   private static class PropertySet implements Iterable<Property> {

      @ElementList(name="list", type=Property.class)
      private Dictionary<Property> list;           

      @Attribute(name="name")
      private String name;

      public Iterator<Property> iterator() {
         return list.iterator();
      }

      public Property get(String name) {
         return list.get(name);              
      }

      public int size() {
         return list.size();              
      }
   }
        
	private Persister serializer;

	public void setUp() {
	   serializer = new Persister();
	}
	
   public void testDictionary() throws Exception {    
      PropertySet set = (PropertySet) serializer.read(PropertySet.class, LIST);

      assertEquals(3, set.size());
      assertEquals("one", set.get("1").text);
      assertEquals("two", set.get("2").text);
      assertEquals("three", set.get("3").text);

      validate(set, serializer);
   }
}
