package example7;

import java.io.File;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Persister;

public class Example7 {

   /* snippet */
   @Root
   @Namespace(prefix="ns1", reference="http://www.blah.com/ns/a")
   public static class Example {
      
      @Namespace(reference="http://www.blah.com/ns/a")
      @Path("a/b")
      @Element
      private String x;
   }
   /* snippet */

   public static void main(String[] list) throws Exception {
      Persister persister = new Persister();
      File file = new File("example7/example7.xml");
      Example example = persister.read(Example.class, file);
      
      System.out.println(example.x);
      
      persister.write(example, System.out);
   }
}
