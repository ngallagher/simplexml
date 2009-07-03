package org.simpleframework.xml.core;

import junit.framework.TestCase;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

public class ConstructorInjectionTest extends TestCase {
   
   private static final String SOURCE = 
   "<example>"+
   "  <integer>12</integer>"+
   "  <string>text</string>"+
   "  <number>32</number>"+
   "</example>";
   
   @Root
   private static class Example {
      
      @Element private int integer;   
      @Element private String string;
      @Element private long number;
      
      public Example(){}
      public Example(@Element(name="integer") int integer){
         this.integer = integer;
      }
      public Example(@Element(name="integer") int integer, @Element(name="string") String string, @Attribute(name="number") long number){
         this.integer = integer;
         this.string = string;
         this.number = number;
      }
      public Example(@Element(name="integer") int integer, @Element(name="string") String string){
         this.integer = integer;
         this.string = string;
      }
   }
   
   public void testConstructor() throws Exception {      
      Persister persister = new Persister();
      Example example = persister.read(Example.class, SOURCE);
      
      assertEquals(example.integer, 12);
      assertEquals(example.number, 32);
      assertEquals(example.string, "text");
      
   }
}
