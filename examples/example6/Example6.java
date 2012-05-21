package example6;

import java.io.File;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Persister;

public class Example6 {

   /* snippet */
   @Root
   public static class Example {
      
      @Namespace(prefix="ns1", reference="http://www.blah.com/ns/a")
      @Element
      private String a;
      
      @Namespace(prefix="ns2", reference="http://www.blah.com/ns/b")
      @Element
      private String b;
   }
   /* snippet */

   public static void main(String[] list) throws Exception {
      Persister persister = new Persister();
      File file = new File("example6/example6.xml");
      Example example = persister.read(Example.class, file);
      
      System.out.println(example.a);
      System.out.println(example.b);
      
      persister.write(example, System.out);
   }
}
