package org.simpleframework.xml.core;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.ValidationTestCase;
import org.simpleframework.xml.transform.Matcher;
import org.simpleframework.xml.transform.Transform;

public class MatcherTest extends ValidationTestCase {
   
   @Root
   @Namespace(prefix="foo", reference="http://www.domain.com/value")
   private static class Example {
      
      @Element
      private Integer value;
      
      @Attribute
      private Integer attr;
      
      public Example() {
         super();
      }
      
      public Example(Integer value, Integer attr) {
         this.value = value;
         this.attr = attr;
      }
   }
   
   private static class ExampleMatcher implements Matcher, Transform<Integer> {

      public Transform match(Class type) throws Exception {
         if(type == Integer.class) {
            return this;
         }
         return null;
      }

      public Integer read(String value) throws Exception {
         return Integer.valueOf(value);
      }

      public String write(Integer value) throws Exception {
         return "12345";
      }
   }
   
   public void testMatcher() throws Exception {
      Matcher matcher = new ExampleMatcher();
      Serializer serializer = new Persister(matcher);
      Example example = new Example(1, 9999);
      
      serializer.write(example, System.out);
    
      validate(serializer, example);
   }

}
