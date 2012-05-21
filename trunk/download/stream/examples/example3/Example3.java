package example3;

import java.io.File;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Persister;

public class Example3 {

   /* snippet */
   @Root
   public static class Point {
      
      @Element
      private final int x;
      
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
      File file = new File("example3/example3.xml");
      Point example = persister.read(Point.class, file);
      
      System.out.println(example.x);
      System.out.println(example.y);
   }
}
