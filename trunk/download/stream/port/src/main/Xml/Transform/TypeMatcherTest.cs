#region Using directives
using SimpleFramework.Xml;
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
      AssertEquals(value, new Integer(1));
   }
   public void TestString() {
      Transform transform = matcher.Match(String.class);
      Object value = transform.read("some text");
      AssertEquals("some text", value);
   }
   public void TestCharacter() {
      Transform transform = matcher.Match(Character.class);
      Object value = transform.read("c");
      AssertEquals(value, new Character('c'));
   }
   public void TestFloat() {
      Transform transform = matcher.Match(Float.class);
      Object value = transform.read("1.12");
      AssertEquals(value, new Float(1.12));
   }
   public void TestDouble() {
      Transform transform = matcher.Match(Double.class);
      Object value = transform.read("12.33");
      AssertEquals(value, new Double(12.33));
   }
   public void TestBoolean() {
      Transform transform = matcher.Match(Boolean.class);
      Object value = transform.read("true");
      AssertEquals(value, Boolean.TRUE);
   }
   public void TestLong() {
      Transform transform = matcher.Match(Long.class);
      Object value = transform.read("1234567");
      AssertEquals(value, new Long(1234567));
   }
   public void TestShort() {
      Transform transform = matcher.Match(Short.class);
      Object value = transform.read("12");
      AssertEquals(value, new Short((short)12));
   }
   public void TestIntegerArray() {
      Transform transform = matcher.Match(Integer[].class);
      Object value = transform.read("1, 2, 3, 4, 5");
      assertTrue(value instanceof Integer[]);
      Integer[] array = (Integer[])value;
      AssertEquals(array.length, 5);
      AssertEquals(array[0], new Integer(1));
      AssertEquals(array[1], new Integer(2));
      AssertEquals(array[2], new Integer(3));
      AssertEquals(array[3], new Integer(4));
      AssertEquals(array[4], new Integer(5));
   }
   public void TestPrimitiveIntegerArray() {
      Matcher matcher = new DefaultMatcher(new BlankMatcher());
      Transform transform = matcher.Match(int[].class);
      Object value = transform.read("1, 2, 3, 4, 5");
      assertTrue(value instanceof int[]);
      int[] array = (int[])value;
      AssertEquals(array.length, 5);
      AssertEquals(array[0], 1);
      AssertEquals(array[1], 2);
      AssertEquals(array[2], 3);
      AssertEquals(array[3], 4);
      AssertEquals(array[4], 5);
   }
}
}
