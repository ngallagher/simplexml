package example9;

import java.io.File;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.core.Persister;

public class Example9 {

   /* snippet */
   @Default(DefaultType.PROPERTY)
   public static class Example {
      
      private String name;
      
      public String getName() {
         return name;
      }
      
      public void setName(String name) {
         this.name = name;
      }
      
   }
   /* snippet */

   public static void main(String[] list) throws Exception {
      Persister persister = new Persister();
      File file = new File("example9/example9.xml");
      Example example = persister.read(Example.class, file);
      
      System.out.println(example.name);
      
      persister.write(example, System.out);
   }
}
