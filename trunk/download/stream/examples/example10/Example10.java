package example10;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Persister;

public class Example10 {

   /* snippet */
   @Root
   public static class Example {
     
      @ElementListUnion({
         @ElementList(entry="int", type=Integer.class, inline=true),
         @ElementList(entry="date", type=Date.class, inline=true),
         @ElementList(entry="text", type=String.class, inline=true)
      })
      private List<Object> list;      
   }
   /* snippet */

   public static void main(String[] list) throws Exception {
      Persister persister = new Persister();
      File file = new File("example10/example10.xml");
      Example example = persister.read(Example.class, file);
      
      System.out.println(example.list);
      
      persister.write(example, System.out);
   }
}
