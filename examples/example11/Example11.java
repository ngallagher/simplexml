package example11;

import java.io.File;
import java.util.Date;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementUnion;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Persister;

public class Example11 {

   /* snippet */
   @Root
   public static class Example {
     
      @ElementUnion({
         @Element(name="int", type=Integer.class),
         @Element(name="date", type=Date.class),
         @Element(name="text", type=String.class)
      })
      private Object value;  
      
      public Example(@Element(name="int") int value) {
         this.value = value;
      }
      
      public Example(@Element(name="date") Date value) {
         this.value = value;
      }
      
      public Example(@Element(name="text") String value) {
         this.value = value;
      }
   }
   /* snippet */

   public static void main(String[] list) throws Exception {
      Persister persister = new Persister();
      File file = new File("example11/example11.xml");
      Example example = persister.read(Example.class, file);
      
      System.out.println(example.value);
      
      persister.write(example, System.out);
   }
}
