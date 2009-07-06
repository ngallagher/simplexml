package org.simpleframework.xml.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

public class SpecificationTest extends TestCase {
   
   @Root
   private static class SimpleClass {
      
      @Element(name="name")
      private final String name;
      
      @Element(name="value")
      private final int value;
      
      public SimpleClass() {
         this("default");
      }
      
      public SimpleClass(@Element(name="name") String name) {
         this(name, 2);
      }
      
      public SimpleClass(@Element(name="name") String name, @Element(name="value") int value) {
         this.name = name;
         this.value = value;
      }
      
      public String getName() {
         return name;
      }
      
      public int getValue() {
         return value;
      }
      
      public boolean equals(Object other) {
         if(other instanceof SimpleClass) {
            SimpleClass otherClass = (SimpleClass)other;
            return otherClass.name.equals(name) && otherClass.value == value;
         }
         return false;
      }
   }
   
   public void testSpecification() throws Exception {
      ConstructorScanner scanner = new ConstructorScanner(SimpleClass.class);
      Specification spec = scanner.getSpecification();
      
      assertTrue(spec.isDefault());
      assertEquals(spec.getBuilder(toSet("name")).getParameter("name").getType(), String.class);
      assertEquals(spec.getBuilder(toSet("name")).getParameter("name").getIndex(), 0);
      assertEquals(spec.getBuilder(toSet("name")).getParameter("name").getName(), "name");
      assertEquals(spec.getBuilder(toSet("name", "value")).getParameter("name").getType(), String.class);
      assertEquals(spec.getBuilder(toSet("name", "value")).getParameter("name").getIndex(), 0);
      assertEquals(spec.getBuilder(toSet("name", "value")).getParameter("name").getName(), "name");
      assertEquals(spec.getBuilder(toSet("name", "value")).getParameter("value").getType(), int.class);
      assertEquals(spec.getBuilder(toSet("name", "value")).getParameter("value").getIndex(), 1);
      assertEquals(spec.getBuilder(toSet("name", "value")).getParameter("value").getName(), "value");
      assertEquals(spec.getBuilder(Collections.EMPTY_SET).build(Collections.EMPTY_LIST).getClass(), SimpleClass.class);
      assertEquals(spec.getDefault().getClass(), SimpleClass.class);
      assertEquals(spec.getBuilder(Collections.EMPTY_SET).build(Collections.EMPTY_LIST), spec.getDefault());
   }
   
   public Set<String> toSet(String... list) {
      Set<String> set = new HashSet<String>();
      
      for(String value : list) {
         set.add(value);
      }
      return set;
   }

}
