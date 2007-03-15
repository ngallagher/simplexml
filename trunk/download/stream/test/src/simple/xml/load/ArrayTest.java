package simple.xml.load;

import java.io.StringReader;

import simple.xml.ValidationTestCase;
import simple.xml.ElementArray;
import simple.xml.Attribute;
import simple.xml.Root;

public class ArrayTest extends ValidationTestCase {

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
   private static class ArrayExample {

      @ElementArray(name="array")           
      public Text[] array;
   }

   @Root(name="root")
   private static class BadArrayExample {
 
      @ElementArray(name="array")
      public Text array;
   }   

   @Root(name="text") 
   private static class Text {

      @Attribute(name="value")
      public String value;

      public Text() {
         super();              
      }

      public Text(String value) {
         this.value = value;              
      }
   }

   private Persister serializer;

   public void setUp() {
      serializer = new Persister();
   }
	
   public void testExample() throws Exception {    
      ArrayExample example = serializer.read(ArrayExample.class, SOURCE);
      
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
         BadArrayExample example = serializer.read(BadArrayExample.class, SOURCE);
      } catch(InstantiationException e) {
         success = true;
      }
      assertTrue(success);
   }

   public void testWriteArray() throws Exception {
      ArrayExample example = new ArrayExample();
      example.array = new Text[100];
              
      for(int i = 0; i < example.array.length; i++) {
         example.array[i] = new Text(String.format("index %s", i));
      }      
      validate(example, serializer);
   }

}
