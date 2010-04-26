#region Using directives
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml.Stream;
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class PrimitiveArrayTest : TestCase {
      public const String ZERO =
      "<array length='0' class='java.lang.String'/>";
      public const String TWO =
       "<array length='2' class='java.lang.String'>"+
       "  <entry>one</entry>" +
       "  <entry>two</entry>" +
       "</array>";
      public void TestZero() {
         Context context = new Source(new TreeStrategy(), new Support(), new DefaultStyle());
         PrimitiveArray primitive = new PrimitiveArray(context, new ClassType(String[].class), new ClassType(String.class), "entry");
         InputNode node = NodeBuilder.read(new StringReader(ZERO));
         Object value = primitive.read(node);
         AssertEquals(value.getClass(), String[].class);
         InputNode newNode = NodeBuilder.read(new StringReader(ZERO));
         assertTrue(primitive.validate(newNode));
      }
      public void TestTwo() {
         Context context = new Source(new TreeStrategy(), new Support(), new DefaultStyle());
         PrimitiveArray primitive = new PrimitiveArray(context, new ClassType(String[].class), new ClassType(String.class), "entry");
         InputNode node = NodeBuilder.read(new StringReader(TWO));
         Object value = primitive.read(node);
         AssertEquals(value.getClass(), String[].class);
         String[] list = (String[]) value;
         AssertEquals(list.length, 2);
         AssertEquals(list[0], "one");
         AssertEquals(list[1], "two");
         InputNode newNode = NodeBuilder.read(new StringReader(TWO));
         assertTrue(primitive.validate(newNode));
      }
   }
}
