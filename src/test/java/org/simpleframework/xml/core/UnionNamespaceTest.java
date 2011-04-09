package org.simpleframework.xml.core;

import java.io.StringWriter;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.ValidationTestCase;
import org.simpleframework.xml.Union;

public class UnionNamespaceTest extends ValidationTestCase {

   @Root
   private static class Example {
      @Path("path")
      @Namespace(prefix="x", reference="http://www.xml.com/ns")
      @Union({
         @Element(name="a"),
         @Element(name="b"),
         @Element(name="c"),
         @Element(name="d"),
         @Element(name="e")
      })
      private String name;
      
      public Example(@Element(name="a") String name){
         this.name = name;
      }
      public String getName(){
         return name;
      }
   }
   
   public void testNamespaceWithUnion() throws Exception{
      Persister persister = new Persister();
      Example example = new Example("example");
      StringWriter writer = new StringWriter();
      persister.write(example, writer);
      String text = writer.toString();
      Example deserialized = persister.read(Example.class, text);
      assertEquals(deserialized.getName(), "example");
      validate(persister, example);
      assertElementExists(text, "/example/path/a");
      assertElementHasValue(text, "/example/path/a", "example");
      assertElementHasNamespace(text, "/example/path/a", "http://www.xml.com/ns");
   }
}
