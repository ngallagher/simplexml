package org.simpleframework.xml.core;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

public class ConstructorTest extends TestCase {

   private static class Example {
      
      public Example(){}
      public Example(@Element(name="integer") int integer){}
      public Example(@Element(name="integer") int integer, @Element(name="string") String string, @Attribute(name="long") Long number){}
      public Example(@Element(name="integer") int integer, @Element(name="string") String string){}
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
      
   }
}
