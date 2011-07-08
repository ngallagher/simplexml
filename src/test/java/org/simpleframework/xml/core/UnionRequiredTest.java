package org.simpleframework.xml.core;

import junit.framework.TestCase;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementUnion;
import org.simpleframework.xml.Root;

public class UnionRequiredTest extends TestCase {

   @Root
   public static class Example {
      
      @ElementUnion({
         @Element(name="double", type=Double.class, required=false),
         @Element(name="string", type=String.class, required=true)
      })
      private Object value;
   }
   
   // FIXME This should fail because of required!!!!!
   public void testRequired() throws Exception {
      Persister persister = new Persister();
      Example example = new Example();
      example.value = "test";
      persister.write(example, System.out);
   }
}
