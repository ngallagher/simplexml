package org.simpleframework.xml.core;

import java.util.Map;
import java.util.Vector;

import junit.framework.TestCase;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

public class CollectionConstructorTest extends TestCase {
   
   private static final String LIST =
   "<example>"+
   "  <entry name='a' value='1'/>"+
   "  <entry name='b' value='2'/>"+
   "</example>";
   
   private static final String MAP =
   "<example>"+
   "  <element key='A'>"+
   "     <entry name='a' value='1'/>"+
   "  </element>"+
   "  <element key='B'>"+
   "     <entry name='b' value='2'/>"+
   "  </element>"+
   "</example>";
   
   @Root
   private static class MapConstructor {
    
      @ElementMap(name="list", entry="element", key="key", attribute=true, inline=true)
      private Map<String, Entry> map;
      
      public MapConstructor(@ElementMap(name="list", entry="element", key="key", attribute=true, inline=true) Map<String, Entry> map) {
         this.map = map;
      }
      
      public int size() {
         return map.size();
      }
   }
   
   @Root(name="example")
   private static class CollectionConstructor {
      
      @ElementList(name="list", inline=true)
      private Vector<Entry> vector;
      
      public CollectionConstructor(@ElementList(name="list", inline=true) Vector<Entry> vector) {
         this.vector = vector;
      }
      
      public int size() {
         return vector.size();
      }
   }
   
   @Root(name="entry")
   private static class Entry {
   
      @Attribute(name="name")
      private String name;
      
      @Attribute(name="value")
      private String value;
      
      public Entry(@Attribute(name="name") String name, @Attribute(name="value") String value) {
         this.name = name;
         this.value = value;
      }
      
      public String getName() {
         return name;
      }
      
      public String getValue() {
         return value;
      }
   }
   
   public void testCollectionConstructor() throws Exception {
      Persister persister = new Persister();
      CollectionConstructor constructor = persister.read(CollectionConstructor.class, LIST);
      
      assertEquals(constructor.size(), 2);
   }
   
   public void testMapConstructor() throws Exception {
      Persister persister = new Persister();
      MapConstructor constructor = persister.read(MapConstructor.class, MAP);
      
      assertEquals(constructor.size(), 2);
   }

}
