package example5;

import java.io.File;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Persister;

public class Example5 {

   /* snippet */
   @Root
   public static class Example {
      
      @Namespace(reference="http://www.blah.com/ns/a")
      @Element
      private String a;
      
      @Namespace(reference="http://www.blah.com/ns/b")
      @Element
      private String b;
   }
   /* snippet */

   public static void main(String[] list) throws Exception {
      Persister persister = new Persister();
      File file = new File("example5/example5.xml");
      Example example = persister.read(Example.class, file);
      
      System.out.println(example.a);
      System.out.println(example.b);
      
      persister.write(example, System.out);
   }
}
