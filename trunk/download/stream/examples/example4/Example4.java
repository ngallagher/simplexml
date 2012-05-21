package example4;

import java.io.File;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Persister;

public class Example4 {

   /* snippet */
   @Root
   public static class Point {
      
      @Path("a/b[1]")
      @Element
      private final int x;
      
      @Path("a/b[1]")
      @Element
      private final int y;
      
      public Point(@Element(name="x") int x, @Element(name="y") int y) {
         this.x = x;
         this.y = y;
      }
   }
   /* snippet */

   public static void main(String[] list) throws Exception {
      Persister persister = new Persister();
      File file = new File("example4/example4.xml");
      Point example = persister.read(Point.class, file);
      
      System.out.println(example.x);
      System.out.println(example.y);
   }
}
