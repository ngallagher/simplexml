package org.simpleframework.xml.core;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementUnion;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.ValidationTestCase;

public class UnionRequiredTest extends ValidationTestCase {

   @Root
   public static class Example {
      
      @ElementUnion({
         @Element(name="double", type=Double.class, required=false),
         @Element(name="string", type=String.class, required=true),
         @Element(name="int", type=Integer.class, required=true)
      })
      private Object value;
   }
   
   public void testRequired() throws Exception {
      Persister persister = new Persister();
      Example example = new Example();
      example.value = "test";
      boolean failure = false;
      try {
         persister.write(example, System.out);
      }catch(Exception e) {
         e.printStackTrace();
         failure = true;
      }
      assertTrue("Requirement did not match", failure);
   }
}
