package example2;

import java.io.File;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementUnion;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Persister;

public class Example2 {

   /* snippet */
   @Root
   public static class Example {
      
      @ElementUnion({
         @Element(name="text", type=String.class),
         @Element(name="int", type=Integer.class),
         @Element(name="double", type=Double.class)
      })
      private Object value;
   }
   /* snippet */

   public static void main(String[] list) throws Exception {
      Persister persister = new Persister();
      File file = new File("example2/example2.xml");
      Example example = persister.read(Example.class, file);
      
      System.out.println(example.value);
   }
}
