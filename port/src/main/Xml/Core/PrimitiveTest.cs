#region Using directives
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml.Stream;
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class PrimitiveTest : TestCase {
      public const String SOURCE =
      "<value class='java.lang.String'>some text</value>";
      public const String CYCLE_1 =
      "<value id='1' class='java.lang.String'>some text</value>";
      public const String CYCLE_2 =
      "<value id='2' class='java.lang.String'>some text</value>";
      public void TestPrimitive() {
         Context context = new Source(new TreeStrategy(), new Support(), new DefaultStyle());
         Primitive primitive = new Primitive(context, new ClassType(String.class));
         InputNode node = NodeBuilder.read(new StringReader(SOURCE));
         Object value = primitive.read(node);
         AssertEquals("some text", value);
         InputNode newNode = NodeBuilder.read(new StringReader(SOURCE));
         assertTrue(primitive.validate(newNode));
      }
      public void TestPrimitiveCycle() {
         Context context = new Source(new CycleStrategy(), new Support(), new DefaultStyle());
         Primitive primitive = new Primitive(context, new ClassType(String.class));
         InputNode node = NodeBuilder.read(new StringReader(CYCLE_1));
         Object value = primitive.read(node);
         AssertEquals("some text", value);
         // Need to use a different id for validate as reading has created the object
         // and an exception is thrown that the value already exists if id=1 is used
         InputNode newNode = NodeBuilder.read(new StringReader(CYCLE_2));
         assertTrue(primitive.validate(newNode));
      }
   }
}
