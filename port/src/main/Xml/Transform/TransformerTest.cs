#region Using directives
using SimpleFramework.Xml.Transform;
using SimpleFramework.Xml;
using System;
#endregion
package SimpleFramework.Xml.transform;
public class TransformerTest : TestCase {
   private static class BlankMatcher : Matcher {
      public Transform Match(Class type) {
         return null;
      }
   }
   private Transformer transformer;
   public void SetUp() {
      this.transformer = new Transformer(new BlankMatcher());
   }
   public void TestInteger() {
      Object value = transformer.Read("1", Integer.class);
      String text = transformer.Write(value, Integer.class);
      AssertEquals(value, new Integer(1));
      AssertEquals(text, "1");
   }
   public void TestString() {
      Object value = transformer.Read("some text", String.class);
      String text = transformer.Write(value, String.class);
      AssertEquals("some text", value);
      AssertEquals("some text", text);
   }
   public void TestCharacter() {
      Object value = transformer.Read("c", Character.class);
      String text = transformer.Write(value, Character.class);
      AssertEquals(value, new Character('c'));
      AssertEquals(text, "c");
   }
   public void TestInvalidCharacter() {
      bool success = false;
      try {
         transformer.Read("too long", Character.class);
      }catch(InvalidFormatException e) {
         e.printStackTrace();
         success = true;
      }
      assertTrue(success);
   }
   public void TestFloat() {
      Object value = transformer.Read("1.12", Float.class);
      String text = transformer.Write(value, Float.class);
      AssertEquals(value, new Float(1.12));
      AssertEquals(text, "1.12");
   }
   public void TestDouble() {
      Object value = transformer.Read("12.33", Double.class);
      String text = transformer.Write(value, Double.class);
      AssertEquals(value, new Double(12.33));
      AssertEquals(text, "12.33");
   }
   public void TestBoolean() {
      Object value = transformer.Read("true", Boolean.class);
      String text = transformer.Write(value, Boolean.class);
      AssertEquals(value, Boolean.TRUE);
      AssertEquals(text, "true");
   }
   public void TestLong() {
      Object value = transformer.Read("1234567", Long.class);
      String text = transformer.Write(value, Long.class);
      AssertEquals(value, new Long(1234567));
      AssertEquals(text, "1234567");
   }
   public void TestShort() {
      Object value = transformer.Read("12", Short.class);
      String text = transformer.Write(value, Short.class);
      AssertEquals(value, new Short((short)12));
      AssertEquals(text, "12");
   }
   public void TestPrimitiveIntegerArray() {
      Object value = transformer.Read("1, 2, 3, 4, 5", int[].class);
      String text = transformer.Write(value, int[].class);
      assertTrue(value instanceof int[]);
      int[] array = (int[])value;
      AssertEquals(array.length, 5);
      AssertEquals(array[0], 1);
      AssertEquals(array[1], 2);
      AssertEquals(array[2], 3);
      AssertEquals(array[3], 4);
      AssertEquals(array[4], 5);
      AssertEquals(text, "1, 2, 3, 4, 5");
   }
   public void TestPrimitiveLongArray() {
      Object value = transformer.Read("1, 2, 3, 4, 5", long[].class);
      String text = transformer.Write(value, long[].class);
      assertTrue(value instanceof long[]);
      long[] array = (long[])value;
      AssertEquals(array.length, 5);
      AssertEquals(array[0], 1);
      AssertEquals(array[1], 2);
      AssertEquals(array[2], 3);
      AssertEquals(array[3], 4);
      AssertEquals(array[4], 5);
      AssertEquals(text, "1, 2, 3, 4, 5");
   }
   public void TestPrimitiveShortArray() {
      Object value = transformer.Read("1, 2, 3, 4, 5", short[].class);
      String text = transformer.Write(value, short[].class);
      assertTrue(value instanceof short[]);
      short[] array = (short[])value;
      AssertEquals(array.length, 5);
      AssertEquals(array[0], 1);
      AssertEquals(array[1], 2);
      AssertEquals(array[2], 3);
      AssertEquals(array[3], 4);
      AssertEquals(array[4], 5);
      AssertEquals(text, "1, 2, 3, 4, 5");
   }
   public void TestPrimitiveByteArray() {
      Object value = transformer.Read("1, 2, 3, 4, 5", byte[].class);
      String text = transformer.Write(value, byte[].class);
      assertTrue(value instanceof byte[]);
      byte[] array = (byte[])value;
      AssertEquals(array.length, 5);
      AssertEquals(array[0], 1);
      AssertEquals(array[1], 2);
      AssertEquals(array[2], 3);
      AssertEquals(array[3], 4);
      AssertEquals(array[4], 5);
      AssertEquals(text, "1, 2, 3, 4, 5");
   }
   public void TestPrimitiveFloatArray() {
      Object value = transformer.Read("1.0, 2.0, 3.0, 4.0, 5.0", float[].class);
      String text = transformer.Write(value, float[].class);
      assertTrue(value instanceof float[]);
      float[] array = (float[])value;
      AssertEquals(array.length, 5);
      AssertEquals(array[0], 1.0f);
      AssertEquals(array[1], 2.0f);
      AssertEquals(array[2], 3.0f);
      AssertEquals(array[3], 4.0f);
      AssertEquals(array[4], 5.0f);
      AssertEquals(text, "1.0, 2.0, 3.0, 4.0, 5.0");
   }
   public void TestPrimitiveDoubleArray() {
      Object value = transformer.Read("1, 2, 3, 4, 5", double[].class);
      String text = transformer.Write(value, double[].class);
      assertTrue(value instanceof double[]);
      double[] array = (double[])value;
      AssertEquals(array.length, 5);
      AssertEquals(array[0], 1.0d);
      AssertEquals(array[1], 2.0d);
      AssertEquals(array[2], 3.0d);
      AssertEquals(array[3], 4.0d);
      AssertEquals(array[4], 5.0d);
      AssertEquals(text, "1.0, 2.0, 3.0, 4.0, 5.0");
   }
   public void TestPrimitiveCharacterArray() {
      Object value = transformer.Read("hello world", char[].class);
      String text = transformer.Write(value, char[].class);
      assertTrue(value instanceof char[]);
      char[] array = (char[])value;
      AssertEquals(array.length, 11);
      AssertEquals(array[0], 'h');
      AssertEquals(array[1], 'e');
      AssertEquals(array[2], 'l');
      AssertEquals(array[3], 'l');
      AssertEquals(array[4], 'o');
      AssertEquals(array[5], ' ');
      AssertEquals(array[6], 'w');
      AssertEquals(array[7], 'o');
      AssertEquals(array[8], 'r');
      AssertEquals(array[9], 'l');
      AssertEquals(array[10], 'd');
      AssertEquals(text, "hello world");
   }
   // Java Language types
   public void TestIntegerArray() {
      Object value = transformer.Read("1, 2, 3, 4, 5", Integer[].class);
      String text = transformer.Write(value, Integer[].class);
      assertTrue(value instanceof Integer[]);
      Integer[] array = (Integer[])value;
      AssertEquals(array.length, 5);
      AssertEquals(array[0], new Integer(1));
      AssertEquals(array[1], new Integer(2));
      AssertEquals(array[2], new Integer(3));
      AssertEquals(array[3], new Integer(4));
      AssertEquals(array[4], new Integer(5));
      AssertEquals(text, "1, 2, 3, 4, 5");
   }
   public void TestBooleanArray() {
      Object value = transformer.Read("true, false, false, false, true", Boolean[].class);
      String text = transformer.Write(value, Boolean[].class);
      assertTrue(value instanceof Boolean[]);
      Boolean[] array = (Boolean[])value;
      AssertEquals(array.length, 5);
      AssertEquals(array[0], Boolean.TRUE);
      AssertEquals(array[1], Boolean.FALSE);
      AssertEquals(array[2], Boolean.FALSE);
      AssertEquals(array[3], Boolean.FALSE);
      AssertEquals(array[4], Boolean.TRUE);
      AssertEquals(text, "true, false, false, false, true");
   }
   public void TestLongArray() {
      Object value = transformer.Read("1, 2, 3, 4, 5", Long[].class);
      String text = transformer.Write(value, Long[].class);
      assertTrue(value instanceof Long[]);
      Long[] array = (Long[])value;
      AssertEquals(array.length, 5);
      AssertEquals(array[0], new Long(1));
      AssertEquals(array[1], new Long(2));
      AssertEquals(array[2], new Long(3));
      AssertEquals(array[3], new Long(4));
      AssertEquals(array[4], new Long(5));
      AssertEquals(text, "1, 2, 3, 4, 5");
   }
   public void TestShortArray() {
      Object value = transformer.Read("1, 2, 3, 4, 5", Short[].class);
      String text = transformer.Write(value, Short[].class);
      assertTrue(value instanceof Short[]);
      Short[] array = (Short[])value;
      AssertEquals(array.length, 5);
      AssertEquals(array[0], new Short((short)1));
      AssertEquals(array[1], new Short((short)2));
      AssertEquals(array[2], new Short((short)3));
      AssertEquals(array[3], new Short((short)4));
      AssertEquals(array[4], new Short((short)5));
      AssertEquals(text, "1, 2, 3, 4, 5");
   }
   public void TestByteArray() {
      Object value = transformer.Read("1, 2, 3, 4, 5", Byte[].class);
      String text = transformer.Write(value, Byte[].class);
      assertTrue(value instanceof Byte[]);
      Byte[] array = (Byte[])value;
      AssertEquals(array.length, 5);
      AssertEquals(array[0], new Byte((byte)1));
      AssertEquals(array[1], new Byte((byte)2));
      AssertEquals(array[2], new Byte((byte)3));
      AssertEquals(array[3], new Byte((byte)4));
      AssertEquals(array[4], new Byte((byte)5));
      AssertEquals(text, "1, 2, 3, 4, 5");
   }
   public void TestFloatArray() {
      Object value = transformer.Read("1.0, 2.0, 3.0, 4.0, 5.0", Float[].class);
      String text = transformer.Write(value, Float[].class);
      assertTrue(value instanceof Float[]);
      Float[] array = (Float[])value;
      AssertEquals(array.length, 5);
      AssertEquals(array[0], new Float(1.0f));
      AssertEquals(array[1], new Float(2.0f));
      AssertEquals(array[2], new Float(3.0f));
      AssertEquals(array[3], new Float(4.0f));
      AssertEquals(array[4], new Float(5.0f));
      AssertEquals(text, "1.0, 2.0, 3.0, 4.0, 5.0");
   }
   public void TestDoubleArray() {
      Object value = transformer.Read("1, 2, 3, 4, 5", Double[].class);
      String text = transformer.Write(value, Double[].class);
      assertTrue(value instanceof Double[]);
      Double[] array = (Double[])value;
      AssertEquals(array.length, 5);
      AssertEquals(array[0], new Double(1.0d));
      AssertEquals(array[1], new Double(2.0d));
      AssertEquals(array[2], new Double(3.0d));
      AssertEquals(array[3], new Double(4.0d));
      AssertEquals(array[4], new Double(5.0d));
      AssertEquals(text, "1.0, 2.0, 3.0, 4.0, 5.0");
   }
   public void TestCharacterArray() {
      Object value = transformer.Read("hello world", Character[].class);
      String text = transformer.Write(value, Character[].class);
      assertTrue(value instanceof Character[]);
      Character[] array = (Character[])value;
      AssertEquals(array.length, 11);
      AssertEquals(array[0], new Character('h'));
      AssertEquals(array[1], new Character('e'));
      AssertEquals(array[2], new Character('l'));
      AssertEquals(array[3], new Character('l'));
      AssertEquals(array[4], new Character('o'));
      AssertEquals(array[5], new Character(' '));
      AssertEquals(array[6], new Character('w'));
      AssertEquals(array[7], new Character('o'));
      AssertEquals(array[8], new Character('r'));
      AssertEquals(array[9], new Character('l'));
      AssertEquals(array[10], new Character('d'));
      AssertEquals(text, "hello world");
   }
}
}
