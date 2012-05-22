package example8;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.core.Persister;

public class Example8 {

   /* snippet */
   @Default
   public static class Example {
      
      private List<Double> a;
      private String b;
      private String c;
      private Date d;
      
   }
   /* snippet */

   public static void main(String[] list) throws Exception {
      Persister persister = new Persister();
      File file = new File("example8/example8.xml");
      Example example = persister.read(Example.class, file);
      
      System.out.println(example.b);
      System.out.println(example.c);
      
      persister.write(example, System.out);
   }
}
