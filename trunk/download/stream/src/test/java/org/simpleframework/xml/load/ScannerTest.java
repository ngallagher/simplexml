package org.simpleframework.xml.load;

import java.util.ArrayList;
import java.util.Collection;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;
import org.simpleframework.xml.load.Label;
import org.simpleframework.xml.load.Scanner;

import junit.framework.TestCase;

public class ScannerTest extends TestCase {
   
   @Root(name="name")
   public static class Example {

      @ElementList(name="list", type=Entry.class)
      private Collection<Entry> list;
      
      @Attribute(name="version")
      private int version;
      
      @Attribute(name="name")
      private String name;
   }
   
   @Root(name="entry")
   public static class Entry {
      
      @Attribute(name="text")
      public String text;
   }
   
   @Root(name="name", strict=false)
   public static class MixedExample extends Example {
      
      private Entry entry;
      
      private String text;
      
      @Element(name="entry", required=false)
      public void setEntry(Entry entry) {
         this.entry = entry;
      }
      
      @Element(name="entry", required=false)
      public Entry getEntry() {
         return entry;
      }
      
      @Element(name="text")
      public void setText(String text) {
         this.text = text;
      }
      
      @Element(name="text")
      public String getText() {
         return text;
      }
   }
   
   public static class DuplicateAttributeExample extends Example {
      
      private String name;
      
      @Attribute(name="name")
      public void setName(String name) {
         this.name = name;
      }
      
      @Attribute(name="name")
      public String getName() {
         return name;
      }
   }
   
   public static class NonMatchingElementExample {
      
      private String name;
      
      @Element(name="name", required=false)
      public void setName(String name) {
         this.name = name;
      }
      
      @Element(name="name")
      public String getName() {
         return name;
      }
   }
   
   public static class IllegalTextExample extends MixedExample {
      
      @Text
      private String text;
   }
   
   public void testExample() throws Exception {
      Scanner scanner = new Scanner(Example.class);
      ArrayList<Class> types = new ArrayList<Class>();
      
      assertEquals(scanner.getElements().size(), 1);
      assertEquals(scanner.getAttributes().size(), 2);
      assertNull(scanner.getText());
      assertTrue(scanner.isStrict());
      
      for(Label label : scanner.getElements()) {
         types.add(label.getType());
      }
      assertTrue(types.contains(Collection.class));      
      
      for(Label label : scanner.getAttributes()) {
         types.add(label.getType());
      }
      assertTrue(types.contains(int.class));
      assertTrue(types.contains(String.class));
   }
   
   public void testMixedExample() throws Exception {
      Scanner scanner = new Scanner(MixedExample.class);
      ArrayList<Class> types = new ArrayList<Class>();
      
      assertEquals(scanner.getElements().size(), 3);
      assertEquals(scanner.getAttributes().size(), 2);
      assertNull(scanner.getText());
      assertFalse(scanner.isStrict());
      
      for(Label label : scanner.getElements()) {
         types.add(label.getType());
      }
      assertTrue(types.contains(Collection.class));
      assertTrue(types.contains(Entry.class));
      assertTrue(types.contains(String.class));
      
      for(Label label : scanner.getAttributes()) {
         types.add(label.getType());
      }
      assertTrue(types.contains(int.class));
      assertTrue(types.contains(String.class));
   }
   
   public void testDuplicateAttribute() {
      boolean success = false;
      
      try {
         new Scanner(DuplicateAttributeExample.class);
      } catch(Exception e) {
         success = true;
      }
      assertTrue(success);
   }
   
   public void testNonMatchingElement() {
      boolean success = false;
      
      try {
         new Scanner(NonMatchingElementExample.class);
      } catch(Exception e) {
         success = true;
      }
      assertTrue(success);
   }
   
   public void testIllegalTextExample() {
      boolean success = false;
      
      try {
         new Scanner(IllegalTextExample.class);
      } catch(Exception e) {
         success = true;
      }
      assertTrue(success);
   }
}
