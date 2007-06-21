package simple.xml.transform.lang;

import junit.framework.TestCase;

public class IntegerArrayTransformTest extends TestCase {

   public void testRead() throws Exception {    
      IntegerArrayTransform transform = new IntegerArrayTransform();
      Integer[] list = transform.read("1,2,3,4");     
 
      assertEquals(1, list[0].intValue());
      assertEquals(2, list[1].intValue());
      assertEquals(3, list[2].intValue());
      assertEquals(4, list[3].intValue());

      list = transform.read("  123 ,\t\n "+
                            "1\n\r," +
                            "100, 23, \t32,\t 0\n,\n"+
                            "3\n\t");

      assertEquals(123, list[0].intValue());
      assertEquals(1, list[1].intValue());
      assertEquals(100, list[2].intValue());
      assertEquals(23, list[3].intValue());
      assertEquals(32, list[4].intValue());
      assertEquals(0, list[5].intValue());
      assertEquals(3, list[6].intValue());
   }

   public void testWrite() throws Exception {
      IntegerArrayTransform transform = new IntegerArrayTransform();
      String value = transform.write(new Integer[] { 1, 2, 3, 4});

      assertEquals(value, "1, 2, 3, 4");

      value = transform.write(new Integer[] {1, null, 3, 4});

      assertEquals(value, "1, 3, 4");
   }
}
