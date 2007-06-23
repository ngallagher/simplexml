package org.simpleframework.xml.transform;

import org.simpleframework.xml.transform.Transformer;

import junit.framework.TestCase;

public class TransformerTest extends TestCase {
   
   private Transformer transformer;
   
   public void setUp() {
      this.transformer = new Transformer();
   }

   public void testInteger() throws Exception {     
      Object value = transformer.read("1", Integer.class);
      String text = transformer.write(value, Integer.class);

      assertEquals(value, new Integer(1));
      assertEquals(text, "1");
   }
   
   public void testString() throws Exception {     
      Object value = transformer.read("some text", String.class);      
      String text = transformer.write(value, String.class);
      
      assertEquals("some text", value);
      assertEquals("some text", text);
   }
   
   public void testCharacter() throws Exception {
      Object value = transformer.read("c", Character.class);      
      String text = transformer.write(value, Character.class);      
      
      assertEquals(value, new Character('c'));
      assertEquals(text, "c");
   }
   
   public void testFloat() throws Exception {
      Object value = transformer.read("1.12", Float.class);      
      String text = transformer.write(value, Float.class);
      
      assertEquals(value, new Float(1.12));
      assertEquals(text, "1.12");
   }
   
   public void testDouble() throws Exception {     
      Object value = transformer.read("12.33", Double.class);
      String text = transformer.write(value, Double.class);      
      
      assertEquals(value, new Double(12.33));
      assertEquals(text, "12.33");
   }
   
   public void testBoolean() throws Exception {
      Object value = transformer.read("true", Boolean.class);
      String text = transformer.write(value, Boolean.class);
      
      assertEquals(value, Boolean.TRUE);
      assertEquals(text, "true");
   }
   
   public void testLong() throws Exception {
      Object value = transformer.read("1234567", Long.class);
      String text = transformer.write(value, Long.class);
      
      assertEquals(value, new Long(1234567));
      assertEquals(text, "1234567");
   }
   
   public void testShort() throws Exception {
      Object value = transformer.read("12", Short.class);
      String text = transformer.write(value, Short.class);
      
      assertEquals(value, new Short((short)12));
      assertEquals(text, "12");
   }
   
   public void testPrimitiveIntegerArray() throws Exception {
      Object value = transformer.read("1, 2, 3, 4, 5", int[].class);
      String text = transformer.write(value, int[].class);
      
      assertTrue(value instanceof int[]);

      int[] array = (int[])value;

      assertEquals(array.length, 5);
      assertEquals(array[0], 1);
      assertEquals(array[1], 2);
      assertEquals(array[2], 3);
      assertEquals(array[3], 4);
      assertEquals(array[4], 5);
      assertEquals(text, "1, 2, 3, 4, 5");      
   }
}
