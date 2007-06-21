package simple.xml.transform.lang;

import junit.framework.TestCase;

public class StringArrayTransformTest extends TestCase {

   public void testRead() throws Exception {    
      StringArrayTransform transform = new StringArrayTransform();
      String[] list = transform.read("one,two,three,four");     
 
      assertEquals("one", list[0]);
      assertEquals("two", list[1]);
      assertEquals("three", list[2]);
      assertEquals("four", list[3]);

      list = transform.read("  this is some string ,\t\n "+
                            "that has each\n\r," +
                            "string split,\t over\n,\n"+
                            "several lines\n\t");


      assertEquals("this is some string", list[0]);
      assertEquals("that has each", list[1]);
      assertEquals("string split", list[2]);
      assertEquals("over", list[3]);
      assertEquals("several lines", list[4]);
   }

   public void testWrite() throws Exception {
      StringArrayTransform transform = new StringArrayTransform();
      String value = transform.write(new String[] { "one", "two", "three", "four"});

      assertEquals(value, "one, two, three, four");

      value = transform.write(new String[] {"one", null, "three", "four"});

      assertEquals(value, "one, three, four");
   }
}
