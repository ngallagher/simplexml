package org.simpleframework.xml.transform;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.ValidationTestCase;
import org.simpleframework.xml.load.Persister;
import org.simpleframework.xml.transform.lang.IntegerTransform;

public class PrimitiveArrayTransformTest extends ValidationTestCase {
   
   @Root
   public static class IntegerArrayExample {
      
      @Attribute     
      private int[] attribute;
      
      @Element
      private int[] element;
      
      @ElementList
      private List<int[]> list;
      
      @ElementArray
      private int[][] array;
      
      public IntegerArrayExample() {
         super();
      }
      
      public IntegerArrayExample(int[] list) {
         this.attribute = list;
         this.element = list;         
         this.list = new ArrayList<int[]>();
         this.list.add(list);
         this.list.add(list);
         this.array = new int[1][];
         this.array[0] = list;
      }   
   }
   
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
   
   public void testPersistence() throws Exception {
      int[] list = new int[] { 1, 2, 3, 4 };
      Persister persister = new Persister();
      IntegerArrayExample example = new IntegerArrayExample(list);
      StringWriter out = new StringWriter();
      
      assertEquals(example.attribute[0], 1);
      assertEquals(example.attribute[1], 2);
      assertEquals(example.attribute[2], 3);
      assertEquals(example.attribute[3], 4);
      assertEquals(example.element[0], 1);
      assertEquals(example.element[1], 2);
      assertEquals(example.element[2], 3);
      assertEquals(example.element[3], 4);      
      assertEquals(example.list.get(0)[0], 1);
      assertEquals(example.list.get(0)[1], 2);
      assertEquals(example.list.get(0)[2], 3);
      assertEquals(example.list.get(0)[3], 4);
      assertEquals(example.array[0][0], 1);
      assertEquals(example.array[0][1], 2);
      assertEquals(example.array[0][2], 3);
      assertEquals(example.array[0][3], 4);
      
      persister.write(example, out);
      String text = out.toString();
      
      example = persister.read(IntegerArrayExample.class, text);
      
      assertEquals(example.attribute[0], 1);
      assertEquals(example.attribute[1], 2);
      assertEquals(example.attribute[2], 3);
      assertEquals(example.attribute[3], 4);
      assertEquals(example.element[0], 1);
      assertEquals(example.element[1], 2);
      assertEquals(example.element[2], 3);
      assertEquals(example.element[3], 4);      
      assertEquals(example.list.get(0)[0], 1);
      assertEquals(example.list.get(0)[1], 2);
      assertEquals(example.list.get(0)[2], 3);
      assertEquals(example.list.get(0)[3], 4);
      assertEquals(example.array[0][0], 1);
      assertEquals(example.array[0][1], 2);
      assertEquals(example.array[0][2], 3);
      assertEquals(example.array[0][3], 4);
      
      validate(example, persister);      
   }
}
