package simple.xml.transform;

import org.simpleframework.xml.transform.PrimitiveArrayTransform;
import org.simpleframework.xml.transform.lang.IntegerTransform;

import junit.framework.TestCase;

public class PrimitiveArrayTransformTest extends TestCase {

   public void testRead() throws Exception {    
      PrimitiveArrayTransform transform = new PrimitiveArrayTransform(new IntegerTransform(), int.class);
      int[] list = (int[])transform.read("1,2,3,4");     
 
      assertEquals(1, list[0]);
      assertEquals(2, list[1]);
      assertEquals(3, list[2]);
      assertEquals(4, list[3]);

      list = (int[])transform.read("  123 ,\t\n "+
                                   "1\n\r," +
                                   "100, 23, \t32,\t 0\n,\n"+
                                   "3\n\t");

      assertEquals(123, list[0]);
      assertEquals(1, list[1]);
      assertEquals(100, list[2]);
      assertEquals(23, list[3]);
      assertEquals(32, list[4]);
      assertEquals(0, list[5]);
      assertEquals(3, list[6]);
   }

   public void testWrite() throws Exception {
      PrimitiveArrayTransform transform = new PrimitiveArrayTransform(new IntegerTransform(), int.class);
      String value = transform.write(new int[] { 1, 2, 3, 4});

      assertEquals(value, "1, 2, 3, 4");

      value = transform.write(new int[] {1, 0, 3, 4});

      assertEquals(value, "1, 0, 3, 4");
   }
}
