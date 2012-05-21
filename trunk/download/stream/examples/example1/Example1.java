package example1;

import java.io.File;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Persister;

public class Example1 {
   
   /* snippet */
   @Root
   public static class Example {
      
      @Path("a/b[1]")
      @Element
      private String x;
      
      @Path("a/b[2]")
      @Element
      private String y;
   }
   /* snippet */

   public static void main(String[] list) throws Exception {
      Persister persister = new Persister();
      File file = new File("example1/example1.xml");
      Example example = persister.read(Example.class, file);
      
      System.out.println(example.x);
      System.out.println(example.y);
   }
}
