package org.simpleframework.xml.core;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

public class ConstructorTest extends TestCase {
   
   private static final String SOURCE = 
      "<example>"+
      "  <integer>12</integer>"+
      "  <string>text</string>"+
      "  <number>12</number>"+
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
      ConstructorScanner scanner = new ConstructorScanner(Example.class);
      Set<String> set = new HashSet<String>();
      
      set.add("integer");
  
      System.err.println(scanner.getBuilder(set));
      
      set.add("integer");
      set.add("string");
      
      System.err.println(scanner.getBuilder(set));
      
      Set<String> odd = new HashSet<String>();
      
      odd.add("boolean");
      
      System.err.println(scanner.getBuilder(odd));
      
      new Persister().read(Example.class, SOURCE);
      
   }
}
