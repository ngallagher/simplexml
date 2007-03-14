package simple.xml.load;

import java.io.StringReader;

import junit.framework.TestCase;
import simple.xml.ElementArray;
import simple.xml.Attribute;
import simple.xml.Root;

public class ArrayTest extends TestCase {

   private static final String SOURCE =
   "<?xml version=\"1.0\"?>\n"+
   "<root>\n"+
   "   <array>\n\r"+
   "      <text value='entry one'/>  \n\r"+
   "      <text value='entry two'/>  \n\r"+
   "      <text value='entry three'/>  \n\r"+
   "      <text value='entry four'/>  \n\r"+
   "      <text value='entry five'/>  \n\r"+
   "   </array>\n\r"+
   "</root>";

   @Root(name="root")
   private static class Example {

      @ElementArray(name="array")           
      public Text[] array;
   }

   @Root(name="root")
   private static class BadExample {
 
      @ElementArray(name="array")
      public Text array;
   }   

   @Root(name="text") 
   private static class Text {

      @Attribute(name="value")
      public String value;
   }

   private Persister serializer;

   public void setUp() {
      serializer = new Persister();
   }
	
   public void testExample() throws Exception {    
      Example example = serializer.read(Example.class, SOURCE);
      
      assertEquals(example.array.length, 5);
      assertEquals(example.array[0].value, "entry one");
      assertEquals(example.array[1].value, "entry two");
      assertEquals(example.array[2].value, "entry three");
      assertEquals(example.array[3].value, "entry four");
      assertEquals(example.array[4].value, "entry five");
   }

   public void testBadExample() throws Exception {    
      boolean success = false;

      try {
         BadExample example = serializer.read(BadExample.class, SOURCE);
      } catch(InstantiationException e) {
         success = true;
      }
      assertTrue(success);
   }

}
