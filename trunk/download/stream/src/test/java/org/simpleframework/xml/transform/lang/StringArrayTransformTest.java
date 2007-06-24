package org.simpleframework.xml.transform.lang;

import java.util.ArrayList;
import java.util.Collection;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.ValidationTestCase;
import org.simpleframework.xml.load.Persister;

public class StringArrayTransformTest extends ValidationTestCase {
   
   @Root
   public class StringArrayExample {
      
      @Attribute     
      private String[] attribute;
      
      @Element
      private String[] element;
      
      @ElementList
      private Collection<String[]> list;
      
      @ElementArray
      private String[][] array;
      
      public StringArrayExample() {
         super();
      }
      
      public StringArrayExample(String[] list) {
         this.attribute = list;
         this.element = list;         
         this.list = new ArrayList<String[]>();
         this.list.add(list);
         this.list.add(list);
         this.array = new String[1][];
         this.array[0] = list;
      }   
   }
   
   public void testRead() throws Exception {    
      StringArrayTransform transform = new StringArrayTransform();
      String[] list = transform.read("one,two,three,four");     
 
      assertEquals("one", list[0]);
      assertEquals("two", list[1]);
      assertEquals("three", list[2]);
      assertEquals("four", list[3]);

      list = transform.read("  this is some string ,\t\n "+
                            "that has each\n\r," +
                            "string split,\t over\n,\n"+
                            "several lines\n\t");


      assertEquals("this is some string", list[0]);
      assertEquals("that has each", list[1]);
      assertEquals("string split", list[2]);
      assertEquals("over", list[3]);
      assertEquals("several lines", list[4]);
   }

   public void testWrite() throws Exception {
      StringArrayTransform transform = new StringArrayTransform();
      String value = transform.write(new String[] { "one", "two", "three", "four"});

      assertEquals(value, "one, two, three, four");

      value = transform.write(new String[] {"one", null, "three", "four"});

      assertEquals(value, "one, three, four");
   }
   
   public void testPersistence() throws Exception {
      String[] list = new String[] { "one", "two", "three", "four"};
      Persister persister = new Persister();
      StringArrayExample example = new StringArrayExample(list);
      
      validate(example, persister);      
   }
}
