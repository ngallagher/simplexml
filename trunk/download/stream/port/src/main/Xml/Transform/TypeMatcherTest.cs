#region Using directives
using System;
#endregion
package SimpleFramework.Xml.transform;
public class TypeMatcherTest : TestCase {
   private static class BlankMatcher : Matcher {
      public Transform Match(Class type) {
         return null;
      }
   }
   private Matcher matcher;
   public void SetUp() {
      this.matcher = new DefaultMatcher(new BlankMatcher());
   }
   public void TestInteger() {
      Transform transform = matcher.Match(Integer.class);
      Object value = transform.read("1");
      assertEquals(value, new Integer(1));
   }
   public void TestString() {
      Transform transform = matcher.Match(String.class);
      Object value = transform.read("some text");
      assertEquals("some text", value);
   }
   public void TestCharacter() {
      Transform transform = matcher.Match(Character.class);
      Object value = transform.read("c");
      assertEquals(value, new Character('c'));
   }
   public void TestFloat() {
      Transform transform = matcher.Match(Float.class);
      Object value = transform.read("1.12");
      assertEquals(value, new Float(1.12));
   }
   public void TestDouble() {
      Transform transform = matcher.Match(Double.class);
      Object value = transform.read("12.33");
      assertEquals(value, new Double(12.33));
   }
   public void TestBoolean() {
      Transform transform = matcher.Match(Boolean.class);
      Object value = transform.read("true");
      assertEquals(value, Boolean.TRUE);
   }
   public void TestLong() {
      Transform transform = matcher.Match(Long.class);
      Object value = transform.read("1234567");
      assertEquals(value, new Long(1234567));
   }
   public void TestShort() {
      Transform transform = matcher.Match(Short.class);
      Object value = transform.read("12");
      assertEquals(value, new Short((short)12));
   }
   public void TestIntegerArray() {
      Transform transform = matcher.Match(Integer[].class);
      Object value = transform.read("1, 2, 3, 4, 5");
      assertTrue(value instanceof Integer[]);
      Integer[] array = (Integer[])value;
      assertEquals(array.length, 5);
      assertEquals(array[0], new Integer(1));
      assertEquals(array[1], new Integer(2));
      assertEquals(array[2], new Integer(3));
      assertEquals(array[3], new Integer(4));
      assertEquals(array[4], new Integer(5));
   }
   public void TestPrimitiveIntegerArray() {
      Matcher matcher = new DefaultMatcher(new BlankMatcher());
      Transform transform = matcher.Match(int[].class);
      Object value = transform.read("1, 2, 3, 4, 5");
      assertTrue(value instanceof int[]);
      int[] array = (int[])value;
      assertEquals(array.length, 5);
      assertEquals(array[0], 1);
      assertEquals(array[1], 2);
      assertEquals(array[2], 3);
      assertEquals(array[3], 4);
      assertEquals(array[4], 5);
   }
}
}
