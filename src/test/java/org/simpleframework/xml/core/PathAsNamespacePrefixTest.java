package org.simpleframework.xml.core;

import java.io.StringWriter;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.ValidationTestCase;
import org.simpleframework.xml.Variant;

public class PathAsNamespacePrefixTest extends ValidationTestCase {

   @Root
   @Namespace(prefix="p", reference="http://www.x.com/y")
   private static class PathWithPrefix {
      @Path("x/y/p:z")
      @Namespace(reference="http://www.x.com/y")
      @Variant({
         @Element(name="name"),
         @Element(name="login"),
         @Element(name="user")
      })
      private final String id;
      public PathWithPrefix(@Element(name="name") String id){
         this.id = id;
      }
      public String getId(){
         return id;
      }
   }
   public void testPathWithPrefix() throws Exception{
      Persister persister = new Persister();
      PathWithPrefix example = new PathWithPrefix("tim");
      StringWriter writer = new StringWriter();
      persister.write(example, writer);
      String text = writer.toString();
      System.out.println(text);
      PathWithPrefix recovered = persister.read(PathWithPrefix.class, text);
      assertEquals(recovered.getId(), "tim");
      validate(persister, example);
   }
}
